package com.sginnovations.asked.presentation.ui.onboarding

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import com.sginnovations.asked.presentation.ui.onboarding.components.OnBoardingButton
import com.sginnovations.asked.presentation.ui.onboarding.components.OnBoardingTimeLine

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
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

    val navigationBarColor = MaterialTheme.colorScheme.background.toArgb()

    SideEffect { (context as Activity).window.navigationBarColor = navigationBarColor }

    HorizontalPager(
        //TODO TUTORIAL END IT
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            OnBoardingTimeLine(pagerState)

            OnBoardingBodyPage(onBoardingPages[page])

            Spacer(modifier = Modifier.weight(1f))

            OnBoardingButton(
                onBoardingPages[page],
                pagerState,
                onBoardingPagesNum,

                onFinish,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOnBoardingScreen() {
    OnBoardingScreen(
        onFinish = {}
    )
}