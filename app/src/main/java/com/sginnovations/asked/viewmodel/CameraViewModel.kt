package com.sginnovations.asked.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.repository.MlkitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(
    private val mlkitRepository: MlkitRepository,
) : ViewModel() {

    val imageText = mutableStateOf("")
    val cameraCategory = mutableStateOf("Text")
    val photoImageBitmap = mutableStateOf(createBlackImageBitmap(100, 100))

    fun onTakePhoto(bitmap: Bitmap) {
        photoImageBitmap.value = convertBitmapToImageBitmap(bitmap)
    }

    fun getTextFromImage(imageBitmap: ImageBitmap) {
        viewModelScope.launch {
            imageText.value = mlkitRepository.getTextFromImage(imageBitmap)
        }
    }

    private fun convertBitmapToImageBitmap(bitmap: Bitmap): ImageBitmap {
        return bitmap.asImageBitmap()
    }

    private fun createBlackImageBitmap(width: Int, height: Int): ImageBitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.Black.toArgb())
        return bitmap.asImageBitmap()
    }
}