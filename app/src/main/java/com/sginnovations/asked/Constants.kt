package com.sginnovations.asked

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.repository.RemoteConfigRepository
import javax.inject.Inject

class Constants {

    companion object {
        // Learned Navigation
        val START_DESTINATION = Auth.route
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
        const val ONE_LESS_TOKEN = -1
        const val CONFIDENCE_RATE_LIMIT = 0.5
        const val MATH_PREFIX_PROMPT = "Resolve step by step, print Math expressions in Markdown syntax."
        const val TEXT_PREFIX_PROMPT = ""
        // Tokens
        const val TOKENS_NAME = "tokens"
        const val LAST_TOKEN_UPDATE = "lastTokensUpdate"
        const val USERS_NAME = "users"
        // Chat image
        const val DEFAULT_PROFILE_URL = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg"
        // ADS
        const val REWARD_AD_UNIT_DEBUG = "ca-app-pub-3940256099942544/5224354917"
        const val REWARD_AD_UNIT_RELEASE = "ca-app-pub-8452230076174340/9855375573"

        const val INTERSTITIAL_AD_UNIT_DEBUG = "ca-app-pub-3940256099942544/1033173712"
        const val INTERSTITIAL_AD_UNIT_RELEASE = "ca-app-pub-8452230076174340/1738896617"

        const val IS_PREMIUM = "isPremium"

        const val KEY_FIRST_TIME = "firstTime"
        const val KEY_THEME = "theme"
        const val KEY_FONT_SIZE_MULTIPLIER =  "fontMultiplier"
        const val KEY_READ_LESSONS =  "readLessons"

        const val TEXT_SIZE_NORMAL = 0f
        const val TEXT_SIZE_BIG = 2f
        const val TEXT_SIZE_BIGGER = 4f

        const val ASSISTANT_MESSAGE_COST = 2
        const val CAMERA_MESSAGE_COST = 1
        // CHAT LIMIT - FEATURE
        const val CHAT_LIMIT_DEFAULT = 500
        const val CHAT_LIMIT_PREMIUM = 4000

        val CHAT_MSG_PADDING = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
        val PURPLE_COLOR = Color(0xFFA161F1)

    }
}