package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FranchiseCatalog
import com.example.engine.AuctionRules
import com.example.engine.TournamentSimulator
import com.example.model.AcquiredPlayer
import com.example.model.FranchiseId
import com.example.model.FranchiseState
import com.example.model.LeagueStandingRow
import com.example.model.LineupValidation
import com.example.model.PlayoffMatchResult
import com.example.model.SeasonSimulationResult
import com.example.ui.theme.BroadcastAmber
import com.example.ui.theme.BroadcastBgDeep
import com.example.ui.theme.BroadcastCrimson
import com.example.ui.theme.BroadcastCyan
import com.example.ui.theme.BroadcastEmerald
import com.example.ui.theme.BroadcastGold
import com.example.ui.theme.BroadcastGoldLight
import com.example.ui.theme.BroadcastOutline
import com.example.ui.theme.BroadcastOutlineSubtle
import com.example.ui.theme.BroadcastSurface
import com.example.ui.theme.BroadcastSurfaceElevated
import com.example.ui.theme.BroadcastSurfaceHighlight
import com.example.ui.theme.BroadcastTextMuted
import com.example.ui.theme.BroadcastTextPrimary
import com.example.ui.theme.BroadcastTextSecondary
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.MonoFontFamily
import com.example.ui.theme.OnBroadcastGold
import java.util.Locale

@Composable
fun PlayingXiBuilderHeaderCard(
    userFranchise: FranchiseState,
    validation: LineupValidation,
    selectionError: String?,
    onAutoFillBestXi: () -> Unit,
    onClearXi: () -> Unit,
    onSimulateSeason: () -> Unit,
    hasSeasonResult: Boolean
) {
    val profile = FranchiseCatalog.get(userFranchise.id)
    val requiredXi = minOf(TournamentSimulator.PLAYING_XI_SIZE, userFranchise.squadCount)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("playing_xi_builder_card"),
        shape = RoundedCornerShape(16.dp),
        color = BroadcastSurfaceElevated,
        border = BorderStroke(1.2.dp, BroadcastGold.copy(alpha = 0.55f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Brush.horizontalGradient(listOf(profile.primaryColor, BroadcastGold, profile.secondaryColor)))
            )

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${profile.shortCode} PLAYING XI + IMPACT PLAYER",
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 21.sp,
                            letterSpacing = 0.7.sp,
                            color = BroadcastTextPrimary
                        )
                        Text(
                            text = "Pick your $requiredXi starters (max 4 Overseas) + 1 Impact Sub, then simulate the IPL season.",
                            style = MaterialTheme.typography.bodySmall,
                            color = BroadcastTextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(BroadcastGold.copy(alpha = 0.16f))
                            .border(1.2.dp, BroadcastGold, RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "XI OVR",
                                fontFamily = MonoFontFamily,
                                fontSize = 8.sp,
                                color = BroadcastGold
                            )
                            Text(
                                text = if (validation.xiCount > 0) "${validation.teamPowerRating}" else "--",
                                fontFamily = DisplayFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                color = BroadcastGoldLight
                            )
                        }
                    }
                }

                // Live Rule Enforcement Telemetry Strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TelemetryPill(
                        label = "Playing XI",
                        value = "${validation.xiCount}/$requiredXi",
                        valueColor = if (validation.xiCount == requiredXi) BroadcastEmerald else BroadcastTextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    TelemetryPill(
                        label = "Overseas",
                        value = "${validation.overseasInXiCount}/4 MAX",
                        valueColor = if (validation.overseasInXiCount >= 4) BroadcastAmber else BroadcastCyan,
                        modifier = Modifier.weight(1f)
                    )
                    TelemetryPill(
                        label = "Impact Sub",
                        value = if (validation.hasImpactPlayer) "1/1 SET" else "0/1",
                        valueColor = if (validation.hasImpactPlayer) BroadcastEmerald else BroadcastTextMuted,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Inline Validation / Max 4 Overseas Warning Banner
                val bannerMsg = selectionError ?: validation.issueMessage
                if (bannerMsg != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(BroadcastCrimson.copy(alpha = 0.14f))
                            .border(1.dp, BroadcastCrimson.copy(alpha = 0.55f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Lineup rule warning",
                            tint = BroadcastAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = bannerMsg,
                            style = MaterialTheme.typography.labelSmall,
                            color = BroadcastTextPrimary
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(BroadcastEmerald.copy(alpha = 0.12f))
                            .border(1.dp, BroadcastEmerald.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Lineup valid",
                            tint = BroadcastEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Lineup verified (${validation.bowlingOptionsCount} bowling options${if (validation.hasWicketkeeper) " • WK set" else " • No specialist WK"})",
                            style = MaterialTheme.typography.labelSmall,
                            color = BroadcastEmerald
                        )
                    }
                }

                // Quick Auto-Fill / Clear Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onAutoFillBestXi,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("auto_fill_xi_button"),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BroadcastCyan.copy(alpha = 0.65f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BroadcastCyan),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = "Auto fill Best XI", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("AUTO BEST XI", fontFamily = DisplayFontFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    OutlinedButton(
                        onClick = onClearXi,
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("clear_xi_button"),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BroadcastOutline),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BroadcastTextSecondary),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Icon(Icons.Filled.ClearAll, contentDescription = "Clear XI", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("CLEAR", fontFamily = DisplayFontFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }

                // Primary CTA: SIMULATE IPL SEASON
                Button(
                    onClick = onSimulateSeason,
                    enabled = validation.isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("simulate_season_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BroadcastGold,
                        contentColor = OnBroadcastGold,
                        disabledContainerColor = BroadcastSurfaceHighlight,
                        disabledContentColor = BroadcastTextMuted
                    )
                ) {
                    Icon(
                        imageVector = if (hasSeasonResult) Icons.Filled.Refresh else Icons.Filled.PlayArrow,
                        contentDescription = "Simulate IPL Season",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (hasSeasonResult) "RE-SIMULATE IPL SEASON" else "SIMULATE IPL SEASON",
                        fontFamily = DisplayFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 19.sp,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SquadLineupPickerRow(
    acquired: AcquiredPlayer,
    isInXi: Boolean,
    isImpactPlayer: Boolean,
    overseasBlocked: Boolean,
    onToggleXi: () -> Unit,
    onToggleImpact: () -> Unit
) {
    val player = acquired.player
    val rowBg = when {
        isInXi -> BroadcastEmerald.copy(alpha = 0.12f)
        isImpactPlayer -> BroadcastCyan.copy(alpha = 0.12f)
        else -> BroadcastSurface
    }
    val rowBorder = when {
        isInXi -> BroadcastEmerald.copy(alpha = 0.65f)
        isImpactPlayer -> BroadcastCyan.copy(alpha = 0.65f)
        else -> BroadcastOutlineSubtle
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("xi_picker_row_${player.id}"),
        shape = RoundedCornerShape(12.dp),
        color = rowBg,
        border = BorderStroke(1.dp, rowBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = player.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = BroadcastTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (player.isOverseas) {
                        Icon(
                            imageVector = Icons.Filled.Public,
                            contentDescription = "Overseas player",
                            tint = BroadcastAmber,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${player.role.shortCode} • ${player.archetype.label} • Form ${player.form} • ${AuctionRules.formatCroresCompact(acquired.boughtPriceLakhs)}",
                    fontFamily = MonoFontFamily,
                    fontSize = 10.sp,
                    color = BroadcastTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // XI Toggle Pill (minimum 48dp touch height)
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(
                        when {
                            isInXi -> BroadcastEmerald
                            overseasBlocked -> BroadcastSurfaceHighlight.copy(alpha = 0.6f)
                            else -> BroadcastSurfaceElevated
                        }
                    )
                    .border(
                        1.dp,
                        when {
                            isInXi -> BroadcastEmerald
                            overseasBlocked -> BroadcastCrimson.copy(alpha = 0.45f)
                            else -> BroadcastOutline
                        },
                        RoundedCornerShape(9.dp)
                    )
                    .clickable(onClick = onToggleXi)
                    .padding(horizontal = 12.dp)
                    .testTag("toggle_xi_${player.id}"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isInXi) "IN XI ✓" else if (overseasBlocked) "OS FULL" else "+ XI",
                    fontFamily = MonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = when {
                        isInXi -> OnBroadcastGold
                        overseasBlocked -> BroadcastCrimson
                        else -> BroadcastTextPrimary
                    }
                )
            }

            // Impact Player Toggle Pill
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(if (isImpactPlayer) BroadcastCyan else BroadcastSurfaceElevated)
                    .border(
                        1.dp,
                        if (isImpactPlayer) BroadcastCyan else BroadcastOutline,
                        RoundedCornerShape(9.dp)
                    )
                    .clickable(onClick = onToggleImpact)
                    .padding(horizontal = 10.dp)
                    .testTag("toggle_imp_${player.id}"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Bolt,
                        contentDescription = "Impact Player",
                        tint = if (isImpactPlayer) OnBroadcastGold else BroadcastCyan,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = if (isImpactPlayer) "IMP ✓" else "IMP",
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (isImpactPlayer) OnBroadcastGold else BroadcastCyan
                    )
                }
            }
        }
    }
}

@Composable
fun SeasonSimulationResultsBlock(
    result: SeasonSimulationResult,
    userFranchiseId: FranchiseId
) {
    val championProfile = FranchiseCatalog.get(result.championId)
    val userWonTitle = result.championId == userFranchiseId

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("season_simulation_results"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. IPL CHAMPION TROPHY BANNER
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BroadcastSurfaceElevated,
            border = BorderStroke(2.dp, BroadcastGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (userWonTitle) {
                    MarqueeCelebrationOverlay(
                        triggerKey = "champion_${result.championId.name}_${result.finalMatch.scoreA}",
                        teamPrimaryColor = championProfile.primaryColor,
                        teamSecondaryColor = championProfile.secondaryColor,
                        modifier = Modifier.matchParentSize()
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    championProfile.primaryColor.copy(alpha = 0.25f),
                                    BroadcastGold.copy(alpha = 0.14f),
                                    BroadcastSurfaceElevated
                                )
                            )
                        )
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = "IPL Champion Trophy",
                        tint = BroadcastGoldLight,
                        modifier = Modifier.size(34.dp)
                    )
                    Text(
                        text = if (userWonTitle) "YOUR FRANCHISE ARE IPL CHAMPIONS!" else "SIMULATED IPL CHAMPIONS",
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = BroadcastGold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = championProfile.fullName.uppercase(),
                        fontFamily = DisplayFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = BroadcastTextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Defeated ${result.runnerUpId.shortCode} in the Final • ${result.finalMatch.marginText}",
                        style = MaterialTheme.typography.bodySmall,
                        color = BroadcastEmerald,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "POTM: ${result.finalMatch.playerOfMatch}",
                        fontFamily = MonoFontFamily,
                        fontSize = 10.sp,
                        color = BroadcastTextSecondary
                    )
                }
            }
        }

        // 2. ORANGE CAP & PURPLE CAP AWARDS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SeasonCapAwardCard(
                title = "ORANGE CAP",
                playerName = result.orangeCap.playerName,
                franchiseId = result.orangeCap.franchiseId,
                statLine = result.orangeCap.statSummary,
                accent = BroadcastAmber,
                modifier = Modifier.weight(1f)
            )
            SeasonCapAwardCard(
                title = "PURPLE CAP",
                playerName = result.purpleCap.playerName,
                franchiseId = result.purpleCap.franchiseId,
                statLine = result.purpleCap.statSummary,
                accent = Color(0xFFB388FF),
                modifier = Modifier.weight(1f)
            )
        }

        // 3. PLAYOFFS BRACKET
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = BroadcastSurface,
            border = BorderStroke(1.dp, BroadcastOutline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "PLAYOFFS & FINAL SCORECARDS",
                    fontFamily = DisplayFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    letterSpacing = 0.7.sp,
                    color = BroadcastGold
                )
                PlayoffMatchRow(result.qualifier1, userFranchiseId)
                PlayoffMatchRow(result.eliminator, userFranchiseId)
                PlayoffMatchRow(result.qualifier2, userFranchiseId)
                PlayoffMatchRow(result.finalMatch, userFranchiseId, isFinal = true)
            }
        }

        // 4. 10-TEAM LEAGUE POINTS TABLE (14 MATCHES EACH)
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = BroadcastSurface,
            border = BorderStroke(1.dp, BroadcastOutline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "IPL LEAGUE POINTS TABLE",
                        fontFamily = DisplayFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        letterSpacing = 0.7.sp,
                        color = BroadcastTextPrimary
                    )
                    Text(
                        text = "TOP 4 QUALIFY",
                        fontFamily = MonoFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = BroadcastEmerald
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("#", fontFamily = MonoFontFamily, fontSize = 10.sp, color = BroadcastTextMuted, modifier = Modifier.width(22.dp))
                    Text("TEAM", fontFamily = MonoFontFamily, fontSize = 10.sp, color = BroadcastTextMuted, modifier = Modifier.weight(1f))
                    Text("OVR", fontFamily = MonoFontFamily, fontSize = 10.sp, color = BroadcastTextMuted, modifier = Modifier.width(34.dp), textAlign = TextAlign.End)
                    Text("W-L", fontFamily = MonoFontFamily, fontSize = 10.sp, color = BroadcastTextMuted, modifier = Modifier.width(42.dp), textAlign = TextAlign.End)
                    Text("NRR", fontFamily = MonoFontFamily, fontSize = 10.sp, color = BroadcastTextMuted, modifier = Modifier.width(52.dp), textAlign = TextAlign.End)
                    Text("PTS", fontFamily = MonoFontFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BroadcastGold, modifier = Modifier.width(34.dp), textAlign = TextAlign.End)
                }
                HorizontalDivider(color = BroadcastOutlineSubtle)

                result.standings.forEachIndexed { idx, row ->
                    PointsTableRow(
                        rank = idx + 1,
                        row = row,
                        isUser = row.franchiseId == userFranchiseId,
                        isPlayoffSpot = idx < 4
                    )
                    if (idx == 3) {
                        HorizontalDivider(color = BroadcastEmerald.copy(alpha = 0.55f), thickness = 1.dp)
                    } else if (idx < result.standings.lastIndex) {
                        HorizontalDivider(color = BroadcastOutlineSubtle.copy(alpha = 0.45f))
                    }
                }
            }
        }
    }
}

@Composable
private fun SeasonCapAwardCard(
    title: String,
    playerName: String,
    franchiseId: FranchiseId,
    statLine: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = BroadcastSurface,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = accent, modifier = Modifier.size(13.dp))
                Text(
                    text = title,
                    fontFamily = MonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    color = accent
                )
            }
            Text(
                text = playerName,
                style = MaterialTheme.typography.titleSmall,
                color = BroadcastTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${franchiseId.shortCode} • $statLine",
                fontFamily = MonoFontFamily,
                fontSize = 10.sp,
                color = BroadcastTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun PlayoffMatchRow(
    match: PlayoffMatchResult,
    userFranchiseId: FranchiseId,
    isFinal: Boolean = false
) {
    val winnerProfile = FranchiseCatalog.get(match.winnerId)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isFinal) BroadcastGold.copy(alpha = 0.12f) else BroadcastBgDeep.copy(alpha = 0.65f))
            .border(1.dp, if (isFinal) BroadcastGold.copy(alpha = 0.65f) else BroadcastOutlineSubtle, RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = match.stageName,
                fontFamily = MonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = if (isFinal) BroadcastGold else BroadcastCyan
            )
            Text(
                text = "${winnerProfile.shortCode} ${match.marginText}",
                fontFamily = MonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = BroadcastEmerald
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FranchiseBadge(franchiseId = match.teamA, isUser = match.teamA == userFranchiseId, compact = true)
                Text(text = match.scoreA, fontFamily = MonoFontFamily, fontSize = 11.sp, color = BroadcastTextPrimary)
            }
            Text("vs", fontFamily = MonoFontFamily, fontSize = 10.sp, color = BroadcastTextMuted)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = match.scoreB, fontFamily = MonoFontFamily, fontSize = 11.sp, color = BroadcastTextPrimary)
                FranchiseBadge(franchiseId = match.teamB, isUser = match.teamB == userFranchiseId, compact = true)
            }
        }
    }
}

@Composable
private fun PointsTableRow(
    rank: Int,
    row: LeagueStandingRow,
    isUser: Boolean,
    isPlayoffSpot: Boolean
) {
    val nrrFormatted = String.format(Locale.US, "%+.3f", row.netRunRate)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isUser) BroadcastGold.copy(alpha = 0.10f) else Color.Transparent)
            .padding(vertical = 7.dp, horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isPlayoffSpot) "Q$rank" else "$rank",
            fontFamily = MonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = if (isPlayoffSpot) BroadcastEmerald else BroadcastTextMuted,
            modifier = Modifier.width(22.dp)
        )
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FranchiseBadge(franchiseId = row.franchiseId, isUser = isUser, compact = true)
        }
        Text(
            text = "${row.xiRating}",
            fontFamily = MonoFontFamily,
            fontSize = 11.sp,
            color = BroadcastCyan,
            modifier = Modifier.width(34.dp),
            textAlign = TextAlign.End
        )
        Text(
            text = "${row.won}-${row.lost}",
            fontFamily = MonoFontFamily,
            fontSize = 11.sp,
            color = BroadcastTextPrimary,
            modifier = Modifier.width(42.dp),
            textAlign = TextAlign.End
        )
        Text(
            text = nrrFormatted,
            fontFamily = MonoFontFamily,
            fontSize = 10.sp,
            color = if (row.netRunRate >= 0) BroadcastEmerald else BroadcastCrimson,
            modifier = Modifier.width(52.dp),
            textAlign = TextAlign.End
        )
        Text(
            text = "${row.points}",
            fontFamily = MonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = BroadcastGold,
            modifier = Modifier.width(34.dp),
            textAlign = TextAlign.End
        )
    }
}
