package com.sginnovations.asked.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.data.database.entities.ConversationEntity
import com.sginnovations.asked.data.database.entities.MessageEntity
import com.sginnovations.asked.data.database.util.Assistant
import com.sginnovations.asked.data.database.util.User
import com.sginnovations.asked.model.ChatCompletionRequest
import com.sginnovations.asked.model.Message
import com.sginnovations.asked.repository.ChatRepository
import com.sginnovations.asked.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ChatViewModel"

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    val idConversation = mutableIntStateOf(0)
    val category = mutableStateOf("Math") //TODO HAHA
    private val timestamp = System.currentTimeMillis()

    // Response of OPENAI API
    private val openaiApiResponse = mutableStateOf<Message?>(null)

    val messages = mutableStateOf<List<MessageEntity>>(emptyList())
    val conversations = mutableStateOf<List<ConversationEntity>>(emptyList())

    /**
     *  MutableList whit all the actual conversation
     */
    private val messageHistory =
        mutableListOf(Message(role = "system", content = "You are a helpful assistant."))

    fun setUpMessageHistory() {
        viewModelScope.launch {
            val lastMessages = messages.value.takeLast(5)

            for (message in lastMessages) {
                messageHistory.add(
                    Message(
                        role = message.role,
                        content = message.content
                    )
                )
            }
        }
    }
    fun setUpNewConversation() {
        viewModelScope.launch {
            idConversation.intValue = 0
        }
    }

    suspend fun getAllConversations() {
        viewModelScope.launch {
            conversations.value = roomRepository.getAllConversations()
        }
    }

    suspend fun getAllMessages() {
        Log.i(TAG, "getting all messages of id: $idConversation")
        viewModelScope.launch {
            messages.value = roomRepository.getAllMessages(idConversation.intValue)
        }
    }

    suspend fun hideConversation(id: Int) {
        viewModelScope.launch {
            roomRepository.hideConversation(id)
            getAllConversations()
        }
    }

    /**
     * Call to GPT
     */
    suspend fun sendMessageToOpenaiApi(prompt: String) {
        val userMessage = Message(role = User.role, content = prompt)

        // Create conversation if needed
        if (idConversation.intValue == 0) {
            idConversation.intValue = roomRepository.createConversation(
                ConversationEntity(name = shortenString(prompt), category = category.value, visible = true)
            ).toInt()
        }

        messageHistory.add(userMessage)

        val chatCompletionRequest = ChatCompletionRequest(
            model = "gpt-3.5-turbo",
            messages = messageHistory
        )

        /**
         * API CALL RESPONSE
         */
        Log.i(TAG, "getChatResponse: Sending to repository")

        val response = chatRepository.getChatResponse(chatCompletionRequest)

        if (response?.isSuccessful == true) {
            Log.i(TAG, "getChatResponse: Correct")

            roomRepository.insertMessage(
                MessageEntity(
                    idConversation = idConversation.intValue,
                    role = User.role,
                    content = userMessage.content,
                    timestamp = timestamp
                )
            )

            openaiApiResponse.value = response.body()?.choices?.first()?.message

            openaiApiResponse.value?.let { apiResponse ->
                messageHistory.add(Message(role = apiResponse.role, content = apiResponse.content))

                roomRepository.insertMessage(
                    MessageEntity(
                        idConversation = idConversation.intValue,
                        role = Assistant.role,
                        content = apiResponse.content,
                        timestamp = timestamp
                    )
                )
            }

            Log.i(TAG, "getChatResponse: ${openaiApiResponse.value.toString()}")
        } else {
            Log.i(TAG, "getChatResponse: Error")
            // Handle error response is not successful
        }

        while (messageHistory.size > 5) {
            messageHistory.removeAt(1)
        }

        getAllMessages()

        Log.i(TAG, "messageHistory: $messageHistory")
        Log.i(TAG, "I just finished sendMessageToOpenaiApi")
    }
    private fun shortenString(input: String): String {
        val maxLength = 30
        return if (input.length > maxLength) {
            input.substring(0, maxLength) + "..."
        } else {
            input
        }
    }
}