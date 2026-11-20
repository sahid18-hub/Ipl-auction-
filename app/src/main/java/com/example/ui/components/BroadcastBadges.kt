package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FranchiseCatalog
import com.example.model.FranchiseId
import com.example.model.Nationality
import com.example.model.PlayerRole
import com.example.model.PlayerStatus
import com.example.ui.theme.BroadcastAmber
import com.example.ui.theme.BroadcastCrimson
import com.example.ui.theme.BroadcastCyan
import com.example.ui.theme.BroadcastEmerald
import com.example.ui.theme.BroadcastGold
import com.example.ui.theme.BroadcastOutline
import com.example.ui.theme.BroadcastPurple
import com.example.ui.theme.BroadcastSurfaceHighlight
import com.example.ui.theme.BroadcastTextMuted
import com.example.ui.theme.BroadcastTextPrimary
import com.example.ui.theme.BroadcastTextSecondary
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.MonoFontFamily

@Composable
fun FranchiseBadge(
    franchiseId: FranchiseId,
    modifier: Modifier = Modifier,
    isUser: Boolean = false,
    compact: Boolean = false
) {
    val profile = FranchiseCatalog.get(franchiseId)
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(if (compact) 6.dp else 8.dp),
        color = profile.primaryColor,
        border = BorderStroke(
            width = if (isUser) 1.5.dp else 1.dp,
            color = if (isUser) BroadcastGold else profile.secondaryColor.copy(alpha = 0.75f)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = if (compact) 7.dp else 10.dp,
                vertical = if (compact) 3.dp else 4.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = profile.shortCode,
                fontFamily = DisplayFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = if (compact) 12.sp else 14.sp,
                letterSpacing = 0.8.sp,
                color = profile.onPrimaryColor,
                maxLines = 1
            )
            if (isUser) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(profile.onPrimaryColor.copy(alpha = 0.18f))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "YOU",
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.sp,
                        color = profile.onPrimaryColor
                    )
                }
            }
        }
    }
}

@Composable
fun RoleChip(
    role: PlayerRole,
    modifier: Modifier = Modifier
) {
    val accent = roleAccentColor(role)
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = accent.copy(alpha = 0.16f),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.SportsCricket,
                contentDescription = role.label,
                tint = accent,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = role.label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = accent,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun roleAccentColor(role: PlayerRole): Color = when (role) {
    PlayerRole.BATTER -> BroadcastCyan
    PlayerRole.WICKETKEEPER -> BroadcastGold
    PlayerRole.ALL_ROUNDER -> BroadcastEmerald
    PlayerRole.FAST_BOWLER -> BroadcastCrimson
    PlayerRole.SPINNER -> BroadcastPurple
}

@Composable
fun NationalityChip(
    nationality: Nationality,
    country: String,
    modifier: Modifier = Modifier
) {
    val isOverseas = nationality == Nationality.OVERSEAS
    val tint = if (isOverseas) BroadcastAmber else BroadcastEmerald
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = tint.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.40f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = if (isOverseas) Icons.Filled.Flight else Icons.Filled.Home,
                contentDescription = if (isOverseas) "Overseas player" else "Indian player",
                tint = tint,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = if (isOverseas) "OS • ${country.uppercase()}" else "IND • DOMESTIC",
                style = MaterialTheme.typography.labelSmall,
                color = tint,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun StatusChip(
    status: PlayerStatus,
    isCaptain: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = BroadcastSurfaceHighlight,
            border = BorderStroke(1.dp, BroadcastOutline)
        ) {
            Text(
                text = status.label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = BroadcastTextSecondary,
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
            )
        }
        if (isCaptain) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = BroadcastGold.copy(alpha = 0.18f),
                border = BorderStroke(1.dp, BroadcastGold.copy(alpha = 0.55f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Captaincy candidate",
                        tint = BroadcastGold,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = "CAPTAIN",
                        style = MaterialTheme.typography.labelSmall,
                        color = BroadcastGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun LiveOnAirIndicator(
    isPaused: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(750),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    val dotColor = if (isPaused) BroadcastAmber else BroadcastCrimson
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = dotColor.copy(alpha = 0.18f),
        border = BorderStroke(1.dp, dotColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .alpha(if (isPaused) 1f else pulseAlpha)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Text(
                text = if (isPaused) "PAUSED" else "ON AIR",
                fontFamily = MonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = BroadcastTextPrimary,
                letterSpacing = 0.8.sp
            )
        }
    }
}

@Composable
fun RatingMeterBar(
    label: String,
    value: Int,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = BroadcastTextMuted
            )
            Text(
                text = "$value/100",
                fontFamily = MonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = accentColor
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(BroadcastSurfaceHighlight)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth((value.coerceIn(0, 100) / 100f))
                    .height(7.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(accentColor.copy(alpha = 0.7f), accentColor)
                        )
                    )
            )
        }
    }
}

@Composable
fun TelemetryPill(
    label: String,
    value: String,
    valueColor: Color = BroadcastTextPrimary,
    modifier: Modifier = Modifier,
    minWidth: Dp = 0.dp
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(BroadcastSurfaceHighlight.copy(alpha = 0.72f))
            .border(1.dp, BroadcastOutline.copy(alpha = 0.65f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label.uppercase(),
            fontFamily = MonoFontFamily,
            fontSize = 9.sp,
            color = BroadcastTextMuted,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            text = value,
            fontFamily = MonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = valueColor,
            maxLines = 1
        )
    }
}
