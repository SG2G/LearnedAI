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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
        //TODO SUPPORT OLD DEVICES
    }

    if (galleryPermissionGranted.value) {
        val context = LocalContext.current
        val bitmap = remember { mutableStateOf<Bitmap?>(null) }
        val imageUri = remember { mutableStateOf<Uri?>(null) }

        var hasExecuted = remember {
            mutableStateOf(false)
        }

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


        LaunchedEffect(Unit) {
            Log.i(TAG, "LaunchedEffect")
            hasExecuted.value = false
            launcher.launch("image/*")
        }
    }
}