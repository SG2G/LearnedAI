package com.sginnovations.asked.ui.crop

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sginnovations.asked.R
import com.sginnovations.asked.data.CategoryOCR
import com.sginnovations.asked.data.GrammarCategoryOCR
import com.sginnovations.asked.data.MathCategoryOCR
import com.sginnovations.asked.data.SummaryCategoryOCR
import com.sginnovations.asked.data.TextCategoryOCR
import com.sginnovations.asked.data.TranslateCategoryOCR
import com.sginnovations.asked.ui.crop.components.IsLoadingCrop
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
    val cameraCategoryOCR = vmCamera.cameraCategoryOCR
    val isLoading = vmCamera.isLoading

    val text = remember { mutableStateOf("") }
    text.value = vmCamera.imageToText.value ?: ""

    val instantCrop = remember { mutableStateOf(false) }

//    val retakeBtnEnabled = remember { mutableStateOf(true) }
//    val cropBtnEnabled = remember { mutableStateOf(true) }

    fun Context.getActivity(): Activity? {
        return when (this) {
            is Activity -> this
            is ContextWrapper -> baseContext.getActivity()
            else -> null
        }
    }

    val activity = context.getActivity()

    LaunchedEffect(cameraCategoryOCR.value) {
        Log.d(TAG, "cameraCategoryOCR: ${cameraCategoryOCR.value}")
        when (cameraCategoryOCR.value.prefix) {
            TextCategoryOCR.prefix -> instantCrop.value = false
            MathCategoryOCR.prefix -> instantCrop.value = true

            GrammarCategoryOCR.prefix -> instantCrop.value = true
            SummaryCategoryOCR.prefix -> instantCrop.value = true
            TranslateCategoryOCR.prefix -> instantCrop.value = true

            else -> instantCrop.value = false
        }
    }

    /**
     * CropStateLess
     */
    CropStateLess(
        photoImageBitmap = photoImageBitmap,
        navController = navController,

        cameraCategoryOCR = cameraCategoryOCR,

        isLoading = isLoading,

        ) { croppedImage -> //TODO CROP VIEWMODEL
        // Delete the text
        vmCamera.imageToText.value = ""
        text.value = ""

        Log.d(TAG, "CropStateFul start: ${text.value}")

        scope.launch {
            /**
             * GET text OCR
             */
            getTextFromCroppedImage(
                vmCamera,
                vmChat,
                vmToken,

                croppedImage,
                cameraCategoryOCR,

                onNavigateConversation = {
                    Log.d(TAG, "onNavigateConversation. instantCrop -> $instantCrop")
                    if (!instantCrop.value) {
                        onNavigateNewChat()
                    } else {
                        scope.launch {
                            /**
                             * Send message instant crop
                             */
                            sendNewMessage(
                                context,
                                activity,

                                vmCamera,
                                vmAds,
                                vmChat,

                                cameraCategoryOCR,
                                text,

                                onNavigateChat = { onNavigateChat() },
                                onNavigateNewChat = { onNavigateNewChat() }
                            )
                        }
                    }
                }
            )
        }
    }
    /**
     * IsLoading
     */
    if (isLoading.value) IsLoadingCrop()
}

@Composable
fun CropStateLess(
    photoImageBitmap: MutableState<ImageBitmap>,
    navController: NavController,

    cameraCategoryOCR: MutableState<CategoryOCR>,

    isLoading: MutableState<Boolean>,

    onImageCropped: (ImageBitmap) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val cropifyState = rememberCropifyState()

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

    val enabled = remember { mutableStateOf(true) }

    /**
     * Enabled btn delay
     */
    LaunchedEffect(isLoading.value) {
        if (!isLoading.value) {
            delay(1000)
            enabled.value = true
        } else {
            enabled.value  = false
        }
    }

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
     * Top Message
     */
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(Color.DarkGray.copy(alpha = 0.4f), RoundedCornerShape(15.dp))
                .padding(horizontal = 4.dp),
        ) {
            Text(
                text = when (cameraCategoryOCR.value) {
                    MathCategoryOCR -> stringResource(R.string.try_to_crop_just_one_problem)
                    else -> "Crop the text you want to " + cameraCategoryOCR.value.getName(context)
                },
                color = Color.White,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(4.dp)
            )
        }
    }

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
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
            enabled = enabled.value
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
            shape = RoundedCornerShape(25.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
            enabled = enabled.value
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

    vmCamera: CameraViewModel,
    vmAds: AdsViewModel,
    vmChat: ChatViewModel,

    cameraCategoryOCR: MutableState<CategoryOCR>,
    text: MutableState<String>,

    onNavigateChat: () -> Unit,
    onNavigateNewChat: () -> Unit,
) {
    while (vmCamera.isLoading.value) delay(200)

    Log.d(TAG, "text 1: ${text.value}")
//    delay(5000)
//    Log.d(TAG, "text 2: ${text.value}")

    if (text.value.isNullOrEmpty() || text.value == "null") {
        Log.d(TAG, "sendNewMessage: text == ${text.value}")
        onNavigateNewChat()
    } else {
        Log.d(TAG, "sendNewMessage: text not null")
        withContext(Dispatchers.IO) {
            // New Message
            if (NetworkUtils.isOnline(context)) {
                vmCamera.isLoading.value = true
                // Show ad
                if (activity != null) {
                    vmAds.showInterstitialAd(activity)
                }

                val prefix = when (cameraCategoryOCR.value.prefix) {
                    TranslateCategoryOCR.prefix -> TranslateCategoryOCR.getPrefix(context)
                    SummaryCategoryOCR.prefix -> SummaryCategoryOCR.getPrefix(context)
                    GrammarCategoryOCR.prefix -> GrammarCategoryOCR.getPrefix(context)
                    else -> ""
                }
                Log.d(
                    TAG,
                    "sendNewMessage: cameraCategoryOCR -> ${cameraCategoryOCR.value.prefix} prefix-> $prefix"
                )
                // GPT call
                val deferred = async { vmChat.sendMessageToOpenaiApi(prefix + text.value) }
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
}

suspend fun getTextFromCroppedImage(
    vmCamera: CameraViewModel,
    vmChat: ChatViewModel,
    vmToken: TokenViewModel,

    croppedImage: ImageBitmap,
    cameraCategoryOCR: MutableState<CategoryOCR>,

    onNavigateConversation: () -> Unit,
) {
    when (cameraCategoryOCR.value.root) {
        TextCategoryOCR.root -> {
            Log.d(TAG, "CropStateLess: CAMERA_TEXT")
            vmChat.setUpNewConversation()

            vmCamera.cameraCategoryOCR.value = cameraCategoryOCR.value //TODO LIADA CATEDRALICIA
            vmChat.categoryOCR.value = cameraCategoryOCR.value

            vmCamera.getTextFromImage(croppedImage)

            onNavigateConversation()
        }

        MathCategoryOCR.root -> {
            Log.d(TAG, "CropStateLess: CAMERA_MATH")
            vmChat.setUpNewConversation()

            vmCamera.cameraCategoryOCR.value =
                cameraCategoryOCR.value//TODO LIADA CATEDRALICIA PARTE 2
            vmChat.categoryOCR.value = cameraCategoryOCR.value

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

