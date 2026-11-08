package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TempleGold,
    onPrimary = SanctumMaroonDark,
    primaryContainer = SanctumMaroon,
    onPrimaryContainer = MarbleIvory,
    secondary = SacredSaffron,
    onSecondary = Color.White,
    secondaryContainer = SanctumMaroonLight,
    onSecondaryContainer = MarbleIvory,
    tertiary = TempleGoldLight,
    onTertiary = SanctumMaroonDark,
    background = MidnightIndigo,
    onBackground = MarbleIvory,
    surface = Color(0xFF161C2E),
    onSurface = MarbleIvory,
    surfaceVariant = Color(0xFF222B42),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = TempleGoldDark,
    outlineVariant = Color(0xFF334155)
)

private val LightColorScheme = lightColorScheme(
    primary = SanctumMaroon,
    onPrimary = MarbleIvory,
    primaryContainer = SanctumMaroonLight,
    onPrimaryContainer = MarbleIvory,
    secondary = TempleGold,
    onSecondary = SanctumMaroonDark,
    secondaryContainer = WarmSand,
    onSecondaryContainer = SanctumMaroon,
    tertiary = SacredSaffron,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDBCB),
    onTertiaryContainer = SacredSaffronDark,
    background = MarbleIvory,
    onBackground = CharcoalInk,
    surface = Color.White,
    onSurface = CharcoalInk,
    surfaceVariant = WarmSand,
    onSurfaceVariant = Color(0xFF4A453E),
    outline = TempleGold,
    outlineVariant = Color(0xFFE2D8CC)
)

@Composable
fun PoojaSetuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
