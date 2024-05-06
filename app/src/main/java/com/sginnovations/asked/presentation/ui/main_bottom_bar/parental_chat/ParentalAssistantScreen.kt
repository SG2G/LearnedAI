package com.sginnovations.asked.presentation.ui.main_bottom_bar.parental_chat

import android.app.Activity
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.Constants
import com.sginnovations.asked.Constants.Companion.DARK_NAVIGATION_BAR_COLOR
import com.sginnovations.asked.Constants.Companion.PURPLE_COLOR
import com.sginnovations.asked.R
import com.sginnovations.asked.data.Assistant
import com.sginnovations.asked.data.database.entities.ConversationEntity
import com.sginnovations.asked.presentation.ui.main_bottom_bar.historychats.components.OptionMenu
import com.sginnovations.asked.presentation.ui.ui_components.other.EmptyConversationsMessage
import com.sginnovations.asked.presentation.ui.utils.ResetStatusBarColor
import com.sginnovations.asked.presentation.viewmodel.AssistantViewModel
import com.sginnovations.asked.presentation.viewmodel.PreferencesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "ParentalChatStateFul"

@Composable
fun ParentalAssistantStateFul(
    vmAssistant: AssistantViewModel,
    vmPreferences: PreferencesViewModel,

    onNavigateNewMessage: () -> Unit,
    onNavigateMessages: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val theme = vmPreferences.theme

    SideEffect {
        (context as Activity).window.navigationBarColor =
            if (!theme.value) {
                Constants.LIGHT_NAVIGATION_BAR_COLOR.toArgb()
            } else {
                DARK_NAVIGATION_BAR_COLOR.toArgb()
            }
    }

    LaunchedEffect(Unit) {
        Log.d(TAG, "StateFulHistoryChats: getAllConversations")
        delay(100)
        vmAssistant.getConversationsFromCategory(Assistant.prefix)
    }

    val conversations = vmAssistant.conversations

    ParentalChatStateLess(
        conversations = conversations,

        onNavigateNewMessage = {
            scope.launch { onNavigateNewMessage() }
        },
        onNavigateMessages = { idConversation ->
            scope.launch {
                Log.i(TAG, "Searching messages whit id: $idConversation")

                vmAssistant.idConversation.intValue = idConversation
                vmAssistant.getMessagesFromIdConversation()
            }
            Log.d(TAG, "StateFulAssistantHistoryChats: Navigating to messages")
            onNavigateMessages()
        },

        onDeleteConversation = { id ->
            scope.launch {
                if (id != null) {
                    Log.d(
                        TAG,
                        "onDeleteConversation. id -> $id"
                    )
                    vmAssistant.hideConversationsAssist(id)
                }
            }
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ParentalChatStateLess(
    conversations: MutableState<List<ConversationEntity>>,

    onNavigateNewMessage: () -> Unit,
    onNavigateMessages: (Int) -> Unit,

    onDeleteConversation: (Int?) -> Unit,
) {
    ResetStatusBarColor()
    val scope = rememberCoroutineScope()

    val showMenu = remember { mutableStateOf(false) }
    val indexMenu = remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = PURPLE_COLOR
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                TextButton(
                    onClick = { onNavigateNewMessage() },
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.asked_assistant),
                            contentDescription = "Icon",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.parental_assistant_title),
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.parental_guidance_subtitle),
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = "Add Icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = stringResource(R.string.parental_assist_previous_chats),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
            )
        }

        if (conversations.value.isEmpty()) {
            item {
                EmptyConversationsMessage(
                    painter = painterResource(id = R.drawable.empty_messages),
                    message = stringResource(R.string.assistant_empty_conversation)
                )
            }
        }


        itemsIndexed(
            items = conversations.value
        ) { index, conversation ->
            AnimatedVisibility(
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(300)
                ),
                visible = conversation.visible,
                exit = shrinkHorizontally(
                    animationSpec = tween(300),
                    targetWidth = { 0 }
                ) + slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                )
            ) {
                val isFirstVisible = conversations.value.indexOfFirst { it.visible } == index
                val isLastVisible = conversations.value.indexOfLast { it.visible } == index

                val cardShape = when {
                    isFirstVisible -> RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
                    isLastVisible -> RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
                    else -> RoundedCornerShape(0.dp)
                }

                Log.d(
                    TAG,
                    "Index -> $index/ firstVisibleIndex-> $isFirstVisible/ lastVisibleIndex-> $isLastVisible"
                )

                Card(
                    shape = cardShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    Log.i(
                                        "StateLessHistoryChats",
                                        "idConversation: ${conversation.idConversation}"
                                    )
                                    onNavigateMessages(conversation.idConversation ?: 0)
                                },
                                onLongPress = {
                                    scope.launch {
                                        Log.d(TAG, "expanded.value = true")

                                        showMenu.value = true
                                    }
                                }
                            )
                        },
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                ) {
                    /**
                     * Conversation
                     */
                    Box {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = conversation.name,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }


                            IconButton(onClick = {
                                showMenu.value = true
                                indexMenu.value = index
                            }) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = "options"
                                )
                            }
                            /**
                             * Menu
                             */

                            val scale by animateFloatAsState(
                                targetValue = if (showMenu.value) 1f else 0.9f,
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = FastOutSlowInEasing
                                ),
                                label = ""
                            )

                            if (showMenu.value && index == indexMenu.value) {
                                conversation.idConversation?.let {
                                    OptionMenu(
                                        showMenu = showMenu,
                                        indexMenu = indexMenu,

                                        scale = scale,

                                        conversationId = it,

                                        onDeleteConversation = { id ->
                                            onDeleteConversation(id)
                                        },
                                    ) {}
                                }
                            }
                        }
                    }
                }
            } // Animated visibility
        }
    }
}