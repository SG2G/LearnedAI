package com.sginnovations.asked.presentation.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.Constants
import com.sginnovations.asked.data.database.entities.MessageEntity
import com.sginnovations.asked.data.database.util.Assistant
import com.sginnovations.asked.data.database.util.User
import com.sginnovations.asked.presentation.ui.ui_components.chat.IconAssistantMsg
import com.sginnovations.asked.presentation.ui.ui_components.chat.IconMsg
import com.sginnovations.asked.presentation.ui.ui_components.chat.TypingTextAnimation
import com.sginnovations.asked.presentation.ui.ui_components.chat.messages.ChatAiMessage
import com.sginnovations.asked.presentation.ui.ui_components.chat.messages.ChatUserMessage

@Composable
fun MessagesDisplay(
    isAssistant: Boolean,

    messages: MutableState<List<MessageEntity>>,

    listState: LazyListState,
    lastIndex: MutableIntState,

    userPlaceHolder: MutableState<String>,
    chatAnimation: MutableState<Boolean>,
    chatPlaceHolder: MutableState<Boolean>,

    onReportMessage: (String) -> Unit,
    onSetClip: (String) -> Unit,

    ) {
    val backgroundColor = MaterialTheme.colorScheme.background

    var lastItemVisible by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 116.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            state = listState,
        ) {
            itemsIndexed(
                items = messages.value
            ) { index, message ->
                if (index == lastIndex.intValue) {
                    Box(Modifier.onGloballyPositioned {
                        lastItemVisible = listState.layoutInfo.visibleItemsInfo.any {
                            it.index == lastIndex.intValue
                        }
                    }
                    ) {
                        if (message.role == Assistant.role) {
                            // Last AI message
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = androidx.compose.ui.Modifier
                                    .background(backgroundColor)
                                    .fillMaxSize()
                            ) {
                                if (chatAnimation.value) {
                                    // Animate the last message
                                    if (!chatPlaceHolder.value) {
                                        TypingTextAnimation(
                                            isAssistant,
                                            message.content,
                                        ) { chatAnimation.value = false }
                                    } else {
                                        Row(
                                            verticalAlignment = Alignment.Top,
                                            modifier = androidx.compose.ui.Modifier
                                                .background(backgroundColor)
                                                .padding(16.dp)
                                                .fillMaxSize()
                                        ) {
                                            if (isAssistant) IconAssistantMsg() else IconMsg()
                                            Column {
                                                ElevatedCard(
                                                    modifier = Modifier.padding(horizontal = 16.dp),
                                                    colors = CardDefaults.elevatedCardColors(
                                                        containerColor = MaterialTheme.colorScheme.surface
                                                    )
                                                ) {
                                                    Text(
                                                        modifier = Modifier.padding(Constants.CHAT_MSG_PADDING),
                                                        text = message.content,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                                messageOptionsIcon(
                                                    onReportMessage = {},
                                                    onSetClip = {}
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    // Message static last AI msg
                                    ChatAiMessage(
                                        assistantMessage = message.content,
                                        isAssistant = isAssistant,

                                        onReportMessage = { onReportMessage(it) },
                                        onSetClip = { onSetClip(it) }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    if (message.role == User.role) {
                        // Other user msg
                        ChatUserMessage(
                            message.content,

                            ) { onSetClip(it) }
                    } else {
                        // Other AI msg
                        ChatAiMessage(
                            assistantMessage = message.content,
                            isAssistant = isAssistant,

                            onReportMessage = { onReportMessage(it) },
                            onSetClip = { onSetClip(it) }
                        )
                    }
                }
            }
            item {
                if (chatPlaceHolder.value) {
                    ChatUserMessage(
                        userPlaceHolder.value,

                        ) { onSetClip(it) }
                    LoadingLottieAnimation()
                }
            }
        }
    }
}