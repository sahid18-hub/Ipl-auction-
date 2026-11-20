package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FranchiseCatalog
import com.example.engine.AuctionRules
import com.example.model.FranchiseId
import com.example.model.Player
import com.example.ui.theme.BroadcastAmber
import com.example.ui.theme.BroadcastBgDeep
import com.example.ui.theme.BroadcastCrimson
import com.example.ui.theme.BroadcastGold
import com.example.ui.theme.BroadcastTextPrimary
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.MonoFontFamily
import java.util.Locale

@Composable
fun FinalCallWarningBanner(
    visible: Boolean,
    player: Player?,
    leadingFranchiseId: FranchiseId?,
    currentBidLakhs: Int,
    finalCallProgress: Float,
    finalCallRemainingSeconds: Float,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible && player != null,
        enter = fadeIn(tween(180)) + expandVertically(spring(dampingRatio = 0.72f, stiffness = 420f)) + scaleIn(initialScale = 0.94f),
        exit = fadeOut(tween(120)) + shrinkVertically(tween(150)),
        modifier = modifier
    ) {
        if (player != null) {
            val infiniteTransition = rememberInfiniteTransition(label = "final_call_pulse")
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.65f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(360),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse_border"
            )
            val iconScale by infiniteTransition.animateFloat(
                initialValue = 0.94f,
                targetValue = 1.12f,
                animationSpec = infiniteRepeatable(
                    animation = tween(360),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse_icon"
            )
            val smoothBar by animateFloatAsState(
                targetValue = finalCallProgress.coerceIn(0f, 1f),
                animationSpec = tween(durationMillis = 80),
                label = "final_call_bar"
            )

            val warningCopy = if (leadingFranchiseId != null) {
                val teamName = FranchiseCatalog.get(leadingFranchiseId).shortCode
                "⚠️ FINAL CALL! ${player.name} going to $teamName for ${AuctionRules.formatCrores(currentBidLakhs)} — LAST CHANCE TO BID!"
            } else {
                "⚠️ FINAL CALL! ${player.name} going UNSOLD at Base ${AuctionRules.formatCrores(player.basePriceLakhs)} — LAST CHANCE TO BID!"
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("final_call_warning_banner"),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF3A0D14),
                border = BorderStroke(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            BroadcastCrimson.copy(alpha = pulseAlpha),
                            BroadcastGold.copy(alpha = pulseAlpha),
                            BroadcastCrimson.copy(alpha = pulseAlpha)
                        )
                    )
                ),
                shadowElevation = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    BroadcastCrimson.copy(alpha = 0.34f * pulseAlpha),
                                    Color(0xFF4B111A).copy(alpha = 0.92f),
                                    BroadcastAmber.copy(alpha = 0.24f * pulseAlpha)
                                )
                            )
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                                imageVector = Icons.Filled.WarningAmber,
                                contentDescription = "Final call warning",
                                tint = BroadcastGold,
                                modifier = Modifier
                                    .size(22.dp)
                                    .scale(iconScale)
                            )
                            Text(
                                text = warningCopy,
                                fontFamily = DisplayFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                lineHeight = 18.sp,
                                letterSpacing = 0.4.sp,
                                color = BroadcastTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(BroadcastBgDeep.copy(alpha = 0.85f))
                                .border(1.dp, BroadcastGold.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = String.format(Locale.US, "%.1fs", finalCallRemainingSeconds.coerceAtLeast(0f)),
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = BroadcastGold,
                                modifier = Modifier.testTag("final_call_timer_text")
                            )
                        }
                    }

                    // 2.5-Second High-Contrast Visual Countdown Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(BroadcastBgDeep.copy(alpha = 0.85f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(smoothBar)
                                .height(7.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(BroadcastCrimson, BroadcastAmber, BroadcastGold)
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}
