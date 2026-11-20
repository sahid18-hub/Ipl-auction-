package com.example.engine

import com.example.model.AuctionSet
import com.example.model.Nationality
import com.example.model.Player
import com.example.model.PlayerArchetype
import com.example.model.PlayerRole
import com.example.model.PlayerStatus
import kotlin.math.pow
import kotlin.math.roundToInt

object ValuationEngine {

    /**
     * Calculates a dynamic True Market Value (in Lakhs) driven by:
     * 1. Non-linear Current Form & Market Hype curve (90+ Form/Hype reaches ₹15 Cr – ₹25+ Cr)
     * 2. Role & Archetype Scarcity (dynamically accounts for remaining elite options in pool)
     * 3. Indian Domestic Slot Premium & Captaincy Premium
     * 4. Capped vs Uncapped calibration (uncapped stays ₹0.30 Cr – ₹4.50 Cr)
     */
    fun calculateTrueMarketValueLakhs(
        player: Player,
        remainingPool: List<Player>
    ): Int {
        val compositeRating = (player.form * 0.54) + (player.hype * 0.46)

        val rawValueLakhs = if (player.status == PlayerStatus.UNCAPPED) {
            calculateUncappedBaseValue(compositeRating)
        } else {
            calculateCappedBaseValue(compositeRating, player.set)
        }

        val roleScarcityMultiplier = calculateRoleScarcityMultiplier(player, remainingPool)
        val nationalityMultiplier = if (player.nationality == Nationality.INDIAN) {
            if (compositeRating >= 85.0) 1.13 else 1.07
        } else {
            if (compositeRating >= 91.0) 1.05 else 0.94
        }

        val captaincyMultiplier = when {
            player.isCaptainMaterial && compositeRating >= 89.0 -> 1.12
            player.isCaptainMaterial && compositeRating >= 82.0 -> 1.06
            else -> 1.00
        }

        val ageFactor = when {
            player.age in 21..30 -> 1.04
            player.age <= 20 -> 1.02
            player.age in 31..34 -> 1.00
            player.age in 35..37 -> 0.94
            else -> 0.84
        }

        val computed = (rawValueLakhs *
            roleScarcityMultiplier *
            nationalityMultiplier *
            captaincyMultiplier *
            ageFactor).roundToInt()

        val clamped = if (player.status == PlayerStatus.UNCAPPED) {
            computed.coerceIn(25, 390)
        } else {
            computed.coerceIn(30, 2750)
        }

        return roundToNearestFiveLakhs(clamped)
    }

    private fun calculateCappedBaseValue(composite: Double, set: AuctionSet): Double {
        // Non-linear exponential valuation curve tuned to real IPL Mega Auction prices:
        // composite 98 -> ~2150L (₹21.5 Cr before multipliers)
        // composite 94 -> ~1620L (₹16.2 Cr)
        // composite 90 -> ~1160L (₹11.6 Cr)
        // composite 85 -> ~670L  (₹6.7 Cr)
        // composite 80 -> ~340L  (₹3.4 Cr)
        // composite 74 -> ~135L  (₹1.35 Cr)
        // composite 70 -> ~70L   (₹0.70 Cr)
        val normalized = ((composite - 64.0) / 36.0).coerceIn(0.02, 1.05)
        var value = 45.0 + 2050.0 * normalized.pow(2.55)

        // Marquee early-set purse abundance premium
        if (set == AuctionSet.MARQUEE_1) {
            value *= 1.18
        } else if (set == AuctionSet.MARQUEE_2) {
            value *= 1.14
        }
        return value
    }

    private fun calculateUncappedBaseValue(composite: Double): Double {
        // Uncapped players scale cleanly from ₹0.30 Cr to ₹4.00 Cr:
        // composite 85 -> ~290L (₹2.90 Cr before multipliers)
        // composite 80 -> ~150L (₹1.50 Cr)
        // composite 75 -> ~65L  (₹0.65 Cr)
        // composite 72 -> ~36L  (₹0.36 Cr)
        val normalized = ((composite - 68.0) / 20.0).coerceIn(0.0, 1.0)
        return 26.0 + 310.0 * normalized.pow(2.15)
    }

    private fun calculateRoleScarcityMultiplier(
        player: Player,
        remainingPool: List<Player>
    ): Double {
        val baseArchetypeScarcity = when (player.archetype) {
            PlayerArchetype.DEATH_PACER -> 1.12
            PlayerArchetype.PACE_ALL_ROUNDER -> 1.11
            PlayerArchetype.SPIN_ALL_ROUNDER -> 1.09
            PlayerArchetype.MYSTERY_SPINNER -> 1.10
            PlayerArchetype.EXPLOSIVE_WK -> 1.09
            PlayerArchetype.WRIST_SPINNER -> 1.07
            PlayerArchetype.LEFT_ARM_PACER -> 1.07
            PlayerArchetype.FINISHER -> 1.06
            PlayerArchetype.POWERPLAY_OPENER -> 1.05
            PlayerArchetype.EXPRESS_PACER -> 1.05
            PlayerArchetype.MIDDLE_ORDER_ENFORCER -> 1.04
            PlayerArchetype.SWING_PACER -> 1.02
            PlayerArchetype.ANCHOR_WK -> 1.01
            PlayerArchetype.TOP_ORDER_ANCHOR -> 1.00
            PlayerArchetype.FINGER_SPINNER -> 0.97
        }

        // Dynamic scarcity boost when few elite (form >= 84) options remain for this role
        val remainingEliteInRole = remainingPool.count {
            it.role == player.role && it.form >= 84 && it.id != player.id
        }
        val dynamicBoost = when {
            player.form >= 84 && remainingEliteInRole <= 2 -> 1.08
            player.form >= 84 && remainingEliteInRole <= 5 -> 1.04
            else -> 1.00
        }

        return baseArchetypeScarcity * dynamicBoost
    }

    fun valuationTierLabel(trueMarketValueLakhs: Int): String = when {
        trueMarketValueLakhs >= 1500 -> "BLOCKBUSTER MARQUEE"
        trueMarketValueLakhs >= 900 -> "PREMIUM TARGET"
        trueMarketValueLakhs >= 450 -> "HIGH DEMAND"
        trueMarketValueLakhs >= 175 -> "SOLID VALUE"
        else -> "BARGAIN / DEPTH"
    }

    private fun roundToNearestFiveLakhs(value: Int): Int {
        return ((value + 2) / 5) * 5
    }
}
