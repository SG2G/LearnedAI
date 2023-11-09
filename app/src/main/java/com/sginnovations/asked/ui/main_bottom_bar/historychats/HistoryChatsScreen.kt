@file:OptIn(ExperimentalFoundationApi::class)

package com.sginnovations.asked.ui.main_bottom_bar.historychats

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.data.All
import com.sginnovations.asked.data.Math
import com.sginnovations.asked.data.Text
import com.sginnovations.asked.data.database.entities.ConversationEntity
import com.sginnovations.asked.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

private const val TAG = "HistoryChats"

@Composable
fun StateFulHistoryChats(
    vmChat: ChatViewModel,

    onNavigateMessages: () -> Unit,
    onNavigateNewConversation: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    SideEffect {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (context as Activity).window.navigationBarColor = Color(0xFF191c22).toArgb()
        }
    }

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
        onChangeCategory = { category ->
            scope.launch {
                if (category == All.name) {
                    vmChat.getAllConversations()
                } else {
                    vmChat.getConversationsFromCategory(category)
                }
            }
        },

        onNavigateMessages = { idConversation ->
            scope.launch {
                Log.i(TAG, "Searching messages whit id: $idConversation")
                vmChat.idConversation.intValue = idConversation
                vmChat.getMessagesFromIdConversation()
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StateLessHistoryChats(
    conversations: MutableState<List<ConversationEntity>>,

    onDeleteConversation: (Int?) -> Unit,
    onChangeCategory: (String) -> Unit,

    onNavigateMessages: (Int) -> Unit,
    onNavigateNewConversation: () -> Unit,
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item {
            CategoryCarousel(onChangeCategory = { onChangeCategory(it) })
        }
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
                        Text(
                            text = "New Chat",
                        )
                    }
                }
            }
        }
        /**
         * History of Chats
         */
        itemsIndexed(
            items = conversations.value.asReversed(),
            itemContent = { index, conversation ->
                AnimatedVisibility(
                    modifier = Modifier.animateItemPlacement(
                        animationSpec = spring(
                            dampingRatio = 0.8f,
                            stiffness = 200f
                        )
                    ),
                    visible = conversation.visible,
                    exit = shrinkHorizontally(
                        animationSpec = tween(600),
                        targetWidth = { 0 }
                    ) + slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(600)
                    )
                ) {
                    val smallestId = 0
                    val largestId = conversations.value.size - 1

                    Log.d(TAG, "Index -> $index/ smallestId-> $smallestId/ largestId-> $largestId")
                    Column {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 2.dp)
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
                            ),
                            shape =
                            if (index == smallestId) {
                                RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                            } else {
                                if (index == largestId) {
                                    RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                                } else {
                                    RoundedCornerShape(0.dp)
                                }
                            }
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
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = conversation.category,
                                    color =
                                    when (conversation.category) {
                                        Text.name -> Color.Yellow
                                        Math.name -> Color.Cyan
                                        else -> MaterialTheme.colorScheme.onBackground
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.onSurfaceVariant,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .padding(8.dp)
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
