package com.sginnovations.asked.ui.ui_components.chat.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.Constants.Companion.CHAT_MSG_PADDING
import com.sginnovations.asked.ui.ui_components.chat.IconAssistantMsg

@Composable
fun ChatAiMessage(
    assistantPlaceHolder: String,
) {
    val backgroundColor = MaterialTheme.colorScheme.primaryContainer

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .background(backgroundColor)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        IconAssistantMsg()

        Text(
            modifier = Modifier.padding(CHAT_MSG_PADDING),
            text = assistantPlaceHolder
        )
    }
}