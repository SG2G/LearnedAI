package com.sginnovations.asked.presentation.ui.onboarding.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.presentation.viewmodel.OnBoardingViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingTimeLine(
    vmOnBoarding: OnBoardingViewModel,
    pagerState: PagerState,
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        ProgressBar(vmOnBoarding = vmOnBoarding, pagerState = pagerState)
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProgressBar(vmOnBoarding: OnBoardingViewModel, pagerState: PagerState) {
    val progressFraction =
        (pagerState.currentPage + pagerState.currentPageOffsetFraction) / (pagerState.pageCount - 1).toFloat()

    vmOnBoarding.currentPage.intValue = pagerState.currentPage
    Log.d("OnBoardingTimeLine", "progressFraction: $progressFraction \n" +
            " pagerState.currentPage ${pagerState.currentPage} \n" +
            "(pagerState.currentPage + pagerState.currentPageOffsetFraction) ${(pagerState.currentPage + pagerState.currentPageOffsetFraction)} \n" +
            "(pagerState.pageCount - 1).toFloat() ${(pagerState.pageCount - 1).toFloat()}"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(4.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                .width(224.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = progressFraction)
                    .background(
                        MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}
