package com.sginnovations.asked.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = primary,
    onPrimary = onPrimary,

    primaryContainer = primaryContainer,
    secondaryContainer = secondaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    onSecondaryContainer = onSecondaryContainer,

    surface = surface,
    onSurfaceVariant = onSurfaceVariant,

    background = Background,
    onBackground = OnBackground,


)

private val LightColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,

    primaryContainer = primaryContainer,
    secondaryContainer = secondaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    onSecondaryContainer = onSecondaryContainer,

    surface = surface,
    onSurfaceVariant = onSurfaceVariant,

    background = Background,
    onBackground = OnBackground,
)

@Composable
fun LearnedAITheme(
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
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = Color(0xFF191c22).toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}