@file:OptIn(ExperimentalPagerApi::class)

package com.sginnovations.asked.presentation.ui.main_bottom_bar.camera.components.carousel

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
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
import com.sginnovations.asked.presentation.ui.ui_components.camera.PremiumCameraDialog
import com.sginnovations.asked.utils.CheckIsPremium
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "sliderList"

@Composable
fun CameraCarousel(
    modifier: Modifier = Modifier,

    controller: LifecycleCameraController,

    onChangeCategory: (CategoryOCR) -> Unit,

    onNavigateSubscriptionScreen: () -> Unit,

    onPhotoTaken: (Bitmap) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = 3)

    var isPremium by remember { mutableStateOf(false) }

    val showPremiumCameraDialog = remember { mutableStateOf(false) }

    val sliderList = listOf(
        SummaryCategoryOCR,
        GrammarCategoryOCR,
        TranslateCategoryOCR,
        TextCategoryOCR,
        MathCategoryOCR
    )

    LaunchedEffect(Unit) {
        isPremium = scope.async { CheckIsPremium.checkIsPremium() }.await()
    }

    HorizontalPager(
        count = sliderList.size,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 64.dp),
        modifier = modifier
    ) { item ->
        val isSelected = pagerState.currentPage == item

        Log.d(TAG, "sliderList: ${sliderList[pagerState.currentPage]}")
        onChangeCategory(sliderList[pagerState.currentPage])

        val verticalOffset = if (isSelected) 0.dp else 18.dp

        Column(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        scope.launch { pagerState.animateScrollToPage(item) }
                    }
                )
            },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = sliderList[item].getName(context),
                modifier = Modifier
                    .offset(y = verticalOffset)
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            // ref 1
            PhotoButton(
                modifier = Modifier.scale(if (isSelected) 1f else 0.8f),
                categoryOCR = sliderList[item],
                controller = controller,

                isSelected = isSelected,

                onChangeIcon = { scope.launch { pagerState.animateScrollToPage(item) } },
            ) {
                if (isSelected) {
                    if (isPremium) {
                        onPhotoTaken(it)
                    } else {
                        when (sliderList[pagerState.currentPage].prefix) {
                            TranslateCategoryOCR.prefix -> showPremiumCameraDialog.value = true
                            GrammarCategoryOCR.prefix -> showPremiumCameraDialog.value = true
                            SummaryCategoryOCR.prefix -> showPremiumCameraDialog.value = true
                            else -> {
                                onPhotoTaken(it)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showPremiumCameraDialog.value) {
        PremiumCameraDialog(
            onDismissRequest = { showPremiumCameraDialog.value = false },
            onSeePremiumSubscription = {
                showPremiumCameraDialog.value = false
                onNavigateSubscriptionScreen()
            }
        )
    }
}