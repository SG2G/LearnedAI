package com.sginnovations.learnedai.repository

import android.util.Log
import com.sginnovations.learnedai.Constants.Companion.OPENAI_API_KEY
import com.sginnovations.learnedai.data.network.OpenAIApiService
import com.sginnovations.learnedai.model.ChatCompletionRequest
import com.sginnovations.learnedai.model.ChatCompletionResponse
import retrofit2.Response
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val openAIApiService: OpenAIApiService
) {

    suspend fun getChatResponse(chatCompletionRequest: ChatCompletionRequest): Response<ChatCompletionResponse> {
        Log.d("ChatRepository", "getChatResponse: calling api")
        return openAIApiService.getChatCompletion(
            authHeader = "Bearer $OPENAI_API_KEY",
            chatCompletionRequest = chatCompletionRequest
        )
    }
}

