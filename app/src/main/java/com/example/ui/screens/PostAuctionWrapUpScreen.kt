package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.FranchiseCatalog
import com.example.engine.AuctionRules
import com.example.engine.TournamentSimulator
import com.example.model.FranchiseId
import com.example.model.FranchiseState
import com.example.model.LineupValidation
import com.example.model.SeasonSimulationResult
import com.example.model.SoldRecord
import com.example.model.TeamGradeReport
import com.example.model.WrapUpAnalytics
import com.example.ui.components.FranchiseBadge
import com.example.ui.components.FranchiseRosterInspector
import com.example.ui.components.PlayingXiBuilderHeaderCard
import com.example.ui.components.SeasonSimulationResultsBlock
import com.example.ui.components.SquadLineupPickerRow
import com.example.ui.components.TelemetryPill
import com.example.ui.theme.BroadcastAmber
import com.example.ui.theme.BroadcastBgDeep
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

@Composable
fun PostAuctionWrapUpScreen(
    analytics: WrapUpAnalytics,
    franchises: Map<FranchiseId, FranchiseState>,
    userFranchiseId: FranchiseId,
    inspectedFranchiseId: FranchiseId?,
    userPlayingXiIds: List<String>,
    userImpactPlayerId: String?,
    lineupValidation: LineupValidation,
    lineupSelectionError: String?,
    seasonSimulationResult: SeasonSimulationResult?,
    onTogglePlayerInXi: (String) -> Unit,
    onToggleImpactPlayer: (String) -> Unit,
    onAutoFillBestXi: () -> Unit,
    onClearXi: () -> Unit,
    onSimulateSeason: () -> Unit,
    onInspectFranchise: (FranchiseId?) -> Unit,
    onStartNewAuction: () -> Unit
) {
    var selectedWrapUpTab by rememberSaveable { mutableStateOf(0) } // 0 = Playing XI & Season, 1 = Auction Grades
    val userFranchise = franchises.getValue(userFranchiseId)
    if (inspectedFranchiseId != null) {
        Scaffold(
            containerColor = BroadcastBgDeep,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                FranchiseRosterInspector(
                    franchiseState = franchises.getValue(inspectedFranchiseId),
                    showBackButton = true,
                    onBack = { onInspectFranchise(null) }
                )
            }
        }
        return
    }

    Scaffold(
        containerColor = BroadcastBgDeep,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Surface(
                color = BroadcastSurface,
                border = BorderStroke(1.dp, BroadcastOutline),
                tonalElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onStartNewAuction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 640.dp)
                            .height(54.dp)
                            .testTag("start_new_auction_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BroadcastGold,
                            contentColor = OnBroadcastGold
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Start new auction",
                            modifier = Modifier.size(19.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "START NEW MEGA AUCTION",
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            letterSpacing = 0.8.sp
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 780.dp)
                    .testTag("wrap_up_screen"),
                contentPadding = PaddingValues(bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Hero Finale Banner
                item {
                    WrapUpHeroBanner(analytics = analytics)
                }

                // Segmented Switcher: PLAYING XI & IPL SEASON vs AUCTION GRADES
                item {
                    WrapUpModeSwitcher(
                        selectedTab = selectedWrapUpTab,
                        onSelectTab = { selectedWrapUpTab = it }
                    )
                }

                if (selectedWrapUpTab == 0) {
                    // SECTION A: BEST PLAYING XI + IMPACT PLAYER BUILDER
                    item {
                        PlayingXiBuilderHeaderCard(
                            userFranchise = userFranchise,
                            validation = lineupValidation,
                            selectionError = lineupSelectionError,
                            onAutoFillBestXi = onAutoFillBestXi,
                            onClearXi = onClearXi,
                            onSimulateSeason = onSimulateSeason,
                            hasSeasonResult = seasonSimulationResult != null
                        )
                    }

                    // SECTION B: SIMULATED IPL SEASON STANDINGS & PLAYOFFS (once simulated)
                    if (seasonSimulationResult != null) {
                        item {
                            SeasonSimulationResultsBlock(
                                result = seasonSimulationResult,
                                userFranchiseId = userFranchiseId
                            )
                        }
                    }

                    // SECTION C: USER SQUAD LINEUP PICKER LIST
                    item {
                        WrapUpSectionHeader(
                            icon = Icons.Filled.EmojiEvents,
                            title = "SELECT YOUR PLAYING XI & IMPACT SUB",
                            accent = BroadcastCyan,
                            subtitle = "Tap + XI (max 4 Overseas) or IMP on any squad player • ${userFranchise.squadCount} players in squad"
                        )
                    }

                    val sortedSquad = userFranchise.squad.sortedWith(
                        compareByDescending<com.example.model.AcquiredPlayer> { it.player.id in userPlayingXiIds }
                            .thenByDescending { it.player.id == userImpactPlayerId }
                            .thenByDescending { it.player.form }
                    )
                    if (sortedSquad.isEmpty()) {
                        item {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = BroadcastSurface,
                                border = BorderStroke(1.dp, BroadcastOutlineSubtle)
                            ) {
                                Text(
                                    text = "No players acquired in your squad. Start a new Mega Auction to build your Playing XI.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = BroadcastTextSecondary,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    } else {
                        items(sortedSquad, key = { "xi_row_${it.player.id}" }) { acquired ->
                            val isInXi = acquired.player.id in userPlayingXiIds
                            val isImpact = acquired.player.id == userImpactPlayerId
                            val overseasBlocked = !isInXi &&
                                acquired.player.isOverseas &&
                                lineupValidation.overseasInXiCount >= TournamentSimulator.MAX_OVERSEAS_IN_XI
                            SquadLineupPickerRow(
                                acquired = acquired,
                                isInXi = isInXi,
                                isImpactPlayer = isImpact,
                                overseasBlocked = overseasBlocked,
                                onToggleXi = { onTogglePlayerInXi(acquired.player.id) },
                                onToggleImpact = { onToggleImpactPlayer(acquired.player.id) }
                            )
                        }
                    }
                } else {
                    // SECTION 1: SQUAD BALANCE GRADES (ALL 10 TEAMS)
                    item {
                        WrapUpSectionHeader(
                            icon = Icons.Filled.EmojiEvents,
                            title = "FRANCHISE SQUAD BALANCE GRADES (A+ TO C)",
                            accent = BroadcastGold,
                            subtitle = "Tap any franchise to inspect its complete roster by role"
                        )
                    }

                    items(analytics.teamGrades, key = { "grade_${it.franchiseId.name}" }) { report ->
                        TeamGradeCard(
                            report = report,
                            isUser = report.franchiseId == userFranchiseId,
                            onClick = { onInspectFranchise(report.franchiseId) }
                        )
                    }

                    // SECTION 2: MOST EXPENSIVE BUYS
                    if (analytics.mostExpensiveBuys.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(6.dp))
                            WrapUpSectionHeader(
                                icon = Icons.Filled.LocalFireDepartment,
                                title = "MOST EXPENSIVE BUYS",
                                accent = BroadcastAmber,
                                subtitle = "Blockbuster bidding wars that shook the auction room"
                            )
                        }

                        items(
                            items = analytics.mostExpensiveBuys,
                            key = { "expensive_${it.player.id}" }
                        ) { record ->
                            HighlightBuyCard(
                                record = record,
                                userFranchiseId = userFranchiseId,
                                isStealCard = false
                            )
                        }
                    }

                    // SECTION 3: BEST VALUE STEALS
                    if (analytics.bestValueSteals.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(6.dp))
                            WrapUpSectionHeader(
                                icon = Icons.Filled.Savings,
                                title = "BEST VALUE STEALS",
                                accent = BroadcastCyan,
                                subtitle = "Highest savings vs. dynamic True Market Value"
                            )
                        }

                        items(
                            items = analytics.bestValueSteals,
                            key = { "steal_${it.player.id}" }
                        ) { record ->
                            HighlightBuyCard(
                                record = record,
                                userFranchiseId = userFranchiseId,
                                isStealCard = true
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WrapUpModeSwitcher(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(BroadcastSurface)
            .border(1.dp, BroadcastOutline, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        listOf(
            0 to "PLAYING XI & IPL SIM",
            1 to "AUCTION GRADES"
        ).forEach { (idx, label) ->
            val selected = selectedTab == idx
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(if (selected) BroadcastGold else Color.Transparent)
                    .clickable { onSelectTab(idx) }
                    .testTag("wrap_up_tab_$idx"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontFamily = DisplayFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    letterSpacing = 0.6.sp,
                    color = if (selected) OnBroadcastGold else BroadcastTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun WrapUpHeroBanner(analytics: WrapUpAnalytics) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_auction_stage_hero),
            contentDescription = "Mega Auction Wrap-Up Stage",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BroadcastBgDeep.copy(alpha = 0.55f),
                            BroadcastBgDeep.copy(alpha = 0.78f),
                            BroadcastBgDeep
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "ALL 9 SETS COMPLETE • FINAL REPORT",
                fontFamily = MonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = BroadcastGold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "MEGA AUCTION WRAP-UP",
                fontFamily = DisplayFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 34.sp,
                color = BroadcastTextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TelemetryPill(
                    label = "Total Sold",
                    value = "${analytics.totalPlayersSold} Players",
                    valueColor = BroadcastEmerald,
                    modifier = Modifier.weight(1f)
                )
                TelemetryPill(
                    label = "League Spend",
                    value = AuctionRules.formatCrores(analytics.totalMoneySpentLakhs),
                    valueColor = BroadcastGold,
                    modifier = Modifier.weight(1.3f)
                )
            }
        }
    }
}

@Composable
private fun WrapUpSectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    accent: Color,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(19.dp)
            )
            Text(
                text = title,
                fontFamily = DisplayFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                letterSpacing = 0.7.sp,
                color = BroadcastTextPrimary
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = BroadcastTextMuted
        )
    }
}

@Composable
private fun TeamGradeCard(
    report: TeamGradeReport,
    isUser: Boolean,
    onClick: () -> Unit
) {
    val profile = FranchiseCatalog.get(report.franchiseId)
    val gradeColor = when {
        report.grade.startsWith("A") -> BroadcastEmerald
        report.grade.startsWith("B") -> BroadcastGold
        else -> BroadcastAmber
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("wrap_up_team_${profile.shortCode.lowercase()}"),
        shape = RoundedCornerShape(14.dp),
        color = if (isUser) BroadcastSurfaceHighlight else BroadcastSurfaceElevated,
        border = BorderStroke(
            width = if (isUser) 1.5.dp else 1.dp,
            color = if (isUser) BroadcastGold else BroadcastOutline
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Brush.horizontalGradient(listOf(profile.primaryColor, profile.secondaryColor)))
            )

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        FranchiseBadge(franchiseId = report.franchiseId, isUser = isUser)
                        Column {
                            Text(
                                text = profile.fullName,
                                style = MaterialTheme.typography.titleSmall,
                                color = BroadcastTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Squad ${report.squadCount}/25 • OS ${report.overseasCount}/8 • Left ${AuctionRules.formatCroresCompact(report.purseRemainingLakhs)}",
                                fontFamily = MonoFontFamily,
                                fontSize = 10.sp,
                                color = BroadcastTextSecondary
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(gradeColor.copy(alpha = 0.16f))
                                .border(1.5.dp, gradeColor, RoundedCornerShape(10.dp))
                                .padding(horizontal = 11.dp, vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = report.grade,
                                fontFamily = DisplayFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                color = gradeColor
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = "Inspect roster",
                            tint = BroadcastTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Role Composition Summary
                Text(
                    text = "BAT ${report.battersCount}  •  WK ${report.keepersCount}  •  AR ${report.allRoundersCount}  •  PACE ${report.pacersCount}  •  SPIN ${report.spinnersCount}",
                    fontFamily = MonoFontFamily,
                    fontSize = 10.sp,
                    color = BroadcastCyan
                )

                // Key strength & watchout
                report.strengths.firstOrNull()?.let { strength ->
                    Text(
                        text = "+ $strength",
                        style = MaterialTheme.typography.bodySmall,
                        color = BroadcastEmerald
                    )
                }
                report.watchouts.firstOrNull()?.let { watchout ->
                    Text(
                        text = "• $watchout",
                        style = MaterialTheme.typography.bodySmall,
                        color = BroadcastTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun HighlightBuyCard(
    record: SoldRecord,
    userFranchiseId: FranchiseId,
    isStealCard: Boolean
) {
    val winningTeam = record.winningFranchiseId ?: return
    val isUser = winningTeam == userFranchiseId

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = BroadcastSurface,
        border = BorderStroke(
            1.dp,
            if (isStealCard) BroadcastCyan.copy(alpha = 0.45f) else BroadcastGold.copy(alpha = 0.45f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.player.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = BroadcastTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                val detail = if (isStealCard) {
                    "Market Est. ${AuctionRules.formatCrores(record.trueMarketValueLakhs)} • Saved ${AuctionRules.formatCrores(record.valueDeltaLakhs)}"
                } else {
                    "${record.player.role.label} • ${record.player.archetype.label} • Form ${record.player.form}"
                }
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isStealCard) BroadcastCyan else BroadcastTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FranchiseBadge(
                    franchiseId = winningTeam,
                    isUser = isUser,
                    compact = true
                )
                Text(
                    text = AuctionRules.formatCrores(record.soldPriceLakhs),
                    fontFamily = MonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = BroadcastGold
                )
            }
        }
    }
}
