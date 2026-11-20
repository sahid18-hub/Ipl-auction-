package com.example.data

import com.example.model.AuctionSet
import com.example.model.Player

object PlayerDatabase {

    val allPlayers: List<Player> by lazy {
        PlayerDatabaseMarqueeAndBat.marquee1Set +
            PlayerDatabaseMarqueeAndBat.marquee2Set +
            PlayerDatabaseMarqueeAndBat.cappedBattersSet +
            PlayerDatabaseAllRounderWk.cappedAllRoundersSet +
            PlayerDatabaseAllRounderWk.cappedWicketkeepersSet +
            PlayerDatabaseBowling.cappedFastBowlersSet +
            PlayerDatabaseBowling.cappedSpinnersSet +
            PlayerDatabaseUncapped.uncappedPlayersSet
    }

    val primarySetsOrdered: List<AuctionSet> = listOf(
        AuctionSet.MARQUEE_1,
        AuctionSet.MARQUEE_2,
        AuctionSet.CAPPED_BATTERS,
        AuctionSet.CAPPED_ALL_ROUNDERS,
        AuctionSet.CAPPED_WICKETKEEPERS,
        AuctionSet.CAPPED_FAST_BOWLERS,
        AuctionSet.CAPPED_SPINNERS,
        AuctionSet.UNCAPPED_PLAYERS
    )

    fun playersForSet(set: AuctionSet): List<Player> = allPlayers.filter { it.set == set }
}
