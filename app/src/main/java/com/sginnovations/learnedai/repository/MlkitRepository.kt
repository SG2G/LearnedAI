package com.sginnovations.learnedai.repository

import androidx.compose.ui.graphics.ImageBitmap
import com.sginnovations.learnedai.model.mlkit.BitmapToInputImage
import javax.inject.Inject

class MlkitRepository @Inject constructor(
    private val bitmapToInputImage: BitmapToInputImage
) {
    suspend fun getTextFromImage(imageBitmap: ImageBitmap): String {
        return bitmapToInputImage(imageBitmap)
    }
}