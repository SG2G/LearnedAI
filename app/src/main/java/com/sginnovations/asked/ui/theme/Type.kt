package com.sginnovations.asked.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.R

val monaSansFontFamily = FontFamily(
    Font(R.font.monasans_light, FontWeight.Light),
    Font(R.font.monasans_regular, FontWeight.Normal),
    Font(R.font.monasans_semibold, FontWeight.Bold)
)

// Set of Material typography styles to start with
fun getTypography(fontSizeIncrease: Float) = Typography(
    displayLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (32.sp.value + fontSizeIncrease).sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (28.sp.value + fontSizeIncrease).sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (26.sp.value + fontSizeIncrease).sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (34.sp.value + fontSizeIncrease).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (28.sp.value + fontSizeIncrease).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (20.sp.value + fontSizeIncrease).sp
    ),
    titleLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (22.sp.value + fontSizeIncrease).sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (18.sp.value + fontSizeIncrease).sp,
//        lineHeight = 28.sp,
//        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (16.sp.value + fontSizeIncrease).sp
    ),
    bodyLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = (16.sp.value + fontSizeIncrease).sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontSize = (14.sp.value + fontSizeIncrease).sp
    ),
    bodySmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontSize = (12.sp.value + fontSizeIncrease).sp
    ),
    labelLarge = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (16.sp.value + fontSizeIncrease).sp
    ),
    labelMedium = TextStyle(
        fontFamily = monaSansFontFamily,
        fontSize = (12.sp.value + fontSizeIncrease).sp
    ),
    labelSmall = TextStyle(
        fontFamily = monaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = (10.sp.value + fontSizeIncrease).sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)