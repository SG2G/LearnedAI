package com.sginnovations.asked.presentation.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingAge
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingCrafting
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingDefault
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingGender
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingInterest
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingQuote

@Composable
fun OnBoardingBodyPage(
    onBoardingPage: OnBoardingPage,
) {
    val context = LocalContext.current

    /**
     * Page body
     */
    when (onBoardingPage.getType(context)) {
        OnBoardingType.Default -> OnBoardingDefault(onBoardingPage)
        OnBoardingType.GenderSelect -> OnBoardingGender(onBoardingPage)
        OnBoardingType.SingleSelect -> OnBoardingAge(onBoardingPage)
        OnBoardingType.MultipleSelect -> OnBoardingInterest(onBoardingPage)
        OnBoardingType.Quote -> OnBoardingQuote(onBoardingPage)
        OnBoardingType.Personalization -> OnBoardingCrafting()
        else -> OnBoardingDefault(onBoardingPage)
    }
}