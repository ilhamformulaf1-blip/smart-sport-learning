package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = SkyBluePrimary,
    onPrimary = Color.White,
    primaryContainer = SkyBlueContainer,
    onPrimaryContainer = OnSkyBlueContainer,
    secondary = NavySurface,
    onSecondary = Color.White,
    secondaryContainer = SurfaceVariantLight,
    onSecondaryContainer = NavyDeep,
    tertiary = SportGreen,
    onTertiary = Color.White,
    tertiaryContainer = SportGreenContainer,
    onTertiaryContainer = OnSportGreenContainer,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight,
    outlineVariant = Color(0xFFCBD5E1),
    error = SafetyRed,
    onError = Color.White,
    errorContainer = SafetyRedContainer,
    onErrorContainer = OnSafetyRedContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlueLight,
    onPrimary = NavyDeep,
    primaryContainer = SkyBlueDark,
    onPrimaryContainer = SkyBlueContainer,
    secondary = Color(0xFF94A3B8),
    onSecondary = NavyDeep,
    secondaryContainer = NavySurface,
    onSecondaryContainer = Color(0xFFF1F5F9),
    tertiary = SportGreenLight,
    onTertiary = NavyDeep,
    tertiaryContainer = SportGreenDark,
    onTertiaryContainer = SportGreenContainer,
    background = Color(0xFF0B1120),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF0F172A),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF334155),
    error = SafetyRed,
    onError = Color.White
)

@Composable
fun SmartSportTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
