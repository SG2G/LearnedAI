package com.sginnovations.learnedai.model

// Request
data class ChatCompletionRequest(
    val model: String,
    val messages: List<Message>
)

// Response
data class ChatCompletionResponse(
    val id: String,
    val created: Int,
    val model: String,
    val usage: Usage,
    val choices: List<Choice>
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

data class Choice(
    val message: Message,
    val finish_reason: String
)

// Messages
data class Message(
    val role: String,
    val content: String
)