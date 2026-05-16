package com.elitec.satexplorer.infrastructure.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SatExplorerColorScheme = darkColorScheme(
    primary = electricCyan,
    onPrimary = deepSpaceBlack,
    secondary = orbitalBlue,
    onSecondary = textPrimary,
    tertiary = signalAmber,
    onTertiary = deepSpaceBlack,
    error = telemetryRed,
    onError = textPrimary,
    background = deepSpaceBlack,
    onBackground = textPrimary,
    surface = midnightNavy,
    onSurface = textPrimary,
    surfaceVariant = spaceGray,
    onSurfaceVariant = textSecondary,
    outline = textDisabled
)

@Composable
fun SatExplorerTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SatExplorerColorScheme,
        typography = Typography,
        content = content
    )
}