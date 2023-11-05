@file:OptIn(ExperimentalPagerApi::class)

package com.sginnovations.asked.ui.ui_components.camera

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.sginnovations.asked.Constants
import com.sginnovations.asked.ui.ui_components.tokens.TokenIcon
import kotlinx.coroutines.launch

@Composable
fun CameraCarousel(
    onChangeCategory: (String) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0)
    val sliderList = listOf(Constants.CAMERA_TEXT, Constants.CAMERA_MATH)
    val scope = rememberCoroutineScope()

    Row {
        HorizontalPager(
            count = sliderList.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 144.dp),
            modifier = Modifier.height(48.dp)
        ) { item ->
            val isSelected = pagerState.currentPage == item
            val targetAlpha = if (isSelected) 1f else 0.5f
            val targetScale = if (isSelected) 1f else 0.8f

            onChangeCategory(sliderList[pagerState.currentPage])

            val alpha by animateFloatAsState(targetAlpha)
            val scale by animateFloatAsState(targetScale)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = sliderList[item],
                    modifier = Modifier
                        .graphicsLayer {
                            this.alpha = alpha
                            this.scaleX = scale
                            this.scaleY = scale
                        }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    scope.launch { pagerState.animateScrollToPage(item) }
                                }
                            )
                        },
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(0f, 0f),
                            blurRadius = 8f
                        ),
                        fontSize = 20.sp
                    )
                )
                AnimatedVisibility(
                    visible = isSelected,
                    enter = slideInVertically(initialOffsetY = { -40 }) + expandVertically() + fadeIn(
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutVertically(targetOffsetY = { -40 }) + shrinkVertically() + fadeOut(),
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Free",
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(0f, 0f),
                                    blurRadius = 8f
                                ),
                                fontSize = 8.sp
                            ),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        TokenIcon()
                    }
                }

            }
        }
    }
}