package com.sginnovations.learnedai.ui.historychats

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.learnedai.data.database.entities.ConversationEntity
import com.sginnovations.learnedai.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@Composable
fun StateFulHistoryChats(
    vmChat: ChatViewModel,

    onNavigateMessages: () -> Unit,
    onNavigateNewConversation: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        vmChat.getAllConversations()
    }

    val conversations = vmChat.conversations

    StateLessHistoryChats(
        conversations = conversations,

        onNavigateMessages = { idConversation ->
            scope.launch {
                Log.i("StateFulHistoryChats", "Searching messages whit id: $idConversation")
                vmChat.idConversation.intValue = idConversation
                vmChat.getAllMessages()
            }
            onNavigateMessages()
        },
        onNavigateNewConversation = {
            scope.launch {
                setUpNewConversation(vmChat)
            }
            onNavigateNewConversation()
        }
    )
}
private fun setUpNewConversation(vmChat: ChatViewModel) { //TODO 1
    vmChat.idConversation.intValue = 0
    vmChat.isNewConversation.value = true
}

@Composable
fun StateLessHistoryChats(
    conversations: MutableState<List<ConversationEntity>>,

    onNavigateMessages: (Int) -> Unit,
    onNavigateNewConversation: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Chats history", color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = TextStyle(
                        fontSize = 22.sp
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                onNavigateNewConversation()
                            }
                        )
                    }
            ) {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.outlinedCardColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = Color.Transparent
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Icon"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "New Chat")
                    }
                }
            }
        }
        itemsIndexed(
            items = conversations.value.asReversed(),
            itemContent = { _, conversation ->
                Column {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        Log.i(
                                            "StateLessHistoryChats",
                                            "idConversation: ${conversation.idConversation}"
                                        )
                                        onNavigateMessages(conversation.idConversation ?: 0)
                                    }
                                )
                            },
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = conversation.name,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        )
    }
}