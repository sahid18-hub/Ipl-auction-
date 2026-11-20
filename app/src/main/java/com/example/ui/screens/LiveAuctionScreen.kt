package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FranchiseCatalog
import com.example.engine.AuctionRules
import com.example.model.AuctionSet
import com.example.model.AuctionTab
import com.example.model.FranchiseId
import com.example.model.HammerStage
import com.example.ui.components.AllTeamsDashboardList
import com.example.ui.components.CurrentSetRosterModal
import com.example.ui.components.FinalCallWarningBanner
import com.example.ui.components.FranchiseBadge
import com.example.ui.components.FranchiseRosterInspector
import com.example.ui.components.LiveOnAirIndicator
import com.example.ui.components.PlayerSpotlightStage
import com.example.ui.theme.BroadcastAmber
import com.example.ui.theme.BroadcastBgDeep
import com.example.ui.theme.BroadcastCrimson
import com.example.ui.theme.BroadcastCyan
import com.example.ui.theme.BroadcastEmerald
import com.example.ui.theme.BroadcastGold
import com.example.ui.theme.BroadcastOutline
import com.example.ui.theme.BroadcastOutlineSubtle
import com.example.ui.theme.BroadcastSurface
import com.example.ui.theme.BroadcastSurfaceElevated
import com.example.ui.theme.BroadcastSurfaceHighlight
import com.example.ui.theme.BroadcastTextMuted
import com.example.ui.theme.BroadcastTextPrimary
import com.example.ui.theme.BroadcastTextSecondary
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.MonoFontFamily
import com.example.ui.theme.OnBroadcastGold
import com.example.viewmodel.AuctionUiState

@Composable
fun LiveAuctionScreen(
    uiState: AuctionUiState,
    onPlaceBid: () -> Unit,
    onSkipPlayer: () -> Unit,
    onSkipSet: () -> Unit,
    onDismissQuickBanner: () -> Unit,
    onTogglePause: () -> Unit,
    onToggleSoundMute: () -> Unit,
    onSelectTab: (AuctionTab) -> Unit,
    onInspectFranchise: (FranchiseId?) -> Unit,
    onOpenSetRosterViewer: () -> Unit,
    onCloseSetRosterViewer: () -> Unit,
    onSelectPreviewSet: (AuctionSet) -> Unit,
    onResetAuction: () -> Unit
) {
    if (uiState.showSetRosterViewer) {
        CurrentSetRosterModal(
            activeSet = uiState.currentSet,
            selectedPreviewSet = uiState.selectedPreviewSet,
            availableSets = uiState.availablePreviewSets,
            rosterItems = uiState.rosterItemsFor(uiState.selectedPreviewSet),
            userFranchiseId = uiState.selectedFranchiseId,
            userPurseLakhs = uiState.userFranchise.purseLakhs,
            userMaxBidLakhs = uiState.userFranchise.maxAllowableBidLakhs,
            isAuctionPaused = uiState.isPaused,
            onSelectPreviewSet = onSelectPreviewSet,
            onTogglePause = onTogglePause,
            onDismiss = onCloseSetRosterViewer
        )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpanded = maxWidth >= 840.dp

        Scaffold(
            containerColor = BroadcastBgDeep,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                BroadcastTopTelemetryBar(
                    uiState = uiState,
                    onTogglePause = onTogglePause,
                    onToggleSoundMute = onToggleSoundMute,
                    onOpenSetRosterViewer = onOpenSetRosterViewer,
                    onResetAuction = onResetAuction
                )
            },
            bottomBar = {
                if (!isExpanded) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (uiState.selectedTab == AuctionTab.STAGE) {
                            AuctionActionControlDock(
                                uiState = uiState,
                                onPlaceBid = onPlaceBid,
                                onSkipPlayer = onSkipPlayer,
                                onSkipSet = onSkipSet,
                                includeNavPadding = false
                            )
                        }
                        BroadcastBottomNavBar(
                            selectedTab = uiState.selectedTab,
                            userSquadCount = uiState.userFranchise.squadCount,
                            onSelectTab = { tab ->
                                onSelectTab(tab)
                                if (tab != AuctionTab.TEAMS) {
                                    onInspectFranchise(null)
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            if (isExpanded) {
                // Canonical Tablet / Desktop Split View: Stage Left + Live Franchise Dashboard Right
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .navigationBarsPadding()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.60f)
                            .fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            uiState.currentPlayer?.let { player ->
                                PlayerSpotlightStage(
                                    player = player,
                                    playerIndexInSet = uiState.currentPlayerIndex,
                                    totalInSet = uiState.currentSetPlayers.size,
                                    trueMarketValueLakhs = uiState.currentTrueMarketValueLakhs,
                                    currentBidLakhs = uiState.currentBidLakhs,
                                    hasOpeningBid = uiState.hasOpeningBid,
                                    highestBidderId = uiState.highestBidderId,
                                    userFranchiseId = uiState.selectedFranchiseId,
                                    hammerStage = uiState.hammerStage,
                                    countdownProgress = uiState.countdownProgress,
                                    paddleFeed = uiState.paddleFeed,
                                    activeBiddingTeams = uiState.activeBiddingTeams,
                                    backedOutTeams = uiState.backedOutTeams,
                                    quickResultBanner = uiState.quickResultBanner,
                                    onDismissQuickBanner = onDismissQuickBanner
                                )
                            }
                        }
                        AuctionActionControlDock(
                            uiState = uiState,
                            onPlaceBid = onPlaceBid,
                            onSkipPlayer = onSkipPlayer,
                            onSkipSet = onSkipSet,
                            includeNavPadding = false
                        )
                    }

                    VerticalDivider(color = BroadcastOutline)

                    Box(
                        modifier = Modifier
                            .weight(0.40f)
                            .fillMaxHeight()
                            .background(BroadcastSurface.copy(alpha = 0.45f))
                    ) {
                        val inspected = uiState.inspectedFranchiseId
                        if (inspected != null) {
                            FranchiseRosterInspector(
                                franchiseState = uiState.franchises.getValue(inspected),
                                showBackButton = true,
                                onBack = { onInspectFranchise(null) }
                            )
                        } else {
                            AllTeamsDashboardList(
                                franchises = uiState.franchises,
                                userFranchiseId = uiState.selectedFranchiseId,
                                onInspectFranchise = onInspectFranchise
                            )
                        }
                    }
                }
            } else {
                // Mobile Compact Layout driven by Bottom Navigation Tabs
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (uiState.selectedTab) {
                        AuctionTab.STAGE -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = 14.dp, vertical = 12.dp)
                            ) {
                                uiState.currentPlayer?.let { player ->
                                    PlayerSpotlightStage(
                                        player = player,
                                        playerIndexInSet = uiState.currentPlayerIndex,
                                        totalInSet = uiState.currentSetPlayers.size,
                                        trueMarketValueLakhs = uiState.currentTrueMarketValueLakhs,
                                        currentBidLakhs = uiState.currentBidLakhs,
                                        hasOpeningBid = uiState.hasOpeningBid,
                                        highestBidderId = uiState.highestBidderId,
                                        userFranchiseId = uiState.selectedFranchiseId,
                                        hammerStage = uiState.hammerStage,
                                        countdownProgress = uiState.countdownProgress,
                                        paddleFeed = uiState.paddleFeed,
                                        activeBiddingTeams = uiState.activeBiddingTeams,
                                        backedOutTeams = uiState.backedOutTeams,
                                        quickResultBanner = uiState.quickResultBanner,
                                        onDismissQuickBanner = onDismissQuickBanner
                                    )
                                }
                            }
                        }

                        AuctionTab.TEAMS -> {
                            val inspected = uiState.inspectedFranchiseId
                            if (inspected != null) {
                                FranchiseRosterInspector(
                                    franchiseState = uiState.franchises.getValue(inspected),
                                    showBackButton = true,
                                    onBack = { onInspectFranchise(null) }
                                )
                            } else {
                                AllTeamsDashboardList(
                                    franchises = uiState.franchises,
                                    userFranchiseId = uiState.selectedFranchiseId,
                                    onInspectFranchise = onInspectFranchise
                                )
                            }
                        }

                        AuctionTab.MY_SQUAD -> {
                            FranchiseRosterInspector(
                                franchiseState = uiState.userFranchise,
                                showBackButton = false
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BroadcastTopTelemetryBar(
    uiState: AuctionUiState,
    onTogglePause: () -> Unit,
    onToggleSoundMute: () -> Unit,
    onOpenSetRosterViewer: () -> Unit,
    onResetAuction: () -> Unit
) {
    val userState = uiState.userFranchise
    val userProfile = FranchiseCatalog.get(userState.id)
    val remainingInSet = (uiState.currentSetPlayers.size - uiState.currentPlayerIndex).coerceAtLeast(0)

    Surface(
        color = BroadcastSurface,
        border = BorderStroke(1.dp, BroadcastOutlineSubtle),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1: Set Name + Live Badge + Pause / Restart Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    LiveOnAirIndicator(isPaused = uiState.isPaused)
                    Column {
                        Text(
                            text = "SET ${uiState.currentSet.setNumber} • ${uiState.currentSet.title.uppercase()}",
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            letterSpacing = 0.6.sp,
                            color = BroadcastTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleSoundMute,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("sound_mute_button")
                    ) {
                        Icon(
                            imageVector = if (uiState.isSoundMuted) Icons.Filled.VolumeOff else Icons.Filled.VolumeUp,
                            contentDescription = if (uiState.isSoundMuted) "Unmute broadcast sound effects" else "Mute broadcast sound effects",
                            tint = if (uiState.isSoundMuted) BroadcastTextMuted else BroadcastCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = onTogglePause,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("pause_resume_button")
                    ) {
                        Icon(
                            imageVector = if (uiState.isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                            contentDescription = if (uiState.isPaused) "Resume auction" else "Pause auction",
                            tint = if (uiState.isPaused) BroadcastGold else BroadcastTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = onResetAuction,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("reset_auction_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.RestartAlt,
                            contentDescription = "Restart auction",
                            tint = BroadcastTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Row 2: Clearly Visible "Current Set Players" Roster Viewer Trigger Button
            OutlinedButton(
                onClick = onOpenSetRosterViewer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("current_set_players_button"),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.2.dp, BroadcastCyan.copy(alpha = 0.65f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = BroadcastCyan.copy(alpha = 0.10f),
                    contentColor = BroadcastTextPrimary
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.FormatListBulleted,
                    contentDescription = "Current Set Players",
                    tint = BroadcastCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Current Set Players",
                    style = MaterialTheme.typography.labelLarge,
                    color = BroadcastTextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${uiState.currentSetPlayers.size} IN SET • $remainingInSet LEFT",
                    fontFamily = MonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = BroadcastCyan
                )
            }

            // Row 2: User Franchise Badge + Purse + Max Allowable Bid + Squad + Overseas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(BroadcastBgDeep.copy(alpha = 0.72f))
                    .border(1.dp, userProfile.primaryColor.copy(alpha = 0.45f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FranchiseBadge(
                    franchiseId = userState.id,
                    isUser = true,
                    compact = true
                )

                CompactMetricItem(
                    label = "PURSE",
                    value = AuctionRules.formatCroresCompact(userState.purseLakhs),
                    color = BroadcastGold
                )

                CompactMetricItem(
                    label = "MAX BID",
                    value = AuctionRules.formatCroresCompact(userState.maxAllowableBidLakhs),
                    color = BroadcastCyan,
                    modifier = Modifier.testTag("max_allowable_bid_text")
                )

                CompactMetricItem(
                    label = "SQUAD",
                    value = "${userState.squadCount}/25",
                    color = if (userState.squadCount >= AuctionRules.MIN_SQUAD_SIZE) BroadcastEmerald else BroadcastTextPrimary
                )

                CompactMetricItem(
                    label = "OS",
                    value = "${userState.overseasCount}/8",
                    color = if (userState.overseasCount >= AuctionRules.MAX_OVERSEAS) BroadcastAmber else BroadcastTextPrimary
                )
            }

            // Row 3: Live Squad Needs & Weakness Radar (single-row horizontally scrollable)
            com.example.ui.components.SquadNeedsRadarBar(
                userState = userState,
                currentSet = uiState.currentSet
            )
        }
    }
}

@Composable
private fun CompactMetricItem(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontFamily = MonoFontFamily,
            fontSize = 8.sp,
            color = BroadcastTextMuted
        )
        Text(
            text = value,
            fontFamily = MonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = color
        )
    }
}

@Composable
private fun AuctionActionControlDock(
    uiState: AuctionUiState,
    onPlaceBid: () -> Unit,
    onSkipPlayer: () -> Unit,
    onSkipSet: () -> Unit,
    includeNavPadding: Boolean
) {
    val eligibility = uiState.userBidEligibility
    val isUserLeading = uiState.highestBidderId == uiState.selectedFranchiseId
    val nextBidText = AuctionRules.formatCrores(uiState.nextLegalBidLakhs)

    Surface(
        color = BroadcastSurface,
        border = BorderStroke(1.dp, BroadcastOutline),
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (includeNavPadding) Modifier.navigationBarsPadding() else Modifier)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Dramatic Pulsing FINAL CALL Pop-Out Warning Banner right above the Bid button
            FinalCallWarningBanner(
                visible = uiState.hammerStage == HammerStage.FINAL_CALL && uiState.quickResultBanner == null,
                player = uiState.currentPlayer,
                leadingFranchiseId = uiState.highestBidderId,
                currentBidLakhs = uiState.currentBidLakhs,
                finalCallProgress = uiState.finalCallProgress,
                finalCallRemainingSeconds = uiState.finalCallRemainingSeconds
            )

            // Guardrail / Eligibility Status Helper Line
            if (!eligibility.allowed && eligibility.reason != null && uiState.quickResultBanner == null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "Bid status",
                        tint = if (isUserLeading) BroadcastEmerald else BroadcastAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = eligibility.reason,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isUserLeading) BroadcastEmerald else BroadcastAmber,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Primary BID CTA Button
            Button(
                onClick = onPlaceBid,
                enabled = eligibility.allowed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("bid_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BroadcastGold,
                    contentColor = OnBroadcastGold,
                    disabledContainerColor = BroadcastSurfaceHighlight,
                    disabledContentColor = BroadcastTextMuted
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Gavel,
                    contentDescription = "Place bid",
                    modifier = Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                val bidLabel = when {
                    isUserLeading -> "HIGHEST BIDDER (${AuctionRules.formatCrores(uiState.currentBidLakhs)})"
                    !uiState.hasOpeningBid -> "OPEN BID • $nextBidText"
                    else -> "RAISE PADDLE • $nextBidText"
                }
                Text(
                    text = bidLabel,
                    fontFamily = DisplayFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    letterSpacing = 0.8.sp
                )
            }

            // Secondary Row: SKIP PLAYER & SKIP ENTIRE SET
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onSkipPlayer,
                    enabled = uiState.quickResultBanner == null && uiState.currentPlayer != null,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("skip_player_button"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, BroadcastOutline),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BroadcastTextPrimary,
                        disabledContentColor = BroadcastTextMuted
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "Skip player",
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isUserLeading) "FAST FINISH" else "SKIP PLAYER",
                        fontFamily = DisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 0.5.sp
                    )
                }

                OutlinedButton(
                    onClick = onSkipSet,
                    enabled = uiState.currentPlayer != null,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("skip_set_button"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, BroadcastCyan.copy(alpha = 0.55f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BroadcastCyan,
                        disabledContentColor = BroadcastTextMuted
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.FastForward,
                        contentDescription = "Skip entire set",
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "SKIP SET",
                        fontFamily = DisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BroadcastBottomNavBar(
    selectedTab: AuctionTab,
    userSquadCount: Int,
    onSelectTab: (AuctionTab) -> Unit
) {
    NavigationBar(
        containerColor = BroadcastBgDeep,
        contentColor = BroadcastTextPrimary,
        windowInsets = WindowInsets.navigationBars,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == AuctionTab.STAGE,
            onClick = { onSelectTab(AuctionTab.STAGE) },
            modifier = Modifier.testTag("nav_tab_stage"),
            icon = {
                Icon(
                    imageVector = if (selectedTab == AuctionTab.STAGE) Icons.Filled.Gavel else Icons.Outlined.Gavel,
                    contentDescription = "Auction Stage"
                )
            },
            label = { Text("Stage", style = MaterialTheme.typography.labelMedium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnBroadcastGold,
                selectedTextColor = BroadcastGold,
                indicatorColor = BroadcastGold,
                unselectedIconColor = BroadcastTextMuted,
                unselectedTextColor = BroadcastTextMuted
            )
        )
        NavigationBarItem(
            selected = selectedTab == AuctionTab.TEAMS,
            onClick = { onSelectTab(AuctionTab.TEAMS) },
            modifier = Modifier.testTag("nav_tab_teams"),
            icon = {
                Icon(
                    imageVector = if (selectedTab == AuctionTab.TEAMS) Icons.Filled.Groups else Icons.Outlined.Groups,
                    contentDescription = "All 10 Franchises"
                )
            },
            label = { Text("10 Teams", style = MaterialTheme.typography.labelMedium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnBroadcastGold,
                selectedTextColor = BroadcastGold,
                indicatorColor = BroadcastGold,
                unselectedIconColor = BroadcastTextMuted,
                unselectedTextColor = BroadcastTextMuted
            )
        )
        NavigationBarItem(
            selected = selectedTab == AuctionTab.MY_SQUAD,
            onClick = { onSelectTab(AuctionTab.MY_SQUAD) },
            modifier = Modifier.testTag("nav_tab_my_squad"),
            icon = {
                Icon(
                    imageVector = if (selectedTab == AuctionTab.MY_SQUAD) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "My Squad"
                )
            },
            label = { Text("My Squad ($userSquadCount)", style = MaterialTheme.typography.labelMedium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OnBroadcastGold,
                selectedTextColor = BroadcastGold,
                indicatorColor = BroadcastGold,
                unselectedIconColor = BroadcastTextMuted,
                unselectedTextColor = BroadcastTextMuted
            )
        )
    }
}
