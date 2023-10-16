@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.sginnovations.learnedai.ui.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.learnedai.Constants.Companion.AI_NAME
import com.sginnovations.learnedai.Constants.Companion.DEFAULT_PROFILE_URL
import com.sginnovations.learnedai.data.database.entities.MessageEntity
import com.sginnovations.learnedai.data.database.util.Assistant
import com.sginnovations.learnedai.data.database.util.User
import com.sginnovations.learnedai.presentation.sign_in.GoogleAuthUiClient
import com.sginnovations.learnedai.ui.ui_components.chat.IconAssistantMsg
import com.sginnovations.learnedai.ui.ui_components.chat.IconUserMsg
import com.sginnovations.learnedai.ui.ui_components.chat.TypingTextAnimation
import com.sginnovations.learnedai.ui.ui_components.points.TokenIcon
import com.sginnovations.learnedai.viewmodel.ChatViewModel
import com.sginnovations.learnedai.viewmodel.TokenViewModel
import kotlinx.coroutines.launch

private const val TAG = "Chat"

@Composable
fun ChatStateFul(
    vmChat: ChatViewModel,
    vmToken: TokenViewModel,

    googleAuthUiClient: GoogleAuthUiClient,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val chatAnimation = remember { mutableStateOf(false) }
    val messages = vmChat.messages

    val userName = googleAuthUiClient.getSignedInUser()?.userName
    val userProfileUrl = googleAuthUiClient.getSignedInUser()?.profilePictureUrl

    LaunchedEffect(messages.value.size) {
        vmChat.setUpMessageHistory()
        if (messages.value.isNotEmpty()) {
            listState.scrollToItem(messages.value.size - 1)
        }
        Log.i(TAG, messages.value.toString())
    }

    ChatStateLess(
        messages = messages,
        chatAnimation = chatAnimation,
        listState = listState,

        userName = userName,
        userProfileUrl = userProfileUrl,

        ) { prompt ->
        scope.launch {
            vmChat.sendMessageToOpenaiApi(prompt)
            vmToken.oneLessToken()
        }
    }
}

@Composable
fun ChatStateLess(
    messages: MutableState<List<MessageEntity>>,
    chatAnimation: MutableState<Boolean>,
    listState: LazyListState,

    userName: String?,
    userProfileUrl: String?,

    onClick: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val text = remember { mutableStateOf("") }

    var lastItemVisible by remember { mutableStateOf(false) }
    var lastIndex  by remember { mutableIntStateOf(0) }

    var chatPlaceHolder by remember { mutableStateOf(false) }
    var userPlaceHolder by remember { mutableStateOf("") }
    var assistantPlaceHolder = "Thinking..."

    val chatMsgPadding = PaddingValues(start = 32.dp, top = 8.dp, end = 8.dp, bottom = 16.dp)

    LaunchedEffect(messages.value.size) {
        if (messages.value.isNotEmpty()) {
            lastIndex = messages.value.size - 1
        }
        chatPlaceHolder = false
        while (chatAnimation.value) {
            listState.animateScrollToItem(lastIndex)
        }
    }

    fun sendButton(text: MutableState<String>) {
        onClick(text.value)

        userPlaceHolder = text.value
        chatAnimation.value = true
        chatPlaceHolder = true
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                                listState.layoutInfo.visibleItemsInfo.any {
                                    it.index == lastIndex
                                }
                        }
                    ) {
                        if (message.role == Assistant.role) {
                            // Last AI message
                            Column {
                                IconAssistantMsg(AI_NAME)

                                if (chatAnimation.value) {
                                    // Animate the last message
                                    if (!chatPlaceHolder) {
                                        TypingTextAnimation(
                                            message.content,
                                            chatMsgPadding,
                                        ) { chatAnimation.value = false }
                                    } else {
                                        Text(
                                            modifier = Modifier.padding(chatMsgPadding),
                                            text = message.content
                                        )
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
                            if (userName != null) {
                                if (userProfileUrl != null) {
                                    IconUserMsg(userName, userProfileUrl)
                                }
                            } else {
                                IconUserMsg(User.role, DEFAULT_PROFILE_URL)
                            }

                            Text(
                                modifier = Modifier.padding(chatMsgPadding),
                                text = message.content
                            )
                        }
                    } else {
                        // Other AI msg
                        Column {
                            IconAssistantMsg(AI_NAME)

                            Text(
                                modifier = Modifier.padding(chatMsgPadding),
                                text = message.content
                            )
                        }
                    }
                }
            }
            item {
                if (chatPlaceHolder) {
                    Column {
                        if (userName != null) {
                            if (userProfileUrl != null) {
                                IconUserMsg(userName, userProfileUrl)
                            }
                        } else {
                            IconUserMsg(User.role, DEFAULT_PROFILE_URL)
                        }

                        Text(
                            modifier = Modifier.padding(chatMsgPadding),
                            text = userPlaceHolder
                        )
                    }
                    Column {
                        IconAssistantMsg(AI_NAME)

                        Text(
                            modifier = Modifier.padding(chatMsgPadding),
                            text = assistantPlaceHolder
                        )
                    }
                }
            }
        }

        /**
         * TextField
         */
        Row(
            modifier = Modifier
                .scale(0.8f)
                .padding(start = 16.dp)
        ) {
            Text(text = "-1")
            TokenIcon()
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "message")
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
                    .imePadding(),
                placeholder = { Text(text = "Enter your text.", fontSize = 14.sp) },
                textStyle = TextStyle(fontSize = 14.sp),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colorScheme.onBackground,
                ),
                maxLines = Int.MAX_VALUE
            )
            IconButton(
                onClick = {
                    scope.launch {
                        if (text.value.isNotEmpty()) {
                            sendButton(text)
                        }
                    }
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}
