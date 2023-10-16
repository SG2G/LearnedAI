@file:OptIn(ExperimentalFoundationApi::class)

package com.sginnovations.learnedai.ui.historychats

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.learnedai.data.database.entities.ConversationEntity
import com.sginnovations.learnedai.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

private const val TAG = "HistoryChats"
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

        onDeleteConversation = { id ->
            scope.launch {
                if (id != null) {
                    vmChat.hideConversation(id)
                }
            }
        },

        onNavigateMessages = { idConversation ->
            scope.launch {
                Log.i(TAG, "Searching messages whit id: $idConversation")
                vmChat.idConversation.intValue = idConversation
                vmChat.getAllMessages()
            }
            Log.d(TAG, "StateFulHistoryChats: Navigating to messages")
            onNavigateMessages()
        },
        onNavigateNewConversation = {
            scope.launch {
                vmChat.setUpNewConversation()
            }
            onNavigateNewConversation()
        }
    )
}

@Composable
fun StateLessHistoryChats(
    conversations: MutableState<List<ConversationEntity>>,

    onDeleteConversation: (Int?) -> Unit,

    onNavigateMessages: (Int) -> Unit,
    onNavigateNewConversation: () -> Unit,
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        /**
         * New Chat
         */
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
                    .animateItemPlacement(
                        animationSpec = spring(
                            dampingRatio = 0.8f,
                            stiffness = 200f
                        )
                    )
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
        /**
         * History of Chats
         */
        itemsIndexed(
            items = conversations.value.asReversed(),
            itemContent = { _, conversation ->
                AnimatedVisibility(
                    modifier = Modifier.animateItemPlacement(
                        animationSpec = spring(
                            dampingRatio = 0.8f,
                            stiffness = 200f
                        )
                    ),
                    visible = conversation.visible,
//                    enter = slideInVertically(
//                        animationSpec = tween(600)
//                    ) {
//                        // Slide in from 40 dp from the top.
//                        with(density) { -40.dp.roundToPx() }
//                    } + expandVertically(
//                        // Expand from the top.
//                        expandFrom = Alignment.Top
//                    ) + fadeIn(
//                        // Fade in with the initial alpha of 0.3f.
//                        initialAlpha = 0.3f
//                    ),
                    exit = shrinkHorizontally(
                        animationSpec = tween(600),
                        targetWidth = { 0 }
                    ) + slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(600)
                    )
                ) {
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .weight(1f),
                                    text = conversation.name,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                IconButton(
                                    onClick = { onDeleteConversation(conversation.idConversation) },
                                ) {
                                    Icon(Icons.Outlined.Delete, null)
                                }
                            }
                        }
                    }
                } // Animated visibility

            }
        )
    }
}