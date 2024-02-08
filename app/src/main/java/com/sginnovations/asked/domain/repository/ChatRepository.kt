package com.sginnovations.asked.domain.repository

import android.util.Log
import com.sginnovations.asked.data.api_gpt.ChatCompletionRequest
import com.sginnovations.asked.data.api_gpt.ChatGPTResponse
import com.sginnovations.asked.data.network.OpenAIApiService
import retrofit2.Response
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val openAIApiService: OpenAIApiService,
) {
    suspend fun getChatResponse(
        openAIAPIKey: String,
        chatCompletionRequest: ChatCompletionRequest,
    ): Response<ChatGPTResponse>? {
        Log.d("ChatRepository", "getChatResponse: calling api")
        return try {
            openAIApiService.getChatCompletion(
                authHeader = "Bearer $openAIAPIKey",
                chatCompletionRequest = chatCompletionRequest
            )
        } catch (e: Exception) {
            Log.d("ChatRepository", "getChatResponse: Error")
            e.printStackTrace()
            null
        }
    }
}

