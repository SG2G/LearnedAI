package com.sginnovations.learnedai.ui.camera.crop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.sginnovations.learnedai.ui.navigation.NewConversation
import com.sginnovations.learnedai.viewmodel.CameraViewModel
import com.sginnovations.learnedai.viewmodel.ChatViewModel
import io.moyuru.cropify.Cropify
import io.moyuru.cropify.CropifyOption
import io.moyuru.cropify.rememberCropifyState

@Composable
fun CropStateFul(
    vmCamera: CameraViewModel,
    vmChat: ChatViewModel,

    navController: NavController,
) {
    val photoImageBitmap = vmCamera.photoImageBitmap

    CropStateLess(
        vmCamera = vmCamera,
        vmChat = vmChat,

        photoImageBitmap = photoImageBitmap,

        navController = navController,
    )
}

@Composable
fun CropStateLess(
    vmCamera: CameraViewModel,
    vmChat: ChatViewModel,

    photoImageBitmap: MutableState<ImageBitmap>,

    navController: NavController,
) {

    val cropifyOptions = CropifyOption(
        backgroundColor = MaterialTheme.colorScheme.background,
        gridColor = Color.Transparent,
    )

    val cropifyState = rememberCropifyState()
    var cropifyOption by remember { mutableStateOf(cropifyOptions) }
    var croppedImage by remember { mutableStateOf<ImageBitmap?>(null) }


    croppedImage?.let {
        //TextPreviewDialog(vmCamera = vmCamera, onDismissRequest = { croppedImage = null })
        setUpNewConversation(vmChat)

        vmCamera.getTextFromImage(it)

        if (navController.currentDestination?.route != NewConversation.route) {
            navController.navigate(NewConversation.route)
        }
    }

    Cropify(
        bitmap = photoImageBitmap.value,
        state = cropifyState,
        option = cropifyOption,
        onImageCropped = { croppedImage = it },
        modifier = Modifier
            .fillMaxSize()
    )
    Column {
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

private fun setUpNewConversation(vmChat: ChatViewModel) {
    vmChat.idConversation.intValue = 0
    vmChat.isNewConversation.value = true
}

@Composable
fun TextPreviewDialog(vmCamera: CameraViewModel, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Text(text = vmCamera.imageText.value)
    }
}