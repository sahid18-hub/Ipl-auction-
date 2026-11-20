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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.FranchiseCatalog
import com.example.engine.AuctionRules
import com.example.model.AuctionSet
import com.example.model.FranchiseId
import com.example.model.SetPlayerStatus
import com.example.model.SetRosterItem
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

@Composable
fun CurrentSetRosterModal(
    activeSet: AuctionSet,
    selectedPreviewSet: AuctionSet,
    availableSets: List<AuctionSet>,
    rosterItems: List<SetRosterItem>,
    userFranchiseId: FranchiseId,
    userPurseLakhs: Int,
    userMaxBidLakhs: Int,
    isAuctionPaused: Boolean,
    onSelectPreviewSet: (AuctionSet) -> Unit,
    onTogglePause: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BroadcastBgDeep.copy(alpha = 0.88f))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 760.dp)
                    .fillMaxHeight(0.94f)
                    .testTag("current_set_roster_modal"),
                shape = RoundedCornerShape(20.dp),
                color = BroadcastSurface,
                border = BorderStroke(1.5.dp, BroadcastOutline),
                shadowElevation = 16.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Top Broadcast Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        BroadcastSurfaceHighlight,
                                        BroadcastSurfaceElevated
                                    )
                                )
                            )
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                    Icon(
                                        imageVector = Icons.Filled.FormatListBulleted,
                                        contentDescription = "Set roster viewer",
                                        tint = BroadcastGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "AUCTION SET ROSTER PLANNER",
                                            fontFamily = DisplayFontFamily,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp,
                                            letterSpacing = 0.7.sp,
                                            color = BroadcastTextPrimary
                                        )
                                        Text(
                                            text = "Plan your purse across current & upcoming sets",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = BroadcastTextSecondary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = onTogglePause,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .testTag("roster_viewer_pause_button")
                                    ) {
                                        Icon(
                                            imageVector = if (isAuctionPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                                            contentDescription = if (isAuctionPaused) "Resume auction" else "Pause auction",
                                            tint = if (isAuctionPaused) BroadcastGold else BroadcastTextSecondary
                                        )
                                    }
                                    IconButton(
                                        onClick = onDismiss,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .testTag("close_set_roster_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Close set roster viewer",
                                            tint = BroadcastTextPrimary
                                        )
                                    }
                                }
                            }

                            // User Purse & Budget Planning Strip
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FranchiseBadge(franchiseId = userFranchiseId, isUser = true, compact = true)
                                TelemetryPill(
                                    label = "Purse Left",
                                    value = AuctionRules.formatCrores(userPurseLakhs),
                                    valueColor = BroadcastGold,
                                    modifier = Modifier.weight(1f)
                                )
                                TelemetryPill(
                                    label = "Max Allowable Bid",
                                    value = AuctionRules.formatCrores(userMaxBidLakhs),
                                    valueColor = BroadcastCyan,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Set Selector Tabs (Active Set + Upcoming Sets)
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BroadcastBgDeep.copy(alpha = 0.65f))
                            .padding(vertical = 10.dp)
                            .testTag("set_preview_tabs"),
                        contentPadding = PaddingValues(horizontal = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableSets, key = { it.name }) { set ->
                            val isSelected = set == selectedPreviewSet
                            val isActive = set == activeSet
                            val isUpcoming = set.setNumber > activeSet.setNumber

                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onSelectPreviewSet(set) }
                                    .testTag("set_tab_${set.setNumber}"),
                                shape = RoundedCornerShape(10.dp),
                                color = when {
                                    isSelected -> BroadcastGold
                                    isActive -> BroadcastSurfaceHighlight
                                    else -> BroadcastSurfaceElevated
                                },
                                border = BorderStroke(
                                    width = if (isActive && !isSelected) 1.5.dp else 1.dp,
                                    color = when {
                                        isSelected -> BroadcastGold
                                        isActive -> BroadcastCrimson
                                        else -> BroadcastOutline
                                    }
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (isActive) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(if (isSelected) OnBroadcastGold else BroadcastCrimson)
                                        )
                                    }
                                    Text(
                                        text = "S${set.setNumber} • ${set.shortTitle}",
                                        fontFamily = DisplayFontFamily,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        letterSpacing = 0.5.sp,
                                        color = if (isSelected) OnBroadcastGold else BroadcastTextPrimary
                                    )
                                    if (isActive) {
                                        Text(
                                            text = "LIVE",
                                            fontFamily = MonoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp,
                                            color = if (isSelected) OnBroadcastGold else BroadcastCrimson
                                        )
                                    } else if (isUpcoming) {
                                        Text(
                                            text = "NEXT",
                                            fontFamily = MonoFontFamily,
                                            fontSize = 9.sp,
                                            color = if (isSelected) OnBroadcastGold.copy(alpha = 0.8f) else BroadcastTextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = BroadcastOutlineSubtle)

                    // Selected Set Status Counts Bar
                    val liveCount = rosterItems.count { it.status == SetPlayerStatus.LIVE_NOW }
                    val upcomingCount = rosterItems.count { it.status == SetPlayerStatus.UPCOMING }
                    val soldCount = rosterItems.count { it.status == SetPlayerStatus.SOLD }
                    val unsoldCount = rosterItems.count { it.status == SetPlayerStatus.UNSOLD }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BroadcastSurfaceElevated.copy(alpha = 0.6f))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "SET ${selectedPreviewSet.setNumber}: ${selectedPreviewSet.title.uppercase()}",
                                fontFamily = DisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = BroadcastTextPrimary
                            )
                            Text(
                                text = selectedPreviewSet.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = BroadcastTextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (liveCount > 0) {
                                MiniCountChip("LIVE $liveCount", BroadcastCrimson)
                            }
                            MiniCountChip("UP $upcomingCount", BroadcastCyan)
                            MiniCountChip("SOLD $soldCount", BroadcastEmerald)
                            if (unsoldCount > 0) {
                                MiniCountChip("UNS $unsoldCount", BroadcastTextMuted)
                            }
                        }
                    }

                    HorizontalDivider(color = BroadcastOutlineSubtle)

                    // Players List
                    if (rosterItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (selectedPreviewSet == AuctionSet.ACCELERATED_ROUND) {
                                    "No unsold players in the Accelerated Round pool yet."
                                } else {
                                    "No players scheduled in this set."
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = BroadcastTextMuted
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .testTag("set_roster_player_list"),
                            contentPadding = PaddingValues(14.dp),
                            verticalArrangement = Arrangement.spacedBy(9.dp)
                        ) {
                            items(
                                items = rosterItems,
                                key = { "${selectedPreviewSet.name}_${it.player.id}" }
                            ) { item ->
                                SetRosterPlayerCard(
                                    item = item,
                                    userFranchiseId = userFranchiseId
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniCountChip(
    text: String,
    color: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(color.copy(alpha = 0.16f))
            .border(1.dp, color.copy(alpha = 0.45f), RoundedCornerShape(5.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontFamily = MonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            color = color
        )
    }
}

@Composable
private fun SetRosterPlayerCard(
    item: SetRosterItem,
    userFranchiseId: FranchiseId
) {
    val player = item.player
    val isLive = item.status == SetPlayerStatus.LIVE_NOW
    val isSold = item.status == SetPlayerStatus.SOLD
    val isUnsold = item.status == SetPlayerStatus.UNSOLD

    val borderColor = when (item.status) {
        SetPlayerStatus.LIVE_NOW -> BroadcastGold
        SetPlayerStatus.SOLD -> if (item.winningFranchiseId == userFranchiseId) BroadcastEmerald else BroadcastOutline
        SetPlayerStatus.UPCOMING -> BroadcastOutlineSubtle
        SetPlayerStatus.UNSOLD -> BroadcastOutlineSubtle
    }

    val cardBg = when (item.status) {
        SetPlayerStatus.LIVE_NOW -> BroadcastSurfaceHighlight
        SetPlayerStatus.SOLD -> BroadcastSurfaceElevated
        SetPlayerStatus.UPCOMING -> BroadcastBgDeep.copy(alpha = 0.65f)
        SetPlayerStatus.UNSOLD -> BroadcastBgDeep.copy(alpha = 0.42f)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("roster_item_${player.id}"),
        shape = RoundedCornerShape(12.dp),
        color = cardBg,
        border = BorderStroke(if (isLive) 1.8.dp else 1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = player.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (isUnsold) BroadcastTextMuted else BroadcastTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    StatusPillForRoster(status = item.status)
                }

                // Role, Nationality & Base Price metadata row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = player.role.label,
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = roleAccentColor(player.role)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = if (player.isOverseas) Icons.Filled.Flight else Icons.Filled.Home,
                            contentDescription = player.nationality.label,
                            tint = if (player.isOverseas) BroadcastAmber else BroadcastEmerald,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = if (player.isOverseas) "OS (${player.country})" else "IND",
                            fontFamily = MonoFontFamily,
                            fontSize = 10.sp,
                            color = BroadcastTextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = "Base ${AuctionRules.formatCroresCompact(player.basePriceLakhs)}",
                        fontFamily = MonoFontFamily,
                        fontSize = 10.sp,
                        color = BroadcastTextMuted
                    )
                }

                Text(
                    text = "${player.archetype.label} • Form ${player.form} • Hype ${player.hype}",
                    style = MaterialTheme.typography.bodySmall,
                    color = BroadcastTextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Right Status/Price Column
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                when (item.status) {
                    SetPlayerStatus.LIVE_NOW -> {
                        if (item.liveLeaderId != null && item.liveBidLakhs != null && item.liveBidLakhs > 0) {
                            FranchiseBadge(
                                franchiseId = item.liveLeaderId,
                                isUser = item.liveLeaderId == userFranchiseId,
                                compact = true
                            )
                            Text(
                                text = AuctionRules.formatCrores(item.liveBidLakhs),
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = BroadcastGold
                            )
                        } else {
                            Text(
                                text = AuctionRules.formatCrores(player.basePriceLakhs),
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = BroadcastGold
                            )
                            Text(
                                text = "ON STAGE",
                                fontFamily = MonoFontFamily,
                                fontSize = 9.sp,
                                color = BroadcastCrimson
                            )
                        }
                    }

                    SetPlayerStatus.SOLD -> {
                        item.winningFranchiseId?.let { winner ->
                            FranchiseBadge(
                                franchiseId = winner,
                                isUser = winner == userFranchiseId,
                                compact = true
                            )
                        }
                        Text(
                            text = AuctionRules.formatCrores(item.soldPriceLakhs ?: 0),
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = BroadcastEmerald
                        )
                    }

                    SetPlayerStatus.UPCOMING -> {
                        Text(
                            text = AuctionRules.formatCrores(player.basePriceLakhs),
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = BroadcastCyan
                        )
                        Text(
                            text = "BASE PRICE",
                            fontFamily = MonoFontFamily,
                            fontSize = 8.sp,
                            color = BroadcastTextMuted
                        )
                    }

                    SetPlayerStatus.UNSOLD -> {
                        Text(
                            text = "PASSED",
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = BroadcastTextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusPillForRoster(status: SetPlayerStatus) {
    val (label, bg, fg) = when (status) {
        SetPlayerStatus.LIVE_NOW -> Triple("LIVE NOW", BroadcastCrimson, BroadcastTextPrimary)
        SetPlayerStatus.UPCOMING -> Triple("UPCOMING", BroadcastCyan.copy(alpha = 0.18f), BroadcastCyan)
        SetPlayerStatus.SOLD -> Triple("SOLD", BroadcastEmerald.copy(alpha = 0.18f), BroadcastEmerald)
        SetPlayerStatus.UNSOLD -> Triple("UNSOLD", BroadcastSurfaceHighlight, BroadcastTextMuted)
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            fontFamily = MonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 8.sp,
            color = fg,
            letterSpacing = 0.4.sp
        )
    }
}
