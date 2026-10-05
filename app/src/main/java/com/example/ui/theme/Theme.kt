package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldPrimaryDark,
    onPrimary = EmeraldOnPrimaryDark,
    primaryContainer = EmeraldContainerDark,
    onPrimaryContainer = OnEmeraldContainerDark,
    secondary = AmberGoldSecondaryDark,
    onSecondary = OnAmberSecondaryDark,
    secondaryContainer = AmberContainerDark,
    background = ParchmentBackgroundDark,
    onBackground = TextPrimaryDark,
    surface = ParchmentSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = ParchmentSurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimaryLight,
    onPrimary = EmeraldOnPrimaryLight,
    primaryContainer = EmeraldContainerLight,
    onPrimaryContainer = OnEmeraldContainerLight,
    secondary = AmberGoldSecondaryLight,
    onSecondary = OnAmberSecondaryLight,
    secondaryContainer = AmberContainerLight,
    onSecondaryContainer = OnAmberContainerLight,
    tertiary = TerracottaTertiaryLight,
    onTertiary = OnTerracottaTertiaryLight,
    background = ParchmentBackgroundLight,
    onBackground = TextPrimaryLight,
    surface = ParchmentSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = ParchmentSurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight
)

@Composable
fun UyghurTibabitiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
