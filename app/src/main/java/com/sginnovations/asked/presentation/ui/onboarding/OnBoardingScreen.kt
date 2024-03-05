package com.sginnovations.asked.presentation.ui.onboarding

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    //onSkip: () -> Unit,
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
    val scrollScope = rememberCoroutineScope()

    val navigationBarColor = MaterialTheme.colorScheme.background.toArgb()
    SideEffect { (context as Activity).window.navigationBarColor = navigationBarColor }

    HorizontalPager(
        //TODO TUTORIAL END IT
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter),
            ) {

                /**
                 * Page body
                 */
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    OnBoardingBodyPage(onBoardingPages[page])
                }

                /**
                 * Page indicator and Next page button
                 */
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
//                    Row(
//                        Modifier
//                            .wrapContentHeight()
//                            .fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        repeat(pagerState.pageCount) { iteration ->
//                            val color =
//                                if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
//                            val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp
//
//                            Box(
//                                modifier = Modifier
//                                    .padding(2.dp)
//                                    .animateContentSize()
//                                    .size(width, 8.dp)
//                                    .clip(
//                                        if (pagerState.currentPage == iteration) RoundedCornerShape(
//                                            10.dp
//                                        ) else CircleShape
//                                    )
//                                    .background(color)
//                            )
//
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            when (pagerState.currentPage) {
                                onBoardingPagesNum - 1 -> onFinish()
                                else -> scrollScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(20),
                        colors = ButtonDefaults.buttonColors(
                            when (pagerState.currentPage) {
                                0 -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onBackground
                            }

                        )
                    ) {
                        Text(
                            text = when (pagerState.currentPage) {
                                0 -> stringResource(R.string.get_started)
                                onBoardingPagesNum - 1 -> stringResource(R.string.finish)
                                else -> stringResource(R.string.next)
                            },
                            color = MaterialTheme.colorScheme.background,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}