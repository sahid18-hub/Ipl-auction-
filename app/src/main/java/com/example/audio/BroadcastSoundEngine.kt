package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

/**
 * Pure procedural broadcast audio synthesizer using Android AudioTrack PCM buffers.
 * Zero external sound assets required. Safe against headless/test audio environments.
 */
class BroadcastSoundEngine {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Volatile
    var isMuted: Boolean = false

    private val bidTickPcm: ShortArray by lazy { generateBidPaddleTickPcm() }
    private val finalCallAlertPcm: ShortArray by lazy { generateFinalCallAlertPcm() }
    private val gavelSlamPcm: ShortArray by lazy { generateGavelSlamPcm() }
    private val marqueeSoldFanfarePcm: ShortArray by lazy { generateMarqueeSoldFanfarePcm() }
    private val unsoldBuzzerPcm: ShortArray by lazy { generateUnsoldBuzzerPcm() }

    fun playBidPaddleTick() {
        if (isMuted) return
        playBufferAsync(bidTickPcm, volume = 0.55f)
    }

    fun playFinalCallAlert() {
        if (isMuted) return
        playBufferAsync(finalCallAlertPcm, volume = 0.78f)
    }

    fun playGavelHammerSlam(isMarqueeCelebration: Boolean = false) {
        if (isMuted) return
        if (isMarqueeCelebration) {
            playBufferAsync(marqueeSoldFanfarePcm, volume = 0.85f)
        } else {
            playBufferAsync(gavelSlamPcm, volume = 0.82f)
        }
    }

    fun playUnsoldBuzzer() {
        if (isMuted) return
        playBufferAsync(unsoldBuzzerPcm, volume = 0.65f)
    }

    private fun playBufferAsync(samples: ShortArray, volume: Float) {
        scope.launch {
            var track: AudioTrack? = null
            try {
                val byteCount = samples.size * 2
                val minBuf = AudioTrack.getMinBufferSize(
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                if (minBuf <= 0) return@launch

                track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .setBufferSizeInBytes(maxOf(byteCount, minBuf))
                    .build()

                if (track.state != AudioTrack.STATE_INITIALIZED) {
                    track.release()
                    return@launch
                }

                track.setVolume(volume.coerceIn(0f, 1f))
                track.write(samples, 0, samples.size)
                track.play()

                val durationMs = ((samples.size.toLong() * 1000L) / SAMPLE_RATE) + 60L
                kotlinx.coroutines.delay(durationMs)
            } catch (_: Throwable) {
                // Gracefully no-op in headless/JVM test environments without audio hardware
            } finally {
                try {
                    track?.stop()
                } catch (_: Throwable) {
                }
                try {
                    track?.release()
                } catch (_: Throwable) {
                }
            }
        }
    }

    /**
     * Crisp, subtle paddle raise tick (~65 ms): sharp woody transient + ascending broadcast chirp.
     */
    private fun generateBidPaddleTickPcm(): ShortArray {
        val durationSec = 0.068
        val count = (SAMPLE_RATE * durationSec).toInt()
        val out = ShortArray(count)
        val rng = Random(101)
        for (i in 0 until count) {
            val t = i.toDouble() / SAMPLE_RATE
            val env = exp(-t * 58.0)
            val freq = 960.0 + (t / durationSec) * 520.0
            val tone = sin(2.0 * PI * freq * t) * 0.72 +
                sin(2.0 * PI * (freq * 2.0) * t) * 0.22
            val click = if (t < 0.008) (rng.nextDouble() * 2.0 - 1.0) * exp(-t * 320.0) * 0.45 else 0.0
            val sample = ((tone * env + click) * 0.75).coerceIn(-1.0, 1.0)
            out[i] = (sample * Short.MAX_VALUE).toInt().toShort()
        }
        return out
    }

    /**
     * Urgent double-pulse alert chime (~330 ms) when the FINAL CALL warning banner appears.
     */
    private fun generateFinalCallAlertPcm(): ShortArray {
        val durationSec = 0.34
        val count = (SAMPLE_RATE * durationSec).toInt()
        val out = ShortArray(count)
        for (i in 0 until count) {
            val t = i.toDouble() / SAMPLE_RATE
            val pulse1 = if (t in 0.0..0.115) {
                val localT = t
                val attack = (localT / 0.008).coerceAtMost(1.0)
                val decay = exp(-localT * 18.0)
                val wave = sin(2.0 * PI * 987.77 * localT) * 0.65 +
                    sin(2.0 * PI * 1479.98 * localT) * 0.35
                wave * attack * decay
            } else {
                0.0
            }

            val pulse2 = if (t >= 0.165) {
                val localT = t - 0.165
                val attack = (localT / 0.008).coerceAtMost(1.0)
                val decay = exp(-localT * 14.0)
                val wave = sin(2.0 * PI * 1174.66 * localT) * 0.65 +
                    sin(2.0 * PI * 1760.0 * localT) * 0.35
                wave * attack * decay
            } else {
                0.0
            }

            val sample = ((pulse1 + pulse2) * 0.82).coerceIn(-1.0, 1.0)
            out[i] = (sample * Short.MAX_VALUE).toInt().toShort()
        }
        return out
    }

    /**
     * Crisp wooden Gavel Hammer Slam (~280 ms): deep mahogany thud + resonant crack + subtle double-bounce.
     */
    private fun generateGavelSlamPcm(): ShortArray {
        val durationSec = 0.29
        val count = (SAMPLE_RATE * durationSec).toInt()
        val out = ShortArray(count)
        val rng = Random(404)
        for (i in 0 until count) {
            val t = i.toDouble() / SAMPLE_RATE
            val primary = gavelStrikeAt(t, rng)
            // Subtle micro wood bounce 115ms later
            val bounce = if (t >= 0.115) gavelStrikeAt(t - 0.115, rng) * 0.32 else 0.0
            val sample = ((primary + bounce) * 0.90).coerceIn(-1.0, 1.0)
            out[i] = (sample * Short.MAX_VALUE).toInt().toShort()
        }
        return out
    }

    /**
     * Gavel slam blended with a brief bright gold broadcast fanfare chord for user ₹12 Cr+ marquee wins.
     */
    private fun generateMarqueeSoldFanfarePcm(): ShortArray {
        val durationSec = 0.52
        val count = (SAMPLE_RATE * durationSec).toInt()
        val out = ShortArray(count)
        val rng = Random(777)
        for (i in 0 until count) {
            val t = i.toDouble() / SAMPLE_RATE
            val slam = gavelStrikeAt(t, rng) * 0.85
            val chime = if (t >= 0.06) {
                val lt = t - 0.06
                val env = (lt / 0.015).coerceAtMost(1.0) * exp(-lt * 6.5)
                val chord = (sin(2.0 * PI * 523.25 * lt) +
                    sin(2.0 * PI * 659.25 * lt) +
                    sin(2.0 * PI * 783.99 * lt) +
                    sin(2.0 * PI * 1046.50 * lt) * 0.7) / 3.7
                chord * env * 0.58
            } else {
                0.0
            }
            val sample = (slam + chime).coerceIn(-1.0, 1.0)
            out[i] = (sample * Short.MAX_VALUE).toInt().toShort()
        }
        return out
    }

    private fun gavelStrikeAt(t: Double, rng: Random): Double {
        if (t < 0.0) return 0.0
        val bodyFreq = 160.0 * exp(-t * 8.0) + 92.0
        val woodBody = sin(2.0 * PI * bodyFreq * t) * exp(-t * 22.0) * 0.72
        val woodKnock = sin(2.0 * PI * 445.0 * t) * exp(-t * 48.0) * 0.42
        val crackNoise = if (t < 0.022) {
            (rng.nextDouble() * 2.0 - 1.0) * exp(-t * 165.0) * 0.65
        } else {
            0.0
        }
        return woodBody + woodKnock + crackNoise
    }

    /**
     * Short low broadcast buzzer tone (~280 ms) when a player goes UNSOLD.
     */
    private fun generateUnsoldBuzzerPcm(): ShortArray {
        val durationSec = 0.28
        val count = (SAMPLE_RATE * durationSec).toInt()
        val out = ShortArray(count)
        for (i in 0 until count) {
            val t = i.toDouble() / SAMPLE_RATE
            val attack = (t / 0.012).coerceAtMost(1.0)
            val release = if (t > 0.21) ((durationSec - t) / 0.07).coerceIn(0.0, 1.0) else 1.0
            val env = attack * release
            val f1 = 188.0 - t * 42.0
            val f2 = 141.0 - t * 30.0
            val wave = sin(2.0 * PI * f1 * t) * 0.55 +
                sin(2.0 * PI * f2 * t) * 0.35 +
                sin(2.0 * PI * (f1 * 3.0) * t) * 0.12
            val sample = (wave * env * 0.68).coerceIn(-1.0, 1.0)
            out[i] = (sample * Short.MAX_VALUE).toInt().toShort()
        }
        return out
    }

    companion object {
        private const val SAMPLE_RATE = 22050
    }
}
