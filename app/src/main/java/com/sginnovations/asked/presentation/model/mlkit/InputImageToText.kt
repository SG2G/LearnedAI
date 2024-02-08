package com.sginnovations.asked.presentation.model.mlkit

import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "InputImageToText"

class InputImageToText @Inject constructor() {
    suspend operator fun invoke(image: InputImage): String {
        val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        var resultText: String
        return suspendCoroutine { continuation ->
            textRecognizer.process(image)
                .addOnSuccessListener { visionText ->

                    resultText = visionText.text
                    if (resultText.isEmpty()) resultText = "null"
                    Log.i(TAG,"getTextFromImage: Image to TEXT process SUCCESSFUL. $resultText")
                    continuation.resume(resultText)
                }
                .addOnFailureListener { e ->

                    resultText = "Something Failed"
                    Log.i(TAG,"getTextFromImage: The process to RECOGNIZE the TEXT on IMAGE FAILED. :(")

                    e.printStackTrace()
                    continuation.resume(resultText)
                }
        }
    }
}