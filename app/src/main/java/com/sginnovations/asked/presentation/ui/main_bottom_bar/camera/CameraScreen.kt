package com.sginnovations.asked.presentation.ui.main_bottom_bar.camera

import android.graphics.Bitmap
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sginnovations.asked.R
import com.sginnovations.asked.data.CategoryOCR
import com.sginnovations.asked.data.GrammarCategoryOCR
import com.sginnovations.asked.data.MathCategoryOCR
import com.sginnovations.asked.data.SummaryCategoryOCR
import com.sginnovations.asked.data.TextCategoryOCR
import com.sginnovations.asked.data.TranslateCategoryOCR
import com.sginnovations.asked.presentation.ui.main_bottom_bar.camera.components.carousel.CameraCarousel
import com.sginnovations.asked.presentation.ui.main_bottom_bar.camera.components.other.CameraExamplesDialog
import com.sginnovations.asked.presentation.ui.main_bottom_bar.camera.components.top.MathExamples
import com.sginnovations.asked.presentation.ui.main_bottom_bar.camera.components.top.TranslateSelector
import com.sginnovations.asked.presentation.ui.ui_components.tokens.TokenDisplay
import com.sginnovations.asked.presentation.viewmodel.CameraViewModel
import com.sginnovations.asked.presentation.viewmodel.TokenViewModel

private const val TAG = "CameraStateFul"

@Composable
fun CameraStateFul(
    vmCamera: CameraViewModel,
    vmToken: TokenViewModel,

    onNavigateSubscriptions: () -> Unit,

    onGetPhotoGallery: () -> Unit,
    onCropNavigation: () -> Unit,
) {
    /// Check Camera Perms ///
    val cameraPermissionGranted = remember { mutableStateOf(false) }
    CheckPermissions(
        permsAsked = android.Manifest.permission.CAMERA,
        permName = "Camera", //TODO TRANSLATE
        onPermissionGranted = {
            cameraPermissionGranted.value = true
        }
    )

    val cameraCategoryOCR = vmCamera.cameraCategoryOCR
    val translateLanguage = vmCamera.translateLanguage

    val showPDFWorkingOn = remember { mutableStateOf(false) }
    val showCategoryExamples = remember { mutableStateOf(false) }

    if (cameraPermissionGranted.value) {
        CameraStateLess(
            vmToken = vmToken,
            cameraCategoryOCR = cameraCategoryOCR,
            translateLanguage = translateLanguage,

            onGetPhotoGallery = { onGetPhotoGallery() },
            onPhotoTaken = { bitmap ->
                vmCamera.onTakePhoto(bitmap)
                onCropNavigation()
            },
            onLoadPDF = {
                // TODO PDF READER
                showPDFWorkingOn.value = true
            },
            onShowCategoryExamples = {
                showCategoryExamples.value = true
            },

            onNavigateSubscriptionScreen = { onNavigateSubscriptions() },

            onChangeTranslateLanguage = { language ->
                vmCamera.translateLanguage.value = language
            }

        ) { categoryOCR ->
            Log.d(TAG, "categoryOCR prefix: $categoryOCR")
            vmCamera.cameraCategoryOCR.value = categoryOCR
        }
        /**
         * Shows
         */
        if (showCategoryExamples.value) {
            CameraExamplesDialog(
                onDismissRequest = { showCategoryExamples.value = false },

                cameraCategoryOCR = cameraCategoryOCR,
            )
        }
    }
}

@Composable
fun CameraStateLess(
    vmToken: TokenViewModel,
    cameraCategoryOCR: MutableState<CategoryOCR>,
    translateLanguage: MutableState<String>,

    onGetPhotoGallery: () -> Unit,
    onPhotoTaken: (Bitmap) -> Unit,
    onLoadPDF: () -> Unit,

    onShowCategoryExamples: () -> Unit,

    onNavigateSubscriptionScreen: () -> Unit,

    onChangeTranslateLanguage: (String) -> Unit,
    onChangeCategory: (CategoryOCR) -> Unit,
) {
    val context = LocalContext.current
    val tokens = vmToken.tokens.collectAsStateWithLifecycle()

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
        /**
         * Camera Top Row
         */
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            when (cameraCategoryOCR.value.prefix) {
                TranslateCategoryOCR.prefix -> TranslateSelector(
                    translateLanguage = translateLanguage
                ) {
                    Log.d(TAG, "Translating to: $it")
                    onChangeTranslateLanguage(it)
                }

                MathCategoryOCR.prefix -> MathExamples(onShowCategoryExamples)
            }

            Spacer(modifier = Modifier.weight(1f))
            TokenDisplay(tokens = tokens, showPlus = false) { vmToken.switchPointsVisibility() }
        }

        /**
         * Camera Bottom Row
         */
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.DarkGray.copy(alpha = 0.4f), RoundedCornerShape(15.dp))
                        .padding(horizontal = 4.dp),
                ) {
                    Text(
                        text =
                        when (cameraCategoryOCR.value.prefix) {
                            TextCategoryOCR.prefix -> stringResource(R.string.camera_tip_take_a_photo_of_a_text_based_problem)
                            MathCategoryOCR.prefix -> stringResource(R.string.camera_tip_take_a_photo_of_math_problems)
                            TranslateCategoryOCR.prefix -> stringResource(R.string.camera_tip_take_photos_to_translate_the_text)
                            SummaryCategoryOCR.prefix -> stringResource(R.string.camera_tip_take_photos_to_make_a_summary)
                            GrammarCategoryOCR.prefix -> stringResource(R.string.camera_tip_take_photos_to_correct)
                            else -> ""
                        },
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }
            }

            /**
             * Selector, etc..
             */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(bottom = 0.dp, start = 16.dp, end = 16.dp, top = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onGetPhotoGallery() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.gallery_wide_svgrepo_com),
                        contentDescription = "PhotoLibrary",
                        modifier = Modifier.size(38.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                /**
                 * Choose OCR Camera
                 */
                CameraCarousel(
                    modifier = Modifier
                        .height(132.dp)
                        .width(212.dp),
                    controller = controller,

                    onChangeCategory = { categoryOCR -> onChangeCategory(categoryOCR) },
                    onNavigateSubscriptionScreen = { onNavigateSubscriptionScreen() }
                ) { onPhotoTaken(it) }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = { /*onLoadPDF()*/ }) { //TODO DELETED
                    Icon(
                        imageVector = Icons.Outlined.PictureAsPdf,
                        contentDescription = "PictureAsPdf",
                        modifier = Modifier.size(38.dp),
                        tint = Color.Transparent
                    )
                }
            }
        }

    }
}
