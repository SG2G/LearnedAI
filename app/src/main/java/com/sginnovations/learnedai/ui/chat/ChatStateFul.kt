@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.sginnovations.learnedai.ui.chat

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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

    val messages = vmChat.messages

    LaunchedEffect(messages) {
        vmChat.setUpMessageHistory()
        Log.d("yowchat", "StateFulChat: ${messages.value}")
        while (true) {
            listState.animateScrollToItem(index = messages.value.size - 1)
        }

    }

    StateLessChat(
        messages = messages,

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

    listState: LazyListState,

    onClick: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val text = remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState,
        ) {
            itemsIndexed(
                items = messages.value,
                itemContent = { _, message ->
                    if (message.role == User.role) {
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
                            AnimatedText(message.content)
//                            Text(
//                                modifier = Modifier.padding(16.dp),
//                                text = message.content
//                            )
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
                        text.value = ""

                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }
        )
    }
}

@Composable
fun AnimatedText(text: String) {
    val textLength by animateFloatAsState(
        targetValue = text.length.toFloat(),
        animationSpec = tween(2000, easing = LinearEasing), label = ""
    )

    Text(text = text.substring(0, textLength.toInt()))
}

