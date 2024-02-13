package com.sginnovations.asked.presentation.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.Constants
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.ui_components.chat.TokenCostDisplay

@Composable
fun ChatTextField(
    writeMessage: MutableState<String>,

    conversationCostToken: String,

    isPremium: MutableState<Boolean>,

    onSendMessage: (String) -> Unit,
) {
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
            if (!isPremium.value) {
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
                    value = writeMessage.value,
                    onValueChange = {
                        if (isPremium.value) {
                            if (it.length <= Constants.CHAT_LIMIT_PREMIUM) {
                                writeMessage.value = it
                            }
                        } else {
                            if (it.length <= Constants.CHAT_LIMIT_DEFAULT) {
                                writeMessage.value = it
                            }
                        }
                    },
                    trailingIcon = {
                        Text(
                            "${writeMessage.value.length}/" +
                                    if (isPremium.value) Constants.CHAT_LIMIT_PREMIUM else Constants.CHAT_LIMIT_DEFAULT,
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
                    writeMessage = writeMessage,
                ) { finalMessage ->
                    onSendMessage(finalMessage.value)
                }
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
    }
}