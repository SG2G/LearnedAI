package com.sginnovations.asked.presentation.ui.chat.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewChatSendIcon(
    text: MutableState<String?>,

    onSendNewMessage: () -> Unit,
) {
    IconButton(
        onClick = { onSendNewMessage() },
        modifier = Modifier.size(36.dp)
    ) {
        Icon(
            Icons.Default.Send,
            contentDescription = "Send",
            modifier = Modifier.size(24.dp),
            tint = if (text.value == "") MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary
        )
    }
}
@Composable
fun ChatSendIcon(
    writeMessage: MutableState<String>,

    sendMessage: (MutableState<String>) -> Unit,
) {
    IconButton(
        onClick = { sendMessage(writeMessage) },
        modifier = Modifier.size(36.dp)
    ) {
        Icon(
            Icons.Default.Send,
            contentDescription = "Send",
            modifier = Modifier.size(24.dp),
            tint = if (writeMessage.value == "") MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary
        )
    }
}