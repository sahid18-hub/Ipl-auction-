package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.engine.AuctionRules

enum class FranchiseId(val shortCode: String) {
    CSK("CSK"),
    MI("MI"),
    RCB("RCB"),
    KKR("KKR"),
    SRH("SRH"),
    RR("RR"),
    DC("DC"),
    PBKS("PBKS"),
    GT("GT"),
    LSG("LSG")
}

data class PositionalBlueprint(
    val openers: Int = 3,
    val middleOrder: Int = 4,
    val wicketkeepers: Int = 2,
    val allRounders: Int = 4,
    val pacers: Int = 5,
    val spinners: Int = 3
)

data class FranchiseProfile(
    val id: FranchiseId,
    val shortCode: String,
    val fullName: String,
    val city: String,
    val homeVenue: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val onPrimaryColor: Color,
    val dnaHeadline: String,
    val dnaTags: List<String>,
    val archetypeAffinities: Map<PlayerArchetype, Double>,
    val youthBias: Double,
    val experienceBias: Double,
    val captaincyBonus: Double,
    val aggression: Double,
    val marqueeStretch: Double,
    val indianStarBias: Double,
    val blueprint: PositionalBlueprint = PositionalBlueprint()
)

data class AcquiredPlayer(
    val player: Player,
    val boughtPriceLakhs: Int,
    val trueMarketValueLakhs: Int,
    val setAcquired: AuctionSet
)

data class FranchiseState(
    val id: FranchiseId,
    val purseLakhs: Int = AuctionRules.STARTING_PURSE_LAKHS,
    val squad: List<AcquiredPlayer> = emptyList(),
    val isUserControlled: Boolean = false
) {
    val squadCount: Int get() = squad.size
    val overseasCount: Int get() = squad.count { it.player.isOverseas }
    val indianCount: Int get() = squadCount - overseasCount
    val remainingMandatorySlots: Int get() = AuctionRules.remainingMandatorySlots(squadCount)
    val maxAllowableBidLakhs: Int get() = AuctionRules.maxAllowableBidLakhs(purseLakhs, squadCount)
    val totalSpentLakhs: Int get() = AuctionRules.STARTING_PURSE_LAKHS - purseLakhs

    fun countByRole(role: PlayerRole): Int = squad.count { it.player.role == role }

    val openerCount: Int
        get() = squad.count {
            it.player.role == PlayerRole.BATTER && it.player.isOpener
        } + squad.count {
            it.player.role == PlayerRole.WICKETKEEPER && it.player.archetype == PlayerArchetype.EXPLOSIVE_WK
        } / 2

    val middleOrderCount: Int
        get() = squad.count {
            it.player.role == PlayerRole.BATTER && !it.player.isOpener
        }

    val hasCaptain: Boolean get() = squad.any { it.player.isCaptainMaterial }
}
