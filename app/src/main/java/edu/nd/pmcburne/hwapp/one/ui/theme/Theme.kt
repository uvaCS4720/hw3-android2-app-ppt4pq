package edu.nd.pmcburne.hwapp.one.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ScoreboardColorScheme = darkColorScheme(
    primary = AmberGold,
    onPrimary = SlateBlack,
    primaryContainer = AmberGoldDim,
    onPrimaryContainer = TextPrimary,
    secondary = LiveRed,
    onSecondary = TextPrimary,
    tertiary = WinnerGreen,
    onTertiary = SlateBlack,
    background = SlateBlack,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = CardStroke,
    outlineVariant = TextDimmed,
    error = LiveRed,
    onError = Color.White
)

@Composable
fun HWStarterRepoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ScoreboardColorScheme,
        typography = Typography,
        content = content
    )
}