@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.sginnovations.learnedai.ui.chat

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.learnedai.Constants.Companion.AI_NAME
import com.sginnovations.learnedai.data.database.entities.MessageEntity
import com.sginnovations.learnedai.data.database.util.Assistant
import com.sginnovations.learnedai.data.database.util.User
import com.sginnovations.learnedai.ui.components.chat.IconUserMsg
import com.sginnovations.learnedai.viewmodel.ChatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StateFulChat(
    vmChat: ChatViewModel,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val chatAnimation = remember { mutableStateOf(false) }
    val messages = vmChat.messages

    LaunchedEffect(messages) {
        vmChat.setUpMessageHistory()
        listState.scrollToItem(messages.value.size - 1)
    }

    StateLessChat(
        messages = messages,

        chatAnimation = chatAnimation,

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

    chatAnimation: MutableState<Boolean>,

    listState: LazyListState,

    onClick: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val text = remember { mutableStateOf("") }

    var lastItemVisible by remember { mutableStateOf(false) }

    var learnedPlaceHolder by remember { mutableStateOf(false) }
    var userPlaceHolder by remember { mutableStateOf("") }

    val lastIndex = messages.value.size - 1

    val chatMsgPadding = PaddingValues(start = 32.dp, top = 8.dp, end = 8.dp, bottom = 16.dp)
    val chatTitleColor = MaterialTheme.colorScheme.onSurfaceVariant

    LaunchedEffect(messages.value.size) {
        learnedPlaceHolder = false
        while (chatAnimation.value) {
            listState.animateScrollToItem(messages.value.size - 1)
        }
    }



    Column(
        Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .imePadding()
                .padding(8.dp),
            state = listState,
        ) {
            itemsIndexed(
                items = messages.value
            ) { index, message ->
                if (index == lastIndex) {
                    Box(
                        Modifier.onGloballyPositioned {
                            lastItemVisible =
                                listState.layoutInfo.visibleItemsInfo.any { it.index == lastIndex }
                        }
                    ) {
                        if (message.role == Assistant.role) {
                            // Last AI message
                            Column {
                                IconUserMsg(AI_NAME, chatTitleColor)

                                if (chatAnimation.value) {
                                    // Animate the last message
                                    if (!learnedPlaceHolder) {
                                        TypingTextAnimation(
                                            message.content,
                                            chatMsgPadding,
                                            lastItemVisible,
                                        ) { chatAnimation.value = false }
                                    }
                                } else {
                                    // Message static last AI msg
                                    Text(
                                        modifier = Modifier.padding(chatMsgPadding),
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
                            IconUserMsg(message.role, chatTitleColor)
                            Text(
                                modifier = Modifier.padding(chatMsgPadding),
                                text = message.content
                            )
                        }
                    } else {
                        // Other AI msg
                        Column {
                            IconUserMsg(AI_NAME, chatTitleColor)

                            Text(
                                modifier = Modifier.padding(chatMsgPadding),
                                text = message.content
                            )
                        }
                    }
                }
            }
            item {
                if (learnedPlaceHolder) {
                    Column {
                        IconUserMsg(User.role, chatTitleColor)

                        Text(
                            modifier = Modifier.padding(chatMsgPadding),
                            text = userPlaceHolder
                        )
                    }
                    Column {
                        IconUserMsg(AI_NAME, chatTitleColor)

                        Text(
                            modifier = Modifier.padding(chatMsgPadding),
                            text = "Tamo procesando tu solicitud rey"
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                modifier = Modifier
                    .weight(1f)
                    .imePadding()
                    .height(48.dp),
                placeholder = { Text(text = "Enter your text.", fontSize = 14.sp) },
                textStyle = TextStyle(fontSize = 14.sp),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colorScheme.onBackground,
                )
            )
            IconButton(
                onClick = {
                    scope.launch {
                        if (text.value.isNotEmpty()) {
                            onClick(text.value)
                            userPlaceHolder = text.value
                            text.value = ""
                            chatAnimation.value = true
                            learnedPlaceHolder = true
                        }
                    }
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun TypingTextAnimation(
    message: String,
    msgPadding: PaddingValues,
    lastItemVisible: Boolean,

    onStopTextAnimation: () -> Unit,
) {
    val typingState = remember { mutableStateOf("") }

    val vibrator = LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    LaunchedEffect(message) {
        var counter = 0
        message.forEach { char ->
            if (counter % 10 == 0) {
                delay(1)
            }
            typingState.value += char

            // Vibration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // For API 26 and higher
                val effect =
                    VibrationEffect.createOneShot(1, 5)
                vibrator.vibrate(effect)
            }

            counter++
        }
        onStopTextAnimation()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(msgPadding)
    ) {
        Text(text = typingState.value)
        Icon(Icons.Default.Info, contentDescription = null)
    }
}


