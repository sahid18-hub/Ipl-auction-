package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FranchiseCatalog
import com.example.engine.AuctionRules
import com.example.model.AcquiredPlayer
import com.example.model.FranchiseId
import com.example.model.FranchiseState
import com.example.model.PlayerRole
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

@Composable
fun AllTeamsDashboardList(
    franchises: Map<FranchiseId, FranchiseState>,
    userFranchiseId: FranchiseId,
    onInspectFranchise: (FranchiseId) -> Unit,
    modifier: Modifier = Modifier
) {
    val ordered = FranchiseId.entries.sortedWith(
        compareByDescending<FranchiseId> { it == userFranchiseId }
            .thenByDescending { franchises.getValue(it).purseLakhs }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("all_teams_dashboard"),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LIVE FRANCHISE PURSES & SQUADS",
                    fontFamily = DisplayFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    letterSpacing = 0.8.sp,
                    color = BroadcastTextPrimary
                )
                Text(
                    text = "TAP TO INSPECT",
                    fontFamily = MonoFontFamily,
                    fontSize = 10.sp,
                    color = BroadcastGold
                )
            }
        }

        items(ordered, key = { it.name }) { id ->
            val state = franchises.getValue(id)
            FranchiseOverviewCard(
                state = state,
                isUser = id == userFranchiseId,
                onClick = { onInspectFranchise(id) }
            )
        }
    }
}

@Composable
private fun FranchiseOverviewCard(
    state: FranchiseState,
    isUser: Boolean,
    onClick: () -> Unit
) {
    val profile = FranchiseCatalog.get(state.id)
    val minSquadProgress = (state.squadCount.toFloat() / AuctionRules.MIN_SQUAD_SIZE.toFloat()).coerceIn(0f, 1f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("dashboard_team_${profile.shortCode.lowercase()}"),
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
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        FranchiseBadge(franchiseId = state.id, isUser = isUser, compact = true)
                        Column {
                            Text(
                                text = profile.fullName,
                                style = MaterialTheme.typography.titleSmall,
                                color = BroadcastTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Max Bid: ${AuctionRules.formatCrores(state.maxAllowableBidLakhs)}",
                                fontFamily = MonoFontFamily,
                                fontSize = 10.sp,
                                color = BroadcastCyan
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = AuctionRules.formatCrores(state.purseLakhs),
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = BroadcastGold
                            )
                            Text(
                                text = "Squad ${state.squadCount}/25 • OS ${state.overseasCount}/8",
                                fontFamily = MonoFontFamily,
                                fontSize = 10.sp,
                                color = if (state.squadCount >= AuctionRules.MIN_SQUAD_SIZE) BroadcastEmerald else BroadcastTextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = "Inspect ${profile.fullName} roster",
                            tint = BroadcastTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Min 18 Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(BroadcastBgDeep)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(minSquadProgress)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (state.squadCount >= 18) BroadcastEmerald else profile.primaryColor)
                    )
                }

                // Role Counts Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PlayerRole.entries.forEach { role ->
                        val count = state.countByRole(role)
                        val accent = roleAccentColor(role)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(BroadcastBgDeep.copy(alpha = 0.65f))
                                .border(1.dp, accent.copy(alpha = 0.28f), RoundedCornerShape(6.dp))
                                .padding(vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${role.shortCode} $count",
                                fontFamily = MonoFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (count > 0) accent else BroadcastTextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FranchiseRosterInspector(
    franchiseState: FranchiseState,
    showBackButton: Boolean,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val profile = FranchiseCatalog.get(franchiseState.id)
    val grouped = PlayerRole.entries.associateWith { role ->
        franchiseState.squad
            .filter { it.player.role == role }
            .sortedByDescending { it.boughtPriceLakhs }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("roster_inspector_${profile.shortCode.lowercase()}"),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BroadcastSurfaceElevated,
                border = BorderStroke(1.5.dp, profile.primaryColor.copy(alpha = 0.75f))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .background(Brush.horizontalGradient(listOf(profile.primaryColor, profile.secondaryColor)))
                    )
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (showBackButton && onBack != null) {
                                IconButton(
                                    onClick = onBack,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .testTag("roster_back_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back to all franchises",
                                        tint = BroadcastTextPrimary
                                    )
                                }
                            }
                            FranchiseBadge(
                                franchiseId = franchiseState.id,
                                isUser = franchiseState.isUserControlled
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = profile.fullName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = BroadcastTextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = profile.dnaHeadline,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BroadcastTextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TelemetryPill(
                                label = "Purse Left",
                                value = AuctionRules.formatCrores(franchiseState.purseLakhs),
                                valueColor = BroadcastGold,
                                modifier = Modifier.weight(1f)
                            )
                            TelemetryPill(
                                label = "Max Bid",
                                value = AuctionRules.formatCrores(franchiseState.maxAllowableBidLakhs),
                                valueColor = BroadcastCyan,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TelemetryPill(
                                label = "Squad (Min 18)",
                                value = "${franchiseState.squadCount} / 25",
                                valueColor = if (franchiseState.squadCount >= 18) BroadcastEmerald else BroadcastAmber,
                                modifier = Modifier.weight(1f)
                            )
                            TelemetryPill(
                                label = "Overseas",
                                value = "${franchiseState.overseasCount} / 8",
                                valueColor = if (franchiseState.overseasCount >= 8) BroadcastAmber else BroadcastTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        if (franchiseState.squad.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = BroadcastSurface,
                    border = BorderStroke(1.dp, BroadcastOutlineSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Groups,
                            contentDescription = "Empty squad",
                            tint = BroadcastTextMuted,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "NO PLAYERS ACQUIRED YET",
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = BroadcastTextSecondary
                        )
                        Text(
                            text = "Place winning bids on the Auction Stage to assemble ${profile.shortCode}'s 18–25 player squad.",
                            style = MaterialTheme.typography.bodySmall,
                            color = BroadcastTextMuted
                        )
                    }
                }
            }
        } else {
            PlayerRole.entries.forEach { role ->
                val playersInRole = grouped[role].orEmpty()
                if (playersInRole.isNotEmpty()) {
                    item(key = "header_${role.name}") {
                        val accent = roleAccentColor(role)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .height(16.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(accent)
                                )
                                Text(
                                    text = "${role.label.uppercase()}S (${playersInRole.size})",
                                    fontFamily = DisplayFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = accent,
                                    letterSpacing = 0.6.sp
                                )
                            }
                            Text(
                                text = AuctionRules.formatCrores(playersInRole.sumOf { it.boughtPriceLakhs }),
                                fontFamily = MonoFontFamily,
                                fontSize = 11.sp,
                                color = BroadcastTextMuted
                            )
                        }
                    }

                    items(playersInRole, key = { it.player.id }) { acquired ->
                        AcquiredPlayerRow(acquired = acquired)
                    }
                }
            }
        }
    }
}

@Composable
private fun AcquiredPlayerRow(acquired: AcquiredPlayer) {
    val player = acquired.player
    val isSteal = acquired.trueMarketValueLakhs >= 400 && (acquired.trueMarketValueLakhs - acquired.boughtPriceLakhs) >= 150

    Surface(
        shape = RoundedCornerShape(11.dp),
        color = BroadcastSurface,
        border = BorderStroke(1.dp, BroadcastOutlineSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = player.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = BroadcastTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (player.isOverseas) {
                        Icon(
                            imageVector = Icons.Filled.Flight,
                            contentDescription = "Overseas",
                            tint = BroadcastAmber,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    if (isSteal) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(BroadcastCyan.copy(alpha = 0.18f))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "VALUE",
                                fontFamily = MonoFontFamily,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = BroadcastCyan
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${player.archetype.label} • Form ${player.form} • Hype ${player.hype}",
                    style = MaterialTheme.typography.bodySmall,
                    color = BroadcastTextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = AuctionRules.formatCrores(acquired.boughtPriceLakhs),
                    fontFamily = MonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = BroadcastGold
                )
                Text(
                    text = "Base ${AuctionRules.formatCroresCompact(player.basePriceLakhs)}",
                    fontFamily = MonoFontFamily,
                    fontSize = 9.sp,
                    color = BroadcastTextMuted
                )
            }
        }
    }
}
