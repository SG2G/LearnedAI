
@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.sginnovations.asked.ui.chat

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableDoubleState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import com.sginnovations.asked.Constants.Companion.CHAT_LIMIT_DEFAULT
import com.sginnovations.asked.Constants.Companion.CHAT_LIMIT_PREMIUM
import com.sginnovations.asked.Constants.Companion.CHAT_MSG_PADDING
import com.sginnovations.asked.Constants.Companion.CONFIDENCE_RATE_LIMIT
import com.sginnovations.asked.R
import com.sginnovations.asked.data.database.entities.MessageEntity
import com.sginnovations.asked.data.database.util.Assistant
import com.sginnovations.asked.data.database.util.User
import com.sginnovations.asked.ui.chat.components.ChatSendIcon
import com.sginnovations.asked.ui.chat.components.ConfidenceDialog
import com.sginnovations.asked.ui.ui_components.chat.IconAssistantMsg
import com.sginnovations.asked.ui.ui_components.chat.TypingTextAnimation
import com.sginnovations.asked.ui.ui_components.chat.messages.ChatAiMessage
import com.sginnovations.asked.ui.ui_components.chat.messages.ChatUserMessage
import com.sginnovations.asked.ui.ui_components.tokens.TokenIcon
import com.sginnovations.asked.utils.CheckIsPremium.checkIsPremium
import com.sginnovations.asked.utils.NetworkUtils
import com.sginnovations.asked.viewmodel.AuthViewModel
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

private const val TAG = "Chat"

@Composable
fun ChatStateFul(
    vmCamera: CameraViewModel,
    vmChat: ChatViewModel,
    vmToken: TokenViewModel,
    vmAuth: AuthViewModel,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    val messages = vmChat.messages
    val chatAnimation = remember { mutableStateOf(false) }

    val userAuth = vmAuth.userAuth.collectAsState() //TODO IMPROVE IT
    val userName = userAuth.value?.userName
    val userProfileUrl = userAuth.value?.profilePictureUrl

    val tokens = vmToken.tokens

    val textConfidence = vmCamera.textConfidence
    val showConfidenceDialog = remember { mutableStateOf(false) }

    // Change navigator bar color
    val navigationBarColor = MaterialTheme.colorScheme.background.toArgb()
    SideEffect { (context as Activity).window.navigationBarColor = navigationBarColor }

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

        textConfidence = textConfidence,
        resetTextConfidence = { scope.launch { vmCamera.resetTextConfidence() } },

        tokens = tokens,

        userName = userName,
        userProfileUrl = userProfileUrl,

        onShowConfidenceDialog = { showConfidenceDialog.value = true },

        sendMessageToChatbot = { message ->
            scope.launch {
                vmChat.sendMessageToOpenaiApi(message)
                vmToken.oneLessToken()
            }
        },
    )
    if (showConfidenceDialog.value) {
        ConfidenceDialog(onDismiss = { showConfidenceDialog.value = false })
    }
}

@Composable
fun ChatStateLess(
    messages: MutableState<List<MessageEntity>>,
    chatAnimation: MutableState<Boolean>,
    listState: LazyListState,

    textConfidence: MutableDoubleState,
    resetTextConfidence: () -> Unit,

    tokens: StateFlow<Long>,

    userName: String?,
    userProfileUrl: String?,

    onShowConfidenceDialog: () -> Unit,
    sendMessageToChatbot: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    val clipboardManager = LocalClipboardManager.current
    val text = remember { mutableStateOf("") }

    var lastItemVisible by remember { mutableStateOf(false) }
    var lastIndex by remember { mutableIntStateOf(0) }

    var chatPlaceHolder by remember { mutableStateOf(false) }
    var userPlaceHolder by remember { mutableStateOf("") }
    val assistantPlaceHolder = stringResource(R.string.chat_thinking)

    var isPremium by remember { mutableStateOf(false) }

    val snackbarOffset = remember { Animatable(0f) }

    val backgroundColor = MaterialTheme.colorScheme.background

    /**
     * SnackBarAnimation
     */
    LaunchedEffect(snackbarHostState.currentSnackbarData) {
        if (snackbarHostState.currentSnackbarData != null) {
            // Animate snackbar in
            snackbarOffset.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
        } else {
            // Animate snackbar out
            snackbarOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        }
    }

    /**
     * TextConfidence snackbar
     */
    LaunchedEffect(textConfidence.doubleValue) {
        if (textConfidence.doubleValue < CONFIDENCE_RATE_LIMIT) {
            textConfidenceWarning(context, snackbarHostState, textConfidence, resetTextConfidence)
            resetTextConfidence()
        }
    }

    /**
     * Screen follow the chatbot writing going down
     */
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

    fun sendMessage(message: MutableState<String>) {
        if (text.value.isNotEmpty()) {
            if (NetworkUtils.isOnline(context)) {
                if (tokens.value > 0) {
                    sendMessageToChatbot(message.value)

                    userPlaceHolder = message.value
                    chatAnimation.value = true
                    chatPlaceHolder = true

                    message.value = ""
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.snackbar_insufficient_tokens),
                            actionLabel = context.getString(R.string.snackbar_ok),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.snackbar_no_internet_connection),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .background(Color.Transparent),
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(13f),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(padding)
                    .zIndex(14f)
                    .offset(y = snackbarOffset.value * (-75).dp),
                snackbar = { data ->
                    Snackbar(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.Transparent)
                            .zIndex(15f),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.warning_svgrepo_com),
                                contentDescription = "WarningAmber",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = data.visuals.message,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.weight(1f)
                            )
                            data.visuals.actionLabel?.let { actionLabel ->
                                IconButton(onClick = {
                                    when (actionLabel) {
                                        context.getString(R.string.snackbar_why) -> {
                                            onShowConfidenceDialog()
                                        }

                                        else -> {
                                            data.dismiss()
                                        }
                                    }
                                }) {
                                    Text(
                                        text = actionLabel,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }

        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 116.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                //.imePadding(),
                state = listState,
            ) {
                itemsIndexed(
                    items = messages.value
                ) { index, message ->
                    if (index == lastIndex) {
                        Box(Modifier.onGloballyPositioned {
                            lastItemVisible = listState.layoutInfo.visibleItemsInfo.any {
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
                                            ElevatedCard(
                                                modifier = Modifier.padding(horizontal = 16.dp),
                                                colors = CardDefaults.elevatedCardColors(
                                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                                )
                                            ) {
                                                Text(
                                                    modifier = Modifier.padding(CHAT_MSG_PADDING),
                                                    text = message.content,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                            }
                                        }
                                    } else {
                                        // Message static last AI msg
                                        ChatAiMessage(
                                            message.content,
                                            haveIcon = false,

                                            onSetClip = { text ->
                                                Log.d(TAG, "clipboardManager: text-> $text ")
                                                clipboardManager.setText(AnnotatedString(text))
                                            }
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
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.background,
                        RoundedCornerShape(
                            topStart = 25.dp,
                            topEnd = 25.dp
                        ) //TODO PREMIUM CANT SEE IT
                    )
            ) {
//                Button(onClick = { textConfidence.doubleValue = 0.3435678 }) { //TODO DELETE
//                    Text(text = "Testing")
//                }
                if (!isPremium) {
                    Row(
                        modifier = Modifier
                            .scale(0.8f)
                            .padding(start = 16.dp, top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                        onValueChange = {
                            if (isPremium) {
                                if (it.length <= CHAT_LIMIT_PREMIUM) {
                                    text.value = it
                                }
                            } else {
                                if (it.length <= CHAT_LIMIT_DEFAULT) {
                                    text.value = it
                                }
                            }
                        },
                        trailingIcon = {
                            Text(
                                "${text.value.length}/" +
                                        if (isPremium) CHAT_LIMIT_PREMIUM else CHAT_LIMIT_DEFAULT,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        },

                        modifier = Modifier
                            .weight(1f)
                            .imePadding(),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.enter_your_text),
                                fontSize = 14.sp
                            )
                        },
                        textStyle = TextStyle(fontSize = 14.sp),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        ),
                        maxLines = Int.MAX_VALUE,

                        )
                    ChatSendIcon(
                        text = text,
                    ) { sendMessage(it) }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.chat_supported_asked_can_make_mistakes_consider_checking_important_information),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        } // Column Free msg + Textfield
    }

}

private suspend fun textConfidenceWarning(
    context: Context,
    snackbarHostState: SnackbarHostState,
    textConfidence: MutableDoubleState,
    resetTextConfidence: () -> Unit,
) {

    try {
        val confidenceLevel = "%.2f".format(Locale.US, textConfidence.doubleValue).toDouble()
        val message = context.getString(R.string.snackbar_be_careful_the_message_may_contain_errors_confidence_level) + confidenceLevel
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = context.getString(R.string.snackbar_why),
            duration = SnackbarDuration.Long
        )
    } catch (e: NumberFormatException) {
        // Handle the parsing error
    }
    resetTextConfidence()
}
