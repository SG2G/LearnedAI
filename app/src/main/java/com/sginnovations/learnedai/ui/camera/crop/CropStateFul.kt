package com.sginnovations.learnedai.ui.camera.crop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.sginnovations.learnedai.ui.navigation.Chat
import com.sginnovations.learnedai.ui.navigation.NewConversation
import com.sginnovations.learnedai.viewmodel.CameraViewModel
import io.moyuru.cropify.Cropify
import io.moyuru.cropify.CropifyOption
import io.moyuru.cropify.rememberCropifyState

@Composable
fun CropStateFul(
    vmCamera: CameraViewModel,

    navController: NavController,
) {
    val photoImageBitmap = vmCamera.photoImageBitmap

    CropStateLess(
        vmCamera = vmCamera,

        photoImageBitmap = photoImageBitmap,

        navController = navController,
    )
}

@Composable
fun CropStateLess(
    vmCamera: CameraViewModel,
    photoImageBitmap: MutableState<ImageBitmap>,

    navController: NavController,
) {
    val cropifyState = rememberCropifyState()
    var cropifyOption by remember { mutableStateOf(CropifyOption()) }
    var croppedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    croppedImage?.let {
        vmCamera.getTextFromImage(it)
        navController.navigate(NewConversation.route) {
            popUpTo(Chat.route) {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Cropify(
            bitmap = photoImageBitmap.value,
            state = cropifyState,
            option = cropifyOption,
            onImageCropped = { croppedImage = it },
            modifier = Modifier
                .fillMaxSize()
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom,
        ) {
            Button(
                onClick = { navController.navigateUp() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(text = "Retake")
            }
            Text(text = vmCamera.imageText.value, color = MaterialTheme.colorScheme.onBackground)
            Button(
                onClick = { cropifyState.crop() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(text = "Crop")
            }
        }
    }
}