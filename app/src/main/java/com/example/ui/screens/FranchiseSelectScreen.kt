package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.FranchiseCatalog
import com.example.data.PlayerDatabase
import com.example.engine.AuctionRules
import com.example.model.FranchiseId
import com.example.model.FranchiseProfile
import com.example.model.SavedAuctionSummary
import com.example.ui.components.FranchiseBadge
import com.example.ui.theme.BroadcastBgDeep
import com.example.ui.theme.BroadcastCyan
import com.example.ui.theme.BroadcastEmerald
import com.example.ui.theme.BroadcastGold
import com.example.ui.theme.BroadcastGoldDeep
import com.example.ui.theme.BroadcastOutline
import com.example.ui.theme.BroadcastSurface
import com.example.ui.theme.BroadcastSurfaceElevated
import com.example.ui.theme.BroadcastSurfaceHighlight
import com.example.ui.theme.BroadcastTextMuted
import com.example.ui.theme.BroadcastTextPrimary
import com.example.ui.theme.BroadcastTextSecondary
import com.example.ui.theme.DisplayFontFamily
import com.example.ui.theme.MonoFontFamily
import com.example.ui.theme.OnBroadcastGold

@Composable
fun FranchiseSelectScreen(
    selectedFranchiseId: FranchiseId,
    savedAuctionSummary: SavedAuctionSummary?,
    onSelectFranchise: (FranchiseId) -> Unit,
    onStartAuction: () -> Unit,
    onResumeSavedAuction: () -> Unit
) {
    val selectedProfile = FranchiseCatalog.get(selectedFranchiseId)

    Scaffold(
        containerColor = BroadcastBgDeep,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Surface(
                color = BroadcastSurface.copy(alpha = 0.96f),
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 720.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "SELECTED FRANCHISE",
                                fontFamily = MonoFontFamily,
                                fontSize = 10.sp,
                                color = BroadcastTextMuted
                            )
                            Text(
                                text = selectedProfile.fullName,
                                style = MaterialTheme.typography.titleMedium,
                                color = BroadcastTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Button(
                            onClick = onStartAuction,
                            modifier = Modifier
                                .height(52.dp)
                                .testTag("start_auction_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BroadcastGold,
                                contentColor = OnBroadcastGold
                            ),
                            contentPadding = PaddingValues(horizontal = 20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Gavel,
                                contentDescription = "Start mega auction",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (savedAuctionSummary != null) {
                                    "START NEW (${selectedProfile.shortCode})"
                                } else {
                                    "START NEW MEGA AUCTION"
                                },
                                fontFamily = DisplayFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                letterSpacing = 0.7.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val columns = when {
                maxWidth >= 960.dp -> 5
                maxWidth >= 640.dp -> 3
                else -> 2
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(span = { GridItemSpan(columns) }) {
                    BroadcastHeroHeader()
                }

                if (savedAuctionSummary != null) {
                    item(span = { GridItemSpan(columns) }) {
                        ResumeSavedAuctionCard(
                            summary = savedAuctionSummary,
                            onResume = onResumeSavedAuction
                        )
                    }
                }

                item(span = { GridItemSpan(columns) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(22.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(BroadcastGold)
                            )
                            Text(
                                text = "SELECT YOUR FRANCHISE",
                                fontFamily = DisplayFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                letterSpacing = 1.sp,
                                color = BroadcastTextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Take command of 1 authentic IPL franchise while 9 independent AI brains execute unique franchise DNA strategies across all 9 official auction sets.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = BroadcastTextSecondary
                        )
                    }
                }

                items(
                    items = FranchiseCatalog.allFranchises,
                    key = { it.id.name }
                ) { franchise ->
                    val index = FranchiseCatalog.allFranchises.indexOf(franchise)
                    val isStartOfRow = index % columns == 0
                    val isEndOfRow = index % columns == columns - 1
                    FranchiseSelectionCard(
                        profile = franchise,
                        isSelected = franchise.id == selectedFranchiseId,
                        onClick = { onSelectFranchise(franchise.id) },
                        modifier = Modifier.padding(
                            start = if (isStartOfRow) 16.dp else 0.dp,
                            end = if (isEndOfRow) 16.dp else 0.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ResumeSavedAuctionCard(
    summary: SavedAuctionSummary,
    onResume: () -> Unit
) {
    val profile = FranchiseCatalog.get(summary.userFranchiseId)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("resume_saved_auction_card"),
        shape = RoundedCornerShape(16.dp),
        color = BroadcastSurfaceElevated,
        border = BorderStroke(
            width = 1.6.dp,
            brush = Brush.horizontalGradient(listOf(BroadcastEmerald, BroadcastGold))
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Brush.horizontalGradient(listOf(BroadcastEmerald, BroadcastGold)))
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Restore,
                            contentDescription = "Saved auction available",
                            tint = BroadcastEmerald,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = "UNFINISHED AUCTION AUTO-SAVED",
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = BroadcastEmerald,
                                letterSpacing = 0.7.sp
                            )
                            Text(
                                text = "${profile.fullName} • Set ${summary.currentSet.setNumber}: ${summary.currentSet.shortTitle}",
                                style = MaterialTheme.typography.titleSmall,
                                color = BroadcastTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    FranchiseBadge(
                        franchiseId = summary.userFranchiseId,
                        isUser = true,
                        compact = true
                    )
                }

                Text(
                    text = "Squad ${summary.userSquadCount}/25 • Purse ${AuctionRules.formatCrores(summary.userPurseLakhs)} • ${summary.totalProcessedPlayers} lots completed",
                    fontFamily = MonoFontFamily,
                    fontSize = 11.sp,
                    color = BroadcastTextSecondary
                )

                Button(
                    onClick = onResume,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("resume_saved_auction_button"),
                    shape = RoundedCornerShape(11.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BroadcastEmerald,
                        contentColor = OnBroadcastGold
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Resume Saved Auction",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "RESUME SAVED AUCTION",
                        fontFamily = DisplayFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BroadcastHeroHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(255.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_auction_stage_hero),
            contentDescription = "IPL Mega Auction Broadcast Studio Stage",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Rich dark gradient scrim for razor-sharp broadcast typography legibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BroadcastBgDeep.copy(alpha = 0.55f),
                            BroadcastBgDeep.copy(alpha = 0.72f),
                            BroadcastBgDeep
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = BroadcastGold.copy(alpha = 0.18f),
                border = BorderStroke(1.dp, BroadcastGold.copy(alpha = 0.65f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Gavel,
                        contentDescription = null,
                        tint = BroadcastGold,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "OFFICIAL MEGA AUCTION BROADCAST ENGINE",
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = BroadcastGold,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "IPL MEGA AUCTION",
                fontFamily = DisplayFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 38.sp,
                lineHeight = 38.sp,
                letterSpacing = 1.2.sp,
                color = BroadcastTextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Official Rule Highlights Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HeroRuleChip(
                    label = "₹120 Cr",
                    sublabel = "Zero Retentions",
                    accent = BroadcastGold,
                    modifier = Modifier.weight(1f)
                )
                HeroRuleChip(
                    label = "18–25",
                    sublabel = "Max 8 OS",
                    accent = BroadcastCyan,
                    modifier = Modifier.weight(1f)
                )
                HeroRuleChip(
                    label = "${PlayerDatabase.allPlayers.size}",
                    sublabel = "Real Stars",
                    accent = BroadcastEmerald,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun HeroRuleChip(
    label: String,
    sublabel: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = BroadcastSurface.copy(alpha = 0.85f),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                fontFamily = MonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = accent,
                maxLines = 1
            )
            Text(
                text = sublabel,
                style = MaterialTheme.typography.labelSmall,
                color = BroadcastTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun FranchiseSelectionCard(
    profile: FranchiseProfile,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderBrush = if (isSelected) {
        Brush.linearGradient(listOf(BroadcastGold, profile.primaryColor))
    } else {
        Brush.linearGradient(listOf(BroadcastOutline, BroadcastOutline))
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                brush = borderBrush,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .testTag("franchise_card_${profile.shortCode.lowercase()}"),
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) BroadcastSurfaceHighlight else BroadcastSurfaceElevated
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Team Color Header Accent Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(profile.primaryColor, profile.secondaryColor)
                        )
                    )
            )

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = profile.primaryColor,
                        border = BorderStroke(1.dp, profile.secondaryColor.copy(alpha = 0.7f))
                    ) {
                        Text(
                            text = profile.shortCode,
                            fontFamily = DisplayFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            letterSpacing = 0.8.sp,
                            color = profile.onPrimaryColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Selected franchise",
                            tint = BroadcastGold,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .border(1.5.dp, BroadcastOutline, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = profile.fullName,
                    style = MaterialTheme.typography.titleSmall,
                    color = BroadcastTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = profile.dnaHeadline,
                    style = MaterialTheme.typography.bodySmall,
                    color = BroadcastTextSecondary,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // DNA Tag pills (top 2)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    profile.dnaTags.take(2).forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .background(BroadcastBgDeep.copy(alpha = 0.65f))
                                .border(1.dp, BroadcastOutline.copy(alpha = 0.6f), RoundedCornerShape(5.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                color = BroadcastTextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
