package com.sginnovations.asked.repository

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.sginnovations.asked.data.mathpix.DataOptions
import com.sginnovations.asked.data.mathpix.MathpixRequest
import com.sginnovations.asked.data.mathpix.MathpixResponse
import com.sginnovations.asked.data.network.MathpixOCRService
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.coroutines.resume

private const val TAG = "MathpixRepository"

class MathpixRepository @Inject constructor(
    private val mathpixOCRService: MathpixOCRService,

    private val remoteConfigRepository: RemoteConfigRepository
) {
    suspend fun getMathFromImage(imageBitmap: ImageBitmap): String = suspendCancellableCoroutine { continuation ->
        val mathpixAPIKey = remoteConfigRepository.getMathpixAPI()
        val base64Image = convertImageBitmapToBase64(imageBitmap)

        Log.d(TAG, "getMathFromImage: Trying to send request ${base64Image.byteInputStream()}")
        val request = MathpixRequest(
            src = "data:image/jpeg;base64,$base64Image",
            formats = listOf("text", "data"),
            data_options = DataOptions(
                include_asciimath = true,
            )
        )

        val call = mathpixOCRService.processImage(
            appId = "asked_e64f8d_941236",
            appKey = mathpixAPIKey,
            request
        )

        Log.d(TAG, "getMathFromImage: call -> $call")

        call.enqueue(object : Callback<MathpixResponse> {
            override fun onResponse(call: Call<MathpixResponse>, response: Response<MathpixResponse>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body().toString()}")
                    try {
                        val mathText = response.body()!!.data.firstOrNull()?.value.toString()
                        Log.d(TAG, "getMathFromImage: ${response.body()!!.data.firstOrNull()?.value.toString()}")
                        continuation.resume(mathText)
                    } catch (e: Exception) {
                        Log.d(TAG, "getMathFromImage: catch Exception Error")
                        e.printStackTrace()
                        continuation.resume("ERROR")
                    }
                } else {
                    Log.d(TAG, "getMathFromImage: Else Error")
                    continuation.resume("ERROR")
                }
            }

            override fun onFailure(call: Call<MathpixResponse>, t: Throwable) {
                // Handle failure
                Log.d(TAG, "onFailure: ")
                t.printStackTrace()
                continuation.resume("ERROR")
            }
        })

        continuation.invokeOnCancellation {
            call.cancel()
        }
    }



    /**
     * Util
     */
    private fun convertImageBitmapToBase64(imageBitmap: ImageBitmap): String {
        val bitmap: Bitmap = imageBitmap.asAndroidBitmap()
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

        val byteArray = byteArrayOutputStream.toByteArray()
        Log.d(TAG, "convertImageBitmapToBase64 ENDING")
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}