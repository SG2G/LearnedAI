package com.sginnovations.asked.presentation.ui.onboarding

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.presentation.ui.onboarding.components.OnBoardingButton
import com.sginnovations.asked.presentation.ui.onboarding.components.OnBoardingTimeLine
import com.sginnovations.asked.presentation.viewmodel.OnBoardingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull

private const val TAG = "OnBoardingScreen"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    vmOnBoarding: OnBoardingViewModel,
    onFinish: () -> Unit,
) {
    val context = LocalContext.current
    val onBoarding = OnBoarding(LocalContext.current)

    // Use remember to keep track of the initialization state
    val isInitialized = remember { mutableStateOf(false) }

    // Observe your ViewModel state
    val onboardingExperimentNum = vmOnBoarding.onBoardingExperiment

    // Only run this effect once upon initialization or when onboardingExperimentNum changes
    LaunchedEffect(onboardingExperimentNum.value) {
        if (!isInitialized.value) {
            Log.d(TAG, "1 onboardingExperimentNum: ${onboardingExperimentNum.value}")
            isInitialized.value = true

            val result = withTimeoutOrNull(3000) {
                while (onboardingExperimentNum.value == "") {
                    delay(100) // Active wait, we check every 100ms
                }
            }

            if (result == null && onboardingExperimentNum.value == "") {
                vmOnBoarding.onBoardingExperiment.value = "0"
            }

            Log.d(TAG, "2 onboardingExperimentNum: ${onboardingExperimentNum.value}")
        }
    }

    val onBoardingPages = onBoarding.getAllPages(onboardingExperimentNum.value)
    val onBoardingPagesNum = onBoarding.getNumberOfPages(onboardingExperimentNum.value)

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0.0f
    ) { onBoardingPagesNum }

    val btnEnable = vmOnBoarding.btnEnable

    val navigationBarColor = MaterialTheme.colorScheme.background.toArgb()

    SideEffect { (context as Activity).window.navigationBarColor = navigationBarColor }

    HorizontalPager(
        //TODO TUTORIAL END IT
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        userScrollEnabled = true,
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        val quoteType = onBoardingPages[page].getType(context) == OnBoardingType.Quote

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                OnBoardingTimeLine(vmOnBoarding, pagerState)

                OnBoardingBodyPage(vmOnBoarding, onBoardingPages[page], pagerState)
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(if (quoteType) 128.dp else 48.dp)
            ) {
                OnBoardingButton(
                    vmOnBoarding,
                    onBoardingPages[page],
                    pagerState,
                    onBoardingPagesNum,

                    btnEnable,

                    onFinish,
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewOnBoardingScreen() {
//    OnBoardingScreen(
//        onFinish = {}
//    )
//}