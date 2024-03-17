package com.sginnovations.asked.presentation.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingOneSelect
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingChildName
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingCrafting
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingDefault
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingGender
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingMultipleSelect
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingQuote
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingResponse
import com.sginnovations.asked.presentation.ui.onboarding.type.OnBoardingSection
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
        OnBoardingType.SingleSelect -> OnBoardingOneSelect(onBoardingPage)
        OnBoardingType.MultipleSelect -> OnBoardingMultipleSelect(vmOnBoarding, onBoardingPage)
        OnBoardingType.Quote -> OnBoardingQuote(onBoardingPage)
        OnBoardingType.Response -> OnBoardingResponse(vmOnBoarding, onBoardingPage)
        OnBoardingType.Section -> OnBoardingSection(onBoardingPage)
        OnBoardingType.Personalization -> OnBoardingCrafting(vmOnBoarding, pagerState)
        OnBoardingType.ChildName -> OnBoardingChildName(vmOnBoarding)
        else -> OnBoardingDefault(onBoardingPage)
    }
}