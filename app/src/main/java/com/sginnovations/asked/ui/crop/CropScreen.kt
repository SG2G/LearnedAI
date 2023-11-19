package com.sginnovations.asked.ui.crop

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.sginnovations.asked.Constants.Companion.CAMERA_MATH
import com.sginnovations.asked.Constants.Companion.CAMERA_TEXT
import com.sginnovations.asked.R
import com.sginnovations.asked.data.Math
import com.sginnovations.asked.data.Text
import com.sginnovations.asked.ChatsHistory
import com.sginnovations.asked.NewConversation
import com.sginnovations.asked.utils.NetworkUtils
import com.sginnovations.asked.viewmodel.AdsViewModel
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel
import io.moyuru.cropify.AspectRatio
import io.moyuru.cropify.Cropify
import io.moyuru.cropify.CropifyOption
import io.moyuru.cropify.rememberCropifyState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "CropStateFul"

@Composable
fun CropStateFul(
    vmCamera: CameraViewModel,
    vmChat: ChatViewModel,
    vmAds: AdsViewModel,
    vmToken: TokenViewModel,

    navController: NavController,

    onNavigateChat: () -> Unit,
) {
    val photoImageBitmap = vmCamera.photoImageBitmap
    val cameraOption = vmCamera.cameraOCRCategory

    // too much
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val text = remember { mutableStateOf("") }
    text.value = vmCamera.imageToText.value

    val prefixPrompt = vmChat.prefixPrompt.value
    val isLoading = vmCamera.isLoading

    val instantCrop = remember { mutableStateOf(false) }

    fun Context.getActivity(): Activity? {
        return when (this) {
            is Activity -> this
            is ContextWrapper -> baseContext.getActivity()
            else -> null
        }
    }

    val activity = context.getActivity()

    if (isLoading.value) {
        Log.d(TAG, "CircularProgressIndicator")
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    CropStateLess(
        onImageCropped = { croppedImage ->
            getTextFromCroppedImage(
                vmCamera,
                vmChat,
                vmToken,
                croppedImage,
                instantCrop.value,
                navController,
                cameraOption
            )

            if (instantCrop.value) {
                scope.launch {
                    while (text.value == "") delay(250)
                    sendNewMessage(
                        scope,
                        context,
                        activity,
                        text.value,
                        prefixPrompt,
                        vmCamera,
                        vmAds,
                        vmChat
                    ) {
                        onNavigateChat()
                    }
                }
            }
        },
        instantCrop = instantCrop,
        photoImageBitmap = photoImageBitmap,

        navController = navController,
    )
}

@Composable
fun CropStateLess(
    onImageCropped: (ImageBitmap) -> Unit,

    instantCrop: MutableState<Boolean>,
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

    suspend fun cropImage(imageCropped: ImageBitmap?) {
        croppedImage = imageCropped

        Log.d(TAG, "cropImage, starting loop until not null")
        while (croppedImage == null) {
            delay(250)
        }
        Log.d(TAG, "cropImage ->> $croppedImage")

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
        /**
         * Buttons
         */
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
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Button(
            onClick = {
                instantCrop.value = false
                cropifyState.crop()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = stringResource(R.string.crop_crop), modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.headlineMedium
            )
        }
//        Button(
//            onClick = {
//                instantCrop.value = true
//                cropifyState.crop()
//            },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
//            ),
//            shape = RoundedCornerShape(10.dp)
//        ) {
//            Text(
//                text = stringResource(R.string.crop_fast_crop), modifier = Modifier.padding(4.dp),
//                style = MaterialTheme.typography.headlineMedium
//            )
//        }

    }
}

/**
 * CAFDWBAHIUF
 */
fun sendNewMessage(
    scope: CoroutineScope,
    context: Context,
    activity: Activity?,
    text: String,
    prefixPrompt: String,
    vmCamera: CameraViewModel,
    vmAds: AdsViewModel,
    vmChat: ChatViewModel,

    onNavigateChat: () -> Unit,
) {
    scope.launch {
        // New Message
        if (NetworkUtils.isOnline(context)) {
            vmCamera.isLoading.value = true
            // Show ad
            if (activity != null) {
                vmAds.showInterstitialAd(activity)
            }

            Log.d(TAG, "invoke: text-> $text prefixPrompt-> $prefixPrompt")
            // GPT call
            val deferred =
                async { vmChat.sendMessageToOpenaiApi("$prefixPrompt $text") }
            deferred.await()

            // Token cost of the call
            try {
                vmChat.lessTokenNewConversationCheckPremium()
            } catch (e: Exception) {
                // NewConversation tokens cost failed
                e.printStackTrace()
            }

            vmCamera.isLoading.value = false

            onNavigateChat()
        } else {
            Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show()
        }
    }
}

fun getTextFromCroppedImage(
    vmCamera: CameraViewModel,
    vmChat: ChatViewModel,
    vmToken: TokenViewModel,

    croppedImage: ImageBitmap,
    instantCrop: Boolean,

    navController: NavController,
    cameraOption: MutableState<String>,
) {

    when (cameraOption.value) {
        CAMERA_TEXT -> {
            Log.d(TAG, "CropStateLess: CAMERA_TEXT")
            vmChat.setUpNewConversation()

            vmCamera.cameraOCRCategory.value = Text.root //TODO LIADA CATEDRALICIA
            vmChat.category.value = Text.root

            vmCamera.getTextFromImage(croppedImage)

            if (!instantCrop) {
                navigateNewConversation(navController)
            }
        }

        CAMERA_MATH -> {
            Log.d(TAG, "CropStateLess: CAMERA_MATH")
            vmChat.setUpNewConversation()

            vmCamera.cameraOCRCategory.value = Math.root //TODO LIADA CATEDRALICIA PARTE 2
            vmChat.category.value = Math.root

            vmCamera.getMathFromImage(croppedImage)

            try {
                val cameraCostTokens = vmToken.getCameraMathTokens()
                vmToken.lessTokenCheckPremium(cameraCostTokens.toInt())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (!instantCrop) {
                navigateNewConversation(navController)
            }
        }
    }
}

fun navigateNewConversation(navController: NavController) {
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