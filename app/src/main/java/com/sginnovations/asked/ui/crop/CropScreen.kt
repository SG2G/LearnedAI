package com.sginnovations.asked.ui.crop

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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

    if (isLoading.value) IsLoadingCrop()

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
                                text.value,

                                onNavigateChat = { onNavigateChat() },
                                onNavigateNewChat = { onNavigateNewChat() }
                            )
                        }
                    }
                }
            )
        }
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
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary)
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
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
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
    text: String?,

    onNavigateChat: () -> Unit,
    onNavigateNewChat: () -> Unit,
) {
    if (text == "null" || text == "") {
        Log.d(TAG, "sendNewMessage: text == \"null\"")
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
                val deferred = async { vmChat.sendMessageToOpenaiApi(prefix + text) }
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

fun getTextFromCroppedImage(
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

            vmCamera.cameraCategoryOCR.value = cameraCategoryOCR.value//TODO LIADA CATEDRALICIA PARTE 2
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

