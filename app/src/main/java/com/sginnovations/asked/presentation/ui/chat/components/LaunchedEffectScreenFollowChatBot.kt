package com.sginnovations.asked.presentation.ui.chat.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import com.sginnovations.asked.data.database.entities.MessageEntity

@Composable
fun LaunchedEffectScreenFollowChatBot(
    messages: MutableState<List<MessageEntity>>,

    listState: LazyListState,

    lastIndex: MutableIntState,

    chatPlaceHolder: MutableState<Boolean>,
    chatAnimation: MutableState<Boolean>,
) {
    LaunchedEffect(messages.value.size) {
        if (messages.value.isNotEmpty()) {
            lastIndex.intValue = messages.value.size - 1
            chatPlaceHolder.value = false
            while (chatAnimation.value) {
                listState.animateScrollToItem(lastIndex.intValue)
            }
        }
    }
}