package com.sginnovations.asked.presentation.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingDefault

@Composable
fun OnBoardingBodyPage(
    onBoardingPage: OnBoardingPage,
) {
    val context = LocalContext.current

    when (onBoardingPage.getType(context)) {
        OnBoardingType.Default -> OnBoardingDefault(onBoardingPage)
        OnBoardingType.GenderSelect -> OnBoardingDefault(onBoardingPage)
        else -> OnBoardingDefault(onBoardingPage)
    }
}