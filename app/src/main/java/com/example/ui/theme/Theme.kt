package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val ColorContainerGold = androidx.compose.ui.graphics.Color(0xFF332609)

private val BroadcastDarkColorScheme = darkColorScheme(
    primary = BroadcastGold,
    onPrimary = OnBroadcastGold,
    primaryContainer = ColorContainerGold,
    onPrimaryContainer = BroadcastGoldLight,
    secondary = BroadcastCyan,
    onSecondary = BroadcastBgDeep,
    secondaryContainer = BroadcastCyanContainer,
    onSecondaryContainer = BroadcastTextPrimary,
    tertiary = BroadcastEmerald,
    onTertiary = BroadcastBgDeep,
    tertiaryContainer = BroadcastEmeraldContainer,
    onTertiaryContainer = BroadcastTextPrimary,
    background = BroadcastBgDeep,
    onBackground = BroadcastTextPrimary,
    surface = BroadcastSurface,
    onSurface = BroadcastTextPrimary,
    surfaceVariant = BroadcastSurfaceElevated,
    onSurfaceVariant = BroadcastTextSecondary,
    surfaceContainerLowest = BroadcastBgDeep,
    surfaceContainerLow = BroadcastSurface,
    surfaceContainer = BroadcastSurfaceElevated,
    surfaceContainerHigh = BroadcastSurfaceHighlight,
    surfaceContainerHighest = BroadcastOutline,
    outline = BroadcastOutline,
    outlineVariant = BroadcastOutlineSubtle,
    error = BroadcastCrimson,
    onError = BroadcastTextPrimary,
    errorContainer = BroadcastCrimsonContainer,
    onErrorContainer = BroadcastTextPrimary
)

val BroadcastShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(26.dp)
)

@Composable
fun IplAuctionTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BroadcastDarkColorScheme,
        typography = BroadcastTypography,
        shapes = BroadcastShapes,
        content = content
    )
}
