package com.example.model

data class SavedAuctionSummary(
    val userFranchiseId: FranchiseId,
    val currentSet: AuctionSet,
    val currentPlayerIndex: Int,
    val totalInCurrentSet: Int,
    val userSquadCount: Int,
    val userPurseLakhs: Int,
    val totalProcessedPlayers: Int,
    val savedAtMillis: Long
)

data class TeamLineup(
    val franchiseId: FranchiseId,
    val playingXiIds: List<String>,
    val impactPlayerId: String?
)

data class LineupValidation(
    val isValid: Boolean,
    val xiCount: Int,
    val overseasInXiCount: Int,
    val hasImpactPlayer: Boolean,
    val hasWicketkeeper: Boolean,
    val bowlingOptionsCount: Int,
    val teamPowerRating: Int,
    val issueMessage: String? = null
)

data class LeagueStandingRow(
    val franchiseId: FranchiseId,
    val played: Int,
    val won: Int,
    val lost: Int,
    val points: Int,
    val netRunRate: Double,
    val xiRating: Int
)

data class PlayoffMatchResult(
    val stageName: String,
    val teamA: FranchiseId,
    val teamB: FranchiseId,
    val scoreA: String,
    val scoreB: String,
    val winnerId: FranchiseId,
    val marginText: String,
    val playerOfMatch: String
)

data class SeasonAwardWinner(
    val playerName: String,
    val franchiseId: FranchiseId,
    val statSummary: String
)

data class SeasonSimulationResult(
    val standings: List<LeagueStandingRow>,
    val qualifier1: PlayoffMatchResult,
    val eliminator: PlayoffMatchResult,
    val qualifier2: PlayoffMatchResult,
    val finalMatch: PlayoffMatchResult,
    val championId: FranchiseId,
    val runnerUpId: FranchiseId,
    val orangeCap: SeasonAwardWinner,
    val purpleCap: SeasonAwardWinner,
    val aiLineups: Map<FranchiseId, TeamLineup>
)
