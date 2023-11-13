package com.sginnovations.asked

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.repository.RemoteConfigRepository
import javax.inject.Inject

class Constants {

    companion object {
        // Room names
        const val MESSAGE_TABLE_NAME = "message_table"
        const val CONVERSATION_TABLE_NAME = "conversation_table"
        // Chat Roles
        const val ROLE_USER = "user"
        const val ROLE_ASSISTANT = "assistant"
        // Categories
        const val CATEGORY_MATH = "math"
        // Chat
        const val AI_NAME = "Asked"
        // Tokens
        const val TOKENS_NAME = "tokens"
        const val USERS_NAME = "users"
        // Chat image
        const val DEFAULT_PROFILE_URL = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg"
        // ADS
        const val REWARD_AD_UNIT_DEBUG = "ca-app-pub-3940256099942544/5224354917"
        const val REWARD_AD_UNIT_RELEASE = "ca-app-pub-8452230076174340/9855375573"

        const val INTERSTITIAL_AD_UNIT_DEBUG = "ca-app-pub-3940256099942544/1033173712"
        const val INTERSTITIAL_AD_UNIT_RELEASE = "ca-app-pub-8452230076174340/1738896617"
        //
        const val CAMERA_TEXT = "Text"
        const val CAMERA_MATH = "Math"

        const val IS_PREMIUM = "isPremium"


        val CHAT_MSG_PADDING = PaddingValues(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)

    }
}