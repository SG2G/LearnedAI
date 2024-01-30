package com.sginnovations.asked.ui.onboarding

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import kotlinx.coroutines.launch

const val NUM_PAGES = 5
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun onBoarding(
    onSkip: () -> Unit,
    onFinish: () -> Unit,
) {
    val context = LocalContext.current
    val onBoarding = OnBoarding(LocalContext.current)

    val onBoardingPages = onBoarding.getAllPages()

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0.0f
    ) { NUM_PAGES }
    val scrollScope = rememberCoroutineScope()

    SideEffect { (context as Activity).window.navigationBarColor = Color(0xFF161718).toArgb() }

    HorizontalPager(
        //TODO TUTORIAL END IT
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    TextButton(
                        onClick = { onSkip() },
                    ) {
                        Text(text = stringResource(R.string.skip))
                    }
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
                    Spacer(modifier = Modifier.height(16.dp))
                    if (pagerState.currentPage == NUM_PAGES-1) {
                        Button(
                            onClick = { onFinish() },
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(20),
                        ) {
                            Text(
                                text = stringResource(R.string.finish),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                scrollScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(20),
                        ) {
                            Text(
                                text = stringResource(R.string.next),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}