package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.engine.ValuationEngine
import com.example.model.FranchiseId
import com.example.model.HammerStage
import com.example.model.PaddleEvent
import com.example.model.PaddleEventType
import com.example.model.Player
import com.example.model.SoldRecord
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

@Composable
fun PlayerSpotlightStage(
    player: Player,
    playerIndexInSet: Int,
    totalInSet: Int,
    trueMarketValueLakhs: Int,
    currentBidLakhs: Int,
    hasOpeningBid: Boolean,
    highestBidderId: FranchiseId?,
    userFranchiseId: FranchiseId,
    hammerStage: HammerStage,
    countdownProgress: Float,
    paddleFeed: List<PaddleEvent>,
    activeBiddingTeams: Set<FranchiseId>,
    backedOutTeams: Set<FranchiseId>,
    quickResultBanner: SoldRecord?,
    onDismissQuickBanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    val leadingColor = highestBidderId?.let { FranchiseCatalog.get(it).primaryColor } ?: BroadcastGold
    val animatedCountdown by animateFloatAsState(
        targetValue = countdownProgress.coerceIn(0f, 1f),
        animationSpec = spring(stiffness = 380f),
        label = "countdown_bar"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. PLAYER SPOTLIGHT BROADCAST CARD
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = BroadcastSurfaceElevated,
            border = BorderStroke(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        leadingColor.copy(alpha = 0.85f),
                        BroadcastOutline,
                        BroadcastGold.copy(alpha = 0.45f)
                    )
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("player_spotlight_card")
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Spotlight Accent Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    leadingColor.copy(alpha = 0.24f),
                                    BroadcastSurfaceHighlight.copy(alpha = 0.55f),
                                    BroadcastSurfaceElevated
                                )
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "LOT #${playerIndexInSet + 1} OF $totalInSet • ${player.set.shortTitle}",
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = BroadcastGold,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = ValuationEngine.valuationTierLabel(trueMarketValueLakhs),
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = BroadcastCyan
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = player.name.uppercase(),
                                    fontFamily = DisplayFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 30.sp,
                                    lineHeight = 32.sp,
                                    letterSpacing = 0.6.sp,
                                    color = BroadcastTextPrimary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "${player.archetype.label} • Age ${player.age} • ${player.battingStyle} / ${player.bowlingStyle}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BroadcastTextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Base Price Callout Badge
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BroadcastBgDeep.copy(alpha = 0.72f))
                                    .border(1.dp, BroadcastOutline, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "BASE PRICE",
                                    fontFamily = MonoFontFamily,
                                    fontSize = 9.sp,
                                    color = BroadcastTextMuted
                                )
                                Text(
                                    text = AuctionRules.formatCrores(player.basePriceLakhs),
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = BroadcastGold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Attribute Chips Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RoleChip(role = player.role)
                            NationalityChip(nationality = player.nationality, country = player.country)
                            StatusChip(status = player.status, isCaptain = player.isCaptainMaterial)
                        }
                    }
                }

                HorizontalDivider(color = BroadcastOutlineSubtle)

                // Stats + Form/Hype Telemetry
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatMiniBox(
                            label = "IPL MATCHES",
                            value = if (player.iplMatches > 0) "${player.iplMatches}" else "DEBUT",
                            modifier = Modifier.weight(1f)
                        )
                        StatMiniBox(
                            label = player.primaryStatLabel,
                            value = player.primaryStatValue,
                            highlight = BroadcastCyan,
                            modifier = Modifier.weight(1f)
                        )
                        StatMiniBox(
                            label = player.secondaryStatLabel,
                            value = player.secondaryStatValue,
                            highlight = BroadcastGold,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        RatingMeterBar(
                            label = "Current Form",
                            value = player.form,
                            accentColor = BroadcastEmerald,
                            modifier = Modifier.weight(1f)
                        )
                        RatingMeterBar(
                            label = "Market Hype",
                            value = player.hype,
                            accentColor = BroadcastAmber,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 2. LIVE BID TELEMETRY CONSOLE & AUCTIONEER TICKER
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BroadcastSurface,
            border = BorderStroke(
                width = if (hammerStage == HammerStage.FINAL_CALL) 2.dp else 1.5.dp,
                color = when (hammerStage) {
                    HammerStage.FINAL_CALL -> BroadcastCrimson
                    HammerStage.GOING_TWICE -> BroadcastCrimson.copy(alpha = 0.85f)
                    HammerStage.GOING_ONCE -> BroadcastAmber.copy(alpha = 0.8f)
                    HammerStage.SOLD -> BroadcastEmerald.copy(alpha = 0.85f)
                    else -> BroadcastOutline
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("live_bid_console")
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (hasOpeningBid) "CURRENT LIVE BID" else "OPENING CALL AT BASE",
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = BroadcastTextMuted,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        val displayAmount = if (hasOpeningBid) currentBidLakhs else player.basePriceLakhs
                        Text(
                            text = AuctionRules.formatCrores(displayAmount),
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 40.sp,
                            lineHeight = 42.sp,
                            color = if (hasOpeningBid) BroadcastGold else BroadcastTextPrimary,
                            modifier = Modifier.testTag("current_bid_amount")
                        )
                    }

                    // Highest Bidder Pillar
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "HIGHEST BIDDER",
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = BroadcastTextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        if (highestBidderId != null) {
                            FranchiseBadge(
                                franchiseId = highestBidderId,
                                isUser = highestBidderId == userFranchiseId
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = FranchiseCatalog.get(highestBidderId).fullName,
                                style = MaterialTheme.typography.labelSmall,
                                color = BroadcastTextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        } else {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = BroadcastSurfaceHighlight,
                                border = BorderStroke(1.dp, BroadcastOutline)
                            ) {
                                Text(
                                    text = "NO BIDS YET",
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = BroadcastTextMuted,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }

                // Auctioneer Status Bar + Smooth Hammer Countdown
                val tickerColor = when (hammerStage) {
                    HammerStage.WAITING_OPENING_BID -> BroadcastCyan
                    HammerStage.ACTIVE_BIDDING -> BroadcastGold
                    HammerStage.GOING_ONCE -> BroadcastAmber
                    HammerStage.GOING_TWICE -> BroadcastCrimson
                    HammerStage.FINAL_CALL -> BroadcastCrimson
                    HammerStage.SOLD -> BroadcastEmerald
                    HammerStage.UNSOLD -> BroadcastTextMuted
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(tickerColor.copy(alpha = 0.12f))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Gavel,
                                contentDescription = "Auctioneer status",
                                tint = tickerColor,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = hammerStage.broadcastText,
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = tickerColor,
                                letterSpacing = 0.6.sp
                            )
                        }
                        Text(
                            text = if (hasOpeningBid) {
                                "NEXT: ${AuctionRules.formatIncrementLabel(currentBidLakhs, true)}"
                            } else {
                                "OPEN AT BASE"
                            },
                            fontFamily = MonoFontFamily,
                            fontSize = 10.sp,
                            color = BroadcastTextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(BroadcastBgDeep)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedCountdown)
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(tickerColor)
                        )
                    }
                }
            }
        }

        // 3. QUICK RESULT BANNER OVERLAY (On SOLD / UNSOLD / SKIP PLAYER)
        AnimatedVisibility(
            visible = quickResultBanner != null,
            enter = fadeIn() + scaleIn(initialScale = 0.94f),
            exit = fadeOut()
        ) {
            quickResultBanner?.let { record ->
                QuickResultBannerCard(
                    record = record,
                    userFranchiseId = userFranchiseId,
                    onClick = onDismissQuickBanner
                )
            }
        }

        // 4. LIVE "PADDLE RAISE" ACTIVITY FEED
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = BroadcastSurface,
            border = BorderStroke(1.dp, BroadcastOutlineSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PanTool,
                            contentDescription = "Live paddle raises",
                            tint = BroadcastGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "LIVE PADDLE ROOM FEED",
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = BroadcastTextSecondary,
                            letterSpacing = 0.7.sp
                        )
                    }
                    if (activeBiddingTeams.isNotEmpty() || backedOutTeams.isNotEmpty()) {
                        Text(
                            text = "${activeBiddingTeams.size} IN • ${backedOutTeams.size} OUT",
                            fontFamily = MonoFontFamily,
                            fontSize = 10.sp,
                            color = BroadcastTextMuted
                        )
                    }
                }

                // Active & Backed Out Team Badges Strip
                if (activeBiddingTeams.isNotEmpty() || backedOutTeams.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        activeBiddingTeams.take(4).forEach { teamId ->
                            val isOut = teamId in backedOutTeams
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = if (isOut) BroadcastSurfaceHighlight else FranchiseCatalog.get(teamId).primaryColor.copy(alpha = 0.25f),
                                border = BorderStroke(
                                    1.dp,
                                    if (isOut) BroadcastOutline else FranchiseCatalog.get(teamId).primaryColor
                                )
                            ) {
                                Text(
                                    text = if (isOut) "${teamId.shortCode} OUT" else "${teamId.shortCode} BIDDING",
                                    fontFamily = MonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isOut) BroadcastTextMuted else BroadcastTextPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = BroadcastOutlineSubtle)

                // Recent 4 Paddle Events
                paddleFeed.take(4).forEach { evt ->
                    PaddleFeedRow(event = evt, userFranchiseId = userFranchiseId)
                }
            }
        }
    }
}

@Composable
private fun StatMiniBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    highlight: Color = BroadcastTextPrimary
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(9.dp))
            .background(BroadcastBgDeep.copy(alpha = 0.65f))
            .border(1.dp, BroadcastOutlineSubtle, RoundedCornerShape(9.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontFamily = MonoFontFamily,
            fontSize = 9.sp,
            color = BroadcastTextMuted,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontFamily = MonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = highlight,
            maxLines = 1
        )
    }
}

@Composable
private fun QuickResultBannerCard(
    record: SoldRecord,
    userFranchiseId: FranchiseId,
    onClick: () -> Unit
) {
    val winnerProfile = record.winningFranchiseId?.let { FranchiseCatalog.get(it) }
    val isUserMarqueeCelebration = record.isSold &&
        record.winningFranchiseId == userFranchiseId &&
        record.soldPriceLakhs >= 1200

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("quick_result_banner"),
        shape = RoundedCornerShape(14.dp),
        color = when {
            isUserMarqueeCelebration -> BroadcastGold.copy(alpha = 0.20f)
            record.isSold -> BroadcastEmerald.copy(alpha = 0.16f)
            else -> BroadcastSurfaceHighlight
        },
        border = BorderStroke(
            width = if (isUserMarqueeCelebration) 2.dp else 1.5.dp,
            color = when {
                isUserMarqueeCelebration -> BroadcastGold
                record.isSold -> BroadcastEmerald
                else -> BroadcastOutline
            }
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (isUserMarqueeCelebration && winnerProfile != null) {
                MarqueeCelebrationOverlay(
                    triggerKey = "${record.player.id}_${record.soldPriceLakhs}",
                    teamPrimaryColor = winnerProfile.primaryColor,
                    teamSecondaryColor = winnerProfile.secondaryColor,
                    modifier = Modifier.matchParentSize()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = when {
                                isUserMarqueeCelebration -> "MARQUEE SIGNING — SOLD!"
                                record.isSold -> "HAMMER DOWN — SOLD!"
                                else -> "PASSED — UNSOLD"
                            },
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = when {
                                isUserMarqueeCelebration -> BroadcastGold
                                record.isSold -> BroadcastEmerald
                                else -> BroadcastTextSecondary
                            },
                            letterSpacing = 0.8.sp
                        )
                        if (isUserMarqueeCelebration) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = BroadcastGold.copy(alpha = 0.28f)
                            ) {
                                Text(
                                    text = "BLOCKBUSTER",
                                    fontFamily = MonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BroadcastGold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        } else if (record.isBiddingWar) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = BroadcastGold.copy(alpha = 0.22f)
                            ) {
                                Text(
                                    text = "BIDDING WAR",
                                    fontFamily = MonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BroadcastGold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        } else if (record.isValueSteal) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = BroadcastCyan.copy(alpha = 0.22f)
                            ) {
                                Text(
                                    text = "STEAL!",
                                    fontFamily = MonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BroadcastCyan,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = if (record.isSold && winnerProfile != null) {
                            "${record.player.name} joins ${winnerProfile.fullName} • Tap for next"
                        } else {
                            "${record.player.name} moves to Accelerated Round pool • Tap for next"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = BroadcastTextPrimary,
                        maxLines = 2
                    )
                }

                if (record.isSold && record.winningFranchiseId != null) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        FranchiseBadge(
                            franchiseId = record.winningFranchiseId,
                            isUser = record.winningFranchiseId == userFranchiseId,
                            compact = true
                        )
                        Spacer(modifier = Modifier.height(4.dp))
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
    }
}

@Composable
private fun PaddleFeedRow(
    event: PaddleEvent,
    userFranchiseId: FranchiseId
) {
    val icon = when (event.type) {
        PaddleEventType.BID_PLACED -> Icons.Filled.TrendingUp
        PaddleEventType.BACKED_OUT -> Icons.Filled.Cancel
        PaddleEventType.SOLD_STAMP -> Icons.Filled.Gavel
        PaddleEventType.HAMMER_CALL -> Icons.Filled.Bolt
        else -> Icons.Filled.Gavel
    }
    val tint = when (event.type) {
        PaddleEventType.BID_PLACED -> event.franchiseId?.let { FranchiseCatalog.get(it).primaryColor } ?: BroadcastGold
        PaddleEventType.BACKED_OUT -> BroadcastCrimson.copy(alpha = 0.85f)
        PaddleEventType.SOLD_STAMP -> BroadcastEmerald
        PaddleEventType.UNSOLD_STAMP -> BroadcastTextMuted
        PaddleEventType.HAMMER_CALL -> BroadcastAmber
        PaddleEventType.OPENING_CALL -> BroadcastCyan
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(13.dp)
            )
        }
        Text(
            text = event.message,
            style = MaterialTheme.typography.bodySmall,
            color = when (event.type) {
                PaddleEventType.BACKED_OUT -> BroadcastTextMuted
                PaddleEventType.SOLD_STAMP -> BroadcastEmerald
                else -> BroadcastTextPrimary
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (event.franchiseId != null && event.type == PaddleEventType.BID_PLACED) {
            FranchiseBadge(
                franchiseId = event.franchiseId,
                isUser = event.franchiseId == userFranchiseId,
                compact = true
            )
        }
    }
}
