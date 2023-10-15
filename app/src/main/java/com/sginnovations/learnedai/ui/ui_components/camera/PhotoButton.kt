package com.sginnovations.learnedai.ui.ui_components.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Camera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun PhotoButton(
    context: Context,
    controller: LifecycleCameraController,

    onPhotoTaken: (Bitmap) -> Unit,
) {
    IconButton(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .size(70.dp)
            .border(4.dp, Color.White, CircleShape),
        onClick = {
            takePhoto(
                context = context,
                controller = controller,
                onPhotoTaken = { onPhotoTaken(it) }
            )
        }
    ) {
        Icon(
            imageVector = Icons.Sharp.Camera,
            contentDescription = "Take photo",
            tint = Color.Transparent,
            modifier = Modifier
                .size(55.dp)
                .border(4.dp, Color.White, CircleShape)
        )
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