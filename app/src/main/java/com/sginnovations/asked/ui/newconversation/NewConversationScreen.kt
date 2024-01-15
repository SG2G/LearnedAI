package com.sginnovations.asked.ui.newconversation

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.Constants
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.chat.components.NewChatSendIcon
import com.sginnovations.asked.ui.newconversation.components.NewConversationSuggestions
import com.sginnovations.asked.ui.ui_components.chat.NoTokensDialog
import com.sginnovations.asked.ui.ui_components.chat.TokenCostDisplay
import com.sginnovations.asked.ui.ui_components.tokens.TokenIcon
import com.sginnovations.asked.utils.CheckIsPremium
import com.sginnovations.asked.utils.NetworkUtils
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "NewConversationStateFul"

@Composable
fun NewConversationStateFul(
    vmChat: ChatViewModel,
    vmCamera: CameraViewModel,
    vmToken: TokenViewModel,

    onNavigateChat: () -> Unit,

    onNavigateSubscriptionScreen: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val showNoTokensDialog = remember { mutableStateOf(false) }

    val text = remember { mutableStateOf<String?>("") }
    text.value = vmCamera.imageToText.value

    val isLoading = vmCamera.isLoading

    val newConversationCostToken = vmChat.newConversationCostTokens()
    var isPremium by remember { mutableStateOf(false) }
    val tokens = vmToken.tokens

    // Change navigator bar color
    val navigationBarColor = MaterialTheme.colorScheme.background.toArgb()
    SideEffect { (context as Activity).window.navigationBarColor = navigationBarColor }

    LaunchedEffect(Unit) {
        isPremium = scope.async { CheckIsPremium.checkIsPremium() }.await()
    }
    NewConversationStateLess(
        text = text,

        newConversationCostToken = newConversationCostToken,

        isLoading = isLoading,
        isPremium = isPremium,

        onSendNewMessage = {
            val processText = text.value
            text.value = ""
            vmCamera.imageToText.value = ""

            vmChat.setUpNewConversation()

            if (processText?.isNotEmpty() == true) {
                if (NetworkUtils.isOnline(context)) {

                    if (isPremium) {
                        sendNewMessage(
                            scope,
                            context,

                            processText,

                            vmCamera,
                            vmChat
                        ) {
                            onNavigateChat()
                        }
                    } else {
                        if (tokens.value >= Constants.CAMERA_MESSAGE_COST) {
                            sendNewMessage(
                                scope,
                                context,

                                processText,

                                vmCamera,
                                vmChat
                            ) {
                                onNavigateChat()
                            }
                        } else {
                            showNoTokensDialog.value = true
                        }
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.snackbar_no_internet_connection), Toast.LENGTH_SHORT).show()
                }
            }
        }
    )
    if (showNoTokensDialog.value) {
        NoTokensDialog(
            onDismissRequest = { showNoTokensDialog.value = false },
            onSeePremiumSubscription = {
                showNoTokensDialog.value = false
                onNavigateSubscriptionScreen()
            }
        )
    }
}

@Composable
fun NewConversationStateLess(
    text: MutableState<String?>,

    newConversationCostToken: String,
    isPremium: Boolean,

    isLoading: MutableState<Boolean>,

    onSendNewMessage: () -> Unit,
) {

    NewConversationSuggestions()

    /**
     * Loading
     */
    if (isLoading.value) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    /**
     * Chats
     */
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.background(
                MaterialTheme.colorScheme.background,
                RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
            )
        ) {
            if (!isPremium) {
                TokenCostDisplay(
                    tokenCost = newConversationCostToken
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
                    value = text.value.toString(),
                    onValueChange = { text.value = it },
                    modifier = Modifier
                        .weight(1f)
                        .imePadding(),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.newchat_placeholder_enter_your_text),
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
                NewChatSendIcon(
                    text = text
                ) { onSendNewMessage() }

            }
        }

    }
}

fun sendNewMessage(
    //TODO REPEAT CROP
    scope: CoroutineScope,
    context: Context,
    text: String,
    vmCamera: CameraViewModel,
    vmChat: ChatViewModel,

    onNavigateChat: () -> Unit,
) {
    scope.launch {
        // New Message
        if (NetworkUtils.isOnline(context)) {
            vmCamera.isLoading.value = true

            // GPT call
            val deferred = async { vmChat.sendMessageToOpenaiApi(text) }
            deferred.await()

            vmCamera.isLoading.value = false

            onNavigateChat()
        } else {
            Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show()
        }
    }
}
