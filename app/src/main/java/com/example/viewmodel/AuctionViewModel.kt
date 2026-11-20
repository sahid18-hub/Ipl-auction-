package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.BroadcastSoundEngine
import com.example.data.AuctionPersistence
import com.example.data.FranchiseCatalog
import com.example.data.PersistedAuctionSnapshot
import com.example.data.PlayerDatabase
import com.example.engine.AiAuctionBrain
import com.example.engine.AiPlayerEvaluation
import com.example.engine.AuctionGrader
import com.example.engine.AuctionRules
import com.example.engine.BidEligibility
import com.example.engine.TournamentSimulator
import com.example.engine.ValuationEngine
import com.example.model.AcquiredPlayer
import com.example.model.AuctionPhase
import com.example.model.AuctionSet
import com.example.model.AuctionTab
import com.example.model.FranchiseId
import com.example.model.FranchiseState
import com.example.model.HammerStage
import com.example.model.LineupValidation
import com.example.model.PaddleEvent
import com.example.model.PaddleEventType
import com.example.model.Player
import com.example.model.SavedAuctionSummary
import com.example.model.SeasonSimulationResult
import com.example.model.SetPlayerStatus
import com.example.model.SetRosterItem
import com.example.model.SetSummaryData
import com.example.model.SoldRecord
import com.example.model.TeamLineup
import com.example.model.WrapUpAnalytics
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

data class AuctionUiState(
    val phase: AuctionPhase = AuctionPhase.FRANCHISE_SELECT,
    val selectedFranchiseId: FranchiseId = FranchiseId.CSK,
    val franchises: Map<FranchiseId, FranchiseState> = initialFranchises(FranchiseId.CSK),
    val currentSet: AuctionSet = AuctionSet.MARQUEE_1,
    val currentSetPlayers: List<Player> = emptyList(),
    val currentPlayerIndex: Int = 0,
    val currentTrueMarketValueLakhs: Int = 0,
    val currentBidLakhs: Int = 0,
    val hasOpeningBid: Boolean = false,
    val highestBidderId: FranchiseId? = null,
    val hammerStage: HammerStage = HammerStage.WAITING_OPENING_BID,
    val countdownProgress: Float = 1f,
    val finalCallProgress: Float = 1f,
    val finalCallRemainingSeconds: Float = 2.5f,
    val paddleFeed: List<PaddleEvent> = emptyList(),
    val activeBiddingTeams: Set<FranchiseId> = emptySet(),
    val backedOutTeams: Set<FranchiseId> = emptySet(),
    val quickResultBanner: SoldRecord? = null,
    val isPaused: Boolean = false,
    val currentSetRecords: List<SoldRecord> = emptyList(),
    val allRecords: List<SoldRecord> = emptyList(),
    val unsoldPool: List<Player> = emptyList(),
    val setSummary: SetSummaryData? = null,
    val wrapUpAnalytics: WrapUpAnalytics? = null,
    val selectedTab: AuctionTab = AuctionTab.STAGE,
    val inspectedFranchiseId: FranchiseId? = null,
    val showSetRosterViewer: Boolean = false,
    val selectedPreviewSet: AuctionSet = AuctionSet.MARQUEE_1,
    val isSoundMuted: Boolean = false,
    val savedAuctionSummary: SavedAuctionSummary? = null,
    val userPlayingXiIds: List<String> = emptyList(),
    val userImpactPlayerId: String? = null,
    val lineupSelectionError: String? = null,
    val seasonSimulationResult: SeasonSimulationResult? = null,
    val userNominatedPlayerIds: Set<String> = emptySet(),
    val aiNominatedPlayersByTeam: Map<FranchiseId, List<String>> = emptyMap(),
    val isAcceleratedEmergencyContinuation: Boolean = false
) {
    val userFranchise: FranchiseState
        get() = franchises.getValue(selectedFranchiseId)

    val userLineupValidation: LineupValidation
        get() = TournamentSimulator.validateLineup(
            franchiseState = userFranchise,
            playingXiIds = userPlayingXiIds,
            impactPlayerId = userImpactPlayerId
        )

    val currentPlayer: Player?
        get() = currentSetPlayers.getOrNull(currentPlayerIndex)

    val availablePreviewSets: List<AuctionSet>
        get() {
            val activeAndUpcoming = PlayerDatabase.primarySetsOrdered.filter {
                it.setNumber >= currentSet.setNumber
            }
            val withAccelerated = if (currentSet == AuctionSet.ACCELERATED_ROUND) {
                listOf(AuctionSet.ACCELERATED_ROUND)
            } else {
                activeAndUpcoming + AuctionSet.ACCELERATED_ROUND
            }
            return withAccelerated
        }

    fun rosterItemsFor(set: AuctionSet): List<SetRosterItem> {
        if (set == currentSet) {
            val recordsByPlayerId = currentSetRecords.associateBy { it.player.id }
            return currentSetPlayers.mapIndexed { index, player ->
                val record = recordsByPlayerId[player.id]
                when {
                    record != null && record.isSold -> SetRosterItem(
                        player = player,
                        status = SetPlayerStatus.SOLD,
                        winningFranchiseId = record.winningFranchiseId,
                        soldPriceLakhs = record.soldPriceLakhs
                    )
                    record != null && !record.isSold -> SetRosterItem(
                        player = player,
                        status = SetPlayerStatus.UNSOLD
                    )
                    index == currentPlayerIndex && quickResultBanner == null -> SetRosterItem(
                        player = player,
                        status = SetPlayerStatus.LIVE_NOW,
                        liveBidLakhs = if (hasOpeningBid) currentBidLakhs else player.basePriceLakhs,
                        liveLeaderId = highestBidderId
                    )
                    else -> SetRosterItem(
                        player = player,
                        status = SetPlayerStatus.UPCOMING
                    )
                }
            }
        }

        if (set == AuctionSet.ACCELERATED_ROUND) {
            return unsoldPool
                .sortedByDescending { (it.form * 0.6 + it.hype * 0.4) }
                .map { player ->
                    SetRosterItem(
                        player = player,
                        status = SetPlayerStatus.UPCOMING
                    )
                }
        }

        val allRecordsByPlayerId = allRecords.filter { it.setSoldIn == set }.associateBy { it.player.id }
        return PlayerDatabase.playersForSet(set).map { player ->
            val record = allRecordsByPlayerId[player.id]
            when {
                record != null && record.isSold -> SetRosterItem(
                    player = player,
                    status = SetPlayerStatus.SOLD,
                    winningFranchiseId = record.winningFranchiseId,
                    soldPriceLakhs = record.soldPriceLakhs
                )
                record != null && !record.isSold -> SetRosterItem(
                    player = player,
                    status = SetPlayerStatus.UNSOLD
                )
                else -> SetRosterItem(
                    player = player,
                    status = SetPlayerStatus.UPCOMING
                )
            }
        }
    }

    val nextLegalBidLakhs: Int
        get() {
            val player = currentPlayer ?: return 0
            return AuctionRules.nextBidAmountLakhs(
                currentBidLakhs = currentBidLakhs,
                basePriceLakhs = player.basePriceLakhs,
                hasOpeningBid = hasOpeningBid
            )
        }

    val userBidEligibility: BidEligibility
        get() {
            val player = currentPlayer ?: return BidEligibility(false, "No player on stage")
            if (quickResultBanner != null || hammerStage == HammerStage.SOLD || hammerStage == HammerStage.UNSOLD) {
                return BidEligibility(false, "Lot finalized")
            }
            return AuctionRules.evaluateBidEligibility(
                franchise = userFranchise,
                player = player,
                bidAmountLakhs = nextLegalBidLakhs,
                currentHighestBidder = highestBidderId
            )
        }
}

private fun initialFranchises(userFranchiseId: FranchiseId): Map<FranchiseId, FranchiseState> {
    return FranchiseId.entries.associateWith { id ->
        FranchiseState(
            id = id,
            purseLakhs = AuctionRules.STARTING_PURSE_LAKHS,
            squad = emptyList(),
            isUserControlled = id == userFranchiseId
        )
    }
}

class AuctionViewModel(application: Application) : AndroidViewModel(application) {

    private val persistence = AuctionPersistence(application)
    private val soundEngine = BroadcastSoundEngine()

    private val _uiState = MutableStateFlow(
        AuctionUiState(
            isSoundMuted = persistence.isSoundMuted(),
            savedAuctionSummary = persistence.loadSummary()
        )
    )
    val uiState: StateFlow<AuctionUiState> = _uiState.asStateFlow()

    private var currentSessionSeed: Int = Random.nextInt()
    private var aiBrain = AiAuctionBrain(currentSessionSeed)
    private var auctionLoopJob: Job? = null
    private var bannerAdvanceJob: Job? = null
    private var eventIdCounter = 1L

    private var currentAiEvaluations: Map<FranchiseId, AiPlayerEvaluation> = emptyMap()
    private val recentBiddersForLot = mutableListOf<FranchiseId>()
    private var remainingPoolCache: List<Player> = emptyList()

    init {
        soundEngine.isMuted = _uiState.value.isSoundMuted
    }

    fun selectFranchise(franchiseId: FranchiseId) {
        _uiState.update { state ->
            state.copy(
                selectedFranchiseId = franchiseId,
                franchises = initialFranchises(franchiseId)
            )
        }
    }

    fun toggleSoundMute() {
        val nextMuted = !_uiState.value.isSoundMuted
        soundEngine.isMuted = nextMuted
        persistence.setSoundMuted(nextMuted)
        _uiState.update { it.copy(isSoundMuted = nextMuted) }
    }

    fun startAuction() {
        cancelTimers()
        persistence.clearSavedAuction()
        currentSessionSeed = Random.nextInt()
        aiBrain = AiAuctionBrain(currentSessionSeed)
        val userTeam = _uiState.value.selectedFranchiseId
        val firstSet = AuctionSet.MARQUEE_1
        val setPlayers = buildSetOrder(firstSet)
        remainingPoolCache = PlayerDatabase.allPlayers

        _uiState.value = AuctionUiState(
            phase = AuctionPhase.LIVE_AUCTION,
            selectedFranchiseId = userTeam,
            franchises = initialFranchises(userTeam),
            currentSet = firstSet,
            currentSetPlayers = setPlayers,
            currentPlayerIndex = 0,
            selectedTab = AuctionTab.STAGE,
            isSoundMuted = _uiState.value.isSoundMuted,
            savedAuctionSummary = null
        )
        presentCurrentPlayer()
    }

    fun resumeSavedAuction() {
        val snapshot = persistence.loadSnapshot() ?: run {
            _uiState.update { it.copy(savedAuctionSummary = null) }
            return
        }
        cancelTimers()
        currentSessionSeed = snapshot.sessionSeed
        aiBrain = AiAuctionBrain(currentSessionSeed)
        eventIdCounter = (snapshot.paddleFeed.maxOfOrNull { it.id } ?: 0L) + 1L

        if (snapshot.phase == AuctionPhase.ACCELERATED_NOMINATION) {
            val aiNominations = aiBrain.nominateUnsoldPlayersForFranchises(
                franchises = snapshot.franchises,
                userFranchiseId = snapshot.selectedFranchiseId,
                unsoldPool = snapshot.unsoldPool
            )
            _uiState.value = AuctionUiState(
                phase = AuctionPhase.ACCELERATED_NOMINATION,
                selectedFranchiseId = snapshot.selectedFranchiseId,
                franchises = snapshot.franchises,
                currentSet = AuctionSet.ACCELERATED_ROUND,
                currentSetPlayers = emptyList(),
                currentPlayerIndex = 0,
                currentSetRecords = emptyList(),
                allRecords = snapshot.allRecords,
                unsoldPool = snapshot.unsoldPool,
                paddleFeed = snapshot.paddleFeed,
                selectedPreviewSet = AuctionSet.ACCELERATED_ROUND,
                isSoundMuted = _uiState.value.isSoundMuted,
                savedAuctionSummary = persistence.loadSummary(),
                userNominatedPlayerIds = emptySet(),
                aiNominatedPlayersByTeam = aiNominations,
                isAcceleratedEmergencyContinuation = false
            )
        } else if (snapshot.phase == AuctionPhase.SET_SUMMARY ||
            snapshot.currentPlayerIndex >= snapshot.currentSetPlayers.size
        ) {
            val nextSet = resolveNextSet(snapshot.currentSet, snapshot.franchises, snapshot.unsoldPool)
            val summary = SetSummaryData(
                set = snapshot.currentSet,
                records = snapshot.currentSetRecords,
                hasNextSet = nextSet != null,
                nextSet = nextSet
            )
            _uiState.value = AuctionUiState(
                phase = AuctionPhase.SET_SUMMARY,
                selectedFranchiseId = snapshot.selectedFranchiseId,
                franchises = snapshot.franchises,
                currentSet = snapshot.currentSet,
                currentSetPlayers = snapshot.currentSetPlayers,
                currentPlayerIndex = snapshot.currentPlayerIndex,
                currentSetRecords = snapshot.currentSetRecords,
                allRecords = snapshot.allRecords,
                unsoldPool = snapshot.unsoldPool,
                paddleFeed = snapshot.paddleFeed,
                setSummary = summary,
                selectedPreviewSet = snapshot.currentSet,
                isSoundMuted = _uiState.value.isSoundMuted,
                savedAuctionSummary = persistence.loadSummary()
            )
        } else {
            _uiState.value = AuctionUiState(
                phase = AuctionPhase.LIVE_AUCTION,
                selectedFranchiseId = snapshot.selectedFranchiseId,
                franchises = snapshot.franchises,
                currentSet = snapshot.currentSet,
                currentSetPlayers = snapshot.currentSetPlayers,
                currentPlayerIndex = snapshot.currentPlayerIndex,
                currentSetRecords = snapshot.currentSetRecords,
                allRecords = snapshot.allRecords,
                unsoldPool = snapshot.unsoldPool,
                paddleFeed = snapshot.paddleFeed,
                selectedTab = AuctionTab.STAGE,
                selectedPreviewSet = snapshot.currentSet,
                isSoundMuted = _uiState.value.isSoundMuted,
                savedAuctionSummary = persistence.loadSummary()
            )
            presentCurrentPlayer()
        }
    }

    fun resetToFranchiseSelect() {
        cancelTimers()
        val currentTeam = _uiState.value.selectedFranchiseId
        val muted = _uiState.value.isSoundMuted
        _uiState.value = AuctionUiState(
            phase = AuctionPhase.FRANCHISE_SELECT,
            selectedFranchiseId = currentTeam,
            franchises = initialFranchises(currentTeam),
            isSoundMuted = muted,
            savedAuctionSummary = persistence.loadSummary()
        )
    }

    fun selectTab(tab: AuctionTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun inspectFranchise(franchiseId: FranchiseId?) {
        _uiState.update { it.copy(inspectedFranchiseId = franchiseId) }
    }

    fun togglePause() {
        _uiState.update { it.copy(isPaused = !it.isPaused) }
    }

    fun openSetRosterViewer() {
        _uiState.update { current ->
            current.copy(
                showSetRosterViewer = true,
                selectedPreviewSet = current.currentSet
            )
        }
    }

    fun closeSetRosterViewer() {
        _uiState.update { it.copy(showSetRosterViewer = false) }
    }

    fun selectPreviewSet(set: AuctionSet) {
        _uiState.update { it.copy(selectedPreviewSet = set) }
    }

    fun placeUserBid() {
        val state = _uiState.value
        val player = state.currentPlayer ?: return
        if (state.phase != AuctionPhase.LIVE_AUCTION) return
        if (!state.userBidEligibility.allowed) return

        val bidAmount = state.nextLegalBidLakhs
        val userId = state.selectedFranchiseId
        val profile = FranchiseCatalog.get(userId)

        recentBiddersForLot.add(userId)
        val newEvent = createEvent(
            franchiseId = userId,
            type = PaddleEventType.BID_PLACED,
            amountLakhs = bidAmount,
            message = "${profile.shortCode} (YOU) raised paddle to ${AuctionRules.formatCrores(bidAmount)}"
        )

        _uiState.update { current ->
            current.copy(
                currentBidLakhs = bidAmount,
                hasOpeningBid = true,
                highestBidderId = userId,
                hammerStage = HammerStage.ACTIVE_BIDDING,
                countdownProgress = 1f,
                finalCallProgress = 1f,
                finalCallRemainingSeconds = 2.5f,
                activeBiddingTeams = current.activeBiddingTeams + userId,
                paddleFeed = (listOf(newEvent) + current.paddleFeed).take(24),
                isPaused = false
            )
        }

        soundEngine.playBidPaddleTick()

        // Immediately cancel any Going Once / Going Twice / Final Call warning & reset bidding cycle
        launchLiveBiddingLoop()
    }

    /**
     * SKIP PLAYER: User steps out. Immediately runs full AI bidding loop to completion
     * so the player sells at their true realistic market price and shows a quick result banner.
     */
    fun skipPlayer() {
        val state = _uiState.value
        val player = state.currentPlayer ?: return
        if (state.phase != AuctionPhase.LIVE_AUCTION || state.quickResultBanner != null) return

        cancelTimers()

        val isAccelerated = state.currentSet == AuctionSet.ACCELERATED_ROUND
        val simResult = aiBrain.simulatePlayerToCompletion(
            player = player,
            franchises = state.franchises,
            userFranchiseId = state.selectedFranchiseId,
            remainingPool = remainingPoolCache,
            currentSet = state.currentSet,
            isAcceleratedRound = isAccelerated,
            startingBidLakhs = state.currentBidLakhs,
            startingHighestBidder = state.highestBidderId
        )

        val record = applyLotOutcome(
            player = player,
            winningFranchiseId = simResult.winningFranchiseId,
            soldPriceLakhs = simResult.finalPriceLakhs,
            trueMarketValueLakhs = state.currentTrueMarketValueLakhs,
            set = state.currentSet,
            wasInstantSkip = true,
            participatingTeams = simResult.biddingTeams
        )

        scheduleAdvanceAfterBanner(record)
    }

    /**
     * SKIP ENTIRE SET: Rapidly simulates all remaining players in the current set
     * using the full AI valuation & squad-balance engine, then shows the Set Summary Modal.
     */
    fun skipEntireSet() {
        val state = _uiState.value
        if (state.phase != AuctionPhase.LIVE_AUCTION) return
        cancelTimers()

        val workingFranchises = state.franchises.toMutableMap()
        val workingSetRecords = state.currentSetRecords.toMutableList()
        val workingAllRecords = state.allRecords.toMutableList()
        val workingUnsold = state.unsoldPool.toMutableList()
        val isAccelerated = state.currentSet == AuctionSet.ACCELERATED_ROUND

        val startIndex = if (state.quickResultBanner != null) {
            state.currentPlayerIndex + 1
        } else {
            state.currentPlayerIndex
        }

        for (idx in startIndex until state.currentSetPlayers.size) {
            val player = state.currentSetPlayers[idx]
            val startingBid = if (idx == state.currentPlayerIndex && state.quickResultBanner == null) {
                state.currentBidLakhs
            } else {
                0
            }
            val startingLeader = if (idx == state.currentPlayerIndex && state.quickResultBanner == null) {
                state.highestBidderId
            } else {
                null
            }

            val remainingPool = state.currentSetPlayers.drop(idx) + futureSetsPlayers(state.currentSet)
            val tmv = ValuationEngine.calculateTrueMarketValueLakhs(player, remainingPool)

            val sim = aiBrain.simulatePlayerToCompletion(
                player = player,
                franchises = workingFranchises,
                userFranchiseId = state.selectedFranchiseId,
                remainingPool = remainingPool,
                currentSet = state.currentSet,
                isAcceleratedRound = isAccelerated,
                startingBidLakhs = startingBid,
                startingHighestBidder = startingLeader
            )

            val record = SoldRecord(
                player = player,
                winningFranchiseId = sim.winningFranchiseId,
                soldPriceLakhs = sim.finalPriceLakhs,
                trueMarketValueLakhs = tmv,
                setSoldIn = state.currentSet
            )
            workingSetRecords.add(record)
            workingAllRecords.add(record)

            if (sim.winningFranchiseId != null) {
                val winner = workingFranchises.getValue(sim.winningFranchiseId)
                workingFranchises[sim.winningFranchiseId] = winner.copy(
                    purseLakhs = winner.purseLakhs - sim.finalPriceLakhs,
                    squad = winner.squad + AcquiredPlayer(
                        player = player,
                        boughtPriceLakhs = sim.finalPriceLakhs,
                        trueMarketValueLakhs = tmv,
                        setAcquired = state.currentSet
                    )
                )
            } else if (!isAccelerated) {
                workingUnsold.add(player)
            }

            // If in Accelerated Emergency Continuation (<18 squad completion), stop immediately once all 10 teams hit 18!
            if (isAccelerated && state.isAcceleratedEmergencyContinuation &&
                workingFranchises.values.all { it.squadCount >= AuctionRules.MIN_SQUAD_SIZE }
            ) {
                break
            }
        }

        // If nominated Accelerated shortlist finished while a team still has < 18 players, auto-continue with remaining unsold players
        if (isAccelerated && !state.isAcceleratedEmergencyContinuation &&
            workingFranchises.values.any { it.squadCount < AuctionRules.MIN_SQUAD_SIZE } &&
            workingUnsold.isNotEmpty()
        ) {
            val remainingToRun = workingUnsold
                .sortedByDescending { (it.form * 0.6 + it.hype * 0.4) }
                .map { it.copy(set = AuctionSet.ACCELERATED_ROUND) }
            workingUnsold.clear()

            for (extraPlayer in remainingToRun) {
                if (workingFranchises.values.all { it.squadCount >= AuctionRules.MIN_SQUAD_SIZE }) break
                val tmv = ValuationEngine.calculateTrueMarketValueLakhs(extraPlayer, remainingToRun)
                val sim = aiBrain.simulatePlayerToCompletion(
                    player = extraPlayer,
                    franchises = workingFranchises,
                    userFranchiseId = state.selectedFranchiseId,
                    remainingPool = remainingToRun,
                    currentSet = AuctionSet.ACCELERATED_ROUND,
                    isAcceleratedRound = true
                )
                val record = SoldRecord(
                    player = extraPlayer,
                    winningFranchiseId = sim.winningFranchiseId,
                    soldPriceLakhs = sim.finalPriceLakhs,
                    trueMarketValueLakhs = tmv,
                    setSoldIn = AuctionSet.ACCELERATED_ROUND
                )
                workingSetRecords.add(record)
                workingAllRecords.add(record)
                if (sim.winningFranchiseId != null) {
                    val winner = workingFranchises.getValue(sim.winningFranchiseId)
                    workingFranchises[sim.winningFranchiseId] = winner.copy(
                        purseLakhs = winner.purseLakhs - sim.finalPriceLakhs,
                        squad = winner.squad + AcquiredPlayer(
                            player = extraPlayer,
                            boughtPriceLakhs = sim.finalPriceLakhs,
                            trueMarketValueLakhs = tmv,
                            setAcquired = AuctionSet.ACCELERATED_ROUND
                        )
                    )
                }
            }
        }

        openSetSummary(
            franchises = workingFranchises,
            set = state.currentSet,
            setRecords = workingSetRecords,
            allRecords = workingAllRecords,
            unsoldPool = workingUnsold
        )
    }

    fun dismissQuickBannerNow() {
        val state = _uiState.value
        if (state.quickResultBanner == null) return
        bannerAdvanceJob?.cancel()
        advanceToNextPlayerOrSummary()
    }

    fun proceedFromSetSummary() {
        val state = _uiState.value
        val summary = state.setSummary ?: return

        val nextSet = summary.nextSet
        if (nextSet == null) {
            finalizeAuctionWrapUp()
            return
        }

        // Authentic Accelerated Round Nomination: open nomination modal instead of dumping all unsold players
        if (nextSet == AuctionSet.ACCELERATED_ROUND) {
            if (state.unsoldPool.isEmpty()) {
                finalizeAuctionWrapUp()
                return
            }
            val aiNominations = aiBrain.nominateUnsoldPlayersForFranchises(
                franchises = state.franchises,
                userFranchiseId = state.selectedFranchiseId,
                unsoldPool = state.unsoldPool
            )
            _uiState.update { current ->
                current.copy(
                    phase = AuctionPhase.ACCELERATED_NOMINATION,
                    currentSet = AuctionSet.ACCELERATED_ROUND,
                    currentSetPlayers = emptyList(),
                    currentPlayerIndex = 0,
                    currentSetRecords = emptyList(),
                    setSummary = null,
                    quickResultBanner = null,
                    showSetRosterViewer = false,
                    selectedPreviewSet = AuctionSet.ACCELERATED_ROUND,
                    userNominatedPlayerIds = emptySet(),
                    aiNominatedPlayersByTeam = aiNominations,
                    isAcceleratedEmergencyContinuation = false
                )
            }
            persistActiveAuctionSnapshot(phaseOverride = AuctionPhase.ACCELERATED_NOMINATION)
            return
        }

        val nextPlayers = buildSetOrder(nextSet)
        if (nextPlayers.isEmpty()) {
            finalizeAuctionWrapUp()
            return
        }

        _uiState.update { current ->
            current.copy(
                phase = AuctionPhase.LIVE_AUCTION,
                currentSet = nextSet,
                currentSetPlayers = nextPlayers,
                currentPlayerIndex = 0,
                currentSetRecords = emptyList(),
                setSummary = null,
                quickResultBanner = null,
                selectedTab = AuctionTab.STAGE,
                showSetRosterViewer = false,
                selectedPreviewSet = nextSet
            )
        }
        persistActiveAuctionSnapshot(nextPlayerIndexOverride = 0)
        presentCurrentPlayer()
    }

    fun toggleUserAcceleratedNomination(playerId: String) {
        _uiState.update { current ->
            val selected = current.userNominatedPlayerIds
            val updated = if (playerId in selected) {
                selected - playerId
            } else if (selected.size < 5) {
                selected + playerId
            } else {
                selected
            }
            current.copy(userNominatedPlayerIds = updated)
        }
    }

    fun startAcceleratedRoundFromNominations() {
        val state = _uiState.value
        if (state.phase != AuctionPhase.ACCELERATED_NOMINATION) return

        val aiNominatedIds = state.aiNominatedPlayersByTeam.values.flatten().toSet()
        val combinedIds = state.userNominatedPlayerIds + aiNominatedIds

        val nominatedShortlist = state.unsoldPool
            .filter { it.id in combinedIds }
            .sortedByDescending { (it.form * 0.6 + it.hype * 0.4) }
            .map { it.copy(set = AuctionSet.ACCELERATED_ROUND) }

        val remainingUnnominatedUnsold = state.unsoldPool.filter { it.id !in combinedIds }

        if (nominatedShortlist.isEmpty()) {
            val anyTeamUnderMin = state.franchises.values.any { it.squadCount < AuctionRules.MIN_SQUAD_SIZE }
            if (anyTeamUnderMin && remainingUnnominatedUnsold.isNotEmpty()) {
                val fallbackList = remainingUnnominatedUnsold
                    .sortedByDescending { (it.form * 0.6 + it.hype * 0.4) }
                    .map { it.copy(set = AuctionSet.ACCELERATED_ROUND) }
                _uiState.update { current ->
                    current.copy(
                        phase = AuctionPhase.LIVE_AUCTION,
                        currentSet = AuctionSet.ACCELERATED_ROUND,
                        currentSetPlayers = fallbackList,
                        currentPlayerIndex = 0,
                        currentSetRecords = emptyList(),
                        unsoldPool = emptyList(),
                        selectedTab = AuctionTab.STAGE,
                        selectedPreviewSet = AuctionSet.ACCELERATED_ROUND,
                        isAcceleratedEmergencyContinuation = true
                    )
                }
                persistActiveAuctionSnapshot(nextPlayerIndexOverride = 0)
                presentCurrentPlayer()
            } else {
                finalizeAuctionWrapUp()
            }
            return
        }

        _uiState.update { current ->
            current.copy(
                phase = AuctionPhase.LIVE_AUCTION,
                currentSet = AuctionSet.ACCELERATED_ROUND,
                currentSetPlayers = nominatedShortlist,
                currentPlayerIndex = 0,
                currentSetRecords = emptyList(),
                unsoldPool = remainingUnnominatedUnsold,
                selectedTab = AuctionTab.STAGE,
                selectedPreviewSet = AuctionSet.ACCELERATED_ROUND,
                isAcceleratedEmergencyContinuation = false
            )
        }
        persistActiveAuctionSnapshot(nextPlayerIndexOverride = 0)
        presentCurrentPlayer()
    }

    private fun presentCurrentPlayer() {
        cancelTimers()
        val state = _uiState.value
        val player = state.currentPlayer
        if (player == null) {
            openSetSummary(
                franchises = state.franchises,
                set = state.currentSet,
                setRecords = state.currentSetRecords,
                allRecords = state.allRecords,
                unsoldPool = state.unsoldPool
            )
            return
        }

        remainingPoolCache = state.currentSetPlayers.drop(state.currentPlayerIndex) +
            futureSetsPlayers(state.currentSet)
        val tmv = ValuationEngine.calculateTrueMarketValueLakhs(player, remainingPoolCache)
        val isAccelerated = state.currentSet == AuctionSet.ACCELERATED_ROUND

        currentAiEvaluations = state.franchises.values
            .filter { it.id != state.selectedFranchiseId }
            .associate { franchiseState ->
                franchiseState.id to aiBrain.evaluateForFranchise(
                    franchiseState = franchiseState,
                    player = player,
                    trueMarketValueLakhs = tmv,
                    currentSet = state.currentSet,
                    isAcceleratedRound = isAccelerated
                )
            }
        recentBiddersForLot.clear()

        val openingEvent = createEvent(
            franchiseId = null,
            type = PaddleEventType.OPENING_CALL,
            amountLakhs = player.basePriceLakhs,
            message = "${player.name} enters the stage • Base ${AuctionRules.formatCrores(player.basePriceLakhs)}"
        )

        _uiState.update { current ->
            current.copy(
                currentTrueMarketValueLakhs = tmv,
                currentBidLakhs = 0,
                hasOpeningBid = false,
                highestBidderId = null,
                hammerStage = HammerStage.WAITING_OPENING_BID,
                countdownProgress = 1f,
                finalCallProgress = 1f,
                finalCallRemainingSeconds = 2.5f,
                paddleFeed = listOf(openingEvent),
                activeBiddingTeams = emptySet(),
                backedOutTeams = emptySet(),
                quickResultBanner = null,
                isPaused = false
            )
        }

        launchLiveBiddingLoop()
    }

    private fun launchLiveBiddingLoop() {
        auctionLoopJob?.cancel()
        auctionLoopJob = viewModelScope.launch {
            val isAccelerated = _uiState.value.currentSet == AuctionSet.ACCELERATED_ROUND
            var elapsedMs = 0L
            // Normal AI reaction fires promptly (550–950ms); Accelerated Round fires even faster (350–620ms)
            var nextAiTriggerMs = computeNextAiDelayMs(isAccelerated)
            var goingOnceLogged = false
            var goingTwiceLogged = false
            var finalCallLogged = false

            // 3-step official auctioneer cadence once bidding pauses:
            // Standard sets: 1.5s Going Once -> 1.5s Going Twice -> 2.5s Final Call
            // Accelerated Round: Snappy 1.0s Going Once -> 1.0s Going Twice -> 1.8s Final Call
            val goingOnceStartMs = if (isAccelerated) 600L else 1000L
            val goingTwiceStartMs = if (isAccelerated) 1600L else 2500L
            val finalCallStartMs = if (isAccelerated) 2600L else 4000L
            val finalCallDurationMs = if (isAccelerated) 1800L else 2500L
            val hammerFallMs = finalCallStartMs + finalCallDurationMs
            val tickMs = 50L

            while (isActive) {
                delay(tickMs)
                val state = _uiState.value
                if (state.phase != AuctionPhase.LIVE_AUCTION || state.quickResultBanner != null) {
                    return@launch
                }
                if (state.isPaused) continue

                val player = state.currentPlayer ?: return@launch
                elapsedMs += tickMs

                // Check AI counter-bid at trigger point (even if we are in Going Once / Twice / Final Call!)
                if (elapsedMs >= nextAiTriggerMs) {
                    val nextBid = AuctionRules.nextBidAmountLakhs(
                        currentBidLakhs = state.currentBidLakhs,
                        basePriceLakhs = player.basePriceLakhs,
                        hasOpeningBid = state.hasOpeningBid
                    )

                    // Log newly priced-out AI franchises for live broadcast realism
                    val newlyBackedOut = currentAiEvaluations.values.filter { eval ->
                        eval.franchiseId != state.highestBidderId &&
                            eval.franchiseId !in state.backedOutTeams &&
                            eval.maxWillingBidLakhs in player.basePriceLakhs until nextBid &&
                            (eval.franchiseId in state.activeBiddingTeams || state.currentBidLakhs >= player.basePriceLakhs)
                    }

                    if (newlyBackedOut.isNotEmpty()) {
                        val dropEvents = newlyBackedOut.take(2).map { eval ->
                            val code = FranchiseCatalog.get(eval.franchiseId).shortCode
                            createEvent(
                                franchiseId = eval.franchiseId,
                                type = PaddleEventType.BACKED_OUT,
                                amountLakhs = null,
                                message = "$code shook head & backed out of bidding"
                            )
                        }
                        _uiState.update { current ->
                            current.copy(
                                backedOutTeams = current.backedOutTeams + newlyBackedOut.map { it.franchiseId },
                                paddleFeed = (dropEvents + current.paddleFeed).take(24)
                            )
                        }
                    }

                    val aiBidder = aiBrain.chooseNextAiBidder(
                        evaluations = currentAiEvaluations,
                        franchises = _uiState.value.franchises,
                        player = player,
                        nextBidLakhs = nextBid,
                        currentHighestBidder = state.highestBidderId,
                        recentBidders = recentBiddersForLot,
                        userFranchiseId = state.selectedFranchiseId
                    )

                    if (aiBidder != null) {
                        recentBiddersForLot.add(aiBidder)
                        val profile = FranchiseCatalog.get(aiBidder)
                        val isOpening = !state.hasOpeningBid
                        val wasLateJumpIn = state.hammerStage == HammerStage.FINAL_CALL ||
                            state.hammerStage == HammerStage.GOING_TWICE
                        val bidMsg = when {
                            isOpening -> "${profile.shortCode} opened the bidding at ${AuctionRules.formatCrores(nextBid)}"
                            wasLateJumpIn -> "${profile.shortCode} jumped in late at ${AuctionRules.formatCrores(nextBid)}!"
                            else -> "${profile.shortCode} raised paddle to ${AuctionRules.formatCrores(nextBid)}"
                        }
                        val event = createEvent(
                            franchiseId = aiBidder,
                            type = PaddleEventType.BID_PLACED,
                            amountLakhs = nextBid,
                            message = bidMsg
                        )

                        // Immediately cancel any Going Once / Going Twice / Final Call warning & reset cycle
                        _uiState.update { current ->
                            current.copy(
                                currentBidLakhs = nextBid,
                                hasOpeningBid = true,
                                highestBidderId = aiBidder,
                                hammerStage = HammerStage.ACTIVE_BIDDING,
                                countdownProgress = 1f,
                                finalCallProgress = 1f,
                                finalCallRemainingSeconds = 2.5f,
                                activeBiddingTeams = current.activeBiddingTeams + aiBidder,
                                paddleFeed = (listOf(event) + current.paddleFeed).take(24)
                            )
                        }

                        soundEngine.playBidPaddleTick()

                        elapsedMs = 0L
                        goingOnceLogged = false
                        goingTwiceLogged = false
                        finalCallLogged = false
                        nextAiTriggerMs = computeNextAiDelayMs(isAccelerated)
                        continue
                    } else {
                        // No AI wants to bid at this price; push trigger past hammer fall
                        nextAiTriggerMs = hammerFallMs + 5000L
                    }
                }

                // Update 3-step auctioneer cadence
                val totalProgress = (1f - (elapsedMs.toFloat() / hammerFallMs.toFloat())).coerceIn(0f, 1f)
                val latestState = _uiState.value

                when {
                    elapsedMs >= hammerFallMs -> {
                        val record = applyLotOutcome(
                            player = player,
                            winningFranchiseId = latestState.highestBidderId,
                            soldPriceLakhs = if (latestState.highestBidderId != null) latestState.currentBidLakhs else 0,
                            trueMarketValueLakhs = latestState.currentTrueMarketValueLakhs,
                            set = latestState.currentSet,
                            wasInstantSkip = false,
                            participatingTeams = latestState.activeBiddingTeams.toList()
                        )
                        scheduleAdvanceAfterBanner(record)
                        return@launch
                    }
                    elapsedMs >= finalCallStartMs -> {
                        val remainingMs = (hammerFallMs - elapsedMs).coerceAtLeast(0L)
                        val finalBarProgress = (remainingMs.toFloat() / finalCallDurationMs.toFloat()).coerceIn(0f, 1f)
                        val remainingSec = remainingMs / 1000f

                        if (!finalCallLogged) {
                            finalCallLogged = true
                            soundEngine.playFinalCallAlert()
                            val callText = if (latestState.highestBidderId != null) {
                                "FINAL CALL! ${player.name} going to ${latestState.highestBidderId.shortCode} for ${AuctionRules.formatCrores(latestState.currentBidLakhs)}!"
                            } else {
                                "FINAL CALL! Last chance to open at ${AuctionRules.formatCrores(player.basePriceLakhs)}!"
                            }
                            val evt = createEvent(null, PaddleEventType.HAMMER_CALL, latestState.currentBidLakhs, callText)
                            _uiState.update { current ->
                                current.copy(
                                    hammerStage = HammerStage.FINAL_CALL,
                                    countdownProgress = totalProgress,
                                    finalCallProgress = finalBarProgress,
                                    finalCallRemainingSeconds = remainingSec,
                                    paddleFeed = (listOf(evt) + current.paddleFeed).take(24)
                                )
                            }
                        } else {
                            _uiState.update { current ->
                                current.copy(
                                    hammerStage = HammerStage.FINAL_CALL,
                                    countdownProgress = totalProgress,
                                    finalCallProgress = finalBarProgress,
                                    finalCallRemainingSeconds = remainingSec
                                )
                            }
                        }
                    }
                    elapsedMs >= goingTwiceStartMs -> {
                        if (!goingTwiceLogged) {
                            goingTwiceLogged = true
                            val callText = if (latestState.highestBidderId != null) {
                                "Going twice at ${AuctionRules.formatCrores(latestState.currentBidLakhs)} with ${latestState.highestBidderId.shortCode}..."
                            } else {
                                "Going twice at Base ${AuctionRules.formatCrores(player.basePriceLakhs)}..."
                            }
                            val evt = createEvent(null, PaddleEventType.HAMMER_CALL, latestState.currentBidLakhs, callText)
                            _uiState.update { current ->
                                current.copy(
                                    hammerStage = HammerStage.GOING_TWICE,
                                    countdownProgress = totalProgress,
                                    finalCallProgress = 1f,
                                    finalCallRemainingSeconds = 2.5f,
                                    paddleFeed = (listOf(evt) + current.paddleFeed).take(24)
                                )
                            }
                        } else {
                            _uiState.update { it.copy(countdownProgress = totalProgress) }
                        }
                    }
                    elapsedMs >= goingOnceStartMs -> {
                        if (!goingOnceLogged) {
                            goingOnceLogged = true
                            val callText = if (latestState.highestBidderId != null) {
                                "Going once at ${AuctionRules.formatCrores(latestState.currentBidLakhs)}..."
                            } else {
                                "Going once — any opening bid at ${AuctionRules.formatCrores(player.basePriceLakhs)}?"
                            }
                            val evt = createEvent(null, PaddleEventType.HAMMER_CALL, latestState.currentBidLakhs, callText)
                            _uiState.update { current ->
                                current.copy(
                                    hammerStage = HammerStage.GOING_ONCE,
                                    countdownProgress = totalProgress,
                                    finalCallProgress = 1f,
                                    finalCallRemainingSeconds = 2.5f,
                                    paddleFeed = (listOf(evt) + current.paddleFeed).take(24)
                                )
                            }
                        } else {
                            _uiState.update { it.copy(countdownProgress = totalProgress) }
                        }
                    }
                    else -> {
                        _uiState.update { it.copy(countdownProgress = totalProgress) }
                    }
                }
            }
        }
    }

    private fun computeNextAiDelayMs(isAccelerated: Boolean = false): Long {
        val roll = Random.nextDouble()
        if (isAccelerated) {
            return when {
                roll < 0.06 -> Random.nextLong(2750L, 3350L)
                roll < 0.16 -> Random.nextLong(850L, 1350L)
                else -> Random.nextLong(350L, 620L)
            }
        }
        return when {
            // 8% dramatic buzzer-beater AI bid during Final Call (cancels the warning & revives bidding!)
            roll < 0.08 -> Random.nextLong(4300L, 5100L)
            // 10% late-hesitation AI bid during Going Once
            roll < 0.18 -> Random.nextLong(1350L, 2100L)
            // 82% crisp normal broadcast paddle response
            else -> Random.nextLong(550L, 950L)
        }
    }

    private fun applyLotOutcome(
        player: Player,
        winningFranchiseId: FranchiseId?,
        soldPriceLakhs: Int,
        trueMarketValueLakhs: Int,
        set: AuctionSet,
        wasInstantSkip: Boolean,
        participatingTeams: List<FranchiseId>
    ): SoldRecord {
        val record = SoldRecord(
            player = player,
            winningFranchiseId = winningFranchiseId,
            soldPriceLakhs = soldPriceLakhs,
            trueMarketValueLakhs = trueMarketValueLakhs,
            setSoldIn = set
        )

        val stampEvent = if (winningFranchiseId != null) {
            val teamCode = FranchiseCatalog.get(winningFranchiseId).shortCode
            val rivalryNote = if (wasInstantSkip && participatingTeams.size > 1) {
                " (${participatingTeams.joinToString(" vs ") { it.shortCode }})"
            } else {
                ""
            }
            createEvent(
                franchiseId = winningFranchiseId,
                type = PaddleEventType.SOLD_STAMP,
                amountLakhs = soldPriceLakhs,
                message = "SOLD to $teamCode for ${AuctionRules.formatCrores(soldPriceLakhs)}$rivalryNote"
            )
        } else {
            createEvent(
                franchiseId = null,
                type = PaddleEventType.UNSOLD_STAMP,
                amountLakhs = null,
                message = "UNSOLD — ${player.name} passes to Accelerated Pool"
            )
        }

        _uiState.update { current ->
            val updatedFranchises = if (winningFranchiseId != null) {
                val winner = current.franchises.getValue(winningFranchiseId)
                current.franchises + (winningFranchiseId to winner.copy(
                    purseLakhs = winner.purseLakhs - soldPriceLakhs,
                    squad = winner.squad + AcquiredPlayer(
                        player = player,
                        boughtPriceLakhs = soldPriceLakhs,
                        trueMarketValueLakhs = trueMarketValueLakhs,
                        setAcquired = set
                    )
                ))
            } else {
                current.franchises
            }

            val updatedUnsold = if (winningFranchiseId == null && set != AuctionSet.ACCELERATED_ROUND) {
                current.unsoldPool + player
            } else {
                current.unsoldPool
            }

            current.copy(
                franchises = updatedFranchises,
                currentBidLakhs = if (winningFranchiseId != null) soldPriceLakhs else current.currentBidLakhs,
                highestBidderId = winningFranchiseId,
                hammerStage = if (winningFranchiseId != null) HammerStage.SOLD else HammerStage.UNSOLD,
                countdownProgress = 0f,
                quickResultBanner = record,
                currentSetRecords = current.currentSetRecords + record,
                allRecords = current.allRecords + record,
                unsoldPool = updatedUnsold,
                paddleFeed = (listOf(stampEvent) + current.paddleFeed).take(24)
            )
        }

        // Play synthesized gavel slam (with marquee fanfare on user ₹12 Cr+ win) or unsold buzzer
        val isUserMarqueeWin = winningFranchiseId != null &&
            winningFranchiseId == _uiState.value.selectedFranchiseId &&
            soldPriceLakhs >= 1200
        if (winningFranchiseId != null) {
            soundEngine.playGavelHammerSlam(isMarqueeCelebration = isUserMarqueeWin)
        } else {
            soundEngine.playUnsoldBuzzer()
        }

        // Auto-save auction state to local storage after every sold/unsold lot
        val nextIndexAfterLot = _uiState.value.currentPlayerIndex + 1
        persistActiveAuctionSnapshot(nextPlayerIndexOverride = nextIndexAfterLot)

        return record
    }

    private fun scheduleAdvanceAfterBanner(record: SoldRecord) {
        bannerAdvanceJob?.cancel()
        val isUserMarqueeWin = record.isSold &&
            record.winningFranchiseId == _uiState.value.selectedFranchiseId &&
            record.soldPriceLakhs >= 1200
        bannerAdvanceJob = viewModelScope.launch {
            val delayMs = when {
                isUserMarqueeWin -> 2150L
                record.isSold -> 1450L
                else -> 1150L
            }
            delay(delayMs)
            advanceToNextPlayerOrSummary()
        }
    }

    private fun advanceToNextPlayerOrSummary() {
        val state = _uiState.value

        // If we are in emergency Accelerated continuation (<18 squad completion), stop as soon as every team reaches 18!
        if (state.currentSet == AuctionSet.ACCELERATED_ROUND &&
            state.isAcceleratedEmergencyContinuation &&
            state.franchises.values.all { it.squadCount >= AuctionRules.MIN_SQUAD_SIZE }
        ) {
            openSetSummary(
                franchises = state.franchises,
                set = state.currentSet,
                setRecords = state.currentSetRecords,
                allRecords = state.allRecords,
                unsoldPool = state.unsoldPool
            )
            return
        }

        val nextIndex = state.currentPlayerIndex + 1
        if (nextIndex < state.currentSetPlayers.size) {
            _uiState.update {
                it.copy(
                    currentPlayerIndex = nextIndex,
                    quickResultBanner = null
                )
            }
            presentCurrentPlayer()
        } else if (state.currentSet == AuctionSet.ACCELERATED_ROUND &&
            !state.isAcceleratedEmergencyContinuation &&
            state.franchises.values.any { it.squadCount < AuctionRules.MIN_SQUAD_SIZE } &&
            state.unsoldPool.isNotEmpty()
        ) {
            // Nominated shortlist finished, but at least one team still has < 18 players -> auto-continue with remaining unsold pool
            val remainingToRun = state.unsoldPool
                .sortedByDescending { (it.form * 0.6 + it.hype * 0.4) }
                .map { it.copy(set = AuctionSet.ACCELERATED_ROUND) }
            val startOfExtensionIndex = state.currentSetPlayers.size
            _uiState.update { current ->
                current.copy(
                    currentSetPlayers = current.currentSetPlayers + remainingToRun,
                    currentPlayerIndex = startOfExtensionIndex,
                    unsoldPool = emptyList(),
                    quickResultBanner = null,
                    isAcceleratedEmergencyContinuation = true
                )
            }
            persistActiveAuctionSnapshot(nextPlayerIndexOverride = startOfExtensionIndex)
            presentCurrentPlayer()
        } else {
            openSetSummary(
                franchises = state.franchises,
                set = state.currentSet,
                setRecords = state.currentSetRecords,
                allRecords = state.allRecords,
                unsoldPool = state.unsoldPool
            )
        }
    }

    private fun openSetSummary(
        franchises: Map<FranchiseId, FranchiseState>,
        set: AuctionSet,
        setRecords: List<SoldRecord>,
        allRecords: List<SoldRecord>,
        unsoldPool: List<Player>
    ) {
        cancelTimers()
        val nextSet = resolveNextSet(set, franchises, unsoldPool)
        val summary = SetSummaryData(
            set = set,
            records = setRecords,
            hasNextSet = nextSet != null,
            nextSet = nextSet
        )
        _uiState.update { current ->
            current.copy(
                phase = AuctionPhase.SET_SUMMARY,
                franchises = franchises,
                currentSetRecords = setRecords,
                allRecords = allRecords,
                unsoldPool = unsoldPool,
                quickResultBanner = null,
                showSetRosterViewer = false,
                setSummary = summary
            )
        }
        persistActiveAuctionSnapshot(phaseOverride = AuctionPhase.SET_SUMMARY)
    }

    private fun resolveNextSet(
        currentSet: AuctionSet,
        franchises: Map<FranchiseId, FranchiseState>,
        unsoldPool: List<Player>
    ): AuctionSet? {
        if (currentSet == AuctionSet.ACCELERATED_ROUND) return null
        val primarySets = PlayerDatabase.primarySetsOrdered
        val idx = primarySets.indexOf(currentSet)
        if (idx in 0 until primarySets.lastIndex) {
            return primarySets[idx + 1]
        }
        // After Uncapped Players: trigger Accelerated Round if unsold players exist & any team has room
        val anyTeamNeedsPlayers = franchises.values.any { it.squadCount < AuctionRules.MAX_SQUAD_SIZE }
        return if (unsoldPool.isNotEmpty() && anyTeamNeedsPlayers) {
            AuctionSet.ACCELERATED_ROUND
        } else {
            null
        }
    }

    private fun finalizeAuctionWrapUp() {
        cancelTimers()
        persistence.clearSavedAuction()
        val state = _uiState.value
        val analytics = AuctionGrader.generateWrapUpAnalytics(
            franchises = state.franchises,
            allRecords = state.allRecords
        )
        val defaultLineup = TournamentSimulator.autoSelectBestLineup(state.userFranchise)
        _uiState.update { current ->
            current.copy(
                phase = AuctionPhase.POST_AUCTION_WRAPUP,
                setSummary = null,
                quickResultBanner = null,
                wrapUpAnalytics = analytics,
                inspectedFranchiseId = null,
                savedAuctionSummary = null,
                userPlayingXiIds = defaultLineup.playingXiIds,
                userImpactPlayerId = defaultLineup.impactPlayerId,
                lineupSelectionError = null,
                seasonSimulationResult = null
            )
        }
    }

    fun togglePlayerInPlayingXi(playerId: String) {
        val state = _uiState.value
        val currentXi = state.userPlayingXiIds
        if (playerId in currentXi) {
            _uiState.update {
                it.copy(
                    userPlayingXiIds = currentXi - playerId,
                    lineupSelectionError = null
                )
            }
            return
        }

        val (allowed, reason) = TournamentSimulator.canAddPlayerToXi(
            franchiseState = state.userFranchise,
            currentXiIds = currentXi,
            candidatePlayerId = playerId
        )
        if (!allowed) {
            _uiState.update { it.copy(lineupSelectionError = reason) }
            return
        }

        val updatedImpact = if (state.userImpactPlayerId == playerId) null else state.userImpactPlayerId
        _uiState.update {
            it.copy(
                userPlayingXiIds = currentXi + playerId,
                userImpactPlayerId = updatedImpact,
                lineupSelectionError = null
            )
        }
    }

    fun toggleImpactPlayer(playerId: String) {
        val state = _uiState.value
        if (state.userImpactPlayerId == playerId) {
            _uiState.update { it.copy(userImpactPlayerId = null, lineupSelectionError = null) }
            return
        }
        val updatedXi = state.userPlayingXiIds.filterNot { it == playerId }
        _uiState.update {
            it.copy(
                userPlayingXiIds = updatedXi,
                userImpactPlayerId = playerId,
                lineupSelectionError = null
            )
        }
    }

    fun autoFillUserBestXi() {
        val best = TournamentSimulator.autoSelectBestLineup(_uiState.value.userFranchise)
        _uiState.update {
            it.copy(
                userPlayingXiIds = best.playingXiIds,
                userImpactPlayerId = best.impactPlayerId,
                lineupSelectionError = null
            )
        }
    }

    fun clearUserPlayingXi() {
        _uiState.update {
            it.copy(
                userPlayingXiIds = emptyList(),
                userImpactPlayerId = null,
                lineupSelectionError = null
            )
        }
    }

    fun simulateIplSeason() {
        val state = _uiState.value
        val validation = state.userLineupValidation
        if (!validation.isValid) {
            _uiState.update { it.copy(lineupSelectionError = validation.issueMessage) }
            return
        }

        val userLineup = TeamLineup(
            franchiseId = state.selectedFranchiseId,
            playingXiIds = state.userPlayingXiIds,
            impactPlayerId = state.userImpactPlayerId
        )
        val simResult = TournamentSimulator.simulateIplSeason(
            franchises = state.franchises,
            userLineup = userLineup,
            seed = Random.nextInt()
        )
        if (simResult.championId == state.selectedFranchiseId) {
            soundEngine.playGavelHammerSlam(isMarqueeCelebration = true)
        } else {
            soundEngine.playBidPaddleTick()
        }
        _uiState.update {
            it.copy(
                seasonSimulationResult = simResult,
                lineupSelectionError = null
            )
        }
    }

    private fun persistActiveAuctionSnapshot(
        phaseOverride: AuctionPhase? = null,
        nextPlayerIndexOverride: Int? = null
    ) {
        val state = _uiState.value
        val targetPhase = phaseOverride ?: state.phase
        if (targetPhase != AuctionPhase.LIVE_AUCTION &&
            targetPhase != AuctionPhase.SET_SUMMARY &&
            targetPhase != AuctionPhase.ACCELERATED_NOMINATION
        ) return

        val indexToSave = nextPlayerIndexOverride ?: state.currentPlayerIndex
        val effectivePhase = if (targetPhase == AuctionPhase.LIVE_AUCTION && indexToSave >= state.currentSetPlayers.size) {
            AuctionPhase.SET_SUMMARY
        } else {
            targetPhase
        }

        persistence.saveSnapshot(
            PersistedAuctionSnapshot(
                phase = effectivePhase,
                selectedFranchiseId = state.selectedFranchiseId,
                franchises = state.franchises,
                currentSet = state.currentSet,
                currentSetPlayers = state.currentSetPlayers,
                currentPlayerIndex = indexToSave,
                currentSetRecords = state.currentSetRecords,
                allRecords = state.allRecords,
                unsoldPool = state.unsoldPool,
                paddleFeed = state.paddleFeed,
                sessionSeed = currentSessionSeed,
                savedAtMillis = System.currentTimeMillis()
            )
        )
    }

    private fun buildSetOrder(set: AuctionSet): List<Player> {
        // Keep sets in realistic marquee/star-first order with gentle local shuffle inside tiers
        val base = PlayerDatabase.playersForSet(set)
        val topTier = base.filter { it.form >= 88 }.shuffled()
        val midTier = base.filter { it.form in 80..87 }.shuffled()
        val depthTier = base.filter { it.form < 80 }.shuffled()
        return topTier + midTier + depthTier
    }

    private fun futureSetsPlayers(currentSet: AuctionSet): List<Player> {
        val ordered = PlayerDatabase.primarySetsOrdered
        val idx = ordered.indexOf(currentSet)
        if (idx < 0 || idx >= ordered.lastIndex) return emptyList()
        return ordered.drop(idx + 1).flatMap { PlayerDatabase.playersForSet(it) }
    }

    private fun createEvent(
        franchiseId: FranchiseId?,
        type: PaddleEventType,
        amountLakhs: Int?,
        message: String
    ): PaddleEvent {
        return PaddleEvent(
            id = eventIdCounter++,
            franchiseId = franchiseId,
            type = type,
            amountLakhs = amountLakhs,
            message = message
        )
    }

    private fun cancelTimers() {
        auctionLoopJob?.cancel()
        bannerAdvanceJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        cancelTimers()
    }
}
