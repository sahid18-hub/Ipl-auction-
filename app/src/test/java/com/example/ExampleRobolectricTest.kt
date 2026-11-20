package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("IPL Auction", appName)
  }

  @Test
  fun `auction persistence auto saves and restores full snapshot accurately`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val store = com.example.data.AuctionPersistence(context)
    store.clearSavedAuction()

    // Verify mute preference persistence
    store.setSoundMuted(true)
    assertEquals(true, store.isSoundMuted())
    store.setSoundMuted(false)
    assertEquals(false, store.isSoundMuted())

    val m1Players = com.example.data.PlayerDatabase.playersForSet(com.example.model.AuctionSet.MARQUEE_1)
    val firstPlayer = m1Players.first()
    val franchises = com.example.model.FranchiseId.entries.associateWith { id ->
      if (id == com.example.model.FranchiseId.RCB) {
        com.example.model.FranchiseState(
          id = id,
          purseLakhs = 10000,
          squad = listOf(
            com.example.model.AcquiredPlayer(firstPlayer, 2000, 2100, com.example.model.AuctionSet.MARQUEE_1)
          ),
          isUserControlled = true
        )
      } else {
        com.example.model.FranchiseState(id = id, isUserControlled = false)
      }
    }

    val record = com.example.model.SoldRecord(
      player = firstPlayer,
      winningFranchiseId = com.example.model.FranchiseId.RCB,
      soldPriceLakhs = 2000,
      trueMarketValueLakhs = 2100,
      setSoldIn = com.example.model.AuctionSet.MARQUEE_1
    )

    store.saveSnapshot(
      com.example.data.PersistedAuctionSnapshot(
        phase = com.example.model.AuctionPhase.LIVE_AUCTION,
        selectedFranchiseId = com.example.model.FranchiseId.RCB,
        franchises = franchises,
        currentSet = com.example.model.AuctionSet.MARQUEE_1,
        currentSetPlayers = m1Players,
        currentPlayerIndex = 1,
        currentSetRecords = listOf(record),
        allRecords = listOf(record),
        unsoldPool = emptyList(),
        paddleFeed = emptyList(),
        sessionSeed = 999,
        savedAtMillis = 123456789L
      )
    )

    val summary = store.loadSummary()
    assertEquals(com.example.model.FranchiseId.RCB, summary?.userFranchiseId)
    assertEquals(1, summary?.userSquadCount)
    assertEquals(10000, summary?.userPurseLakhs)

    val restored = store.loadSnapshot()!!
    assertEquals(1, restored.currentPlayerIndex)
    assertEquals(10000, restored.franchises.getValue(com.example.model.FranchiseId.RCB).purseLakhs)
    assertEquals(firstPlayer.id, restored.franchises.getValue(com.example.model.FranchiseId.RCB).squad.first().player.id)
    assertEquals(999, restored.sessionSeed)
  }
}
