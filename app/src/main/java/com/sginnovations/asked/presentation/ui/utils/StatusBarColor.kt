package com.sginnovations.asked.presentation.ui.utils

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val backGroundColor = Color(0xFF3C5AFA)

@Composable
fun ResetStatusBarColor() {
    val view = LocalView.current
    val color = MaterialTheme.colorScheme.background

    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = color.toArgb()
    }
}
@Composable
fun StatusBarColorBlue() {
    val view = LocalView.current

    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = backGroundColor.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }
}