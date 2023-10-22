package com.sginnovations.asked.data.network

import com.sginnovations.asked.model.ChatCompletionRequest
import com.sginnovations.asked.model.ChatCompletionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIApiService {

    @POST("v1/chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") authHeader: String,
        @Body chatCompletionRequest: ChatCompletionRequest
    ): Response<ChatCompletionResponse>
}
