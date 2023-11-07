package com.sginnovations.asked.ui.ui_components.chat.messages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.Constants.Companion.CHAT_MSG_PADDING
import com.sginnovations.asked.Constants.Companion.DEFAULT_PROFILE_URL
import com.sginnovations.asked.ui.ui_components.chat.IconUserMsg

@Composable
fun ChatUserMessage(
    userName: String?,
    userProfileUrl: String?,

    message: String,

    onSetClip: (String) -> Unit,
) {

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        if (userName != null && userProfileUrl != null) {
            IconUserMsg(userProfileUrl)
        } else {
            IconUserMsg(DEFAULT_PROFILE_URL)
        }

        Text(
            modifier = Modifier
                .padding(CHAT_MSG_PADDING)
                .clickable {
                    onSetClip(message)
                },
            text = message
        )
    }
}