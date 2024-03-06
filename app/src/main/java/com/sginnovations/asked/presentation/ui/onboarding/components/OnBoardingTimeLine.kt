package com.sginnovations.asked.presentation.ui.onboarding.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingTimeLine(
    pagerState: PagerState,
) {
    Spacer(modifier = Modifier.height(16.dp))
    ProgressBar(pagerState = pagerState)
//    Row(
//        Modifier
//            .wrapContentHeight()
//            .fillMaxWidth(),
//        horizontalArrangement = Arrangement.Center
//    ) {
//        repeat(pagerState.pageCount) { iteration ->
//            val color =
//                if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
//            val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp
//
//            Box(
//                modifier = Modifier
//                    .padding(2.dp)
//                    .animateContentSize()
//                    .size(width, 8.dp)
//                    .clip(
//                        if (pagerState.currentPage == iteration) RoundedCornerShape(
//                            10.dp
//                        ) else CircleShape
//                    )
//                    .background(color)
//            )
//
//        }
//    }
    Spacer(modifier = Modifier.height(16.dp))
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProgressBar(pagerState: PagerState) {
    val progressFraction =
        (pagerState.currentPage + pagerState.currentPageOffsetFraction) / (pagerState.pageCount - 1).toFloat()

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
                    .background(MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(4.dp))
            )
        }
    }
}
