package com.sginnovations.asked.presentation.ui.utils

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.sginnovations.asked.Constants

private val backGroundColor = Color(0xFF3C5AFA)

fun isColorLight(color: Color): Boolean {
    val darkness = 1 - (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue)
    return darkness < 0.5
}
@Composable
fun ResetStatusBarColor() {
    val view = LocalView.current
    val color = MaterialTheme.colorScheme.background

    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = color.toArgb()
        window.navigationBarColor = color.toArgb()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = isColorLight(color)
    }
}
@Composable
fun StatusBarColorBlue() {
    val view = LocalView.current

    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        window.statusBarColor = backGroundColor.toArgb()
        window.navigationBarColor = backGroundColor.toArgb()
    }
}