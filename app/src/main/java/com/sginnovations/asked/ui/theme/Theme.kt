package com.sginnovations.asked.ui.theme

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberUpdatedState
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

    background = background,
    onBackground = onBackground,

    error = error,
)

val LightColorScheme = lightColorScheme(
    primary = primaryL,
    onPrimary = onPrimaryL,

    primaryContainer = primaryContainerL,
    secondaryContainer = secondaryContainerL,
    onPrimaryContainer = onPrimaryContainerL,
    onSecondaryContainer = onSecondaryContainerL,

    surface = surfaceL,
    onSurfaceVariant = onSurfaceVariantL,

    background = backgroundL,
    onBackground = onBackgroundL,

    error = errorL,
)


@Composable
fun LearnedAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,

    fontSizeMultiplier: MutableFloatState,

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
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme

            if (!darkTheme) {
                window.navigationBarColor = Color(0xFFe9effd).toArgb()
            } else {
                window.navigationBarColor = Color(0xFF0C1622).toArgb()
            }
        }
    }

    Log.d("LearnedApp", "fontSizeMultiplier2: ${fontSizeMultiplier.floatValue} ")

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(fontSizeMultiplier = fontSizeMultiplier.floatValue),
        content = content
    )
}