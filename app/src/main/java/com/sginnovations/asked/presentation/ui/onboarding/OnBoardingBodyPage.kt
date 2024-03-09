package com.sginnovations.asked.presentation.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingAge
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingCrafting
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingDefault
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingGender
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingInterest
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingQuote
import com.sginnovations.asked.presentation.viewmodel.OnBoardingViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingBodyPage(
    vmOnBoarding: OnBoardingViewModel,
    onBoardingPage: OnBoardingPage,
    pagerState: PagerState,
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
        OnBoardingType.Personalization -> {
            OnBoardingCrafting(vmOnBoarding, pagerState)
        }

        else -> OnBoardingDefault(onBoardingPage)
    }
}