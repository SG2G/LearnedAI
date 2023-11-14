package com.sginnovations.asked.ui.ui_components.chat.messages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.Constants.Companion.CHAT_MSG_PADDING
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.ui_components.chat.IconAssistantMsg

@Composable
fun ChatAiMessage(
    assistantMessage: String,

    onSetClip: (String) -> Unit,
) {
    val context = LocalContext.current
    val copyMsg = stringResource(R.string.copy_copied)

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
            modifier = Modifier
                .padding(CHAT_MSG_PADDING)
                .clickable {
                    onSetClip(assistantMessage)
                    Toast
                        .makeText(context, copyMsg, Toast.LENGTH_SHORT)
                        .show()
                },
            text = assistantMessage,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}