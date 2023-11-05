package com.sginnovations.asked.ui.main_bottom_bar.camera

import android.graphics.Bitmap
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sginnovations.asked.ui.subscription.SubscriptionStateFull
import com.sginnovations.asked.ui.top_bottom_bar.Gallery
import com.sginnovations.asked.ui.ui_components.camera.CameraCarousel
import com.sginnovations.asked.ui.ui_components.camera.CameraPreview
import com.sginnovations.asked.ui.ui_components.camera.PhotoButton
import com.sginnovations.asked.ui.ui_components.tokens.TokenDisplay
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel

private const val TAG = "CameraStateFul"

@Composable
fun CameraStateFul(
    navController: NavHostController,

    vmCamera: CameraViewModel,
    vmToken: TokenViewModel,

    onCropNavigation: () -> Unit,
    onNavigateNewConversation: () -> Unit,
) {
    /// Check Camera Perms ///
    val cameraPermissionGranted = remember { mutableStateOf(false) }

    CheckPermissions(
        permsAsked = android.Manifest.permission.CAMERA,
        onPermissionGranted = {
            cameraPermissionGranted.value = true
        }
    )

    if (cameraPermissionGranted.value) {
        CameraStateLess(
            vmToken = vmToken,

            onGetPhotoGallery = {
                navController.navigate(route = Gallery.route) //TODO SUVBELO
            },

            onPhotoTaken = { bitmap ->
                vmCamera.onTakePhoto(bitmap)
                onCropNavigation()
            },
            onNavigateNewConversation = { onNavigateNewConversation() },
            onChangeCategory = { category ->
                vmCamera.cameraCategory.value = category
            }
        )
    }

}

@Composable
fun CameraStateLess(
    vmToken: TokenViewModel,

    onGetPhotoGallery: () -> Unit,
    onPhotoTaken: (Bitmap) -> Unit,
    onNavigateNewConversation: () -> Unit,

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

    val tokens = vmToken.tokens.collectAsStateWithLifecycle()

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
                .fillMaxSize()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End,
        ) {
            TokenDisplay(tokens = tokens, showPlus = true) { vmToken.switchPointsVisibility() }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(bottom = 0.dp, start = 16.dp, end = 16.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CameraCarousel(
                    onChangeCategory = {
                        onChangeCategory(it)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onGetPhotoGallery() }) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoLibrary,
                            contentDescription = "PhotoLibrary",
                            modifier = Modifier.size(38.dp)
                        )
                    }
                    PhotoButton(
                        context = context,
                        controller = controller

                    ) { onPhotoTaken(it) }
                    IconButton(onClick = { onNavigateNewConversation() }) {
                        Icon(
                            imageVector = Icons.Outlined.Keyboard,
                            contentDescription = "Keyboard",
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }

            }
        }
    }
}
