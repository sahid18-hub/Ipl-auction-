package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.AuctionRules
import com.example.model.FranchiseId
import com.example.model.FranchiseState
import com.example.model.Player
import com.example.model.PlayerRole
import com.example.ui.theme.BroadcastAmber
import com.example.ui.theme.BroadcastBgDeep
import com.example.ui.theme.BroadcastCyan
import com.example.ui.theme.BroadcastEmerald
import com.example.ui.theme.BroadcastGold
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

const val MAX_USER_ACCELERATED_NOMINATIONS = 5

@Composable
fun AcceleratedNominationModalScreen(
    unsoldPlayers: List<Player>,
    userFranchise: FranchiseState,
    userNominatedIds: Set<String>,
    aiNominationsByTeam: Map<FranchiseId, List<String>>,
    onToggleNominatePlayer: (String) -> Unit,
    onStartAcceleratedRound: () -> Unit
) {
    var selectedRoleFilter by rememberSaveable { mutableStateOf<String>("ALL") }

    // Map playerId -> list of AI shortCodes that nominated them
    val aiNominatorsByPlayer: Map<String, List<FranchiseId>> = remember(aiNominationsByTeam) {
        buildMap<String, MutableList<FranchiseId>> {
            aiNominationsByTeam.forEach { (teamId, playerIds) ->
                playerIds.forEach { pid ->
                    getOrPut(pid) { mutableListOf() }.add(teamId)
                }
            }
        }
    }

    val uniqueAiNominatedIds = remember(aiNominatorsByPlayer) { aiNominatorsByPlayer.keys }
    val combinedShortlistCount = remember(userNominatedIds, uniqueAiNominatedIds) {
        (userNominatedIds + uniqueAiNominatedIds).size
    }

    val filteredPlayers = remember(unsoldPlayers, selectedRoleFilter, userNominatedIds, aiNominatorsByPlayer) {
        val roleMatched = if (selectedRoleFilter == "ALL") {
            unsoldPlayers
        } else {
            val targetRole = PlayerRole.valueOf(selectedRoleFilter)
            unsoldPlayers.filter { it.role == targetRole }
        }
        roleMatched.sortedWith(
            compareByDescending<Player> { it.id in userNominatedIds }
                .thenByDescending { aiNominatorsByPlayer[it.id]?.size ?: 0 }
                .thenByDescending { it.form * 0.6 + it.hype * 0.4 }
        )
    }

    Scaffold(
        containerColor = BroadcastBgDeep,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Surface(
                color = BroadcastSurface,
                border = BorderStroke(1.dp, BroadcastOutline),
                tonalElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 680.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "YOUR PICKS: ${userNominatedIds.size}/$MAX_USER_ACCELERATED_NOMINATIONS  •  AI PICKS: ${uniqueAiNominatedIds.size}",
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = BroadcastCyan
                            )
                            Text(
                                text = "1.0s FAST CADENCE",
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = BroadcastGold
                            )
                        }

                        Button(
                            onClick = onStartAcceleratedRound,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("start_accelerated_round_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BroadcastGold,
                                contentColor = OnBroadcastGold
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Bolt,
                                contentDescription = "Start Accelerated Round",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (combinedShortlistCount > 0) {
                                    "START ACCELERATED ROUND ($combinedShortlistCount LOTS)"
                                } else {
                                    "COMPLETE AUCTION"
                                },
                                fontFamily = DisplayFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 760.dp)
                    .testTag("accelerated_nomination_modal")
            ) {
                // Header
                Surface(
                    color = BroadcastSurface,
                    border = BorderStroke(1.dp, BroadcastOutlineSubtle)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Gavel,
                                contentDescription = null,
                                tint = BroadcastGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "OFFICIAL ACCELERATED ROUND SHORTLIST",
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = BroadcastGold,
                                letterSpacing = 0.9.sp
                            )
                        }

                        Text(
                            text = "ACCELERATED ROUND NOMINATION",
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp,
                            letterSpacing = 0.8.sp,
                            color = BroadcastTextPrimary
                        )

                        Text(
                            text = "Select up to 5 unsold players to recall to the block. The 9 AI franchises have nominated ${uniqueAiNominatedIds.size} gap-fillers automatically.",
                            style = MaterialTheme.typography.bodySmall,
                            color = BroadcastTextSecondary
                        )

                        // User Squad Telemetry + Radar Reminder
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TelemetryPill(
                                label = "Your Squad",
                                value = "${userFranchise.squadCount}/25",
                                valueColor = if (userFranchise.squadCount >= AuctionRules.MIN_SQUAD_SIZE) BroadcastEmerald else BroadcastAmber,
                                modifier = Modifier.weight(1f)
                            )
                            TelemetryPill(
                                label = "Purse Left",
                                value = AuctionRules.formatCroresCompact(userFranchise.purseLakhs),
                                valueColor = BroadcastGold,
                                modifier = Modifier.weight(1f)
                            )
                            TelemetryPill(
                                label = "Your Picks",
                                value = "${userNominatedIds.size}/$MAX_USER_ACCELERATED_NOMINATIONS",
                                valueColor = BroadcastCyan,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Role Filter Pills
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val filters = listOf(
                                "ALL" to "ALL (${unsoldPlayers.size})",
                                PlayerRole.BATTER.name to "BAT",
                                PlayerRole.WICKETKEEPER.name to "WK",
                                PlayerRole.ALL_ROUNDER.name to "AR",
                                PlayerRole.FAST_BOWLER.name to "PACE",
                                PlayerRole.SPINNER.name to "SPIN"
                            )
                            filters.forEach { (key, label) ->
                                val selected = selectedRoleFilter == key
                                Box(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .clip(RoundedCornerShape(9.dp))
                                        .background(if (selected) BroadcastGold else BroadcastSurfaceElevated)
                                        .border(
                                            1.dp,
                                            if (selected) BroadcastGold else BroadcastOutline,
                                            RoundedCornerShape(9.dp)
                                        )
                                        .clickable { selectedRoleFilter = key }
                                        .padding(horizontal = 12.dp)
                                        .testTag("nomination_filter_${key.lowercase()}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontFamily = MonoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (selected) OnBroadcastGold else BroadcastTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = BroadcastOutline)

                // Unsold Pool Nomination List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (filteredPlayers.isEmpty()) {
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = BroadcastSurface,
                                border = BorderStroke(1.dp, BroadcastOutlineSubtle)
                            ) {
                                Text(
                                    text = "No unsold players match this role filter.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = BroadcastTextSecondary,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    } else {
                        items(filteredPlayers, key = { "nom_${it.id}" }) { player ->
                            val isSelectedByUser = player.id in userNominatedIds
                            val aiTeams = aiNominatorsByPlayer[player.id].orEmpty()
                            val atUserLimit = !isSelectedByUser && userNominatedIds.size >= MAX_USER_ACCELERATED_NOMINATIONS

                            NominationPlayerCard(
                                player = player,
                                isSelectedByUser = isSelectedByUser,
                                aiNominators = aiTeams,
                                atUserLimit = atUserLimit,
                                onToggle = { onToggleNominatePlayer(player.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NominationPlayerCard(
    player: Player,
    isSelectedByUser: Boolean,
    aiNominators: List<FranchiseId>,
    atUserLimit: Boolean,
    onToggle: () -> Unit
) {
    val bgColor = when {
        isSelectedByUser -> BroadcastGold.copy(alpha = 0.14f)
        aiNominators.isNotEmpty() -> BroadcastCyan.copy(alpha = 0.08f)
        else -> BroadcastSurface
    }
    val borderBrush = when {
        isSelectedByUser -> Brush.horizontalGradient(listOf(BroadcastGold, BroadcastEmerald))
        aiNominators.isNotEmpty() -> Brush.horizontalGradient(listOf(BroadcastCyan.copy(alpha = 0.55f), BroadcastOutline))
        else -> Brush.horizontalGradient(listOf(BroadcastOutlineSubtle, BroadcastOutlineSubtle))
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelectedByUser) 1.5.dp else 1.dp,
                brush = borderBrush,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = !atUserLimit || isSelectedByUser, onClick = onToggle)
            .testTag("nominate_player_${player.id}"),
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
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
                            contentDescription = "Overseas",
                            tint = BroadcastAmber,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
                Text(
                    text = "${player.role.shortCode} • ${player.archetype.label} • Form ${player.form} • Base ${AuctionRules.formatCroresCompact(player.basePriceLakhs)}",
                    fontFamily = MonoFontFamily,
                    fontSize = 10.sp,
                    color = BroadcastTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (aiNominators.isNotEmpty()) {
                    Text(
                        text = "AI NOMINATED: ${aiNominators.joinToString(", ") { it.shortCode }}",
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = BroadcastCyan
                    )
                }
            }

            // Selection pill (48dp height for easy tapping)
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(
                        when {
                            isSelectedByUser -> BroadcastGold
                            atUserLimit -> BroadcastSurfaceHighlight.copy(alpha = 0.55f)
                            else -> BroadcastSurfaceElevated
                        }
                    )
                    .border(
                        1.dp,
                        when {
                            isSelectedByUser -> BroadcastGold
                            else -> BroadcastOutline
                        },
                        RoundedCornerShape(9.dp)
                    )
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isSelectedByUser) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Nominated",
                            tint = OnBroadcastGold,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Text(
                        text = when {
                            isSelectedByUser -> "PICKED"
                            atUserLimit -> "MAX 5"
                            else -> "+ PICK"
                        },
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = when {
                            isSelectedByUser -> OnBroadcastGold
                            atUserLimit -> BroadcastTextMuted
                            else -> BroadcastTextPrimary
                        }
                    )
                }
            }
        }
    }
}
