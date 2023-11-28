@file:OptIn(ExperimentalPagerApi::class)

package com.sginnovations.asked.ui.ui_components.camera

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.view.LifecycleCameraController
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.sginnovations.asked.data.CategoryOCR
import com.sginnovations.asked.data.GrammarCategoryOCR
import com.sginnovations.asked.data.MathCategoryOCR
import com.sginnovations.asked.data.SummaryCategoryOCR
import com.sginnovations.asked.data.TextCategoryOCR
import com.sginnovations.asked.data.TranslateCategoryOCR
import com.sginnovations.asked.ui.ui_components.tokens.TokenIcon
import com.sginnovations.asked.viewmodel.TokenViewModel
import kotlinx.coroutines.launch

private const val TAG = "sliderList"
@Composable
fun CameraCarousel(
    modifier: Modifier = Modifier,

    vmToken: TokenViewModel,
    controller: LifecycleCameraController,

    onChangeCategory: (CategoryOCR) -> Unit,

    onPhotoTaken: (Bitmap) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = 3)

    val sliderList = listOf(GrammarCategoryOCR,SummaryCategoryOCR,TranslateCategoryOCR, TextCategoryOCR, MathCategoryOCR)

    HorizontalPager(
        count = sliderList.size,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 64.dp),
        modifier = modifier
    ) { item ->
        val isSelected = pagerState.currentPage == item
        val targetAlpha = if (isSelected) 1f else 0.5f
        val targetScale = if (isSelected) 1f else 0.8f
        val alpha by animateFloatAsState(targetAlpha)
        val scale by animateFloatAsState(targetScale)

        Log.d(TAG, "sliderList: ${sliderList[pagerState.currentPage]}")
        onChangeCategory(sliderList[pagerState.currentPage])

        val mathCostToken = vmToken.getCameraMathTokens()

        // Tokens Cost Subtitle
        val tokenCost = when (sliderList[item].root) {
            TextCategoryOCR.root -> "Free"
            MathCategoryOCR.root ->
                if (mathCostToken == "0") {
                    "Free"
                } else {
                    mathCostToken
                }

            else -> {
                "Free"
            }
        }

        Column(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        scope.launch { pagerState.animateScrollToPage(item) }
                    }
                )
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = sliderList[item].getName(context),
                modifier = Modifier
                    .graphicsLayer {
                        this.alpha = alpha
                        this.scaleX = scale
                        this.scaleY = scale
                    },
                style = MaterialTheme.typography.titleSmall
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
                        text = tokenCost,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TokenIcon()
                }
            }

            PhotoButton(
                modifier = Modifier.scale(if (isSelected) 1f else 0.8f),
                categoryOCR = sliderList[item],
                controller = controller,

                isSelected = isSelected,

                onChangeIcon = { scope.launch { pagerState.animateScrollToPage(item) } },
                ) {
                if (isSelected) {
                    onPhotoTaken(it)
                }
            }

        }
    }
}