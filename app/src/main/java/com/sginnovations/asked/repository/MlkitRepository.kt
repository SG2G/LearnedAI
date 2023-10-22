package com.sginnovations.asked.repository

import androidx.compose.ui.graphics.ImageBitmap
import com.sginnovations.asked.model.mlkit.BitmapToInputImage
import javax.inject.Inject

class MlkitRepository @Inject constructor(
    private val bitmapToInputImage: BitmapToInputImage
) {
    suspend fun getTextFromImage(imageBitmap: ImageBitmap): String {
        return bitmapToInputImage(imageBitmap)
    }
}