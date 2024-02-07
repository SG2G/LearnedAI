package com.sginnovations.asked.ui.main_bottom_bar.parental_chat

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.Constants
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.chat.components.NewChatSendIcon
import com.sginnovations.asked.ui.main_bottom_bar.parental_chat.dialog.WhyTrustAiDialog
import com.sginnovations.asked.ui.ui_components.chat.NoTokensDialog
import com.sginnovations.asked.ui.ui_components.chat.TokenCostDisplay
import com.sginnovations.asked.utils.CheckIsPremium
import com.sginnovations.asked.utils.NetworkUtils
import com.sginnovations.asked.viewmodel.AssistantViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "AssistantNewConversationStateFul"

@Composable
fun AssistantNewConversationStateFul(
    vmAssistant: AssistantViewModel,
    vmToken: TokenViewModel,

    onNavigateChat: () -> Unit,

    onNavigateSubscriptionScreen: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val showNoTokensDialog = remember { mutableStateOf(false) }
    val showTrustAI = remember { mutableStateOf(false) }

    val text = remember { mutableStateOf<String?>("") }
    text.value = vmAssistant.firstMessage.value

    val isLoading = vmAssistant.isLoading

    val newConversationCostToken = vmAssistant.newConversationCostTokens()
    var isPremium by remember { mutableStateOf(false) }
    val tokens = vmToken.tokens

    LaunchedEffect(Unit) {
        isPremium = scope.async { CheckIsPremium.checkIsPremium() }.await()
    }

    // Change navigator bar color
    val navigationBarColor = MaterialTheme.colorScheme.background.toArgb()
    SideEffect { (context as Activity).window.navigationBarColor = navigationBarColor }

    AssistantNewConversationStateLess(
        text = text,

        newConversationCostToken = newConversationCostToken,

        isLoading = isLoading,
        isPremium = isPremium,

        onWhyTrust = { showTrustAI.value = true },

        onSendNewMessage = {
            val processText = text.value
            text.value = ""

            vmAssistant.setUpNewConversation()

            if (processText?.isNotEmpty() == true) {
                if (NetworkUtils.isOnline(context)) {

                    if (isPremium) {
                        sendNewMessage(
                            context,

                            processText,

                            vmAssistant,
                        ) {
                            onNavigateChat()
                        }
                    } else {
                        if (tokens.value >= Constants.ASSISTANT_MESSAGE_COST) {
                            sendNewMessage(
                                context,

                                processText,

                                vmAssistant,
                            ) {
                                onNavigateChat()
                            }
                        } else {
                            showNoTokensDialog.value = true
//                            Toast.makeText(context, context.getString(R.string.snackbar_insufficient_tokens), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.snackbar_no_internet_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    )
    /**
     * No Tokens Dialog
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
    if (showTrustAI.value) {
        WhyTrustAiDialog(
            onDismissRequest = { showTrustAI.value = false },
            onAcceptRequest = { showTrustAI.value = false },
        )
    }
}

@Composable
fun AssistantNewConversationStateLess(
    text: MutableState<String?>,

    newConversationCostToken: String,

    isLoading: MutableState<Boolean>,
    isPremium: Boolean,

    onWhyTrust: () -> Unit,

    onSendNewMessage: () -> Unit,
) {

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
     * Text In Mid
     */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 84.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.asked_assistant),
            contentDescription = "Icon",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
        )
        Text(
            modifier = Modifier.padding(horizontal = 64.dp, vertical = 4.dp),
            text = stringResource(R.string.how_can_i_help_you_today),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )
        TextButton(onClick = { onWhyTrust() }) {
            Text(
                text = stringResource(R.string.why_trust_ai),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }

    /**
     * TextField ++
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
            NewAssistantSuggestions(
                onUseSuggestion = { suggestedText ->
                    text.value = suggestedText
                }
            )
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
    context: Context,

    message: String,

    vmAssistant: AssistantViewModel,

    onNavigateChat: () -> Unit,
) {
    //TODO vmAssistant CHECK

    Log.d(TAG, "sendNewMessage: sending message")
    if (NetworkUtils.isOnline(context)) {
        vmAssistant.sendNewMessage(
            message = message,

            onNavigateChat = { onNavigateChat() }
        )
    } else {
        Toast.makeText(context, context.getString(R.string.internet_error), Toast.LENGTH_SHORT)
            .show()
    }

}
