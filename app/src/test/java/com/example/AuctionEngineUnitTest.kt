package com.example

import com.example.data.PlayerDatabase
import com.example.engine.AiAuctionBrain
import com.example.engine.AuctionGrader
import com.example.engine.AuctionRules
import com.example.engine.ValuationEngine
import com.example.model.AcquiredPlayer
import com.example.model.AuctionSet
import com.example.model.FranchiseId
import com.example.model.FranchiseState
import com.example.model.PlayerStatus
import com.example.model.SoldRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuctionEngineUnitTest {

    @Test
    fun officialBidIncrements_matchAllFourSlabs() {
        // Opening bid returns base price
        assertEquals(30, AuctionRules.nextBidAmountLakhs(0, 30, hasOpeningBid = false))
        assertEquals(200, AuctionRules.nextBidAmountLakhs(0, 200, hasOpeningBid = false))

        // ₹0.30 Cr to ₹1.00 Cr: +₹0.05 Cr (5L)
        assertEquals(35, AuctionRules.nextBidAmountLakhs(30, 30, hasOpeningBid = true))
        assertEquals(100, AuctionRules.nextBidAmountLakhs(95, 30, hasOpeningBid = true))

        // ₹1.00 Cr to ₹2.00 Cr: +₹0.10 Cr (10L)
        assertEquals(110, AuctionRules.nextBidAmountLakhs(100, 100, hasOpeningBid = true))
        assertEquals(200, AuctionRules.nextBidAmountLakhs(190, 100, hasOpeningBid = true))

        // ₹2.00 Cr to ₹5.00 Cr: +₹0.20 Cr (20L)
        assertEquals(220, AuctionRules.nextBidAmountLakhs(200, 200, hasOpeningBid = true))
        assertEquals(500, AuctionRules.nextBidAmountLakhs(480, 200, hasOpeningBid = true))

        // Above ₹5.00 Cr: +₹0.25 Cr (25L)
        assertEquals(525, AuctionRules.nextBidAmountLakhs(500, 200, hasOpeningBid = true))
        assertEquals(1625, AuctionRules.nextBidAmountLakhs(1600, 200, hasOpeningBid = true))
    }

    @Test
    fun purseGuardrail_enforcesCompletionReserveStrictly() {
        // 0 players, ₹120.00 Cr purse -> buying 1 leaves 17 mandatory slots * 30L = 510L reserved -> Max = 11490L
        assertEquals(11490, AuctionRules.maxAllowableBidLakhs(12000, 0))

        // 17 players, ₹4.00 Cr purse -> buying 18th leaves 0 mandatory slots -> Max = 400L
        assertEquals(400, AuctionRules.maxAllowableBidLakhs(400, 17))

        // 25 players -> Squad full -> Max = 0L
        assertEquals(0, AuctionRules.maxAllowableBidLakhs(5000, 25))

        // Block bid violating reserve
        val tightFranchise = FranchiseState(
            id = FranchiseId.CSK,
            purseLakhs = 600, // ₹6.00 Cr left
            squad = List(10) { idx ->
                AcquiredPlayer(PlayerDatabase.allPlayers[idx], 100, 100, AuctionSet.UNCAPPED_PLAYERS)
            }
        )
        // With 10 players, buying 1 leaves 7 mandatory slots * 30L = 210L reserve -> max bid = 390L
        assertEquals(390, tightFranchise.maxAllowableBidLakhs)
        val star = PlayerDatabase.allPlayers.first { !it.isOverseas }
        assertTrue(AuctionRules.evaluateBidEligibility(tightFranchise, star, 390, null).allowed)
        assertFalse(AuctionRules.evaluateBidEligibility(tightFranchise, star, 395, null).allowed)
    }

    @Test
    fun playerDatabase_hasOver200UniquePlayersWithValidSetDistributionsAndSlabs() {
        val all = PlayerDatabase.allPlayers
        assertTrue("Expected at least 200 players, got ${all.size}", all.size >= 200)

        // Zero duplicate IDs
        val uniqueIds = all.map { it.id }.toSet()
        assertEquals("Duplicate player IDs detected", all.size, uniqueIds.size)

        // Zero duplicate player names (case-insensitive)
        val normalizedNames = all.map { it.name.trim().lowercase() }
        val uniqueNames = normalizedNames.toSet()
        assertEquals(
            "Duplicate player names detected: ${normalizedNames.groupingBy { it }.eachCount().filter { it.value > 1 }}",
            all.size,
            uniqueNames.size
        )

        // Verify requested set distribution bounds
        val m1Count = PlayerDatabase.playersForSet(AuctionSet.MARQUEE_1).size
        val m2Count = PlayerDatabase.playersForSet(AuctionSet.MARQUEE_2).size
        val batCount = PlayerDatabase.playersForSet(AuctionSet.CAPPED_BATTERS).size
        val arCount = PlayerDatabase.playersForSet(AuctionSet.CAPPED_ALL_ROUNDERS).size
        val wkCount = PlayerDatabase.playersForSet(AuctionSet.CAPPED_WICKETKEEPERS).size
        val paceCount = PlayerDatabase.playersForSet(AuctionSet.CAPPED_FAST_BOWLERS).size
        val spinCount = PlayerDatabase.playersForSet(AuctionSet.CAPPED_SPINNERS).size
        val uncappedSetCount = PlayerDatabase.playersForSet(AuctionSet.UNCAPPED_PLAYERS).size

        assertTrue("Marquee 1 count $m1Count not in 12..15", m1Count in 12..15)
        assertTrue("Marquee 2 count $m2Count not in 12..15", m2Count in 12..15)
        assertTrue("Capped Batters count $batCount not in 25..30", batCount in 25..30)
        assertTrue("Capped All-Rounders count $arCount not in 25..30", arCount in 25..30)
        assertTrue("Capped Wicketkeepers count $wkCount not in 15..18", wkCount in 15..18)
        assertTrue("Capped Fast Bowlers count $paceCount not in 30..35", paceCount in 30..35)
        assertTrue("Capped Spinners count $spinCount not in 20..25", spinCount in 20..25)
        assertTrue("Uncapped Indian Players count $uncappedSetCount < 50", uncappedSetCount >= 50)

        // Verify all base prices match official IPL base price slabs and ratings are valid
        all.forEach { p ->
            assertTrue(
                "Player ${p.name} has non-official base price ${p.basePriceLakhs}",
                p.basePriceLakhs in AuctionRules.OFFICIAL_BASE_SLABS_LAKHS
            )
            assertTrue("Player ${p.name} form ${p.form} out of bounds", p.form in 1..100)
            assertTrue("Player ${p.name} hype ${p.hype} out of bounds", p.hype in 1..100)
        }
    }

    @Test
    fun valuationEngine_pricesMarqueeCappedAndUncappedRealistically() {
        val pool = PlayerDatabase.allPlayers

        val bumrah = pool.first { it.id == "m1_bumrah" }
        val bumrahTmv = ValuationEngine.calculateTrueMarketValueLakhs(bumrah, pool)
        assertTrue("Expected Bumrah TMV >= 1800L (₹18 Cr), got $bumrahTmv", bumrahTmv >= 1800)

        val kohli = pool.first { it.id == "m1_kohli" }
        val kohliTmv = ValuationEngine.calculateTrueMarketValueLakhs(kohli, pool)
        assertTrue("Expected Kohli TMV >= 1800L (₹18 Cr), got $kohliTmv", kohliTmv >= 1800)

        // Strong capped starter (e.g., Yashasvi Jaiswal / Shubman Gill / Rinku Singh)
        val rinku = pool.first { it.id == "bat_rinku" }
        val rinkuTmv = ValuationEngine.calculateTrueMarketValueLakhs(rinku, pool)
        assertTrue("Expected Rinku Singh TMV in 500..1450L (₹5–14.5 Cr), got $rinkuTmv", rinkuTmv in 500..1450)

        val uncappedPlayers = pool.filter { it.status == PlayerStatus.UNCAPPED }
        assertTrue(uncappedPlayers.size >= 50)
        uncappedPlayers.forEach { p ->
            val tmv = ValuationEngine.calculateTrueMarketValueLakhs(p, pool)
            assertTrue("Expected uncapped ${p.name} TMV in 25..400L, got $tmv", tmv in 25..400)
        }
    }

    @Test
    fun aiBrain_runsFullMultiSetSimulationWithRealisticPriceBandsAndZeroRuleViolations() {
        val brain = AiAuctionBrain(sessionSeed = 42)
        val franchises = FranchiseId.entries.associateWith { id ->
            FranchiseState(id = id, isUserControlled = id == FranchiseId.CSK)
        }.toMutableMap()

        val allRecords = mutableListOf<SoldRecord>()
        val remainingPool = PlayerDatabase.allPlayers.toMutableList()

        // Simulate all 8 primary sets across the 9 AI franchises
        PlayerDatabase.primarySetsOrdered.forEach { set ->
            val setPlayers = PlayerDatabase.playersForSet(set)
            setPlayers.forEach { player ->
                val tmv = ValuationEngine.calculateTrueMarketValueLakhs(player, remainingPool)
                val result = brain.simulatePlayerToCompletion(
                    player = player,
                    franchises = franchises,
                    userFranchiseId = FranchiseId.CSK,
                    remainingPool = remainingPool,
                    currentSet = set,
                    isAcceleratedRound = false
                )
                remainingPool.remove(player)
                val winnerId = result.winningFranchiseId
                if (winnerId != null) {
                    val winner = franchises.getValue(winnerId)
                    franchises[winnerId] = winner.copy(
                        purseLakhs = winner.purseLakhs - result.finalPriceLakhs,
                        squad = winner.squad + AcquiredPlayer(player, result.finalPriceLakhs, tmv, set)
                    )
                    allRecords.add(SoldRecord(player, winnerId, result.finalPriceLakhs, tmv, set))
                }
            }
        }

        // 1. Marquee 1 & 2 stars must trigger fierce ₹18–26+ Cr bidding wars
        val marqueeSales = allRecords.filter {
            it.player.set == AuctionSet.MARQUEE_1 || it.player.set == AuctionSet.MARQUEE_2
        }
        assertTrue("Expected most Marquee stars to sell", marqueeSales.size >= 22)
        val maxMarqueeSale = marqueeSales.maxOf { it.soldPriceLakhs }
        assertTrue(
            "Expected top Marquee bidding war >= ₹18 Cr (1800L), got $maxMarqueeSale",
            maxMarqueeSale >= 1800
        )

        // 2. Strong capped starters (Sets 3–7) must sell cleanly in ₹5–14 Cr band
        val cappedStarterMidBandSales = allRecords.filter {
            it.player.set in setOf(
                AuctionSet.CAPPED_BATTERS,
                AuctionSet.CAPPED_ALL_ROUNDERS,
                AuctionSet.CAPPED_WICKETKEEPERS,
                AuctionSet.CAPPED_FAST_BOWLERS,
                AuctionSet.CAPPED_SPINNERS
            ) && it.soldPriceLakhs in 500..1500
        }
        assertTrue(
            "Expected >= 15 capped starters selling in ₹5–15 Cr range, got ${cappedStarterMidBandSales.size}",
            cappedStarterMidBandSales.size >= 15
        )

        // 3. Uncapped players must strictly sell within ₹0.30–4.00 Cr (30..400L)
        val uncappedSales = allRecords.filter { it.player.status == PlayerStatus.UNCAPPED }
        assertTrue("Expected uncapped sales to occur", uncappedSales.isNotEmpty())
        uncappedSales.forEach { rec ->
            assertTrue(
                "Expected uncapped ${rec.player.name} sold price in 30..400L, got ${rec.soldPriceLakhs}",
                rec.soldPriceLakhs in 30..400
            )
        }

        // 4. Verify all franchises strictly respected purse guardrail, squad cap & overseas cap
        franchises.values.forEach { state ->
            val minReserveNeeded = AuctionRules.remainingMandatorySlots(state.squadCount) * AuctionRules.MIN_BASE_PRICE_LAKHS
            assertTrue("Franchise ${state.id} violated purse reserve", state.purseLakhs >= minReserveNeeded)
            assertTrue("Franchise ${state.id} exceeded max squad size", state.squadCount <= AuctionRules.MAX_SQUAD_SIZE)
            assertTrue("Franchise ${state.id} exceeded max overseas", state.overseasCount <= AuctionRules.MAX_OVERSEAS)
        }

        val wrapUp = AuctionGrader.generateWrapUpAnalytics(franchises, allRecords)
        assertEquals(10, wrapUp.teamGrades.size)
    }

    @Test
    fun tournamentSimulator_enforcesMaxFourOverseasAndSimulatesFullIplSeason() {
        val allPlayers = PlayerDatabase.allPlayers
        val indianPlayers = allPlayers.filter { !it.isOverseas }
        val overseasPlayers = allPlayers.filter { it.isOverseas }

        // Build 10 realistic 18-player squads (12 Indian + 6 Overseas each)
        val franchises = FranchiseId.entries.mapIndexed { teamIdx, id ->
            val teamIndians = indianPlayers.drop(teamIdx * 12).take(12)
            val teamOverseas = overseasPlayers.drop(teamIdx * 6).take(6)
            val squad = (teamIndians + teamOverseas).map { p ->
                AcquiredPlayer(p, p.basePriceLakhs, p.basePriceLakhs, p.set)
            }
            id to FranchiseState(
                id = id,
                purseLakhs = 1500,
                squad = squad,
                isUserControlled = id == FranchiseId.MI
            )
        }.toMap()

        val userState = franchises.getValue(FranchiseId.MI)
        val userOverseas = userState.squad.filter { it.player.isOverseas }
        assertTrue("Expected at least 5 overseas in test squad", userOverseas.size >= 5)

        // Strictly block adding a 5th Overseas player to Playing XI
        val fourOsIds = userOverseas.take(4).map { it.player.id }
        val fifthOsId = userOverseas[4].player.id
        val (allowed5thOs, reason) = com.example.engine.TournamentSimulator.canAddPlayerToXi(
            franchiseState = userState,
            currentXiIds = fourOsIds,
            candidatePlayerId = fifthOsId
        )
        assertFalse("Should block 5th Overseas player in Playing XI", allowed5thOs)
        assertTrue("Expected clear max-4 Overseas reason", reason?.contains("Max 4 Overseas") == true)

        // Auto-select Best XI must respect max 4 Overseas and 11 XI + 1 Impact
        val bestUserLineup = com.example.engine.TournamentSimulator.autoSelectBestLineup(userState)
        assertEquals(11, bestUserLineup.playingXiIds.size)
        assertTrue(bestUserLineup.impactPlayerId != null)
        assertFalse(bestUserLineup.impactPlayerId in bestUserLineup.playingXiIds)

        val validation = com.example.engine.TournamentSimulator.validateLineup(
            franchiseState = userState,
            playingXiIds = bestUserLineup.playingXiIds,
            impactPlayerId = bestUserLineup.impactPlayerId
        )
        assertTrue("Auto-selected Best XI must be valid: ${validation.issueMessage}", validation.isValid)
        assertTrue("Auto-selected XI exceeded max 4 OS", validation.overseasInXiCount <= 4)

        // Simulate full 10-team IPL season (14 league games each + Q1, Eliminator, Q2, Final)
        val sim = com.example.engine.TournamentSimulator.simulateIplSeason(
            franchises = franchises,
            userLineup = bestUserLineup,
            seed = 2026
        )
        assertEquals(10, sim.standings.size)
        sim.standings.forEach { row ->
            assertEquals("Every franchise must play exactly 14 league matches", 14, row.played)
            assertEquals(row.won + row.lost, row.played)
            assertEquals(row.won * 2, row.points)
        }
        assertEquals(70, sim.standings.sumOf { it.won })
        assertEquals(70, sim.standings.sumOf { it.lost })
        assertTrue(sim.championId in FranchiseId.entries)
        assertTrue(sim.championId != sim.runnerUpId)
        assertEquals(10, sim.aiLineups.size)
    }

    @Test
    fun aiBrain_nominatesTwoToThreeUnsoldPlayersPerTeamBasedOnGaps() {
        val brain = AiAuctionBrain(sessionSeed = 108)
        val unsoldSample = PlayerDatabase.allPlayers.takeLast(45)
        val franchises = FranchiseId.entries.associateWith { id ->
            FranchiseState(
                id = id,
                purseLakhs = 1800,
                squad = emptyList(),
                isUserControlled = id == FranchiseId.CSK
            )
        }

        val nominations = brain.nominateUnsoldPlayersForFranchises(
            franchises = franchises,
            userFranchiseId = FranchiseId.CSK,
            unsoldPool = unsoldSample
        )

        // All 9 AI franchises nominate 2–3 valid players from the unsold pool
        assertEquals(9, nominations.size)
        assertFalse(FranchiseId.CSK in nominations)
        val validIds = unsoldSample.map { it.id }.toSet()
        nominations.values.forEach { picks ->
            assertTrue("Expected AI to nominate 2..3 players, got ${picks.size}", picks.size in 2..3)
            picks.forEach { pid ->
                assertTrue("Nominated player $pid not in unsold pool", pid in validIds)
            }
        }
    }
}
