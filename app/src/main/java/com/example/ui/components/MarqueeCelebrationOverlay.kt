package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.ui.theme.BroadcastAmber
import com.example.ui.theme.BroadcastCyan
import com.example.ui.theme.BroadcastEmerald
import com.example.ui.theme.BroadcastGold
import com.example.ui.theme.BroadcastGoldLight
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class ConfettiParticle(
    val startX: Float,
    val startY: Float,
    val velocityX: Float,
    val velocityY: Float,
    val sizeDp: Float,
    val aspect: Float,
    val spinSpeed: Float,
    val initialAngle: Float,
    val color: Color
)

/**
 * Brief, non-intrusive gold shimmer sweep + celebratory confetti particle burst
 * layered onto the Sold banner whenever the user wins a Marquee player (₹12 Cr+).
 */
@Composable
fun MarqueeCelebrationOverlay(
    triggerKey: String,
    teamPrimaryColor: Color,
    teamSecondaryColor: Color,
    modifier: Modifier = Modifier
) {
    val confettiProgress = remember(triggerKey) { Animatable(0f) }
    val shimmerProgress = remember(triggerKey) { Animatable(-0.35f) }

    val particles = remember(triggerKey, teamPrimaryColor, teamSecondaryColor) {
        val rng = Random(triggerKey.hashCode())
        val palette = listOf(
            BroadcastGold,
            BroadcastGoldLight,
            BroadcastAmber,
            teamPrimaryColor,
            teamSecondaryColor,
            BroadcastEmerald,
            BroadcastCyan
        )
        List(32) { idx ->
            val spreadAngle = -Math.PI * (0.12 + rng.nextDouble() * 0.76)
            val speed = 0.28f + rng.nextFloat() * 0.52f
            ConfettiParticle(
                startX = 0.08f + (idx / 32f) * 0.84f + rng.nextFloat() * 0.04f,
                startY = 0.15f + rng.nextFloat() * 0.35f,
                velocityX = (cos(spreadAngle) * speed * 0.45).toFloat(),
                velocityY = (sin(spreadAngle) * speed * 0.65).toFloat(),
                sizeDp = 4f + rng.nextFloat() * 4.5f,
                aspect = 0.45f + rng.nextFloat() * 0.5f,
                spinSpeed = (rng.nextFloat() - 0.5f) * 520f,
                initialAngle = rng.nextFloat() * 360f,
                color = palette[idx % palette.size]
            )
        }
    }

    LaunchedEffect(triggerKey) {
        confettiProgress.snapTo(0f)
        confettiProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1750, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(triggerKey) {
        shimmerProgress.snapTo(-0.35f)
        shimmerProgress.animateTo(
            targetValue = 1.45f,
            animationSpec = tween(durationMillis = 1050, easing = LinearEasing)
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        // 1. Diagonal broadcast gold shimmer sweep across the Sold card
        val sweepPos = shimmerProgress.value
        if (sweepPos in -0.35f..1.4f) {
            val bandWidth = w * 0.28f
            val centerX = w * sweepPos
            val shimmerBrush = Brush.linearGradient(
                colors = listOf(
                    Color.Transparent,
                    BroadcastGoldLight.copy(alpha = 0.28f),
                    Color.White.copy(alpha = 0.22f),
                    BroadcastGold.copy(alpha = 0.18f),
                    Color.Transparent
                ),
                start = Offset(centerX - bandWidth, 0f),
                end = Offset(centerX + bandWidth, h)
            )
            drawRect(brush = shimmerBrush, size = size)
        }

        // 2. Celebratory gold & franchise-colored confetti burst
        val t = confettiProgress.value
        val fadeAlpha = when {
            t < 0.12f -> t / 0.12f
            t > 0.72f -> ((1f - t) / 0.28f).coerceIn(0f, 1f)
            else -> 1f
        }

        if (fadeAlpha > 0.01f) {
            val gravity = 0.85f
            particles.forEach { p ->
                val x = (p.startX + p.velocityX * t) * w
                val y = (p.startY + p.velocityY * t + 0.5f * gravity * t * t) * h
                if (x in -20f..(w + 20f) && y in -20f..(h + 20f)) {
                    val pxSize = p.sizeDp * density
                    val angle = p.initialAngle + p.spinSpeed * t
                    rotate(degrees = angle, pivot = Offset(x, y)) {
                        drawRect(
                            color = p.color.copy(alpha = fadeAlpha * 0.92f),
                            topLeft = Offset(x - pxSize / 2f, y - (pxSize * p.aspect) / 2f),
                            size = Size(pxSize, pxSize * p.aspect)
                        )
                    }
                }
            }
        }
    }
}
