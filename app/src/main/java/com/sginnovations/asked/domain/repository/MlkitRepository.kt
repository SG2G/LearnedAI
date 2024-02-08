package com.sginnovations.asked.domain.repository

import androidx.compose.ui.graphics.ImageBitmap
import com.sginnovations.asked.presentation.model.mlkit.BitmapToInputImage
import javax.inject.Inject

class MlkitRepository @Inject constructor(
    private val bitmapToInputImage: BitmapToInputImage
) {
    suspend fun getTextFromImage(imageBitmap: ImageBitmap): String {
        return bitmapToInputImage(imageBitmap)
    }
}