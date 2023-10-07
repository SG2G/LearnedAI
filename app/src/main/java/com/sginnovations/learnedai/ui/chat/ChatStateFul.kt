@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.sginnovations.learnedai.ui.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.sginnovations.learnedai.data.database.entities.MessageEntity
import com.sginnovations.learnedai.data.database.util.Assistant
import com.sginnovations.learnedai.data.database.util.User
import com.sginnovations.learnedai.viewmodel.ChatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StateFulChat(
    vmChat: ChatViewModel,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val chatMode = remember { mutableStateOf(false) }
    val messages = vmChat.messages

    LaunchedEffect(messages) {
        vmChat.setUpMessageHistory()
        listState.animateScrollToItem(index = messages.value.size - 1) //TODO DELETEE
    }

    StateLessChat(
        messages = messages,

        chatMode = chatMode,

        listState = listState

    ) { prompt ->
        scope.launch {
            vmChat.sendMessageToOpenaiApi(prompt)
        }
    }
}

@Composable
fun StateLessChat(
    messages: MutableState<List<MessageEntity>>,

    chatMode: MutableState<Boolean>,

    listState: LazyListState,

    onClick: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val text = remember { mutableStateOf("") }

    var lastItemVisible by remember { mutableStateOf(false) }

    var learnedPlaceHolder by remember { mutableStateOf(false) }
    var userPlaceHolder by remember { mutableStateOf("") }

    val lastIndex = messages.value.size - 1

    LaunchedEffect(messages.value.size) {
        listState.animateScrollToItem(messages.value.size - 1)
        learnedPlaceHolder = false
    }
    LaunchedEffect(Unit) { //TODO TRY TO DELETE IT
        while (true) {
            listState.animateScrollToItem(messages.value.size - 1)
        }
    }


    Column(
        Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .imePadding(),
            state = listState,
        ) {
            itemsIndexed(
                items = messages.value,
                itemContent = { index, message ->
                    if (index == lastIndex) {
                        Box(
                            Modifier.onGloballyPositioned {
                                lastItemVisible =
                                    listState.layoutInfo.visibleItemsInfo.any { it.index == lastIndex }
                            }
                        ) {
                            if (message.role == User.role) {
                                // Last user msg
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "Info Icon"
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = User.name)
                                    }
                                    Text(
                                        modifier = Modifier.padding(16.dp),
                                        text = userPlaceHolder
                                    )
                                }
                            } else {
                                // Last AI message
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "Info Icon"
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = Assistant.name)
                                    }
                                    if (chatMode.value) {
                                        // Animate the last message
                                        if (learnedPlaceHolder) {
                                            Text(
                                                modifier = Modifier.padding(16.dp),
                                                text = "Toy pensando"
                                            ) // When i enter this is the last message
                                        } else {
                                            TypingTextAnimation(message.content)
                                        }
                                    } else {
                                        // Message just enter the chat
                                        Text(
                                            modifier = Modifier.padding(16.dp),
                                            text = message.content
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        if (message.role == User.role) {
                            // Other user msg
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info Icon"
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = User.name)
                                }
                                Text(
                                    modifier = Modifier.padding(16.dp),
                                    text = message.content
                                )
                            }
                        } else {
                            // Other AI msg
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info Icon"
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = Assistant.name)
                                }
                                Text(
                                    modifier = Modifier.padding(16.dp),
                                    text = message.content
                                )
                            }
                        }
                    }
                }
            )
        }

        TextField(
            value = text.value,
            onValueChange = { text.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(),
            trailingIcon = {
                IconButton(onClick = {
                    scope.launch {
                        onClick(text.value)

                        userPlaceHolder = text.value

                        text.value = ""

                        learnedPlaceHolder = true
                        chatMode.value = true
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }
        )
    }
}


@Composable
fun TypingTextAnimation(message: String) {
    val typingState = remember { mutableStateOf("") }
    LaunchedEffect(message) {
        message.forEach { char ->
            delay(1)
            typingState.value += char
        }
    }
    Text(
        modifier = Modifier.padding(16.dp),
        text = typingState.value
    )
}


