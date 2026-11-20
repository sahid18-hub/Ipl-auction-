package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AuctionPhase
import com.example.model.AuctionTab
import com.example.ui.components.AcceleratedNominationModalScreen
import com.example.ui.components.SetSummaryModalScreen
import com.example.ui.screens.FranchiseSelectScreen
import com.example.ui.screens.LiveAuctionScreen
import com.example.ui.screens.PostAuctionWrapUpScreen
import com.example.ui.theme.IplAuctionTheme
import com.example.viewmodel.AuctionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            IplAuctionTheme {
                IplMegaAuctionApp()
            }
        }
    }
}

@Composable
fun IplMegaAuctionApp(
    viewModel: AuctionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle Android Back button cleanly across modals, sub-screens & inspectors
    BackHandler(enabled = uiState.showSetRosterViewer) {
        viewModel.closeSetRosterViewer()
    }

    BackHandler(enabled = !uiState.showSetRosterViewer && uiState.inspectedFranchiseId != null) {
        viewModel.inspectFranchise(null)
    }

    BackHandler(
        enabled = !uiState.showSetRosterViewer &&
            uiState.phase == AuctionPhase.LIVE_AUCTION &&
            uiState.inspectedFranchiseId == null &&
            uiState.selectedTab != AuctionTab.STAGE
    ) {
        viewModel.selectTab(AuctionTab.STAGE)
    }

    when (uiState.phase) {
        AuctionPhase.FRANCHISE_SELECT -> {
            FranchiseSelectScreen(
                selectedFranchiseId = uiState.selectedFranchiseId,
                savedAuctionSummary = uiState.savedAuctionSummary,
                onSelectFranchise = viewModel::selectFranchise,
                onStartAuction = viewModel::startAuction,
                onResumeSavedAuction = viewModel::resumeSavedAuction
            )
        }

        AuctionPhase.LIVE_AUCTION -> {
            LiveAuctionScreen(
                uiState = uiState,
                onPlaceBid = viewModel::placeUserBid,
                onSkipPlayer = viewModel::skipPlayer,
                onSkipSet = viewModel::skipEntireSet,
                onDismissQuickBanner = viewModel::dismissQuickBannerNow,
                onTogglePause = viewModel::togglePause,
                onToggleSoundMute = viewModel::toggleSoundMute,
                onSelectTab = viewModel::selectTab,
                onInspectFranchise = viewModel::inspectFranchise,
                onOpenSetRosterViewer = viewModel::openSetRosterViewer,
                onCloseSetRosterViewer = viewModel::closeSetRosterViewer,
                onSelectPreviewSet = viewModel::selectPreviewSet,
                onResetAuction = viewModel::resetToFranchiseSelect
            )
        }

        AuctionPhase.SET_SUMMARY -> {
            uiState.setSummary?.let { summary ->
                SetSummaryModalScreen(
                    summary = summary,
                    userFranchiseId = uiState.selectedFranchiseId,
                    onProceed = viewModel::proceedFromSetSummary
                )
            }
        }

        AuctionPhase.ACCELERATED_NOMINATION -> {
            AcceleratedNominationModalScreen(
                unsoldPlayers = uiState.unsoldPool,
                userFranchise = uiState.userFranchise,
                userNominatedIds = uiState.userNominatedPlayerIds,
                aiNominationsByTeam = uiState.aiNominatedPlayersByTeam,
                onToggleNominatePlayer = viewModel::toggleUserAcceleratedNomination,
                onStartAcceleratedRound = viewModel::startAcceleratedRoundFromNominations
            )
        }

        AuctionPhase.POST_AUCTION_WRAPUP -> {
            uiState.wrapUpAnalytics?.let { analytics ->
                PostAuctionWrapUpScreen(
                    analytics = analytics,
                    franchises = uiState.franchises,
                    userFranchiseId = uiState.selectedFranchiseId,
                    inspectedFranchiseId = uiState.inspectedFranchiseId,
                    userPlayingXiIds = uiState.userPlayingXiIds,
                    userImpactPlayerId = uiState.userImpactPlayerId,
                    lineupValidation = uiState.userLineupValidation,
                    lineupSelectionError = uiState.lineupSelectionError,
                    seasonSimulationResult = uiState.seasonSimulationResult,
                    onTogglePlayerInXi = viewModel::togglePlayerInPlayingXi,
                    onToggleImpactPlayer = viewModel::toggleImpactPlayer,
                    onAutoFillBestXi = viewModel::autoFillUserBestXi,
                    onClearXi = viewModel::clearUserPlayingXi,
                    onSimulateSeason = viewModel::simulateIplSeason,
                    onInspectFranchise = viewModel::inspectFranchise,
                    onStartNewAuction = viewModel::resetToFranchiseSelect
                )
            }
        }
    }
}
