package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.data.FranchiseCatalog
import com.example.engine.AuctionRules
import com.example.model.FranchiseId
import com.example.model.SetSummaryData
import com.example.model.SoldRecord
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
fun SetSummaryModalScreen(
    summary: SetSummaryData,
    userFranchiseId: FranchiseId,
    onProceed: () -> Unit
) {
    val soldList = summary.records.filter { it.isSold }.sortedByDescending { it.soldPriceLakhs }
    val unsoldList = summary.records.filter { !it.isSold }

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
                        onClick = onProceed,
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 640.dp)
                            .height(54.dp)
                            .testTag("proceed_next_set_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BroadcastGold,
                            contentColor = OnBroadcastGold
                        )
                    ) {
                        val ctaText = summary.nextSet?.let {
                            "PROCEED TO SET ${it.setNumber}: ${it.title.uppercase()}"
                        } ?: "VIEW POST-AUCTION WRAP-UP & GRADES"
                        Text(
                            text = ctaText,
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            letterSpacing = 0.8.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Proceed",
                            modifier = Modifier.size(19.dp)
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
                    .widthIn(max = 760.dp)
                    .testTag("set_summary_list"),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header Banner
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        BroadcastSurfaceHighlight,
                                        BroadcastSurface,
                                        BroadcastBgDeep
                                    )
                                )
                            )
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 18.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Gavel,
                                    contentDescription = null,
                                    tint = BroadcastGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "SET ${summary.set.setNumber} COMPLETE • OFFICIAL SUMMARY",
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = BroadcastGold,
                                    letterSpacing = 0.8.sp
                                )
                            }

                            Text(
                                text = summary.set.title.uppercase(),
                                fontFamily = DisplayFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 32.sp,
                                color = BroadcastTextPrimary
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TelemetryPill(
                                    label = "Players Sold",
                                    value = "${summary.soldCount}",
                                    valueColor = BroadcastEmerald,
                                    modifier = Modifier.weight(1f)
                                )
                                TelemetryPill(
                                    label = "Unsold",
                                    value = "${summary.unsoldCount}",
                                    valueColor = BroadcastTextSecondary,
                                    modifier = Modifier.weight(1f)
                                )
                                TelemetryPill(
                                    label = "Set Spend",
                                    value = AuctionRules.formatCrores(summary.totalSpentLakhs),
                                    valueColor = BroadcastGold,
                                    modifier = Modifier.weight(1.3f)
                                )
                            }

                            // Top Buy Spotlight
                            summary.topBuy?.let { top ->
                                val team = FranchiseCatalog.get(top.winningFranchiseId!!)
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = BroadcastSurfaceElevated,
                                    border = BorderStroke(1.5.dp, BroadcastGold.copy(alpha = 0.65f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.EmojiEvents,
                                                contentDescription = "Top buy in set",
                                                tint = BroadcastGold,
                                                modifier = Modifier.size(26.dp)
                                            )
                                            Column {
                                                Text(
                                                    text = "TOP BUY OF ${summary.set.shortTitle}",
                                                    fontFamily = MonoFontFamily,
                                                    fontSize = 9.sp,
                                                    color = BroadcastGold,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = top.player.name,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = BroadcastTextPrimary,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = "${team.fullName} • ${top.player.archetype.label}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = BroadcastTextSecondary,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(horizontalAlignment = Alignment.End) {
                                            FranchiseBadge(
                                                franchiseId = top.winningFranchiseId,
                                                isUser = top.winningFranchiseId == userFranchiseId,
                                                compact = true
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = AuctionRules.formatCrores(top.soldPriceLakhs),
                                                fontFamily = MonoFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = BroadcastGold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Sold Players Header
                if (soldList.isNotEmpty()) {
                    item {
                        SectionTitleBar(
                            title = "SOLD PLAYERS (${soldList.size})",
                            accentColor = BroadcastEmerald,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    items(soldList, key = { "sold_${it.player.id}" }) { record ->
                        SetSummaryPlayerRow(
                            record = record,
                            userFranchiseId = userFranchiseId,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                // Unsold Players Header
                if (unsoldList.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        SectionTitleBar(
                            title = "UNSOLD PLAYERS (${unsoldList.size})",
                            accentColor = BroadcastTextMuted,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    items(unsoldList, key = { "unsold_${it.player.id}" }) { record ->
                        SetSummaryPlayerRow(
                            record = record,
                            userFranchiseId = userFranchiseId,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitleBar(
    title: String,
    accentColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(accentColor)
        )
        Text(
            text = title,
            fontFamily = DisplayFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = accentColor,
            letterSpacing = 0.7.sp
        )
    }
}

@Composable
private fun SetSummaryPlayerRow(
    record: SoldRecord,
    userFranchiseId: FranchiseId,
    modifier: Modifier = Modifier
) {
    val player = record.player
    val isUserBuy = record.winningFranchiseId == userFranchiseId

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isUserBuy) BroadcastSurfaceHighlight else BroadcastSurface,
        border = BorderStroke(
            width = if (isUserBuy) 1.5.dp else 1.dp,
            color = if (isUserBuy) BroadcastGold else BroadcastOutlineSubtle
        )
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
                    if (record.isValueSteal) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(BroadcastCyan.copy(alpha = 0.2f))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "STEAL",
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
                    text = "${player.role.shortCode} • ${player.archetype.label} • Base ${AuctionRules.formatCroresCompact(player.basePriceLakhs)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = BroadcastTextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (record.isSold && record.winningFranchiseId != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FranchiseBadge(
                        franchiseId = record.winningFranchiseId,
                        isUser = isUserBuy,
                        compact = true
                    )
                    Text(
                        text = AuctionRules.formatCrores(record.soldPriceLakhs),
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = BroadcastGold
                    )
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = BroadcastSurfaceElevated,
                    border = BorderStroke(1.dp, BroadcastOutline)
                ) {
                    Text(
                        text = "UNSOLD",
                        fontFamily = MonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BroadcastTextMuted,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
