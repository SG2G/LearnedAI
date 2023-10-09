package com.sginnovations.learnedai.model.mlkit

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.google.mlkit.vision.common.InputImage
import javax.inject.Inject

private const val TAG = "BitmapToInputImage"

class BitmapToInputImage @Inject constructor(
    private val inputImageToText: InputImageToText,
) {
    suspend operator fun invoke(imageBitmap: ImageBitmap): String {
        val bitmap = imageBitmap.asAndroidBitmap()
        try {
            Log.i(TAG, "ImageBitmap converted to InputImage")
            return inputImageToText(InputImage.fromBitmap(bitmap, 0))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.i(TAG, "Image convert failed")
        return "TEXT RECOGNIZER CALL FAILED"
    }
}