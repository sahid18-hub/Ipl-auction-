package com.example.engine

import com.example.data.FranchiseCatalog
import com.example.model.FranchiseId
import com.example.model.FranchiseState
import com.example.model.PlayerRole
import com.example.model.SoldRecord
import com.example.model.TeamGradeReport
import com.example.model.WrapUpAnalytics
import kotlin.math.roundToInt

object AuctionGrader {

    fun generateWrapUpAnalytics(
        franchises: Map<FranchiseId, FranchiseState>,
        allRecords: List<SoldRecord>
    ): WrapUpAnalytics {
        val soldRecords = allRecords.filter { it.isSold }

        val mostExpensive = soldRecords
            .sortedByDescending { it.soldPriceLakhs }
            .take(6)

        val bestSteals = soldRecords
            .filter { it.trueMarketValueLakhs >= 350 && it.valueDeltaLakhs > 0 }
            .sortedByDescending { it.valueDeltaLakhs }
            .take(6)

        val teamGrades = franchises.values
            .map { gradeFranchise(it) }
            .sortedByDescending { it.numericScore }

        return WrapUpAnalytics(
            mostExpensiveBuys = mostExpensive,
            bestValueSteals = bestSteals,
            teamGrades = teamGrades,
            totalPlayersSold = soldRecords.size,
            totalMoneySpentLakhs = soldRecords.sumOf { it.soldPriceLakhs }
        )
    }

    private fun gradeFranchise(state: FranchiseState): TeamGradeReport {
        val profile = FranchiseCatalog.get(state.id)
        val batters = state.countByRole(PlayerRole.BATTER)
        val keepers = state.countByRole(PlayerRole.WICKETKEEPER)
        val allRounders = state.countByRole(PlayerRole.ALL_ROUNDER)
        val pacers = state.countByRole(PlayerRole.FAST_BOWLER)
        val spinners = state.countByRole(PlayerRole.SPINNER)

        val avgQuality = if (state.squad.isEmpty()) 0 else {
            state.squad.map { (it.player.form + it.player.hype) / 2.0 }.average().roundToInt()
        }

        // Scoring breakdown out of 100
        var score = 0.0

        // 1. Squad Regulations Compliance (25 pts)
        score += when {
            state.squadCount in AuctionRules.MIN_SQUAD_SIZE..AuctionRules.MAX_SQUAD_SIZE -> 25.0
            state.squadCount >= 16 -> 17.0
            state.squadCount >= 13 -> 10.0
            else -> 4.0
        }

        // 2. Role Balance Coverage (35 pts)
        val batScore = (batters.coerceAtMost(6) / 6.0) * 8.0
        val wkScore = (keepers.coerceAtMost(2) / 2.0) * 6.0
        val arScore = (allRounders.coerceAtMost(4) / 4.0) * 7.0
        val paceScore = (pacers.coerceAtMost(5) / 5.0) * 8.0
        val spinScore = (spinners.coerceAtMost(3) / 3.0) * 6.0
        score += batScore + wkScore + arScore + paceScore + spinScore

        // 3. Starting XI Star Power & Average Form (25 pts)
        val top11AvgForm = if (state.squad.isEmpty()) 0.0 else {
            state.squad.sortedByDescending { it.player.form }
                .take(11)
                .map { it.player.form.toDouble() }
                .average()
        }
        val starPowerScore = (((top11AvgForm - 72.0) / 18.0).coerceIn(0.0, 1.0)) * 25.0
        score += starPowerScore

        // 4. Value Efficiency & Overseas Optimization (15 pts)
        val avgDelta = if (state.squad.isEmpty()) 0.0 else {
            state.squad.map { it.trueMarketValueLakhs - it.boughtPriceLakhs }.average()
        }
        val valueScore = ((avgDelta + 150.0) / 300.0).coerceIn(0.0, 1.0) * 8.0
        val osScore = when (state.overseasCount) {
            in 6..8 -> 7.0
            in 4..5 -> 5.0
            else -> 2.5
        }
        score += valueScore + osScore

        val finalScore = score.roundToInt().coerceIn(50, 99)
        val letterGrade = when {
            finalScore >= 91 -> "A+"
            finalScore >= 86 -> "A"
            finalScore >= 82 -> "A-"
            finalScore >= 77 -> "B+"
            finalScore >= 72 -> "B"
            finalScore >= 67 -> "B-"
            finalScore >= 61 -> "C+"
            else -> "C"
        }

        val strengths = mutableListOf<String>()
        val watchouts = mutableListOf<String>()

        if (top11AvgForm >= 86.0) strengths.add("Match-winning Top XI firepower (${top11AvgForm.roundToInt()} avg form)")
        if (allRounders >= 4) strengths.add("Deep all-rounder flexibility ($allRounders ARs)")
        if (pacers >= 5) strengths.add("Relentless pace battery ($pacers pacers)")
        if (spinners >= 3) strengths.add("Potent spin attack ($spinners spinners)")
        if (state.hasCaptain) strengths.add("Proven leadership core on roster")
        if (avgDelta > 40) strengths.add("Smart value efficiency across buys")
        if (strengths.isEmpty()) strengths.add("Aligned with ${profile.shortCode} franchise blueprint")

        if (state.squadCount < AuctionRules.MIN_SQUAD_SIZE) watchouts.add("Short of 18-player minimum (${state.squadCount}/18)")
        if (keepers < 2) watchouts.add("Light on backup wicketkeeping ($keepers WK)")
        if (spinners < 2) watchouts.add("Thin spin department ($spinners spinners)")
        if (pacers < 4) watchouts.add("Pace depth could be tested ($pacers pacers)")
        if (state.purseLakhs > 2200) watchouts.add("Significant unspent purse (${AuctionRules.formatCrores(state.purseLakhs)})")
        if (watchouts.isEmpty()) watchouts.add("Well-rounded squad with no glaring holes")

        return TeamGradeReport(
            franchiseId = state.id,
            grade = letterGrade,
            numericScore = finalScore,
            purseRemainingLakhs = state.purseLakhs,
            squadCount = state.squadCount,
            overseasCount = state.overseasCount,
            battersCount = batters,
            keepersCount = keepers,
            allRoundersCount = allRounders,
            pacersCount = pacers,
            spinnersCount = spinners,
            avgQuality = avgQuality,
            strengths = strengths.take(2),
            watchouts = watchouts.take(2)
        )
    }
}
