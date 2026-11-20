package com.example.model

enum class PlayerRole(val label: String, val shortCode: String) {
    BATTER("Batter", "BAT"),
    WICKETKEEPER("Wicketkeeper", "WK"),
    ALL_ROUNDER("All-Rounder", "AR"),
    FAST_BOWLER("Fast Bowler", "PACE"),
    SPINNER("Spinner", "SPIN")
}

enum class PlayerArchetype(val label: String) {
    POWERPLAY_OPENER("Powerplay Opener"),
    TOP_ORDER_ANCHOR("Top-Order Anchor"),
    MIDDLE_ORDER_ENFORCER("Middle-Order Enforcer"),
    FINISHER("Power Finisher"),
    EXPLOSIVE_WK("Explosive WK-Batter"),
    ANCHOR_WK("Anchor WK-Batter"),
    PACE_ALL_ROUNDER("Pace All-Rounder"),
    SPIN_ALL_ROUNDER("Spin All-Rounder"),
    EXPRESS_PACER("Express Pacer"),
    DEATH_PACER("Death-Overs Pacer"),
    SWING_PACER("New-Ball Swing Pacer"),
    LEFT_ARM_PACER("Left-Arm Pacer"),
    WRIST_SPINNER("Wrist Spinner"),
    MYSTERY_SPINNER("Mystery Spinner"),
    FINGER_SPINNER("Finger Spinner")
}

enum class Nationality(val label: String, val shortCode: String) {
    INDIAN("Indian", "IND"),
    OVERSEAS("Overseas", "OS")
}

enum class PlayerStatus(val label: String) {
    CAPPED("Capped"),
    UNCAPPED("Uncapped")
}

enum class AuctionSet(
    val setNumber: Int,
    val title: String,
    val shortTitle: String,
    val subtitle: String
) {
    MARQUEE_1(1, "Marquee Set 1", "MARQUEE 1", "Headline franchise icons & captains"),
    MARQUEE_2(2, "Marquee Set 2", "MARQUEE 2", "Premier match-winners & global stars"),
    CAPPED_BATTERS(3, "Capped Batters", "BATTERS", "International openers, anchors & finishers"),
    CAPPED_ALL_ROUNDERS(4, "Capped All-Rounders", "ALL-ROUNDERS", "Multi-skilled pace & spin match-shapers"),
    CAPPED_WICKETKEEPERS(5, "Capped Wicketkeepers", "KEEPERS", "Glovemen & explosive batting leaders"),
    CAPPED_FAST_BOWLERS(6, "Capped Fast Bowlers", "FAST BOWLERS", "Express quicks, death specialists & swing bowlers"),
    CAPPED_SPINNERS(7, "Capped Spinners", "SPINNERS", "Mystery tweakers, wrist & finger spinners"),
    UNCAPPED_PLAYERS(8, "Uncapped Players", "UNCAPPED", "High-upside Indian domestic breakout stars"),
    ACCELERATED_ROUND(9, "Accelerated Round", "ACCELERATED", "Unsold players return to complete 18-man squads")
}

data class Player(
    val id: String,
    val name: String,
    val country: String,
    val role: PlayerRole,
    val archetype: PlayerArchetype,
    val nationality: Nationality,
    val status: PlayerStatus,
    val set: AuctionSet,
    val basePriceLakhs: Int,
    val form: Int,
    val hype: Int,
    val age: Int,
    val iplMatches: Int,
    val primaryStatLabel: String,
    val primaryStatValue: String,
    val secondaryStatLabel: String,
    val secondaryStatValue: String,
    val battingStyle: String,
    val bowlingStyle: String,
    val isCaptainMaterial: Boolean = false
) {
    val isOverseas: Boolean get() = nationality == Nationality.OVERSEAS
    val isOpener: Boolean
        get() = archetype == PlayerArchetype.POWERPLAY_OPENER || archetype == PlayerArchetype.TOP_ORDER_ANCHOR
}
