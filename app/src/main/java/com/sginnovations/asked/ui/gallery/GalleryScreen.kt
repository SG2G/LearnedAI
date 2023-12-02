package com.sginnovations.asked.ui.gallery

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.data.GrammarCategoryOCR
import com.sginnovations.asked.data.MathCategoryOCR
import com.sginnovations.asked.data.Soon
import com.sginnovations.asked.data.SummaryCategoryOCR
import com.sginnovations.asked.data.TextCategoryOCR
import com.sginnovations.asked.data.TranslateCategoryOCR
import com.sginnovations.asked.ui.main_bottom_bar.camera.CheckPermissions
import com.sginnovations.asked.viewmodel.CameraViewModel

private const val TAG = "GalleryStateFull"

@Composable
fun GalleryStateFull(
    vmCamera: CameraViewModel,

    onCropNavigation: () -> Unit,
) {
    val galleryPermissionGranted = remember { mutableStateOf(false) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        CheckPermissions(
            permsAsked = Manifest.permission.READ_MEDIA_IMAGES,
            permName = stringResource(R.string.perms_name_gallery),
            onPermissionGranted = {
                galleryPermissionGranted.value = true
            }
        )
    } else {
        CheckPermissions(
            permsAsked = Manifest.permission.READ_EXTERNAL_STORAGE,
            permName = stringResource(R.string.perms_name_gallery),
            onPermissionGranted = {
                galleryPermissionGranted.value = true
            }
        )
    }

    if (galleryPermissionGranted.value) {
        val context = LocalContext.current
        val bitmap = remember { mutableStateOf<Bitmap?>(null) }
        val imageUri = remember { mutableStateOf<Uri?>(null) }

        val hasExecuted = remember { mutableStateOf(false) }
        hasExecuted.value = false

        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                imageUri.value = uri

                uri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap.value =
                            MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        bitmap.value = ImageDecoder.decodeBitmap(source)
                    }
                }
            }

        LaunchedEffect(bitmap.value) {
            Log.d(TAG, "GalleryStateFull: ${bitmap.value}")
            if (bitmap.value != null) {
                vmCamera.onTakePhoto(bitmap.value!!)
                onCropNavigation()
                bitmap.value = null
            }
        }

        /**
         * StateLess
         */
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.gallery_select_the_ocr_you_want_to_use),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            // List of OCR categories
            val ocrCategories = listOf(
                TextCategoryOCR to R.drawable.text_camera,
                MathCategoryOCR to R.drawable.math_camera,
                TranslateCategoryOCR to R.drawable.translate_camera,
                SummaryCategoryOCR to R.drawable.summary_camera,
                GrammarCategoryOCR to R.drawable.grammar_camera,
                Soon to R.drawable.asked30,
                // Add more categories if needed
            )

            // Create rows with two items each
            ocrCategories.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { (category, iconResId) ->
                        Button(
                            onClick = {
                                vmCamera.cameraCategoryOCR.value = category
                                launcher.launch("image/*")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(148.dp)
                                .padding(16.dp)
                                .weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    text = when (category) {
                                        TextCategoryOCR -> stringResource(R.string.gallery_process_text_based_problem)
                                        MathCategoryOCR -> stringResource(R.string.gallery_process_math_problem)
                                        TranslateCategoryOCR -> "Translate a text"
                                        SummaryCategoryOCR -> "Summary a text"
                                        GrammarCategoryOCR -> "Correct the grammar"
                                        // Add more category-specific text if needed
                                        else -> {"Soon..."}
                                    },
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                )
                                Image(
                                    painter = painterResource(id = iconResId),
                                    contentDescription = null,
                                    alignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(48.dp)
                                        .padding(4.dp),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}