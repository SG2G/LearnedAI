package com.sginnovations.asked.presentation.ui.main_bottom_bar.parental_chat

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import com.sginnovations.asked.Constants.Companion.ASSISTANT_MESSAGE_COST
import com.sginnovations.asked.data.database.entities.MessageEntity
import com.sginnovations.asked.data.report.Report
import com.sginnovations.asked.presentation.ui.chat.components.ChatTextField
import com.sginnovations.asked.presentation.ui.chat.components.ChatsSnackBar
import com.sginnovations.asked.presentation.ui.chat.components.LaunchedEffectScreenFollowChatBot
import com.sginnovations.asked.presentation.ui.chat.components.LaunchedEffectSnackBarAnimation
import com.sginnovations.asked.presentation.ui.chat.components.MessagesDisplay
import com.sginnovations.asked.presentation.ui.ui_components.chat.NoTokensDialog
import com.sginnovations.asked.presentation.ui.ui_components.chat.ReportDialog
import com.sginnovations.asked.presentation.viewmodel.AssistantViewModel
import com.sginnovations.asked.presentation.viewmodel.ReportViewModel
import com.sginnovations.asked.presentation.viewmodel.TokenViewModel
import com.sginnovations.asked.utils.CheckIsPremium
import com.sginnovations.asked.utils.NetworkUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "Chat Assistant"

@Composable
fun AssistantChatStateFul(
    vmAssistant: AssistantViewModel,
    vmToken: TokenViewModel,
    vmReport: ReportViewModel,

    onNavigateSubscriptionScreen: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()
    val clipboardManager = LocalClipboardManager.current

    val conversationCostToken = vmAssistant.newConversationCostTokens()

    val tokens = vmToken.tokens
    val messages = vmAssistant.messages

    val writeMessage = vmAssistant.writeMessage

    val userPlaceHolder = vmAssistant.userPlaceHolder
    val chatAnimation = vmAssistant.chatAnimation
    val chatPlaceHolder = vmAssistant.chatPlaceHolder

    val isPremium = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isPremium.value = scope.async { CheckIsPremium.checkIsPremium() }.await()
    }

    val reportText = remember { mutableStateOf("") }

    val showNoTokensDialog = remember { mutableStateOf(false) }
    val showReportDialog = remember { mutableStateOf(false) }

    // Change Navigation Bar Color
    val navigationBarColor = MaterialTheme.colorScheme.background.toArgb()
    SideEffect { (context as Activity).window.navigationBarColor = navigationBarColor }

    LaunchedEffect(messages.value.size) {
        vmAssistant.setUpMessageHistory()

        if (messages.value.isNotEmpty()) {
            listState.scrollToItem(messages.value.size - 1)
        }

        Log.i(TAG, "All Messages -> " + messages.value.toString())
    }

    /**
     * Dialogs
     */
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

    /**
     * StateLess
     */
    AssistantChatStateLess(
        messages = messages,
        writeMessage = writeMessage,

        userPlaceHolder = userPlaceHolder,
        chatAnimation = chatAnimation,
        chatPlaceHolder = chatPlaceHolder,

        isPremium = isPremium,

        listState = listState,

        conversationCostToken = conversationCostToken,

        onReportMessage = { text ->
            reportText.value = text
            showReportDialog.value = true
        },
        onSetClip = { text ->
            clipboardManager.setText(AnnotatedString(text))
        },

        onSendMessage = { message ->
            if (message.isNotEmpty()) {
                if (NetworkUtils.isOnline(context)) {
                    if (isPremium.value) {
                        scope.launch { vmAssistant.sendMessageToOpenaiApi(message) }

                        vmAssistant.messageSent()
                    } else {
                        // No Premium
                        if (tokens.value >= ASSISTANT_MESSAGE_COST) {
                            scope.launch { vmAssistant.sendMessageToOpenaiApi(message) }

                            vmAssistant.messageSent()
                        } else {
                            // No Tokens
                            showNoTokensDialog.value = true
                        }
                    }
                } else {
                    // Internet Error
                }
            } else {
                // Empty Message
            }
        }
    )

}

@Composable
fun AssistantChatStateLess(
    messages: MutableState<List<MessageEntity>>,
    writeMessage: MutableState<String>,

    userPlaceHolder: MutableState<String>,
    chatAnimation: MutableState<Boolean>,
    chatPlaceHolder: MutableState<Boolean>,

    isPremium: MutableState<Boolean>,

    listState: LazyListState,

    conversationCostToken: String,

    onReportMessage: (String) -> Unit,
    onSetClip: (String) -> Unit,

    onSendMessage: (String) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val lastIndex = remember { mutableIntStateOf(0) }

    val snackbarOffset = remember { Animatable(0f) }

    /**
     * LaunchedEffectSnackBarAnimation
     */
    LaunchedEffectSnackBarAnimation(
        snackbarHostState = snackbarHostState,
        snackbarOffset = snackbarOffset,
    )

    /**
     * Screen follow the chatbot writing going down
     */
    LaunchedEffectScreenFollowChatBot(
        messages = messages,

        listState = listState,

        lastIndex = lastIndex,

        chatPlaceHolder = chatPlaceHolder,
        chatAnimation = chatAnimation,
    )


    /**
     * SnackBar
     */
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

                    ChatsSnackBar(
                        data = data,

                        onClickSnackBar = { _ ->
                            data.dismiss()
                        },
                    )
                }

            )
        }

        /**
         * Message Display
         */
        MessagesDisplay(
            isAssistant = true,

            messages = messages,

            listState = listState,
            lastIndex = lastIndex,

            userPlaceHolder = userPlaceHolder,
            chatAnimation = chatAnimation,
            chatPlaceHolder = chatPlaceHolder,

            onReportMessage = { onReportMessage(it) },
            onSetClip = { onSetClip(it) },
        )

        /**
         * TextField
         */
        ChatTextField(
            writeMessage = writeMessage,

            isPremium = isPremium

        ) { onSendMessage(it) } //onSendMessage
    }
}

//scope.launch {
//    snackbarHostState.showSnackbar(
//        message = context.getString(R.string.snackbar_no_internet_connection),
//        duration = SnackbarDuration.Short
//    )
//}