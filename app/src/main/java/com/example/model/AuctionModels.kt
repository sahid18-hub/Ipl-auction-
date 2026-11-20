package com.example.model

enum class AuctionPhase {
    FRANCHISE_SELECT,
    LIVE_AUCTION,
    SET_SUMMARY,
    ACCELERATED_NOMINATION,
    POST_AUCTION_WRAPUP
}

enum class HammerStage(val broadcastText: String) {
    WAITING_OPENING_BID("ON THE BLOCK • AWAITING OPENING BID"),
    ACTIVE_BIDDING("LIVE BIDDING IN PROGRESS"),
    GOING_ONCE("GOING ONCE..."),
    GOING_TWICE("GOING TWICE..."),
    FINAL_CALL("FINAL CALL — LAST CHANCE TO BID!"),
    SOLD("SOLD!"),
    UNSOLD("UNSOLD")
}

enum class SetPlayerStatus {
    LIVE_NOW,
    UPCOMING,
    SOLD,
    UNSOLD
}

data class SetRosterItem(
    val player: Player,
    val status: SetPlayerStatus,
    val winningFranchiseId: FranchiseId? = null,
    val soldPriceLakhs: Int? = null,
    val liveBidLakhs: Int? = null,
    val liveLeaderId: FranchiseId? = null
)

enum class PaddleEventType {
    OPENING_CALL,
    BID_PLACED,
    BACKED_OUT,
    HAMMER_CALL,
    SOLD_STAMP,
    UNSOLD_STAMP
}

data class PaddleEvent(
    val id: Long,
    val franchiseId: FranchiseId?,
    val type: PaddleEventType,
    val amountLakhs: Int?,
    val message: String
)

data class SoldRecord(
    val player: Player,
    val winningFranchiseId: FranchiseId?,
    val soldPriceLakhs: Int,
    val trueMarketValueLakhs: Int,
    val setSoldIn: AuctionSet
) {
    val isSold: Boolean get() = winningFranchiseId != null
    val valueDeltaLakhs: Int get() = if (isSold) trueMarketValueLakhs - soldPriceLakhs else 0
    val isBiddingWar: Boolean get() = isSold && soldPriceLakhs >= 1200
    val isValueSteal: Boolean get() = isSold && trueMarketValueLakhs >= 450 && valueDeltaLakhs >= 175
}

data class SetSummaryData(
    val set: AuctionSet,
    val records: List<SoldRecord>,
    val hasNextSet: Boolean,
    val nextSet: AuctionSet?
) {
    val soldCount: Int get() = records.count { it.isSold }
    val unsoldCount: Int get() = records.count { !it.isSold }
    val totalSpentLakhs: Int get() = records.filter { it.isSold }.sumOf { it.soldPriceLakhs }
    val topBuy: SoldRecord? get() = records.filter { it.isSold }.maxByOrNull { it.soldPriceLakhs }
}

data class TeamGradeReport(
    val franchiseId: FranchiseId,
    val grade: String,
    val numericScore: Int,
    val purseRemainingLakhs: Int,
    val squadCount: Int,
    val overseasCount: Int,
    val battersCount: Int,
    val keepersCount: Int,
    val allRoundersCount: Int,
    val pacersCount: Int,
    val spinnersCount: Int,
    val avgQuality: Int,
    val strengths: List<String>,
    val watchouts: List<String>
)

data class WrapUpAnalytics(
    val mostExpensiveBuys: List<SoldRecord>,
    val bestValueSteals: List<SoldRecord>,
    val teamGrades: List<TeamGradeReport>,
    val totalPlayersSold: Int,
    val totalMoneySpentLakhs: Int
)

enum class AuctionTab {
    STAGE,
    TEAMS,
    MY_SQUAD
}
