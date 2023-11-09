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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.data.Math
import com.sginnovations.asked.data.Text
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
            onPermissionGranted = {
                galleryPermissionGranted.value = true
            }
        )
    } else {
        CheckPermissions(
            permsAsked = Manifest.permission.READ_EXTERNAL_STORAGE,
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

        bitmap.value?.let { btm ->
            if (!hasExecuted.value) {
                Log.i(TAG, " bitmap.value?.let")
                vmCamera.onTakePhoto(btm)
                onCropNavigation()
                hasExecuted.value = true
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
                text = "Select the OCR you want to use",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
            /**
             * Process whit Text
             */
            Button(
                onClick = {
                    vmCamera.cameraOCRCategory.value = Text.name
                    launcher.launch("image/*")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "Process Text Based Problem",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            /**
             * Process whit Math
             */
            Button(
                onClick = {
                    vmCamera.cameraOCRCategory.value = Math.name
                    launcher.launch("image/*")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "Process Math Problem",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}