package com.sginnovations.asked.ui.newconversation

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.newconversation.components.SubTitleChatUseExample
import com.sginnovations.asked.ui.newconversation.components.TitleChatUseExample
import com.sginnovations.asked.ui.ui_components.tokens.TokenIcon
import com.sginnovations.asked.utils.NetworkUtils
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "NewConversationStateFul"

@Composable
fun NewConversationStateFul(
    vmChat: ChatViewModel,
    vmCamera: CameraViewModel,

    onNavigateChat: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val text = remember { mutableStateOf<String?>("") }
    text.value = vmCamera.imageToText.value

    val idConversation = vmChat.idConversation.intValue
    val isLoading = vmCamera.isLoading

    val newConversationCostToken = vmChat.newConversationCostTokens()

    SideEffect { (context as Activity).window.navigationBarColor = Color(0xFF161718).toArgb() }

    NewConversationStateLess(
        text = text,

        newConversationCostToken = newConversationCostToken,

        isLoading = isLoading

    ) {
        val processText = text.value
        text.value = ""
        vmCamera.imageToText.value = ""

        if (processText != null) {
            sendNewMessage(
                scope,
                context,

                processText,
                idConversation,

                vmCamera,
                vmChat
            ) {
                onNavigateChat()
            }
        }
    }
}

@Composable
fun NewConversationStateLess(
    text: MutableState<String?>,

    newConversationCostToken: String,

    isLoading: MutableState<Boolean>,

    onSendNewMessage: () -> Unit,
) {
    val elevatedCardPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ElevatedCard(
            modifier = Modifier.padding(elevatedCardPadding),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
            ) {
                TitleChatUseExample(
                    painterResource = painterResource(id = R.drawable.icons8_brain_96),
                    text = stringResource(R.string.new_conversation_resolve)
                )
                SubTitleChatUseExample(text = stringResource(R.string.new_conversation_take_photos_of_your_text_or_mathematical_problems_and_learn_by_talking_to_the_chat))
            }
        }
        ElevatedCard(
            modifier = Modifier.padding(elevatedCardPadding),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
            ) {
                TitleChatUseExample(
                    painterResource = painterResource(id = R.drawable.icons8_write_64),
                    text = stringResource(R.string.new_conversation_write)
                )
                SubTitleChatUseExample(text = stringResource(R.string.new_conversation_give_me_a_summary_of_the_book_to_kill_a_mockingbird))
            }
        }

        ElevatedCard(
            modifier = Modifier.padding(elevatedCardPadding),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
            ) {
                TitleChatUseExample(
                    painterResource = painterResource(id = R.drawable.icons8_magic_wand_64),
                    text = stringResource(R.string.new_conversation_creativity)
                )
                SubTitleChatUseExample(text = stringResource(R.string.new_conversation_create_a_slogan_for_a_dogs_social_media))
            }
        }
    }

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
            if (isLoading.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .scale(0.8f)
                    .padding(start = 16.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TokenIcon()
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text =
                    when (newConversationCostToken.toInt()) {
                        0 -> stringResource(R.string.new_conversation_free_message)
                        else -> {
                            stringResource(
                                R.string.new_conversation_cost_message,
                                newConversationCostToken
                            )
                        }
                    }
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
                IconButton(
                    onClick = { onSendNewMessage() },
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
}

fun sendNewMessage( //TODO REPEAT CROP
    scope: CoroutineScope,
    context: Context,
    text: String,
    idConversation: Int,
    vmCamera: CameraViewModel,
    vmChat: ChatViewModel,

    onNavigateChat: () -> Unit,
) {
    scope.launch {
        // New Message
        if (NetworkUtils.isOnline(context)) {
            vmCamera.isLoading.value = true

            // GPT call
            val deferred = async { vmChat.sendMessageToOpenaiApi(text)}
            deferred.await()

            Log.i(TAG, "Continuing the code, Sending $idConversation")
            // Token cost of the call
            try {
                vmChat.lessTokenNewConversationCheckPremium()
            } catch (e: Exception) {
                // NewConversation tokens cost failed
                e.printStackTrace()
            }

            vmCamera.isLoading.value = false

            onNavigateChat()
        } else {
            Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show()
        }
    }
}
