@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.sginnovations.asked.ui.chat

import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.Constants.Companion.CHAT_MSG_PADDING
import com.sginnovations.asked.R
import com.sginnovations.asked.data.database.entities.MessageEntity
import com.sginnovations.asked.data.database.util.Assistant
import com.sginnovations.asked.data.database.util.User
import com.sginnovations.asked.ui.ui_components.chat.IconAssistantMsg
import com.sginnovations.asked.ui.ui_components.chat.TypingTextAnimation
import com.sginnovations.asked.ui.ui_components.chat.messages.ChatAiMessage
import com.sginnovations.asked.ui.ui_components.chat.messages.ChatUserMessage
import com.sginnovations.asked.ui.ui_components.tokens.TokenIcon
import com.sginnovations.asked.utils.CheckIsPremium.checkIsPremium
import com.sginnovations.asked.utils.NetworkUtils
import com.sginnovations.asked.viewmodel.AuthViewModel
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "Chat"

@Composable
fun ChatStateFul(
    vmChat: ChatViewModel,
    vmToken: TokenViewModel,
    vmAuth: AuthViewModel,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val chatAnimation = remember { mutableStateOf(false) }
    val messages = vmChat.messages

    val userAuth = vmAuth.userAuth.collectAsState()
    val userName = userAuth.value?.userName
    val userProfileUrl = userAuth.value?.profilePictureUrl

    // Change navigator bar color
    SideEffect {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (context as Activity).window.navigationBarColor = Color(0xFF161718).toArgb()
        }
    }

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

        vmToken = vmToken,

        userName = userName,
        userProfileUrl = userProfileUrl,

        ) { prompt ->
        scope.launch {
            vmChat.sendMessageToOpenaiApi(prompt)
            vmToken.lessTokenCheckPremium(-1)
        }
    }
}

@Composable
fun ChatStateLess(
    messages: MutableState<List<MessageEntity>>,
    chatAnimation: MutableState<Boolean>,
    listState: LazyListState,

    vmToken: TokenViewModel,

    userName: String?,
    userProfileUrl: String?,

    onClick: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val clipboardManager = LocalClipboardManager.current
    val text = remember { mutableStateOf("") }

    var lastItemVisible by remember { mutableStateOf(false) }
    var lastIndex by remember { mutableIntStateOf(0) }

    var chatPlaceHolder by remember { mutableStateOf(false) }
    var userPlaceHolder by remember { mutableStateOf("") }
    val assistantPlaceHolder = stringResource(R.string.chat_thinking)

    val backgroundColor = MaterialTheme.colorScheme.primaryContainer

    var isPremium by remember { mutableStateOf(false) }

    LaunchedEffect(messages.value.size) {
        isPremium = scope.async { checkIsPremium() }.await()
        if (messages.value.isNotEmpty()) {
            lastIndex = messages.value.size - 1
            chatPlaceHolder = false
            while (chatAnimation.value) {
                listState.animateScrollToItem(lastIndex)
            }
        }
    }

    fun chatButtonClicked(text: MutableState<String>) {
        if (NetworkUtils.isOnline(context)) {
            if (vmToken.tokens.value > 0) {
                onClick(text.value)

                userPlaceHolder = text.value
                chatAnimation.value = true
                chatPlaceHolder = true

                text.value = ""
            } else {
                Toast.makeText(context, "Insufficient Tokens", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .imePadding(),
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
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier
                                    .background(backgroundColor)
                                    .padding(16.dp)
                                    .fillMaxSize()
                            ) {
                                IconAssistantMsg()

                                if (chatAnimation.value) {
                                    // Animate the last message
                                    if (!chatPlaceHolder) {
                                        TypingTextAnimation(
                                            message.content,
                                        ) { chatAnimation.value = false }
                                    } else {
                                        Text(
                                            modifier = Modifier.padding(CHAT_MSG_PADDING),
                                            text = message.content
                                        )
                                    }
                                } else {
                                    // Message static last AI msg
                                    Text(
                                        modifier = Modifier
                                            .padding(CHAT_MSG_PADDING)
                                            .clickable {
                                                clipboardManager.setText(AnnotatedString(message.content))
                                            },
                                        text = message.content
                                    )
                                }
                            }
                        }
                    }
                } else {
                    if (message.role == User.role) {
                        // Other user msg
                        ChatUserMessage(
                            userName,
                            userProfileUrl,
                            message.content,

                            onSetClip = { text ->
                                clipboardManager.setText(AnnotatedString(text))
                            }
                        )
                    } else {
                        // Other AI msg
                        ChatAiMessage(
                            message.content,

                            onSetClip = { text ->
                                clipboardManager.setText(AnnotatedString(text))
                            }
                        )
                    }
                }
            }
            item {
                if (chatPlaceHolder) {
                    ChatUserMessage(
                        userName,
                        userProfileUrl,
                        userPlaceHolder,

                        onSetClip = { text ->
                            clipboardManager.setText(AnnotatedString(text))
                        }
                    )
                    ChatAiMessage(
                        assistantPlaceHolder,

                        onSetClip = { text ->
                            clipboardManager.setText(AnnotatedString(text))
                        }
                    )
                }
            }
        }
    }

        /**
         * TextField
         */
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.background(
                MaterialTheme.colorScheme.background,
                RoundedCornerShape(topStart =  25.dp, topEnd = 25.dp)
            )
        ) {
            Row(
                modifier = Modifier
                    .scale(0.8f)
                    .padding(start = 16.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isPremium) {
                    Text(text = "-1")
                    TokenIcon()
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = stringResource(R.string.message))
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
                        .imePadding(),
                    placeholder = { Text(text = stringResource(R.string.enter_your_text), fontSize = 14.sp) },
                    textStyle = TextStyle(fontSize = 14.sp),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    maxLines = Int.MAX_VALUE,
                )
                IconButton(
                    onClick = {
                        scope.launch {
                            if (text.value.isNotEmpty()) {
                                /**
                                 * Send message
                                 */
                                chatButtonClicked(text)

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
    } // Column Free msg + Textfield
}
