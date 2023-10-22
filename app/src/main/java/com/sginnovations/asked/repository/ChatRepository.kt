package com.sginnovations.asked.repository

import android.util.Log
import com.sginnovations.asked.Constants.Companion.OPENAI_API_KEY
import com.sginnovations.asked.data.network.OpenAIApiService
import com.sginnovations.asked.model.ChatCompletionRequest
import com.sginnovations.asked.model.ChatCompletionResponse
import retrofit2.Response
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val openAIApiService: OpenAIApiService
) {

    suspend fun getChatResponse(chatCompletionRequest: ChatCompletionRequest): Response<ChatCompletionResponse>? {
        Log.d("ChatRepository", "getChatResponse: calling api")
        return try {
            openAIApiService.getChatCompletion(
                authHeader = "Bearer $OPENAI_API_KEY",
                chatCompletionRequest = chatCompletionRequest
            )
        } catch (e: Exception) {
            Log.d("ChatRepository", "getChatResponse: Error")
            e.printStackTrace()
            null
        }
    }
}

