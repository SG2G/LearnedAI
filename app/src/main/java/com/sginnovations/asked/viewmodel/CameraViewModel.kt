package com.sginnovations.asked.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.data.CategoryOCR
import com.sginnovations.asked.data.TextCategoryOCR
import com.sginnovations.asked.repository.MathpixRepository
import com.sginnovations.asked.repository.MlkitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CameraViewModel"

@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val mlkitRepository: MlkitRepository,
    private val mathpixRepository: MathpixRepository,
) : ViewModel() {

    val imageToText = mutableStateOf("")
    val textConfidence = mutableDoubleStateOf(1.0)

    val isLoading = mutableStateOf(false)

    val cameraCategoryOCR = mutableStateOf<CategoryOCR>(TextCategoryOCR)
    val photoImageBitmap = mutableStateOf(createBlackImageBitmap(100, 100))

    fun onTakePhoto(bitmap: Bitmap) {
        photoImageBitmap.value = convertBitmapToImageBitmap(bitmap)
    }

    fun getTextFromImage(imageBitmap: ImageBitmap) {
        Log.d(TAG, "getTextFromImage")
        isLoading.value = true
        viewModelScope.launch {
            imageToText.value = mlkitRepository.getTextFromImage(imageBitmap)
            isLoading.value = false
        }
    }
    suspend fun getMathFromImage(imageBitmap: ImageBitmap) {
        Log.d(TAG, "getMathFromImage")
        isLoading.value = true
        viewModelScope.launch {
            val mathResponse = mathpixRepository.getMathFromImage(imageBitmap)
            imageToText.value = mathResponse.text
            textConfidence.doubleValue = mathResponse.confidence
            isLoading.value = false
        }
    }

    /**
     * Do not touch it
     */
    private fun convertBitmapToImageBitmap(bitmap: Bitmap): ImageBitmap { return bitmap.asImageBitmap() }
    private fun createBlackImageBitmap(width: Int, height: Int): ImageBitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.Black.toArgb())
        return bitmap.asImageBitmap()
    }
}
