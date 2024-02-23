package com.sginnovations.asked

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class Constants {

    companion object {
        // Billing ViewModel
        const val MAX_RECONNECTION_ATTEMPTS = 25 // Maximum number of reconnection attempts
        const val RECONNECTION_DELAY_MILLIS = 250L // Delay between reconnection attempts in milliseconds
        // Room names
        const val MESSAGE_TABLE_NAME = "message_table"
        const val CONVERSATION_TABLE_NAME = "conversation_table"
        // Chat Roles
        const val ROLE_USER = "user"
        const val ROLE_ASSISTANT = "assistant"
        // Chat
        const val CONFIDENCE_RATE_LIMIT = 0.5
        const val MATH_PREFIX_PROMPT = "Resolve step by step, print Math expressions in Markdown syntax."
        const val TEXT_PREFIX_PROMPT = ""
        // Tokens
        const val TOKENS_NAME = "tokens"
        const val LAST_TOKEN_UPDATE = "lastTokensUpdate"
        const val USERS_NAME = "users"
        // Chat image
        const val DEFAULT_PROFILE_URL = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg"

        const val IS_PREMIUM = "isPremium"

        // Remote Config IDs
        const val KEY_FIRST_TIME = "firstTime"
        const val KEY_THEME = "theme"
        const val KEY_FONT_SIZE_MULTIPLIER =  "fontMultiplier"
        const val KEY_OFFER =  "showOffer"

        // Settings - Text Size
        const val TEXT_SIZE_NORMAL = 1f
        const val TEXT_SIZE_BIG = 2f
        const val TEXT_SIZE_BIGGER = 4f
        // Tokens Chat Cost
        const val ASSISTANT_MESSAGE_COST = 2
        const val CAMERA_MESSAGE_COST = 1
        // Chats Text Limit - FEATURE
        const val CHAT_LIMIT_DEFAULT = 500
        const val CHAT_LIMIT_PREMIUM = 4000
        const val DEV_ID = "K2Hr8qLCZaJfBb4KBLFz55"

        // Other
        val CHAT_MSG_PADDING = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
        val PURPLE_COLOR = Color(0xFFBD98F8)
        val LIGHT_NAVIGATION_BAR_COLOR = Color(0xFFF0EDED)
        val DARK_NAVIGATION_BAR_COLOR = Color(0xFF282931)

    }
}