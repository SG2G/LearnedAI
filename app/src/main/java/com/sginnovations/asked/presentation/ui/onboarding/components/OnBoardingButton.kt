package com.sginnovations.asked.presentation.ui.onboarding.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingPage
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingType
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingButton(
    onBoardingPages: OnBoardingPage,
    pagerState: PagerState,
    onBoardingPagesNum: Int,

    onFinish: () -> Unit,
) {
    val context = LocalContext.current
    val scrollScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        if (onBoardingPages.getType(context) == OnBoardingType.Quote) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                            scrollScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(20),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "No",
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        scrollScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(20),
                    colors = ButtonDefaults.buttonColors( MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Yes",
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
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
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
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