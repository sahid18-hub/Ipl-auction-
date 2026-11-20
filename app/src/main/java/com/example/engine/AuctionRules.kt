package com.example.engine

import com.example.model.FranchiseId
import com.example.model.FranchiseState
import com.example.model.Player
import java.util.Locale

data class BidEligibility(
    val allowed: Boolean,
    val reason: String? = null
)

object AuctionRules {
    const val STARTING_PURSE_LAKHS = 12000 // ₹120.00 Cr
    const val MIN_SQUAD_SIZE = 18
    const val MAX_SQUAD_SIZE = 25
    const val MAX_OVERSEAS = 8
    const val MIN_BASE_PRICE_LAKHS = 30 // ₹0.30 Cr

    val OFFICIAL_BASE_SLABS_LAKHS = listOf(30, 50, 75, 100, 125, 150, 200)

    /**
     * Official IPL Bid Increments:
     * - ₹0.30 Cr to ₹1.00 Cr: +₹0.05 Cr (5 Lakhs)
     * - ₹1.00 Cr to ₹2.00 Cr: +₹0.10 Cr (10 Lakhs)
     * - ₹2.00 Cr to ₹5.00 Cr: +₹0.20 Cr (20 Lakhs)
     * - Above ₹5.00 Cr: +₹0.25 Cr (25 Lakhs)
     */
    fun bidIncrementLakhs(currentBidLakhs: Int): Int = when {
        currentBidLakhs < 100 -> 5
        currentBidLakhs < 200 -> 10
        currentBidLakhs < 500 -> 20
        else -> 25
    }

    fun nextBidAmountLakhs(
        currentBidLakhs: Int,
        basePriceLakhs: Int,
        hasOpeningBid: Boolean
    ): Int {
        if (!hasOpeningBid) return basePriceLakhs
        return currentBidLakhs + bidIncrementLakhs(currentBidLakhs)
    }

    fun remainingMandatorySlots(squadSize: Int): Int {
        return (MIN_SQUAD_SIZE - squadSize).coerceAtLeast(0)
    }

    /**
     * Reserve needed AFTER acquiring one player right now to still reach 18 players at ₹0.30 Cr each.
     */
    fun completionReserveAfterBuyingLakhs(currentSquadSize: Int): Int {
        val slotsStillNeededAfterBuy = (MIN_SQUAD_SIZE - (currentSquadSize + 1)).coerceAtLeast(0)
        return slotsStillNeededAfterBuy * MIN_BASE_PRICE_LAKHS
    }

    /**
     * Strictly computes the maximum single bid a franchise may legally place
     * without violating the Completion Reserve guardrail.
     */
    fun maxAllowableBidLakhs(purseLakhs: Int, currentSquadSize: Int): Int {
        if (currentSquadSize >= MAX_SQUAD_SIZE) return 0
        val reserve = completionReserveAfterBuyingLakhs(currentSquadSize)
        return (purseLakhs - reserve).coerceAtLeast(0)
    }

    fun evaluateBidEligibility(
        franchise: FranchiseState,
        player: Player,
        bidAmountLakhs: Int,
        currentHighestBidder: FranchiseId?
    ): BidEligibility {
        if (currentHighestBidder == franchise.id) {
            return BidEligibility(false, "You already hold the highest bid")
        }
        if (franchise.squadCount >= MAX_SQUAD_SIZE) {
            return BidEligibility(false, "Squad full (${MAX_SQUAD_SIZE}/${MAX_SQUAD_SIZE})")
        }
        if (player.isOverseas && franchise.overseasCount >= MAX_OVERSEAS) {
            return BidEligibility(false, "Overseas limit reached (${MAX_OVERSEAS}/${MAX_OVERSEAS})")
        }
        val maxAllowed = maxAllowableBidLakhs(franchise.purseLakhs, franchise.squadCount)
        if (bidAmountLakhs > maxAllowed) {
            val reserveLakhs = completionReserveAfterBuyingLakhs(franchise.squadCount)
            return BidEligibility(
                false,
                "Blocked by Purse Guardrail (Max ${formatCrores(maxAllowed)}; ${formatCrores(reserveLakhs)} reserved)"
            )
        }
        if (bidAmountLakhs > franchise.purseLakhs) {
            return BidEligibility(false, "Insufficient purse (${formatCrores(franchise.purseLakhs)})")
        }
        return BidEligibility(true, null)
    }

    fun formatCrores(lakhs: Int): String {
        val crores = lakhs / 100.0
        return String.format(Locale.US, "₹%.2f Cr", crores)
    }

    fun formatCroresCompact(lakhs: Int): String {
        val crores = lakhs / 100.0
        return String.format(Locale.US, "₹%.2fCr", crores)
    }

    fun formatIncrementLabel(currentBidLakhs: Int, hasOpeningBid: Boolean): String {
        if (!hasOpeningBid) return "BASE"
        val inc = bidIncrementLakhs(currentBidLakhs)
        return "+₹${inc}L"
    }
}
