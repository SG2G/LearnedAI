package com.sginnovations.asked.presentation.ui.onboarding

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.presentation.ui.onboarding.components.OnBoardingButton
import com.sginnovations.asked.presentation.ui.onboarding.components.OnBoardingTimeLine
import com.sginnovations.asked.presentation.viewmodel.OnBoardingViewModel

private const val TAG = "OnBoardingScreen"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    vmOnBoarding: OnBoardingViewModel,
    onFinish: () -> Unit,
) {
    val context = LocalContext.current
    val onBoarding = OnBoarding(LocalContext.current)

    val onBoardingPages = onBoarding.getAllPages()
    val onBoardingPagesNum = onBoarding.getNumberOfPages()

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
        userScrollEnabled = false,
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
                    .height(if (quoteType) 92.dp else 48.dp)
            ) {
                OnBoardingButton(
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