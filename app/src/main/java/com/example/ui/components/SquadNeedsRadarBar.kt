package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.AuctionRules
import com.example.model.AuctionSet
import com.example.model.FranchiseState
import com.example.model.PlayerRole
import com.example.ui.theme.BroadcastAmber
import com.example.ui.theme.BroadcastBgDeep
import com.example.ui.theme.BroadcastCrimson
import com.example.ui.theme.BroadcastEmerald
import com.example.ui.theme.BroadcastOutline
import com.example.ui.theme.BroadcastTextSecondary
import com.example.ui.theme.MonoFontFamily

private enum class RadarPillState {
    CRITICAL,
    BUILDING,
    MET,
    OS_WARNING
}

private data class RadarSlotSpec(
    val testTag: String,
    val label: String,
    val countText: String,
    val state: RadarPillState,
    val isOverseas: Boolean = false
)

/**
 * Compact, single-row horizontally scrollable Live Squad Needs & Weakness Radar
 * displayed directly below the user's top purse bar.
 * Uses a single lightweight infinite transition shared across all critical pills for smooth 60 FPS.
 */
@Composable
fun SquadNeedsRadarBar(
    userState: FranchiseState,
    currentSet: AuctionSet,
    modifier: Modifier = Modifier
) {
    val batCount = userState.countByRole(PlayerRole.BATTER)
    val wkCount = userState.countByRole(PlayerRole.WICKETKEEPER)
    val arCount = userState.countByRole(PlayerRole.ALL_ROUNDER)
    val paceCount = userState.countByRole(PlayerRole.FAST_BOWLER)
    val spinCount = userState.countByRole(PlayerRole.SPINNER)
    val osCount = userState.overseasCount

    val setNum = currentSet.setNumber
    val lateStage = setNum >= 7 || userState.squadCount >= 11

    fun roleState(count: Int, minTarget: Int, criticalEmptyFromSet: Int, criticalLowFromSet: Int): RadarPillState {
        return when {
            count >= minTarget -> RadarPillState.MET
            count == 0 && (setNum >= criticalEmptyFromSet || userState.squadCount >= 9) -> RadarPillState.CRITICAL
            count < (minTarget - 1).coerceAtLeast(1) && (setNum >= criticalLowFromSet || lateStage) -> RadarPillState.CRITICAL
            setNum >= 8 && count < minTarget -> RadarPillState.CRITICAL
            else -> RadarPillState.BUILDING
        }
    }

    val osState = when {
        osCount >= AuctionRules.MAX_OVERSEAS -> RadarPillState.OS_WARNING
        osCount == AuctionRules.MAX_OVERSEAS - 1 && setNum <= 6 -> RadarPillState.OS_WARNING
        else -> RadarPillState.BUILDING
    }

    val slots = listOf(
        RadarSlotSpec(
            testTag = "radar_pill_bat",
            label = "Bat",
            countText = "$batCount/5-6",
            state = roleState(batCount, minTarget = 5, criticalEmptyFromSet = 4, criticalLowFromSet = 6)
        ),
        RadarSlotSpec(
            testTag = "radar_pill_wk",
            label = "WK",
            countText = "$wkCount/2",
            state = roleState(wkCount, minTarget = 2, criticalEmptyFromSet = 5, criticalLowFromSet = 7)
        ),
        RadarSlotSpec(
            testTag = "radar_pill_ar",
            label = "AR",
            countText = "$arCount/4",
            state = roleState(arCount, minTarget = 4, criticalEmptyFromSet = 5, criticalLowFromSet = 7)
        ),
        RadarSlotSpec(
            testTag = "radar_pill_pace",
            label = "Pace",
            countText = "$paceCount/5",
            state = roleState(paceCount, minTarget = 5, criticalEmptyFromSet = 6, criticalLowFromSet = 7)
        ),
        RadarSlotSpec(
            testTag = "radar_pill_spin",
            label = "Spin",
            countText = "$spinCount/3",
            state = roleState(spinCount, minTarget = 3, criticalEmptyFromSet = 7, criticalLowFromSet = 8)
        ),
        RadarSlotSpec(
            testTag = "radar_pill_os",
            label = "OS",
            countText = "$osCount/8",
            state = osState,
            isOverseas = true
        )
    )

    val hasAnyCritical = slots.any { it.state == RadarPillState.CRITICAL }
    val pulseAlpha = if (hasAnyCritical) {
        val transition = rememberInfiniteTransition(label = "radar_critical_pulse")
        val animated by transition.animateFloat(
            initialValue = 0.45f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 720),
                repeatMode = RepeatMode.Reverse
            ),
            label = "radar_pulse_alpha"
        )
        animated
    } else {
        1f
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .testTag("squad_needs_radar_bar"),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        slots.forEach { slot ->
            RadarNeedPill(
                spec = slot,
                pulseAlpha = if (slot.state == RadarPillState.CRITICAL) pulseAlpha else 1f
            )
        }
    }
}

@Composable
private fun RadarNeedPill(
    spec: RadarSlotSpec,
    pulseAlpha: Float
) {
    val bgColor: Color
    val borderColor: Color
    val contentColor: Color

    when (spec.state) {
        RadarPillState.CRITICAL -> {
            bgColor = BroadcastCrimson.copy(alpha = 0.16f + (0.14f * pulseAlpha))
            borderColor = BroadcastCrimson.copy(alpha = 0.55f + (0.45f * pulseAlpha))
            contentColor = BroadcastAmber
        }
        RadarPillState.MET -> {
            bgColor = BroadcastEmerald.copy(alpha = 0.16f)
            borderColor = BroadcastEmerald.copy(alpha = 0.65f)
            contentColor = BroadcastEmerald
        }
        RadarPillState.OS_WARNING -> {
            bgColor = BroadcastAmber.copy(alpha = 0.16f)
            borderColor = BroadcastAmber.copy(alpha = 0.70f)
            contentColor = BroadcastAmber
        }
        RadarPillState.BUILDING -> {
            bgColor = BroadcastBgDeep.copy(alpha = 0.72f)
            borderColor = BroadcastOutline
            contentColor = BroadcastTextSecondary
        }
    }

    Surface(
        modifier = Modifier.testTag(spec.testTag),
        shape = RoundedCornerShape(7.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            when {
                spec.state == RadarPillState.CRITICAL -> {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "Critical squad need",
                        tint = BroadcastAmber,
                        modifier = Modifier.size(11.dp)
                    )
                }
                spec.state == RadarPillState.MET -> {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Target quota met",
                        tint = BroadcastEmerald,
                        modifier = Modifier.size(11.dp)
                    )
                }
                spec.isOverseas -> {
                    Icon(
                        imageVector = Icons.Filled.Public,
                        contentDescription = "Overseas slots",
                        tint = contentColor,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
            Text(
                text = "${spec.label}: ${spec.countText}",
                fontFamily = MonoFontFamily,
                fontWeight = if (spec.state == RadarPillState.BUILDING) FontWeight.SemiBold else FontWeight.Bold,
                fontSize = 10.sp,
                color = contentColor,
                maxLines = 1
            )
        }
    }
}
