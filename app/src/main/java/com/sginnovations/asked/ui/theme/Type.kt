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
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = monaSansFontFamily,
    ),
    headlineMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = monaSansFontFamily,
    ),
    titleLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = monaSansFontFamily,
    ),
    bodySmall = TextStyle(
        fontFamily = monaSansFontFamily,
    ),
    labelLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold
    ),
    labelMedium = TextStyle(
        fontFamily = monaSansFontFamily,
    ),

    labelSmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
)