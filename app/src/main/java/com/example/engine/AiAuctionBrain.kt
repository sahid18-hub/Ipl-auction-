package com.example.engine

import com.example.data.FranchiseCatalog
import com.example.model.AuctionSet
import com.example.model.FranchiseId
import com.example.model.FranchiseProfile
import com.example.model.FranchiseState
import com.example.model.Nationality
import com.example.model.Player
import com.example.model.PlayerRole
import com.example.model.PlayerStatus
import kotlin.math.roundToInt
import kotlin.random.Random

data class AiPlayerEvaluation(
    val franchiseId: FranchiseId,
    val maxWillingBidLakhs: Int,
    val isPurseOpeningStretch: Boolean,
    val dnaMatchScore: Double
)

data class InstantPlayerSimResult(
    val winningFranchiseId: FranchiseId?,
    val finalPriceLakhs: Int,
    val biddingTeams: List<FranchiseId>,
    val bidCount: Int
)

class AiAuctionBrain(private val sessionSeed: Int = Random.nextInt()) {

    /**
     * Computes each AI franchise's true personal valuation ceiling for a player
     * combining Franchise DNA, Archetype Priorities, Squad Balance, Anti-Hoarding,
     * Purse-Opening Exception, Budget Pacing, and Mandatory Slot Urgency.
     */
    fun evaluateForFranchise(
        franchiseState: FranchiseState,
        player: Player,
        trueMarketValueLakhs: Int,
        currentSet: AuctionSet,
        isAcceleratedRound: Boolean
    ): AiPlayerEvaluation {
        val profile = FranchiseCatalog.get(franchiseState.id)

        // Hard legality guards first
        val maxLegalBid = AuctionRules.maxAllowableBidLakhs(
            franchiseState.purseLakhs,
            franchiseState.squadCount
        )
        if (franchiseState.squadCount >= AuctionRules.MAX_SQUAD_SIZE ||
            (player.isOverseas && franchiseState.overseasCount >= AuctionRules.MAX_OVERSEAS) ||
            maxLegalBid < player.basePriceLakhs
        ) {
            return AiPlayerEvaluation(franchiseState.id, 0, false, 0.0)
        }

        // 1. Franchise DNA & Archetype Priority Multiplier
        val archetypeAffinity = profile.archetypeAffinities[player.archetype] ?: 0.96
        var dnaScore = archetypeAffinity

        if (player.age <= 24) {
            dnaScore *= profile.youthBias
        } else if (player.age >= 32) {
            dnaScore *= profile.experienceBias
        }

        if (player.isCaptainMaterial && !franchiseState.hasCaptain) {
            dnaScore *= profile.captaincyBonus
        }

        if (player.nationality == Nationality.INDIAN && player.form >= 85) {
            dnaScore *= (0.94 + (profile.indianStarBias - 1.0) * 0.75)
        }

        // 2. Positional Blueprint & Anti-Hoarding Decay
        val roleDemand = calculatePositionalNeedFactor(franchiseState, profile, player)
        if (roleDemand <= 0.10) {
            return AiPlayerEvaluation(franchiseState.id, 0, false, dnaScore)
        }

        // 3. Overseas Slot Conservation (don't burn all 8 overseas slots on low-form depth early)
        val overseasFactor = calculateOverseasConservationFactor(franchiseState, player, isAcceleratedRound)

        // 4. Anti-Blow-Budget Early Pacing
        val budgetPacingFactor = calculateBudgetPacingFactor(franchiseState, trueMarketValueLakhs, currentSet)

        // 5. Purse-Opening Exception (Core DNA match + critical missing slot -> stretch ceiling!)
        val isCriticalMissingSlot = isCriticalSlotMissing(franchiseState, profile, player)
        val isMarqueeSet = player.set == AuctionSet.MARQUEE_1 || player.set == AuctionSet.MARQUEE_2
        val dnaThreshold = if (isMarqueeSet) 1.10 else 1.15
        val isPurseOpeningStretch = dnaScore >= dnaThreshold &&
            isCriticalMissingSlot &&
            player.form >= 84 &&
            franchiseState.purseLakhs >= 2200

        val stretchMultiplier = if (isPurseOpeningStretch) {
            profile.marqueeStretch.coerceIn(1.16, 1.35)
        } else {
            1.0
        }

        // 6. Deterministic-per-auction organic team variance (±11%)
        val pairSeed = sessionSeed xor franchiseState.id.ordinal.shl(12) xor player.id.hashCode()
        val pairRand = Random(pairSeed)
        val organicVariance = 0.89 + (pairRand.nextDouble() * 0.22)

        // 7. Mandatory Squad Completion Urgency in late sets / Accelerated Round
        val urgencyFloorLakhs = calculateMandatoryUrgencyFloor(
            franchiseState = franchiseState,
            player = player,
            currentSet = currentSet,
            isAcceleratedRound = isAcceleratedRound,
            roleDemand = roleDemand
        )

        val aggressionFactor = 0.92 + (profile.aggression - 0.90) * 0.55

        var rawCeiling = (trueMarketValueLakhs *
            dnaScore *
            roleDemand *
            overseasFactor *
            budgetPacingFactor *
            stretchMultiplier *
            aggressionFactor *
            organicVariance).roundToInt()

        // Ensure high-form superstars have genuine bidding war ceilings across rival AIs
        if (player.form >= 92 && player.hype >= 93 && isCriticalMissingSlot && franchiseState.purseLakhs >= 3200) {
            rawCeiling = (rawCeiling * 1.12).roundToInt()
        }

        // Keep uncapped talent pricing realistic (₹0.30 Cr – ₹4.00 Cr)
        if (player.status == PlayerStatus.UNCAPPED && rawCeiling > 380) {
            rawCeiling = (380 + ((rawCeiling - 380) / 5)).coerceAtMost(400)
        }

        // Apply mandatory slot urgency floor if squad still needs players to hit 18
        if (urgencyFloorLakhs > rawCeiling) {
            rawCeiling = urgencyFloorLakhs
        }

        // Cap ceiling at strict legal max allowable bid & per-player healthy budget cap
        val singlePlayerPrudenceCap = calculatePrudenceCapLakhs(
            franchiseState = franchiseState,
            player = player,
            isPurseOpeningStretch = isPurseOpeningStretch
        )
        val finalCeiling = rawCeiling
            .coerceAtMost(maxLegalBid)
            .coerceAtMost(singlePlayerPrudenceCap)

        // If ceiling falls below base price, team will not enter bidding
        val effectiveCeiling = if (finalCeiling < player.basePriceLakhs) 0 else finalCeiling

        return AiPlayerEvaluation(
            franchiseId = franchiseState.id,
            maxWillingBidLakhs = effectiveCeiling,
            isPurseOpeningStretch = isPurseOpeningStretch,
            dnaMatchScore = dnaScore
        )
    }

    private fun calculatePositionalNeedFactor(
        state: FranchiseState,
        profile: FranchiseProfile,
        player: Player
    ): Double {
        val bp = profile.blueprint
        val (currentCount, targetCount) = when (player.role) {
            PlayerRole.BATTER -> if (player.isOpener) {
                state.openerCount to bp.openers
            } else {
                state.middleOrderCount to bp.middleOrder
            }
            PlayerRole.WICKETKEEPER -> state.countByRole(PlayerRole.WICKETKEEPER) to bp.wicketkeepers
            PlayerRole.ALL_ROUNDER -> state.countByRole(PlayerRole.ALL_ROUNDER) to bp.allRounders
            PlayerRole.FAST_BOWLER -> state.countByRole(PlayerRole.FAST_BOWLER) to bp.pacers
            PlayerRole.SPINNER -> state.countByRole(PlayerRole.SPINNER) to bp.spinners
        }

        val ratio = currentCount.toDouble() / targetCount.coerceAtLeast(1).toDouble()
        return when {
            currentCount == 0 -> 1.16
            ratio < 0.50 -> 1.08
            ratio < 0.85 -> 0.98
            currentCount < targetCount -> 0.86
            currentCount == targetCount -> if (player.form >= 88) 0.58 else 0.34
            currentCount == targetCount + 1 -> 0.22
            else -> 0.08
        }
    }

    private fun isCriticalSlotMissing(
        state: FranchiseState,
        profile: FranchiseProfile,
        player: Player
    ): Boolean {
        val bp = profile.blueprint
        return when (player.role) {
            PlayerRole.BATTER -> if (player.isOpener) state.openerCount < 2 else state.middleOrderCount < 2
            PlayerRole.WICKETKEEPER -> state.countByRole(PlayerRole.WICKETKEEPER) == 0
            PlayerRole.ALL_ROUNDER -> state.countByRole(PlayerRole.ALL_ROUNDER) < 2
            PlayerRole.FAST_BOWLER -> state.countByRole(PlayerRole.FAST_BOWLER) < 3
            PlayerRole.SPINNER -> state.countByRole(PlayerRole.SPINNER) < 2
        }
    }

    private fun calculateOverseasConservationFactor(
        state: FranchiseState,
        player: Player,
        isAcceleratedRound: Boolean
    ): Double {
        if (!player.isOverseas) return 1.04
        val remainingOsSlots = AuctionRules.MAX_OVERSEAS - state.overseasCount
        return when {
            remainingOsSlots <= 0 -> 0.0
            remainingOsSlots == 1 -> if (player.form >= 88) 1.02 else if (isAcceleratedRound) 0.72 else 0.45
            remainingOsSlots == 2 -> if (player.form >= 85) 1.00 else 0.70
            remainingOsSlots <= 4 -> if (player.form >= 80) 0.98 else 0.80
            else -> 1.00
        }
    }

    private fun calculateBudgetPacingFactor(
        state: FranchiseState,
        trueMarketValueLakhs: Int,
        currentSet: AuctionSet
    ): Double {
        val slotsToMinimum = (AuctionRules.MIN_SQUAD_SIZE - state.squadCount).coerceAtLeast(1)
        val avgPursePerRemainingSlot = state.purseLakhs.toDouble() / slotsToMinimum.toDouble()

        return when {
            // Early sets: plenty of money, but avoid blowing everything if already spent heavily
            state.squadCount <= 4 && state.purseLakhs < 6500 -> 0.76
            state.squadCount <= 6 && state.purseLakhs < 4500 -> 0.72
            avgPursePerRemainingSlot > 750 -> 1.08
            avgPursePerRemainingSlot > 450 -> 1.00
            avgPursePerRemainingSlot > 250 -> 0.88
            avgPursePerRemainingSlot > 120 -> 0.74
            else -> if (trueMarketValueLakhs <= 120) 0.92 else 0.56
        }
    }

    private fun calculatePrudenceCapLakhs(
        franchiseState: FranchiseState,
        player: Player,
        isPurseOpeningStretch: Boolean
    ): Int {
        val maxLegal = AuctionRules.maxAllowableBidLakhs(
            franchiseState.purseLakhs,
            franchiseState.squadCount
        )
        // Even in Marquee Set 1, no single player should absorb > 28% of starting purse unless iconic stretch
        val hardSinglePlayerCap = when {
            player.status == PlayerStatus.UNCAPPED -> 400
            player.form >= 93 && player.hype >= 94 && isPurseOpeningStretch -> 2750
            player.form >= 91 && player.hype >= 92 -> 2500
            player.form >= 87 -> 1850
            else -> 1400
        }

        // Don't spend more than 42% of remaining purse if team still needs 8+ players
        val remainingNeeded = AuctionRules.remainingMandatorySlots(franchiseState.squadCount)
        val remainingShareCap = when {
            remainingNeeded >= 12 -> (franchiseState.purseLakhs * 0.30).roundToInt()
            remainingNeeded >= 8 -> (franchiseState.purseLakhs * 0.38).roundToInt()
            remainingNeeded >= 4 -> (franchiseState.purseLakhs * 0.52).roundToInt()
            else -> maxLegal
        }

        return maxLegal.coerceAtMost(hardSinglePlayerCap).coerceAtMost(remainingShareCap.coerceAtLeast(player.basePriceLakhs))
    }

    private fun calculateMandatoryUrgencyFloor(
        franchiseState: FranchiseState,
        player: Player,
        currentSet: AuctionSet,
        isAcceleratedRound: Boolean,
        roleDemand: Double
    ): Int {
        val neededForMin = AuctionRules.remainingMandatorySlots(franchiseState.squadCount)
        if (neededForMin <= 0) return 0

        val isLateAuction = currentSet == AuctionSet.UNCAPPED_PLAYERS || isAcceleratedRound
        if (!isLateAuction) return 0

        // In Uncapped or Accelerated rounds, teams that still need players to hit 18 actively scoop affordable fits
        val maxLegal = AuctionRules.maxAllowableBidLakhs(
            franchiseState.purseLakhs,
            franchiseState.squadCount
        )
        if (maxLegal < player.basePriceLakhs) return 0

        val affordableForUrgency = player.basePriceLakhs <= 150 ||
            (isAcceleratedRound && franchiseState.purseLakhs >= player.basePriceLakhs + neededForMin * 40)

        if (!affordableForUrgency) return 0

        return if (isAcceleratedRound) {
            val boost = (player.basePriceLakhs + (neededForMin * 15).coerceAtMost(120))
            boost.coerceAtMost(maxLegal)
        } else if (roleDemand >= 0.70 || neededForMin >= 4) {
            val boost = (player.basePriceLakhs + 30 + (player.form - 70).coerceAtLeast(0) * 4)
            boost.coerceAtMost(maxLegal)
        } else {
            0
        }
    }

    /**
     * Picks the next AI bidder to raise the paddle during live bidding.
     * Alternates naturally among interested teams so bidding wars feel authentic.
     */
    fun chooseNextAiBidder(
        evaluations: Map<FranchiseId, AiPlayerEvaluation>,
        franchises: Map<FranchiseId, FranchiseState>,
        player: Player,
        nextBidLakhs: Int,
        currentHighestBidder: FranchiseId?,
        recentBidders: List<FranchiseId>,
        userFranchiseId: FranchiseId
    ): FranchiseId? {
        val eligibleAiTeams = evaluations.values.filter { eval ->
            eval.franchiseId != userFranchiseId &&
                eval.franchiseId != currentHighestBidder &&
                eval.maxWillingBidLakhs >= nextBidLakhs &&
                AuctionRules.evaluateBidEligibility(
                    franchise = franchises.getValue(eval.franchiseId),
                    player = player,
                    bidAmountLakhs = nextBidLakhs,
                    currentHighestBidder = currentHighestBidder
                ).allowed
        }

        if (eligibleAiTeams.isEmpty()) return null

        // Prefer teams that haven't JUST bid 2 turns ago if multiple want in, or the highest-headroom rival
        val topCandidates = eligibleAiTeams.sortedByDescending { eval ->
            val headroom = (eval.maxWillingBidLakhs - nextBidLakhs).toDouble()
            val recentPenalty = if (recentBidders.lastOrNull() == eval.franchiseId) -180.0 else 0.0
            val activeRivalBonus = if (recentBidders.takeLast(3).contains(eval.franchiseId)) 95.0 else 0.0
            headroom + activeRivalBonus + recentPenalty + (eval.dnaMatchScore * 80.0)
        }

        return if (topCandidates.size >= 2 && Random.nextDouble() < 0.25) {
            topCandidates[1].franchiseId
        } else {
            topCandidates[0].franchiseId
        }
    }

    /**
     * Instant, full-loop AI bidding simulation used by SKIP PLAYER and SKIP ENTIRE SET.
     * Runs the complete official bid-increment loop among all 9 AI brains (and honors any existing
     * current bid/highest bidder if the user skipped mid-bidding!) so players ALWAYS sell at their
     * true realistic market price (second-highest ceiling + 1 increment) — never defaulting to base
     * price unless only 1 AI team wants them!
     */
    fun simulatePlayerToCompletion(
        player: Player,
        franchises: Map<FranchiseId, FranchiseState>,
        userFranchiseId: FranchiseId,
        remainingPool: List<Player>,
        currentSet: AuctionSet,
        isAcceleratedRound: Boolean,
        startingBidLakhs: Int = 0,
        startingHighestBidder: FranchiseId? = null
    ): InstantPlayerSimResult {
        val trueMarketValue = ValuationEngine.calculateTrueMarketValueLakhs(player, remainingPool)
        val aiEvaluations = franchises.values
            .filter { it.id != userFranchiseId }
            .associate { state ->
                state.id to evaluateForFranchise(
                    franchiseState = state,
                    player = player,
                    trueMarketValueLakhs = trueMarketValue,
                    currentSet = currentSet,
                    isAcceleratedRound = isAcceleratedRound
                )
            }

        var currentBid = startingBidLakhs
        var highestBidder = startingHighestBidder
        var hasBid = highestBidder != null
        val participants = linkedSetOf<FranchiseId>()
        highestBidder?.let { participants.add(it) }
        val recentBidders = mutableListOf<FranchiseId>()
        var bidCount = if (hasBid) 1 else 0

        var safetyCounter = 0
        while (safetyCounter < 350) {
            safetyCounter++
            val nextBid = AuctionRules.nextBidAmountLakhs(
                currentBidLakhs = currentBid,
                basePriceLakhs = player.basePriceLakhs,
                hasOpeningBid = hasBid
            )
            val nextTeam = chooseNextAiBidder(
                evaluations = aiEvaluations,
                franchises = franchises,
                player = player,
                nextBidLakhs = nextBid,
                currentHighestBidder = highestBidder,
                recentBidders = recentBidders,
                userFranchiseId = userFranchiseId
            ) ?: break

            currentBid = nextBid
            highestBidder = nextTeam
            hasBid = true
            participants.add(nextTeam)
            recentBidders.add(nextTeam)
            bidCount++
        }

        return InstantPlayerSimResult(
            winningFranchiseId = highestBidder,
            finalPriceLakhs = if (highestBidder != null) currentBid else 0,
            biddingTeams = participants.toList(),
            bidCount = bidCount
        )
    }

    /**
     * Each of the 9 AI franchises automatically nominates 2–3 unsold players for the
     * Accelerated Round based on its remaining positional blueprint gaps, overseas headroom,
     * and remaining purse legality.
     */
    fun nominateUnsoldPlayersForFranchises(
        franchises: Map<FranchiseId, FranchiseState>,
        userFranchiseId: FranchiseId,
        unsoldPool: List<Player>
    ): Map<FranchiseId, List<String>> {
        if (unsoldPool.isEmpty()) return emptyMap()

        val result = linkedMapOf<FranchiseId, List<String>>()
        franchises.values
            .filter { it.id != userFranchiseId }
            .forEach { state ->
                if (state.squadCount >= AuctionRules.MAX_SQUAD_SIZE) {
                    result[state.id] = emptyList()
                    return@forEach
                }

                val profile = FranchiseCatalog.get(state.id)
                val maxLegalBid = AuctionRules.maxAllowableBidLakhs(state.purseLakhs, state.squadCount)
                val canTakeOverseas = state.overseasCount < AuctionRules.MAX_OVERSEAS

                // Nominate 3 players if team still needs mandatory slots (<18) or has plenty of room, else 2
                val targetNominationCount = if (state.squadCount < AuctionRules.MIN_SQUAD_SIZE || state.squadCount <= 20) 3 else 2

                val scoredCandidates = unsoldPool
                    .filter { p ->
                        p.basePriceLakhs <= maxLegalBid && (!p.isOverseas || canTakeOverseas)
                    }
                    .map { p ->
                        val positionalNeed = calculatePositionalNeedFactor(state, profile, p)
                        val dnaAffinity = profile.archetypeAffinities[p.archetype] ?: 0.96
                        val quality = (p.form * 0.62) + (p.hype * 0.38)
                        val criticalGapBoost = if (isCriticalSlotMissing(state, profile, p)) 18.0 else 0.0
                        val affordabilityBoost = if (p.basePriceLakhs <= 75) 6.0 else 0.0
                        val pairNoise = Random(sessionSeed xor state.id.ordinal.shl(8) xor p.id.hashCode()).nextDouble(-4.0, 4.0)
                        val totalScore = (quality * positionalNeed * dnaAffinity) + criticalGapBoost + affordabilityBoost + pairNoise
                        p to totalScore
                    }
                    .sortedByDescending { it.second }

                // Pick top 2-3 while diversifying roles if multiple gaps exist
                val chosen = mutableListOf<Player>()
                val usedRoles = mutableSetOf<PlayerRole>()
                for ((candidate, _) in scoredCandidates) {
                    if (chosen.size >= targetNominationCount) break
                    if (candidate.role !in usedRoles) {
                        chosen.add(candidate)
                        usedRoles.add(candidate.role)
                    }
                }
                if (chosen.size < targetNominationCount) {
                    for ((candidate, _) in scoredCandidates) {
                        if (chosen.size >= targetNominationCount) break
                        if (candidate !in chosen) chosen.add(candidate)
                    }
                }
                result[state.id] = chosen.map { it.id }
            }

        return result
    }
}
