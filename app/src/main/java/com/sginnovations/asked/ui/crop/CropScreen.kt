package com.sginnovations.asked.ui.crop

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.sginnovations.asked.Constants.Companion.CAMERA_MATH
import com.sginnovations.asked.Constants.Companion.CAMERA_TEXT
import com.sginnovations.asked.R
import com.sginnovations.asked.data.Math
import com.sginnovations.asked.data.Text
import com.sginnovations.asked.ChatsHistory
import com.sginnovations.asked.NewConversation
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel
import io.moyuru.cropify.Cropify
import io.moyuru.cropify.CropifyOption
import io.moyuru.cropify.rememberCropifyState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "CropStateFul"

@Composable
fun CropStateFul(
    vmCamera: CameraViewModel,
    vmChat: ChatViewModel,
    vmToken: TokenViewModel,

    navController: NavController,
) {
    val photoImageBitmap = vmCamera.photoImageBitmap
    val cameraOption = vmCamera.cameraCategory

    CropStateLess(
        onImageCropped = { croppedImage ->
            getTextFromCroppedImage(vmCamera, vmChat,vmToken, croppedImage, navController, cameraOption)
        },

        photoImageBitmap = photoImageBitmap,

        navController = navController,
    )
}

@Composable
fun CropStateLess(
    onImageCropped: (ImageBitmap) -> Unit,

    photoImageBitmap: MutableState<ImageBitmap>,

    navController: NavController,
) {
    val cropifyOptions = CropifyOption(
        backgroundColor = MaterialTheme.colorScheme.background,
        gridColor = Color.Transparent,
    )

    val cropifyState = rememberCropifyState()
    val scope = rememberCoroutineScope()

    val cropifyOption by remember { mutableStateOf(cropifyOptions) }
    var croppedImage by remember { mutableStateOf<ImageBitmap?>(null) }


    // Variable whit the cameraOptionSelect

    suspend fun cropImage(imageCropped: ImageBitmap?) {
        croppedImage = imageCropped

        while (croppedImage == null) { delay(250) }

        try {
            onImageCropped(croppedImage!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Cropify(
        bitmap = photoImageBitmap.value,
        state = cropifyState,
        option = cropifyOption,
        onImageCropped = {
            scope.launch {
                cropImage(it)
            }
        },
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
            ),
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(
                text = stringResource(R.string.crop_retake), modifier = Modifier.padding(4.dp),
                style = TextStyle(
                    fontSize = 16.sp
                )
            )
        }
        Button(
            onClick = { cropifyState.crop() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = stringResource(R.string.crop_crop), modifier = Modifier.padding(4.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

fun getTextFromCroppedImage(
    vmCamera: CameraViewModel,
    vmChat: ChatViewModel,
    vmToken: TokenViewModel,

    croppedImage: ImageBitmap,

    navController: NavController,
    cameraOption: MutableState<String>,
) {
    when (cameraOption.value) {
        CAMERA_TEXT -> {
            Log.d(TAG, "CropStateLess: CAMERA_TEXT")
            vmChat.setUpNewConversation()

            vmCamera.cameraCategory.value = Text.name
            vmCamera.getTextFromImage(croppedImage)

            if (navController.currentDestination?.route != NewConversation.route) {
                Log.i(TAG, "Starting nav")

                navController.navigate(ChatsHistory.route) {
                    // This ensures that the previous screen is removed from the backstack
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
                navController.navigate(NewConversation.route)
            }
        }

        CAMERA_MATH -> {
            Log.d(TAG, "CropStateLess: CAMERA_MATH")
            vmChat.setUpNewConversation()

            vmCamera.cameraCategory.value = Math.name
            vmCamera.getMathFromImage(croppedImage)

            val cameraCostTokens = vmToken.getCameraMathTokens()
            vmToken.lessToken(cameraCostTokens.toInt())

            if (navController.currentDestination?.route != NewConversation.route) {
                Log.i(TAG, "Starting nav")

                navController.navigate(ChatsHistory.route) {
                    // This ensures that the previous screen is removed from the backstack
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
                navController.navigate(NewConversation.route)
            }
        }
    }
}

@Composable
fun TextPreviewDialog(vmCamera: CameraViewModel, onDismissRequest: () -> Unit) { // TODO DELETE
    Dialog(onDismissRequest = onDismissRequest) { //TODO TRANSLATE
        Text(text = "")
    }
}