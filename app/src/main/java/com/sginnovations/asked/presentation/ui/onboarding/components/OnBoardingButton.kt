package com.sginnovations.asked.presentation.ui.onboarding.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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

    btnEnable: MutableState<Boolean>,

    onFinish: () -> Unit,
) {
    val context = LocalContext.current
    val scrollScope = rememberCoroutineScope()

    if (onBoardingPages.getType(context) == OnBoardingType.Quote) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = {
                    scrollScope.launch {
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage + 1,
                            animationSpec = tween(1000)
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(124.dp),
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(
                    Color.White
                ),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = stringResource(R.string.no),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    scrollScope.launch {
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage + 1,
                            animationSpec = tween(1000)
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(124.dp),
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(
                    Color.White
                ),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column {
                    Icon(
                        imageVector = Icons.Rounded.Done,
                        contentDescription = null,
                        tint = Color(0xFF469C29),
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = stringResource(R.string.yes),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    } else {
        Button(
            onClick = {
                if (btnEnable.value) {
                    when (pagerState.currentPage) {
                        onBoardingPagesNum - 1 -> onFinish()
                        else -> scrollScope.launch {
                            pagerState.animateScrollToPage(
                                page = pagerState.currentPage + 1,
                                animationSpec = tween(1000)
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20),
            colors = ButtonDefaults.buttonColors(
                if (btnEnable.value) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.LightGray
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