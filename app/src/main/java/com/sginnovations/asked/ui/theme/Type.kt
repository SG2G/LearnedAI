package com.sginnovations.asked.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.R

val monaSansFontFamily = FontFamily(
    Font(R.font.monasans_light, FontWeight.Light),
    Font(R.font.monasans_regular, FontWeight.Normal),
    Font(R.font.monasans_semibold, FontWeight.Bold)
)

// Set of Material typography styles to start with
fun getTypography(fontSizeMultiplier: Float) = Typography(
    displayLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp * fontSizeMultiplier,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp * fontSizeMultiplier,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp * fontSizeMultiplier,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp * fontSizeMultiplier,
    ),
    headlineMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp * fontSizeMultiplier,
    ),
    headlineSmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp * fontSizeMultiplier,
    ),

    titleLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp * fontSizeMultiplier,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp * fontSizeMultiplier,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp * fontSizeMultiplier,
    ),
    bodyLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp * fontSizeMultiplier,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontSize = 14.sp * fontSizeMultiplier,
    ),
    bodySmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontSize = 12.sp * fontSizeMultiplier,
    ),
    labelLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp * fontSizeMultiplier,
    ),
    labelMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontSize = 12.sp * fontSizeMultiplier,
    ),

    labelSmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp * fontSizeMultiplier,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
)