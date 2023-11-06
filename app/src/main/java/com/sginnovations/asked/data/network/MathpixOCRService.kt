package com.sginnovations.asked.data.network

import com.sginnovations.asked.data.mathpix.MathpixRequest
import com.sginnovations.asked.data.mathpix.MathpixResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MathpixOCRService {
    @POST("v3/text")
    fun processImage(
        @Header("app_id") appId: String,
        @Header("app_key") appKey: String,
        @Body request: MathpixRequest
    ): Call<MathpixResponse>
}
