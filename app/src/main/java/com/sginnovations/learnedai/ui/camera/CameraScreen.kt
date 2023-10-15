package com.sginnovations.learnedai.ui.camera

import android.graphics.Bitmap
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sginnovations.learnedai.ui.ui_components.camera.CameraPreview
import com.sginnovations.learnedai.ui.ui_components.camera.PhotoButton
import com.sginnovations.learnedai.viewmodel.CameraViewModel

private const val TAG = "CameraStateFul"

@Composable
fun CameraStateFul(
    vmCamera: CameraViewModel,

    onCropNavigation: () -> Unit,
) {
    /// Check Camera Perms ///
    val cameraPermissionGranted = remember { mutableStateOf(false) }
    CameraCheckPermissions(onPermissionGranted = {
        cameraPermissionGranted.value = true
    }) //TODO RARETE


    if (cameraPermissionGranted.value) {
        CameraStateLess(

            onPhotoTaken = { bitmap ->
                vmCamera.onTakePhoto(bitmap)
                onCropNavigation()
            }
        )
    }

}

@Composable
fun CameraStateLess(
    onPhotoTaken: (Bitmap) -> Unit,
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
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            PhotoButton(
                context = context,
                controller = controller

            ) { onPhotoTaken(it) }
        }
    }
}

