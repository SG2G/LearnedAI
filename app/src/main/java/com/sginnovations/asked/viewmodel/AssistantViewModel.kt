package com.sginnovations.asked.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.data.CategoryOCR
import com.sginnovations.asked.data.TextCategoryOCR
import com.sginnovations.asked.data.api_gpt.ChatCompletionRequest
import com.sginnovations.asked.data.api_gpt.Message
import com.sginnovations.asked.data.database.entities.ConversationEntity
import com.sginnovations.asked.data.database.entities.MessageEntity
import com.sginnovations.asked.data.database.util.Assistant
import com.sginnovations.asked.data.database.util.User
import com.sginnovations.asked.repository.ChatRepository
import com.sginnovations.asked.repository.RemoteConfigRepository
import com.sginnovations.asked.repository.RoomRepository
import com.sginnovations.asked.repository.TokenRepository
import com.sginnovations.asked.viewmodel.ChatViewModel.Companion.shortenString
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AssistantViewModel"
@HiltViewModel
class AssistantViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val chatRepository: ChatRepository,
    private val roomRepository: RoomRepository,
    private val tokensRepository: TokenRepository,

    private val remoteConfigRepository: RemoteConfigRepository,
    ) : ViewModel() {

    val firstMessage = mutableStateOf<String?>("")
    val isLoading = mutableStateOf(false)

    val idConversation = mutableIntStateOf(0)

    private val prefixPrompt = mutableStateOf("")
    private val timestamp = System.currentTimeMillis()

    // Response of OPENAI API
    private val openaiApiResponse = mutableStateOf<Message?>(null)

    val messages = mutableStateOf<List<MessageEntity>>(emptyList())
    val conversations = mutableStateOf<List<ConversationEntity>>(emptyList())

    /**
     *  MutableList whit all the actual conversation
     */
    private val messageHistory =
        mutableListOf(
            Message(
                role = "system",
                content = "You are a helpful assistant. Respond on language:${Locale.current.language}"
            )
        )

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
            Log.d(TAG, "setUpMessageHistory: messageHistory -> $messageHistory ")
        }
    }
    fun setUpNewConversation() {
        viewModelScope.launch {
            idConversation.intValue = 0
        }
    }
    suspend fun getConversationsFromCategory(category: String) {
        viewModelScope.launch {
            conversations.value = roomRepository.getConversationsFromCategory(category).asReversed()
        }
    }

    suspend fun getMessagesFromIdConversation() {
        Log.i(TAG, "getting all messages of id: $idConversation")
        viewModelScope.launch {
            messages.value = roomRepository.getAllMessages(idConversation.intValue)
        }
    }

    suspend fun hideConversationsAssist(id: Int) {
        viewModelScope.launch {
            Log.d(TAG, "hideConversationsAssist: id -> $id")
            roomRepository.hideConversation(id)
            getConversationsFromCategory(com.sginnovations.asked.data.Assistant.prefix)
        }
    }

    //TODO CHANGE
    fun newConversationCostTokens() = remoteConfigRepository.getNewAssistConversationCostTokens()
    private suspend fun lessTokenNewConversationAssistCheckPremium() {
        viewModelScope.launch {
            val costTokens = newConversationCostTokens()
            tokensRepository.lessTokenCheckPremium(costTokens.toInt())
        }
    }

    /**
     * Call to GPT
     */
    suspend fun sendMessageToOpenaiApi(prompt: String) {
        /**
         * Set up
         */
        //Get Key
        val openAIAPIKey = remoteConfigRepository.getOpenAIAPI()

        val userMessage = Message(role = User.role, content = prefixPrompt.value + prompt)

        // Create conversation if needed
        if (idConversation.intValue == 0) {
            idConversation.intValue = roomRepository.createConversation(
                ConversationEntity(
                    name = shortenString(prompt),
                    category = com.sginnovations.asked.data.Assistant.prefix,
                    visible = true
                )
            ).toInt()
        }

        messageHistory.add(userMessage)

        val chatCompletionRequest = ChatCompletionRequest(
            model = "gpt-3.5-turbo-1106",
            messages = messageHistory
        )

        /**
         * API CALL RESPONSE
         */
        Log.i(TAG, "getChatResponse: Sending to repository")

        val response = chatRepository.getChatResponse(openAIAPIKey, chatCompletionRequest)

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

        getMessagesFromIdConversation()

        /**
         * Less TOKENS
         */
        lessTokenNewConversationAssistCheckPremium()

        Log.i(TAG, "messageHistory: $messageHistory")
        Log.i(TAG, "I just finished sendMessageToOpenaiApi")
    }

}
