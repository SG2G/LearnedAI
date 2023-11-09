package com.sginnovations.asked.ui.ui_components.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun PhotoButton(
    modifier: Modifier = Modifier,
    context: Context,
    controller: LifecycleCameraController,

    isSelected: Boolean,

    onChangeIcon: () -> Unit,

    onPhotoTaken: (Bitmap) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed && isSelected) 0.90f else 1f)

    Box(
        modifier = modifier
    ) {
        IconButton(
            modifier = modifier
                .padding(bottom = 16.dp)
                .size(70.dp)
                .border(4.dp, Color.White, CircleShape)
                .graphicsLayer {
                    shape = CircleShape
                    clip = true
                    scaleX = scale
                    scaleY = scale
                },
            onClick = {
                if (isSelected) {
                    takePhoto(
                        context = context,
                        controller = controller,
                        onPhotoTaken = { onPhotoTaken(it) }
                    )
                } else {
                    onChangeIcon()
                }
            },
            interactionSource = interactionSource
        ) {
            Icon(
                imageVector = Icons.Sharp.Circle,
                contentDescription = "Take photo",
                modifier = Modifier
                    .size(65.dp)
                    .alpha(if (!isSelected) 0.6f else 1f)
            )
        }
    }

}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
) {
    val desiredHeight = 700
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val originalBitmap = image.toBitmap()

                val startY = (originalBitmap.height - desiredHeight) / 2

                val croppedBitmap = Bitmap.createBitmap(
                    originalBitmap,
                    0,
                    startY,
                    originalBitmap.width,
                    desiredHeight
                )

                val rotatedBitmap = Bitmap.createBitmap(
                    croppedBitmap,
                    0,
                    0,
                    croppedBitmap.width,
                    croppedBitmap.height,
                    matrix,
                    true
                )

                onPhotoTaken(rotatedBitmap)
            }
        }
    )
}