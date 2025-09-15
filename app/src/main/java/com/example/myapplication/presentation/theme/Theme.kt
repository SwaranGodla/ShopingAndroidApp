package com.example.myapplication.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Custom color scheme for the shopping app
private val LightColorScheme = lightColorScheme(
    primary = ShoppingBlue,
    onPrimary = White,
    primaryContainer = LightBlue,
    onPrimaryContainer = DarkBlue,
    secondary = ShoppingOrange,
    onSecondary = White,
    secondaryContainer = LightOrange,
    onSecondaryContainer = DarkOrange,
    tertiary = ShoppingGreen,
    onTertiary = White,
    tertiaryContainer = LightGreen,
    onTertiaryContainer = DarkGreen,
    background = LightBackground,
    onBackground = DarkText,
    surface = White,
    onSurface = DarkText,
    surfaceVariant = LightGray,
    onSurfaceVariant = MediumGray,
    error = ErrorRed,
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = ShoppingBlueLight,
    onPrimary = DarkBlue,
    primaryContainer = ShoppingBlue,
    onPrimaryContainer = LightBlue,
    secondary = ShoppingOrangeLight,
    onSecondary = DarkOrange,
    secondaryContainer = ShoppingOrange,
    onSecondaryContainer = LightOrange,
    tertiary = ShoppingGreenLight,
    onTertiary = DarkGreen,
    tertiaryContainer = ShoppingGreen,
    onTertiaryContainer = LightGreen,
    background = DarkBackground,
    onBackground = LightText,
    surface = DarkSurface,
    onSurface = LightText,
    surfaceVariant = DarkGray,
    onSurfaceVariant = LightGray,
    error = ErrorRedLight,
    onError = DarkRed
)

@Composable
fun ShoppingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ShoppingTypography,
        shapes = ShoppingShapes,
        content = content
    )
}
