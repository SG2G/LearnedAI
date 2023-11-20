package com.sginnovations.asked.ui.main_bottom_bar.camera

import android.graphics.Bitmap
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.PhotoLibrary
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sginnovations.asked.R
import com.sginnovations.asked.data.Math
import com.sginnovations.asked.data.Text
import com.sginnovations.asked.ui.ui_components.camera.CameraCarousel
import com.sginnovations.asked.ui.ui_components.camera.CameraExamplesDialog
import com.sginnovations.asked.ui.ui_components.camera.CameraPreview
import com.sginnovations.asked.ui.ui_components.camera.PDFAlertDialog
import com.sginnovations.asked.ui.ui_components.tokens.TokenDisplay
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel

private const val TAG = "CameraStateFul"

@Composable
fun CameraStateFul(
    vmCamera: CameraViewModel,
    vmToken: TokenViewModel,

    onGetPhotoGallery: () -> Unit,
    onCropNavigation: () -> Unit,
) {
    /// Check Camera Perms ///
    val cameraPermissionGranted = remember { mutableStateOf(false) }
    CheckPermissions(
        permsAsked = android.Manifest.permission.CAMERA,
        permName = "Camera",
        onPermissionGranted = {
            cameraPermissionGranted.value = true
        }
    )

    val cameraOCRCategory = vmCamera.cameraOCRCategory
    val showPDFWorkingOn = remember { mutableStateOf(false) }
    val showCategoryExamples = remember { mutableStateOf(false) }

    if (cameraPermissionGranted.value) {
        CameraStateLess(
            vmToken = vmToken,
            cameraOCRCategory = cameraOCRCategory,

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

            ) { category ->
            vmCamera.cameraOCRCategory.value = category
        }
        /**
         * Shows
         */
        if (showPDFWorkingOn.value) {
            PDFAlertDialog(onDismiss = { showPDFWorkingOn.value = false })
        }
        if (showCategoryExamples.value) {
            CameraExamplesDialog(
                onDismissRequest = { showCategoryExamples.value = false },

                cameraOCRCategory = cameraOCRCategory,
            )
        }
    }
}

@Composable
fun CameraStateLess(
    vmToken: TokenViewModel,
    cameraOCRCategory: MutableState<String>,

    onGetPhotoGallery: () -> Unit,
    onPhotoTaken: (Bitmap) -> Unit,
    onLoadPDF: () -> Unit,

    onShowCategoryExamples: () -> Unit,

    onChangeCategory: (String) -> Unit,
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
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = when (cameraOCRCategory.value) {
                    Text.root -> "" // TODO TRANSLATE
                    Math.root -> stringResource(R.string.camera_math_examples)
                    else -> ""

                },
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.clickable {
                    onShowCategoryExamples()
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            TokenDisplay(tokens = tokens, showPlus = true) { vmToken.switchPointsVisibility() }
        }

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
                        when (cameraOCRCategory.value) {
                            Text.root -> stringResource(R.string.camera_tip_take_a_photo_of_a_text_based_problem)
                            Math.root -> stringResource(R.string.camera_tip_take_a_photo_of_math_problems)
                            else -> ""
                        },
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }
            }

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
                        imageVector = Icons.Outlined.PhotoLibrary,
                        contentDescription = "PhotoLibrary",
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                CameraCarousel(
                    modifier = Modifier
                        .height(148.dp)
                        .width(200.dp),
                    vmToken = vmToken,
                    controller = controller,

                    onChangeCategory = { onChangeCategory(it) }
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
