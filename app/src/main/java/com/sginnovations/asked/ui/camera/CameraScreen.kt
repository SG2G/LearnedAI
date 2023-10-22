@file:OptIn(ExperimentalPagerApi::class)

package com.sginnovations.asked.ui.camera

import android.graphics.Bitmap
import androidx.camera.view.CameraController
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.sginnovations.asked.Constants.Companion.CAMERA_MATH
import com.sginnovations.asked.Constants.Companion.CAMERA_TEXT
import com.sginnovations.asked.ui.ui_components.camera.CameraPreview
import com.sginnovations.asked.ui.ui_components.camera.PhotoButton
import com.sginnovations.asked.ui.ui_components.points.TokenIcon
import com.sginnovations.asked.viewmodel.CameraViewModel
import kotlinx.coroutines.launch

private const val TAG = "CameraStateFul"

@Composable
fun CameraStateFul(
    vmCamera: CameraViewModel,

    onCropNavigation: () -> Unit,
) {
    /// Check Camera Perms ///
    val cameraPermissionGranted = remember { mutableStateOf(false) }
    CameraCheckPermissions(
        onPermissionGranted = {
            cameraPermissionGranted.value = true
        }
    )

    if (cameraPermissionGranted.value) {
        CameraStateLess(

            onPhotoTaken = { bitmap ->
                vmCamera.onTakePhoto(bitmap)
                onCropNavigation()
            },
            onChangeCategory = { category ->
                vmCamera.cameraCategory.value = category
            }
        )
    }

}

@Composable
fun CameraStateLess(
    onPhotoTaken: (Bitmap) -> Unit,

    onChangeCategory: (String) -> Unit,
) {
    val context = LocalContext.current

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Component - Camera Preview
        CameraPreview(
            controller = controller,
            modifier = Modifier
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 0.dp, start = 16.dp, end = 16.dp, top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Carousel(
                    onChangeCategory = {
                        onChangeCategory(it)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PhotoButton(
                    context = context,
                    controller = controller

                ) { onPhotoTaken(it) }
            }
        }
    }
}

@Composable
fun Carousel(
    onChangeCategory: (String) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0)
    val sliderList = listOf(CAMERA_TEXT, CAMERA_MATH)
    val scope = rememberCoroutineScope()

    Row {
        HorizontalPager(
            count = sliderList.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 144.dp),
            modifier = Modifier.height(38.dp)
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
                    enter = slideInVertically(initialOffsetY = { -40 }) + expandVertically() + fadeIn(initialAlpha = 0.3f),
                    exit = slideOutVertically(targetOffsetY = { -40 }) + shrinkVertically() + fadeOut()
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
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        TokenIcon()
                    }
                }

            }
        }
    }
}



