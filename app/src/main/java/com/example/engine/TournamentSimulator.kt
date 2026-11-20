package com.example.engine

import com.example.model.FranchiseId
import com.example.model.FranchiseState
import com.example.model.LeagueStandingRow
import com.example.model.LineupValidation
import com.example.model.Player
import com.example.model.PlayerRole
import com.example.model.PlayoffMatchResult
import com.example.model.SeasonAwardWinner
import com.example.model.SeasonSimulationResult
import com.example.model.TeamLineup
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

object TournamentSimulator {

    const val PLAYING_XI_SIZE = 11
    const val MAX_OVERSEAS_IN_XI = 4

    /**
     * Auto-picks a balanced Playing XI (strictly max 4 Overseas) + 1 Impact Player for any franchise.
     */
    fun autoSelectBestLineup(franchiseState: FranchiseState): TeamLineup {
        val squadPlayers = franchiseState.squad.map { it.player }
        if (squadPlayers.isEmpty()) {
            return TeamLineup(franchiseState.id, emptyList(), null)
        }

        val ranked = squadPlayers.sortedByDescending { playerMatchImpactScore(it) }
        val selected = mutableListOf<Player>()
        var overseasCount = 0

        fun canAdd(p: Player): Boolean {
            if (p in selected) return false
            if (selected.size >= PLAYING_XI_SIZE) return false
            if (p.isOverseas && overseasCount >= MAX_OVERSEAS_IN_XI) return false
            return true
        }

        fun tryPick(predicate: (Player) -> Boolean, targetToAdd: Int) {
            var added = 0
            for (p in ranked) {
                if (added >= targetToAdd) break
                if (predicate(p) && canAdd(p)) {
                    selected.add(p)
                    if (p.isOverseas) overseasCount++
                    added++
                }
            }
        }

        // Core IPL XI blueprint: 1 WK, 3 Batters, 2 All-Rounders, 2 Fast Bowlers, 1 Spinner, then best 2 remaining
        tryPick({ it.role == PlayerRole.WICKETKEEPER }, 1)
        tryPick({ it.role == PlayerRole.BATTER }, 3)
        tryPick({ it.role == PlayerRole.ALL_ROUNDER }, 2)
        tryPick({ it.role == PlayerRole.FAST_BOWLER }, 2)
        tryPick({ it.role == PlayerRole.SPINNER }, 1)

        // Fill remaining slots up to 11 with highest-rated legal players (preferring bowling depth if <5 bowlers)
        val bowlingOptionsNow = selected.count { isBowlingOption(it) }
        if (bowlingOptionsNow < 5) {
            tryPick({ isBowlingOption(it) }, 5 - bowlingOptionsNow)
        }
        tryPick({ true }, PLAYING_XI_SIZE - selected.size)

        // Pick best Impact Player from remaining bench (prefer Indian specialist if XI already has 4 OS)
        val remainingBench = ranked.filter { it !in selected }
        val impactPlayer = if (overseasCount >= MAX_OVERSEAS_IN_XI) {
            remainingBench.firstOrNull { !it.isOverseas } ?: remainingBench.firstOrNull()
        } else {
            remainingBench.firstOrNull()
        }

        return TeamLineup(
            franchiseId = franchiseState.id,
            playingXiIds = selected.map { it.id },
            impactPlayerId = impactPlayer?.id
        )
    }

    /**
     * Strictly validates a user-picked Playing XI + Impact Player against official IPL rules.
     */
    fun validateLineup(
        franchiseState: FranchiseState,
        playingXiIds: List<String>,
        impactPlayerId: String?
    ): LineupValidation {
        val playerMap = franchiseState.squad.associate { it.player.id to it.player }
        val xiPlayers = playingXiIds.mapNotNull { playerMap[it] }
        val impactPlayer = impactPlayerId?.let { playerMap[it] }
        val overseasInXi = xiPlayers.count { it.isOverseas }
        val requiredXiSize = minOf(PLAYING_XI_SIZE, franchiseState.squadCount)
        val hasWk = xiPlayers.any { it.role == PlayerRole.WICKETKEEPER }
        val bowlingOptions = xiPlayers.count { isBowlingOption(it) } +
            if (impactPlayer != null && isBowlingOption(impactPlayer)) 1 else 0
        val rating = computeLineupPowerRating(xiPlayers, impactPlayer)

        val issue = when {
            franchiseState.squadCount == 0 -> "No players in squad to simulate."
            overseasInXi > MAX_OVERSEAS_IN_XI ->
                "Max $MAX_OVERSEAS_IN_XI Overseas allowed in Playing XI (currently $overseasInXi)."
            xiPlayers.size < requiredXiSize ->
                "Select ${requiredXiSize - xiPlayers.size} more player(s) to complete Playing XI ($requiredXiSize)."
            xiPlayers.size > PLAYING_XI_SIZE ->
                "Playing XI cannot exceed $PLAYING_XI_SIZE players."
            impactPlayerId != null && impactPlayerId in playingXiIds ->
                "Impact Player must be separate from your Playing XI."
            franchiseState.squadCount > PLAYING_XI_SIZE && impactPlayer == null ->
                "Pick 1 Impact Player from your bench to unlock Season Simulation."
            else -> null
        }

        return LineupValidation(
            isValid = issue == null,
            xiCount = xiPlayers.size,
            overseasInXiCount = overseasInXi,
            hasImpactPlayer = impactPlayer != null,
            hasWicketkeeper = hasWk,
            bowlingOptionsCount = bowlingOptions,
            teamPowerRating = rating,
            issueMessage = issue
        )
    }

    fun canAddPlayerToXi(
        franchiseState: FranchiseState,
        currentXiIds: List<String>,
        candidatePlayerId: String
    ): Pair<Boolean, String?> {
        if (candidatePlayerId in currentXiIds) return true to null
        if (currentXiIds.size >= PLAYING_XI_SIZE) {
            return false to "Playing XI is full (11/11). Remove a player first."
        }
        val playerMap = franchiseState.squad.associate { it.player.id to it.player }
        val candidate = playerMap[candidatePlayerId] ?: return false to "Player not found."
        val currentOverseas = currentXiIds.mapNotNull { playerMap[it] }.count { it.isOverseas }
        if (candidate.isOverseas && currentOverseas >= MAX_OVERSEAS_IN_XI) {
            return false to "IPL Rule: Max 4 Overseas players allowed in Playing XI!"
        }
        return true to null
    }

    /**
     * Simulates an entire 10-team IPL Season (70 league matches + Qualifier 1, Eliminator, Qualifier 2, IPL Final)
     * based on every team's Playing XI + Impact Player ratings and squad balance.
     */
    fun simulateIplSeason(
        franchises: Map<FranchiseId, FranchiseState>,
        userLineup: TeamLineup,
        seed: Int = Random.nextInt()
    ): SeasonSimulationResult {
        val rng = Random(seed)

        // Generate optimal Playing XI + Impact Player for all 9 AI teams, honor user's custom XI
        val allLineups = FranchiseId.entries.associateWith { id ->
            if (id == userLineup.franchiseId) {
                userLineup
            } else {
                autoSelectBestLineup(franchises.getValue(id))
            }
        }

        val xiPlayersByTeam: Map<FranchiseId, List<Player>> = allLineups.mapValues { (id, lineup) ->
            val map = franchises.getValue(id).squad.associate { it.player.id to it.player }
            lineup.playingXiIds.mapNotNull { map[it] }
        }
        val impactByTeam: Map<FranchiseId, Player?> = allLineups.mapValues { (id, lineup) ->
            val map = franchises.getValue(id).squad.associate { it.player.id to it.player }
            lineup.impactPlayerId?.let { map[it] }
        }

        val teamRatings: Map<FranchiseId, Int> = FranchiseId.entries.associateWith { id ->
            computeLineupPowerRating(xiPlayersByTeam.getValue(id), impactByTeam[id])
        }

        // 14 League Matches per team (70 total league fixtures in 10-team IPL format)
        val wins = FranchiseId.entries.associateWith { 0 }.toMutableMap()
        val losses = FranchiseId.entries.associateWith { 0 }.toMutableMap()
        val nrrAccumulator = FranchiseId.entries.associateWith { 0.0 }.toMutableMap()

        val teamOrder = FranchiseId.entries.toList()
        val fixtures = buildOfficial14MatchSchedule(teamOrder)

        fixtures.forEach { (teamA, teamB) ->
            val ratingA = teamRatings.getValue(teamA)
            val ratingB = teamRatings.getValue(teamB)
            val outcome = simulateSingleMatch(
                stageName = "League",
                teamA = teamA,
                teamB = teamB,
                ratingA = ratingA,
                ratingB = ratingB,
                rosterA = xiPlayersByTeam.getValue(teamA) + listOfNotNull(impactByTeam[teamA]),
                rosterB = xiPlayersByTeam.getValue(teamB) + listOfNotNull(impactByTeam[teamB]),
                rng = rng
            )
            val winner = outcome.match.winnerId
            val loser = if (winner == teamA) teamB else teamA
            wins[winner] = wins.getValue(winner) + 1
            losses[loser] = losses.getValue(loser) + 1
            nrrAccumulator[winner] = nrrAccumulator.getValue(winner) + outcome.nrrDelta
            nrrAccumulator[loser] = nrrAccumulator.getValue(loser) - outcome.nrrDelta
        }

        val standings = FranchiseId.entries.map { id ->
            val w = wins.getValue(id)
            val l = losses.getValue(id)
            val played = w + l
            val avgNrr = if (played > 0) (nrrAccumulator.getValue(id) / played) else 0.0
            LeagueStandingRow(
                franchiseId = id,
                played = played,
                won = w,
                lost = l,
                points = w * 2,
                netRunRate = (avgNrr * 1000.0).roundToInt() / 1000.0,
                xiRating = teamRatings.getValue(id)
            )
        }.sortedWith(
            compareByDescending<LeagueStandingRow> { it.points }
                .thenByDescending { it.netRunRate }
                .thenByDescending { it.xiRating }
        )

        val rank1 = standings[0].franchiseId
        val rank2 = standings[1].franchiseId
        val rank3 = standings[2].franchiseId
        val rank4 = standings[3].franchiseId

        fun runPlayoff(stage: String, a: FranchiseId, b: FranchiseId): PlayoffMatchResult {
            return simulateSingleMatch(
                stageName = stage,
                teamA = a,
                teamB = b,
                ratingA = teamRatings.getValue(a),
                ratingB = teamRatings.getValue(b),
                rosterA = xiPlayersByTeam.getValue(a) + listOfNotNull(impactByTeam[a]),
                rosterB = xiPlayersByTeam.getValue(b) + listOfNotNull(impactByTeam[b]),
                rng = rng
            ).match
        }

        val q1 = runPlayoff("QUALIFIER 1", rank1, rank2)
        val q1Loser = if (q1.winnerId == rank1) rank2 else rank1
        val eliminator = runPlayoff("ELIMINATOR", rank3, rank4)
        val q2 = runPlayoff("QUALIFIER 2", q1Loser, eliminator.winnerId)
        val finalMatch = runPlayoff("IPL FINAL", q1.winnerId, q2.winnerId)
        val champion = finalMatch.winnerId
        val runnerUp = if (champion == q1.winnerId) q2.winnerId else q1.winnerId

        val orangeCap = pickOrangeCapWinner(standings, xiPlayersByTeam, impactByTeam, rng)
        val purpleCap = pickPurpleCapWinner(standings, xiPlayersByTeam, impactByTeam, rng)

        return SeasonSimulationResult(
            standings = standings,
            qualifier1 = q1,
            eliminator = eliminator,
            qualifier2 = q2,
            finalMatch = finalMatch,
            championId = champion,
            runnerUpId = runnerUp,
            orangeCap = orangeCap,
            purpleCap = purpleCap,
            aiLineups = allLineups
        )
    }

    /**
     * Generates an exact 14-match-per-team (70 total games) schedule for 10 franchises:
     * Every team plays all 9 opponents once (45 matches), plus 5 additional rematches across two virtual groups (25 matches).
     */
    private fun buildOfficial14MatchSchedule(teams: List<FranchiseId>): List<Pair<FranchiseId, FranchiseId>> {
        val fixtures = mutableListOf<Pair<FranchiseId, FranchiseId>>()
        // Round 1: Single round-robin (9 games each = 45 matches)
        for (i in teams.indices) {
            for (j in i + 1 until teams.size) {
                fixtures.add(teams[i] to teams[j])
            }
        }
        // Round 2: Virtual groups (5 teams A, 5 teams B):
        // Each team plays its 4 group rivals again (20 matches) + its corresponding paired rival in the other group (5 matches) = +5 games each (14 total)
        val groupA = teams.take(5)
        val groupB = teams.drop(5)
        for (i in groupA.indices) {
            for (j in i + 1 until groupA.size) {
                fixtures.add(groupA[i] to groupA[j])
                fixtures.add(groupB[i] to groupB[j])
            }
        }
        for (i in 0 until 5) {
            fixtures.add(groupA[i] to groupB[i])
        }
        return fixtures
    }

    private data class MatchSimInternal(
        val match: PlayoffMatchResult,
        val nrrDelta: Double
    )

    private fun simulateSingleMatch(
        stageName: String,
        teamA: FranchiseId,
        teamB: FranchiseId,
        ratingA: Int,
        ratingB: Int,
        rosterA: List<Player>,
        rosterB: List<Player>,
        rng: Random
    ): MatchSimInternal {
        // Rating difference influences win probability while preserving authentic T20 upsets
        val diff = (ratingA - ratingB).toDouble()
        val probA = (1.0 / (1.0 + kotlin.math.exp(-diff / 6.2))).coerceIn(0.16, 0.84)
        val teamAWins = rng.nextDouble() < probA
        val winner = if (teamAWins) teamA else teamB
        val winningRoster = if (teamAWins) rosterA else rosterB

        val chaseWin = rng.nextBoolean()
        val baseScore = 166 + ((ratingA + ratingB - 150) / 3).coerceIn(-18, 28) + rng.nextInt(-18, 26)
        val firstInningsRuns = baseScore.coerceIn(132, 236)
        val firstInningsWickets = rng.nextInt(3, 10)

        val scoreA: String
        val scoreB: String
        val marginText: String
        val nrrDelta: Double

        if (chaseWin) {
            val chasingRuns = firstInningsRuns + rng.nextInt(1, 7)
            val chasingWickets = rng.nextInt(1, 8)
            val ballsLeft = rng.nextInt(1, 22)
            val oversCompleted = 20 - (ballsLeft / 6) - if (ballsLeft % 6 > 0) 1 else 0
            val ballsInOver = if (ballsLeft % 6 == 0) 0 else 6 - (ballsLeft % 6)
            val oversStr = if (ballsInOver == 0) "$oversCompleted.0" else "$oversCompleted.$ballsInOver"
            val wicketsRemaining = (10 - chasingWickets).coerceAtLeast(1)
            marginText = "Won by $wicketsRemaining wkts (${ballsLeft}b left)"
            nrrDelta = ((ballsLeft.toDouble() * 0.042) + 0.14).coerceIn(0.12, 1.55)

            if (teamAWins) {
                scoreB = "$firstInningsRuns/$firstInningsWickets (20.0)"
                scoreA = "$chasingRuns/$chasingWickets ($oversStr)"
            } else {
                scoreA = "$firstInningsRuns/$firstInningsWickets (20.0)"
                scoreB = "$chasingRuns/$chasingWickets ($oversStr)"
            }
        } else {
            val runMargin = rng.nextInt(4, 42)
            val secondInningsRuns = (firstInningsRuns - runMargin).coerceAtLeast(108)
            val secondInningsWickets = rng.nextInt(5, 10)
            marginText = "Won by $runMargin runs"
            nrrDelta = (runMargin.toDouble() / 20.0).coerceIn(0.18, 2.05)

            if (teamAWins) {
                scoreA = "$firstInningsRuns/$firstInningsWickets (20.0)"
                scoreB = "$secondInningsRuns/$secondInningsWickets (20.0)"
            } else {
                scoreB = "$firstInningsRuns/$firstInningsWickets (20.0)"
                scoreA = "$secondInningsRuns/$secondInningsWickets (20.0)"
            }
        }

        val potmPlayer = winningRoster
            .sortedByDescending { it.form + rng.nextInt(-10, 14) }
            .firstOrNull()
        val potmLabel = if (potmPlayer != null) {
            if (potmPlayer.role == PlayerRole.FAST_BOWLER || potmPlayer.role == PlayerRole.SPINNER) {
                "${potmPlayer.name} (${rng.nextInt(2, 5)}/${rng.nextInt(16, 31)})"
            } else {
                "${potmPlayer.name} (${rng.nextInt(48, 92)}* off ${rng.nextInt(28, 54)})"
            }
        } else {
            "${winner.shortCode} Match-Winner"
        }

        return MatchSimInternal(
            match = PlayoffMatchResult(
                stageName = stageName,
                teamA = teamA,
                teamB = teamB,
                scoreA = scoreA,
                scoreB = scoreB,
                winnerId = winner,
                marginText = marginText,
                playerOfMatch = potmLabel
            ),
            nrrDelta = abs(nrrDelta)
        )
    }

    private fun pickOrangeCapWinner(
        standings: List<LeagueStandingRow>,
        xiMap: Map<FranchiseId, List<Player>>,
        impactMap: Map<FranchiseId, Player?>,
        rng: Random
    ): SeasonAwardWinner {
        val topTeams = standings.take(6).map { it.franchiseId }
        val candidates = topTeams.flatMap { id ->
            val pool = xiMap.getValue(id) + listOfNotNull(impactMap[id])
            pool.filter { it.role == PlayerRole.BATTER || it.role == PlayerRole.WICKETKEEPER || it.role == PlayerRole.ALL_ROUNDER }
                .map { id to it }
        }
        val best = candidates.maxByOrNull { (_, p) ->
            (p.form * 0.65 + p.hype * 0.35) + rng.nextDouble(-6.0, 9.0)
        }
        return if (best != null) {
            val runs = (515 + ((best.second.form - 75) * 11) + rng.nextInt(0, 75)).coerceIn(490, 815)
            val sr = (138 + rng.nextInt(0, 34))
            SeasonAwardWinner(best.second.name, best.first, "$runs runs • SR $sr")
        } else {
            SeasonAwardWinner("Top Scorer", standings.first().franchiseId, "584 runs • SR 148")
        }
    }

    private fun pickPurpleCapWinner(
        standings: List<LeagueStandingRow>,
        xiMap: Map<FranchiseId, List<Player>>,
        impactMap: Map<FranchiseId, Player?>,
        rng: Random
    ): SeasonAwardWinner {
        val topTeams = standings.take(6).map { it.franchiseId }
        val candidates = topTeams.flatMap { id ->
            val pool = xiMap.getValue(id) + listOfNotNull(impactMap[id])
            pool.filter { it.role == PlayerRole.FAST_BOWLER || it.role == PlayerRole.SPINNER }
                .map { id to it }
        }
        val best = candidates.maxByOrNull { (_, p) ->
            (p.form * 0.68 + p.hype * 0.32) + rng.nextDouble(-6.0, 9.0)
        }
        return if (best != null) {
            val wkts = (18 + ((best.second.form - 75) / 2) + rng.nextInt(0, 5)).coerceIn(18, 31)
            val econ = 7.1 + rng.nextDouble(0.0, 1.45)
            val econFmt = String.format(java.util.Locale.US, "%.2f", econ)
            SeasonAwardWinner(best.second.name, best.first, "$wkts wkts • Econ $econFmt")
        } else {
            SeasonAwardWinner("Leading Bowler", standings.first().franchiseId, "24 wkts • Econ 7.45")
        }
    }

    fun computeLineupPowerRating(xiPlayers: List<Player>, impactPlayer: Player?): Int {
        if (xiPlayers.isEmpty()) return 0
        val avgPlayerQuality = xiPlayers.map { playerMatchImpactScore(it) }.average()
        val impactBonus = impactPlayer?.let { (playerMatchImpactScore(it) - 72.0) * 0.14 } ?: -2.0

        val hasWk = xiPlayers.any { it.role == PlayerRole.WICKETKEEPER }
        val bowlers = xiPlayers.count { isBowlingOption(it) } + if (impactPlayer != null && isBowlingOption(impactPlayer)) 1 else 0
        val batters = xiPlayers.count { it.role == PlayerRole.BATTER || it.role == PlayerRole.WICKETKEEPER || it.role == PlayerRole.ALL_ROUNDER }
        val hasCaptain = xiPlayers.any { it.isCaptainMaterial }

        var balanceAdj = 0.0
        if (!hasWk) balanceAdj -= 4.5
        if (bowlers < 5) balanceAdj -= (5 - bowlers) * 3.5
        if (bowlers >= 6) balanceAdj += 2.0
        if (batters < 6) balanceAdj -= 3.0
        if (hasCaptain) balanceAdj += 1.5
        if (xiPlayers.size < PLAYING_XI_SIZE) balanceAdj -= (PLAYING_XI_SIZE - xiPlayers.size) * 4.5

        return (avgPlayerQuality + impactBonus + balanceAdj).roundToInt().coerceIn(40, 99)
    }

    private fun playerMatchImpactScore(player: Player): Double {
        val base = player.form * 0.66 + player.hype * 0.34
        val captainBoost = if (player.isCaptainMaterial) 1.4 else 0.0
        return base + captainBoost
    }

    private fun isBowlingOption(player: Player): Boolean {
        return player.role == PlayerRole.FAST_BOWLER ||
            player.role == PlayerRole.SPINNER ||
            player.role == PlayerRole.ALL_ROUNDER
    }
}
