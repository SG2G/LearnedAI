package com.sginnovations.asked.ui.main_bottom_bar.parental_chat

import android.app.Activity
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.Modifier
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
import com.sginnovations.asked.Constants
import com.sginnovations.asked.Constants.Companion.ASSISTANT_MESSAGE_COST
import com.sginnovations.asked.R
import com.sginnovations.asked.data.database.entities.MessageEntity
import com.sginnovations.asked.data.database.util.Assistant
import com.sginnovations.asked.data.database.util.User
import com.sginnovations.asked.data.report.Report
import com.sginnovations.asked.ui.chat.components.ChatSendIcon
import com.sginnovations.asked.ui.chat.components.messageOptionsIcon
import com.sginnovations.asked.ui.ui_components.chat.IconAssistantMsg
import com.sginnovations.asked.ui.ui_components.chat.NoTokensDialog
import com.sginnovations.asked.ui.ui_components.chat.ReportDialog
import com.sginnovations.asked.ui.ui_components.chat.TokenCostDisplay
import com.sginnovations.asked.ui.ui_components.chat.TypingTextAnimation
import com.sginnovations.asked.ui.ui_components.chat.messages.ChatAiMessage
import com.sginnovations.asked.ui.ui_components.chat.messages.ChatUserMessage
import com.sginnovations.asked.utils.CheckIsPremium
import com.sginnovations.asked.utils.NetworkUtils
import com.sginnovations.asked.viewmodel.AssistantViewModel
import com.sginnovations.asked.viewmodel.AuthViewModel
import com.sginnovations.asked.viewmodel.ReportViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val TAG = "Chat"

@Composable
fun AssistantChatStateFul(
    vmAssistant: AssistantViewModel,
    vmToken: TokenViewModel,
    vmAuth: AuthViewModel,
    vmReport: ReportViewModel,

    onNavigateSubscriptionScreen: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val showNoTokensDialog = remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    val messages = vmAssistant.messages
    val chatAnimation = remember { mutableStateOf(false) }

    val userAuth = vmAuth.userAuth.collectAsState() //TODO IMPROVE IT
    val userName = userAuth.value?.userName
    val userProfileUrl = userAuth.value?.profilePictureUrl

    val tokens = vmToken.tokens

    val conversationCostToken = vmAssistant.newConversationCostTokens()

    val reportText = remember { mutableStateOf("") }
    val showReportDialog = remember { mutableStateOf(false) }

    // Change navigator bar color
    val navigationBarColor = MaterialTheme.colorScheme.background.toArgb()
    SideEffect { (context as Activity).window.navigationBarColor = navigationBarColor }

    LaunchedEffect(messages.value.size) {
        vmAssistant.setUpMessageHistory()

        if (messages.value.isNotEmpty()) {
            listState.scrollToItem(messages.value.size - 1)
        }
        Log.i(TAG, messages.value.toString())
    }

    AssistantChatStateLess(
        messages = messages,
        chatAnimation = chatAnimation,
        listState = listState,

        showNoTokensDialog = showNoTokensDialog,

        tokens = tokens,

        conversationCostToken = conversationCostToken,

        userName = userName,
        userProfileUrl = userProfileUrl,

        onReportMessage = { text ->
            reportText.value = text
            showReportDialog.value = true
        },

        ) { message ->
        scope.launch {
            vmAssistant.sendMessageToOpenaiApi(message)
        }
    }
    if (showNoTokensDialog.value) {
        NoTokensDialog(
            onDismissRequest = { showNoTokensDialog.value = false },
            onSeePremiumSubscription = {
                showNoTokensDialog.value = false
                onNavigateSubscriptionScreen()
            }
        )
    }
    if (showReportDialog.value) {
        ReportDialog(
            reportText = reportText.value,

            onDismissRequest = { showReportDialog.value = false },
            onSendReport = {
                val report = Report(reportText.value, null)
                vmReport.sendReport(report)
                showReportDialog.value = false
                Toast.makeText(context, "Report sent successfully ", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun AssistantChatStateLess(
    messages: MutableState<List<MessageEntity>>,
    chatAnimation: MutableState<Boolean>,
    listState: LazyListState,

    showNoTokensDialog: MutableState<Boolean>,

    tokens: StateFlow<Long>,

    conversationCostToken: String,

    userName: String?,
    userProfileUrl: String?,

    onReportMessage: (String) -> Unit,

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
     * Screen follow the chatbot writing going down
     */
    LaunchedEffect(messages.value.size) {
        isPremium = scope.async { CheckIsPremium.checkIsPremium() }.await()
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

                if (isPremium) {
                    sendMessageToChatbot(message.value)

                    userPlaceHolder = message.value
                    chatAnimation.value = true
                    chatPlaceHolder = true

                    message.value = ""
                } else {
                    if (tokens.value >= ASSISTANT_MESSAGE_COST) {
                        sendMessageToChatbot(message.value)

                        userPlaceHolder = message.value
                        chatAnimation.value = true
                        chatPlaceHolder = true

                        message.value = ""
                    } else {
                        showNoTokensDialog.value = true
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
                        containerColor = MaterialTheme.colorScheme.surface,
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
                                IconButton(onClick = { data.dismiss() }) {
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
                                        .fillMaxSize()
                                ) {
                                    if (chatAnimation.value) {
                                        // Animate the last message
                                        if (!chatPlaceHolder) {
                                            TypingTextAnimation(
                                                true,
                                                message.content,
                                            ) { chatAnimation.value = false }
                                        } else {
                                            Row(
                                                verticalAlignment = Alignment.Top,
                                                modifier = Modifier
                                                    .background(backgroundColor)
                                                    .padding(16.dp)
                                                    .fillMaxSize()
                                            ) {
                                                IconAssistantMsg()
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
                                            message.content,
                                            isAssistant = true,

                                            onReportMessage = { onReportMessage(it) },
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
                                assistantMessage = message.content,
                                isAssistant = true,

                                onReportMessage = { onReportMessage(it) },
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
                            assistantMessage = assistantPlaceHolder,
                            isAssistant = true,

                            onReportMessage = { onReportMessage(it) },
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
                if (!isPremium) {
                    TokenCostDisplay(
                        tokenCost = conversationCostToken
                    )
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
                                if (it.length <= Constants.CHAT_LIMIT_PREMIUM) {
                                    text.value = it
                                }
                            } else {
                                if (it.length <= Constants.CHAT_LIMIT_DEFAULT) {
                                    text.value = it
                                }
                            }
                        },
                        trailingIcon = {
                            Text(
                                "${text.value.length}/" +
                                        if (isPremium) Constants.CHAT_LIMIT_PREMIUM else Constants.CHAT_LIMIT_DEFAULT,
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
//}