package edu.nd.pmcburne.hwapp.one.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.nd.pmcburne.hwapp.one.data.db.GameEntity
import edu.nd.pmcburne.hwapp.one.ui.theme.AmberGold
import edu.nd.pmcburne.hwapp.one.ui.theme.CardStroke
import edu.nd.pmcburne.hwapp.one.ui.theme.LiveRed
import edu.nd.pmcburne.hwapp.one.ui.theme.TextDimmed
import edu.nd.pmcburne.hwapp.one.ui.theme.TextSecondary
import edu.nd.pmcburne.hwapp.one.ui.theme.WinnerGreen

@Composable
fun GameCard(
    game: GameEntity,
    modifier: Modifier = Modifier
) {
    val accentColor = when (game.gameState) {
        "live" -> LiveRed
        "pre" -> AmberGold
        else -> TextDimmed
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, CardStroke),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top accent bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(accentColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Away team
                TeamSection(
                    label = "AWAY",
                    teamName = game.awayTeamName,
                    score = game.awayScore,
                    rank = game.awayRank,
                    seed = game.awaySeed,
                    isWinner = game.awayWinner,
                    gameState = game.gameState
                )

                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(1.dp)
                        .background(CardStroke)
                )

                // Home team
                TeamSection(
                    label = "HOME",
                    teamName = game.homeTeamName,
                    score = game.homeScore,
                    rank = game.homeRank,
                    seed = game.homeSeed,
                    isWinner = game.homeWinner,
                    gameState = game.gameState
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Status line + network
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusLine(game = game)

                    if (game.network.isNotBlank()) {
                        NetworkBadge(network = game.network)
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamSection(
    label: String,
    teamName: String,
    score: String,
    rank: String,
    seed: String,
    isWinner: Boolean,
    gameState: String
) {
    val textColor = when {
        gameState == "final" && !isWinner -> TextDimmed
        isWinner -> WinnerGreen
        else -> MaterialTheme.colorScheme.onSurface
    }

    val fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal

    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = TextDimmed,
        letterSpacing = 1.5.sp
    )

    Spacer(modifier = Modifier.height(2.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            val prefix = when {
                rank.isNotBlank() -> "#$rank "
                seed.isNotBlank() -> "($seed) "
                else -> ""
            }
            if (prefix.isNotBlank()) {
                Text(
                    text = prefix,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = teamName,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                color = textColor,
                fontWeight = fontWeight,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (score.isNotBlank()) {
            Text(
                text = score,
                style = MaterialTheme.typography.headlineMedium,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StatusLine(game: GameEntity) {
    when (game.gameState) {
        "pre" -> {
            Text(
                text = "Tipoff: ${game.startTime}",
                style = MaterialTheme.typography.titleSmall,
                color = AmberGold
            )
        }
        "live" -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PulsingDot(color = LiveRed)
                Spacer(modifier = Modifier.width(6.dp))
                val periodText = formatLivePeriod(game.currentPeriod, game.contestClock)
                Text(
                    text = periodText,
                    style = MaterialTheme.typography.titleSmall,
                    color = LiveRed,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        "final" -> {
            Text(
                text = "FINAL",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun PulsingDot(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Canvas(modifier = Modifier.size(8.dp)) {
        drawCircle(color = color.copy(alpha = alpha))
    }
}

@Composable
private fun NetworkBadge(network: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = network,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}

private fun formatLivePeriod(currentPeriod: String, contestClock: String): String {
    val periodUpper = currentPeriod.uppercase()
    return when {
        periodUpper == "HALFTIME" || periodUpper == "HALF" -> "HALFTIME"
        periodUpper == "1ST" || periodUpper == "1" -> "1st Half \u00b7 $contestClock"
        periodUpper == "2ND" || periodUpper == "2" -> "2nd Half \u00b7 $contestClock"
        periodUpper == "3RD" || periodUpper == "3" -> "3rd Qtr \u00b7 $contestClock"
        periodUpper == "4TH" || periodUpper == "4" -> "4th Qtr \u00b7 $contestClock"
        periodUpper.contains("OT") -> "$currentPeriod \u00b7 $contestClock"
        else -> "$currentPeriod \u00b7 $contestClock"
    }
}
