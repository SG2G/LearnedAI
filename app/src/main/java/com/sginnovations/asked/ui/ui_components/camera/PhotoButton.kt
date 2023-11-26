package com.sginnovations.asked.ui.ui_components.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.sginnovations.asked.R
import com.sginnovations.asked.data.Category
import com.sginnovations.asked.data.Math
import com.sginnovations.asked.data.Text

@Composable
fun PhotoButton(
    modifier: Modifier = Modifier,
    category: Category,
    controller: LifecycleCameraController,

    isSelected: Boolean,

    onChangeIcon: () -> Unit,

    onPhotoTaken: (Bitmap) -> Unit,
) {
    val context = LocalContext.current

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
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Sharp.Circle,
                    contentDescription = "Outer Icon",
                    modifier = Modifier
                        .size(65.dp)
                        .alpha(if (!isSelected) 0.6f else 1f)
                )
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .alpha(if (!isSelected) 0.8f else 1f),
                    painter = when (category.root) {
                        Text.root -> painterResource(id = R.drawable.text_camera)
                        Math.root -> painterResource(id = R.drawable.math_camera)
                        else -> painterResource(id = R.drawable.text_camera)
                    },
                    contentDescription = "Inner Icon",
                    tint = Color.LightGray

                )
            }
        }
    }

}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
) {


    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val originalBitmap = image.toBitmap()

                // Set the aspect ratio to 9:16
                val aspectRatio = 16.0 / 9.0

                // Calculate the width and height based on the original bitmap's dimensions and the aspect ratio
                val width: Int
                val height: Int
                if (originalBitmap.width < originalBitmap.height * aspectRatio) {
                    width = originalBitmap.width
                    height = (width / aspectRatio).toInt()
                } else {
                    height = originalBitmap.height
                    width = (height * aspectRatio).toInt()
                }

                // Calculate the starting position for the crop
                val startX = (originalBitmap.width - width) / 2
                var startY = (originalBitmap.height - height) / 2

                // Ensure startY + height does not exceed the original bitmap's height
                if (startY + height > originalBitmap.height) {
                    startY = originalBitmap.height - height
                }

                val croppedBitmap = Bitmap.createBitmap(
                    originalBitmap,
                    startX,
                    startY,
                    width,
                    height
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