package com.sginnovations.asked.ui.crop

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.sginnovations.asked.ui.crop.components.RotatingText
import com.sginnovations.asked.utils.NetworkUtils
import com.sginnovations.asked.viewmodel.AdsViewModel
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel
import io.moyuru.cropify.Cropify
import io.moyuru.cropify.CropifyOption
import io.moyuru.cropify.rememberCropifyState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private const val TAG = "CropStateFul"

@Composable
fun CropStateFul(
    vmCamera: CameraViewModel,
    vmChat: ChatViewModel,
    vmAds: AdsViewModel,
    vmToken: TokenViewModel,

    navController: NavController,

    onNavigateChat: () -> Unit,
    onNavigateNewChat: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val photoImageBitmap = vmCamera.photoImageBitmap
    val cameraCategory = vmCamera.cameraOCRCategory
    val isLoading = vmCamera.isLoading

    val text = remember { mutableStateOf("") }
    text.value = vmCamera.imageToText.value

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
            Column(
                modifier = Modifier.padding(72.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Color.DarkGray.copy(alpha = 0.4f),
                                RoundedCornerShape(15.dp)
                            )
                    )
                    Column(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        RotatingText()
                    }
                }

            }

        }
    }

    LaunchedEffect(cameraCategory.value) {
        when (cameraCategory.value) {
            Text.root -> instantCrop.value = false
            Math.root -> instantCrop.value = true

            else -> instantCrop.value = false
        }
    }

    /**
     *
     */
    CropStateLess(
        photoImageBitmap = photoImageBitmap,
        navController = navController,

        onImageCropped = { croppedImage -> //TODO CROP VIEWMODEL
            // Delete the text
            vmCamera.imageToText.value = ""
            text.value = ""

            Log.d(TAG, "CropStateFul start: ${text.value}")

            getTextFromCroppedImage(
                vmCamera,
                vmChat,
                vmToken,
                croppedImage,
                cameraCategory,

                onNavigateConversation = {
                    if (!instantCrop.value) {
                        onNavigateNewChat()
                    }
                }
            )

            if (instantCrop.value) {
                scope.launch {
                    Log.d(TAG, "CropStateFul 1: ${text.value}")
                    while (text.value == "") delay(200)
                    Log.d(TAG, "CropStateFul 2: ${text.value}")
                    sendNewMessage(
                        context,
                        activity,
                        text.value,
                        vmCamera,
                        vmAds,
                        vmChat
                    ) {
                        onNavigateChat()
                    }
                }
            }
        },
    )
}

@Composable
fun CropStateLess(
    photoImageBitmap: MutableState<ImageBitmap>,
    navController: NavController,

    onImageCropped: (ImageBitmap) -> Unit,
) {
    val cropifyState = rememberCropifyState()
    val scope = rememberCoroutineScope()

    val cropifyOptions = CropifyOption(
        backgroundColor = Color(0xFF191c22),
        maskColor = Color(0xFF191c22),
        gridColor = Color.Transparent,
        frameColor = MaterialTheme.colorScheme.primary,
        frameAlpha = 0f,
        frameWidth = 6.dp
    )

    val cropifyOption by remember { mutableStateOf(cropifyOptions) }
    var croppedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    suspend fun cropImage() {
        Log.d(TAG, "cropImage, starting loop until not null")

        runBlocking {
            val croppedImageDeferred = async {
                while (croppedImage == null) {
                    delay(200)
                }
                croppedImage!!
            }

            try {
                onImageCropped(croppedImageDeferred.await())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Crop
     */
    Cropify(
        bitmap = photoImageBitmap.value,
        state = cropifyState,
        option = cropifyOption,
        onImageCropped = {
            scope.launch {
                croppedImage = it
                cropImage()
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    /**
     * UI Layout
     */
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
            shape = RoundedCornerShape(25.dp),
        ) {
            Text(
                text = stringResource(R.string.crop_retake),
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Button(
            onClick = { cropifyState.crop() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(
                text = stringResource(R.string.crop_crop),
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

/**
 * CAFDWBAHIUF
 */
suspend fun sendNewMessage(
    context: Context,
    activity: Activity?,
    text: String,
    vmCamera: CameraViewModel,
    vmAds: AdsViewModel,
    vmChat: ChatViewModel,

    onNavigateChat: () -> Unit,
) {
    withContext(Dispatchers.IO) {
        // New Message
        if (NetworkUtils.isOnline(context)) {
            vmCamera.isLoading.value = true
            // Show ad
            if (activity != null) {
                vmAds.showInterstitialAd(activity)
            }

            // GPT call
            val deferred = async { vmChat.sendMessageToOpenaiApi(text) }
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
    cameraCategory: MutableState<String>,

    onNavigateConversation: () -> Unit,
) {
    when (cameraCategory.value) {
        CAMERA_TEXT -> {
            Log.d(TAG, "CropStateLess: CAMERA_TEXT")
            vmChat.setUpNewConversation()

            vmCamera.cameraOCRCategory.value = Text.root //TODO LIADA CATEDRALICIA
            vmChat.category.value = Text.root

            vmCamera.getTextFromImage(croppedImage)

            onNavigateConversation()
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

            onNavigateConversation()
        }
    }
}

