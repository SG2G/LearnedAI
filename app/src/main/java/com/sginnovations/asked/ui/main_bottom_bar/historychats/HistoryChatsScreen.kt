@file:OptIn(ExperimentalFoundationApi::class)

package com.sginnovations.asked.ui.main_bottom_bar.historychats

import android.app.Activity
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.data.All
import com.sginnovations.asked.data.GrammarCategoryOCR
import com.sginnovations.asked.data.MathCategoryOCR
import com.sginnovations.asked.data.SummaryCategoryOCR
import com.sginnovations.asked.data.TextCategoryOCR
import com.sginnovations.asked.data.TranslateCategoryOCR
import com.sginnovations.asked.data.database.entities.ConversationEntity
import com.sginnovations.asked.ui.main_bottom_bar.historychats.components.OptionMenu
import com.sginnovations.asked.ui.ui_components.other.EmptyConversationsMessage
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.PreferencesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "HistoryChats"

@Composable
fun StateFulHistoryChats(
    vmChat: ChatViewModel,
    vmPreferences: PreferencesViewModel,

    onNavigateMessages: () -> Unit,
    onNavigateNewConversation: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val theme = vmPreferences.theme

    SideEffect { (context as Activity).window.navigationBarColor =
        if (!theme.value) {
            Color(0xFFe9effd).toArgb()
        } else {
            Color(0xFF0C1622).toArgb()
        }
    }

    LaunchedEffect(Unit) {
        Log.d(TAG, "StateFulHistoryChats: getAllConversations")
        delay(100)
        vmChat.getAllConversationsExceptAssistant()
    }

    val conversations = vmChat.conversations

    StateLessHistoryChats(
        conversations = conversations,

        onDeleteConversation = { id ->
            scope.launch {
                if (id != null) {
                    Log.d(TAG, "onDeleteConversation. id -> $id")
                    vmChat.hideConversation(id)
                }
            }
        },
        onChangeCategory = { category ->
            scope.launch {
                Log.d(TAG, "onChangeCategory")
                if (category == All.root) {
                    vmChat.getAllConversationsExceptAssistant()
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
        }
    ) {
        scope.launch {
            vmChat.setUpNewConversation()
            onNavigateNewConversation()
        }
    }
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val showMenu = remember { mutableStateOf(false) }
    val indexMenu = remember { mutableStateOf<Int?>(null) }

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
                        contentColor = MaterialTheme.colorScheme.onBackground,
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
                            text = stringResource(R.string.historychats_new_chat),
                        )
                    }
                }
            }
        }
        if (conversations.value.isEmpty()) {
            item {
                EmptyConversationsMessage()
            }
        }

        /**
         * History of Chats
         */
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
                val smallestId = 0
                val largestId = conversations.value.size - 1

                Log.d(TAG, "Index -> $index/ smallestId-> $smallestId/ largestId-> $largestId")
                val elevatedCardShape = RoundedCornerShape(10.dp)
//                    if (index == smallestId) {
//                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
//                } else {
//                    if (index == largestId) {
//                        RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
//                    } else {
//                        RoundedCornerShape(0.dp)
//                    }
//                }

                Column {
                    ElevatedCard(
                        shape = elevatedCardShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .shadow(1.dp, elevatedCardShape)
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
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 1.dp
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
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                    ) {
                                        Text(
                                            text = stringResource(R.string.historychats_category) +
                                                    when (conversation.category) {
                                                        TextCategoryOCR.prefix -> TextCategoryOCR.getName(context)
                                                        MathCategoryOCR.prefix -> MathCategoryOCR.getName(context)
                                                        GrammarCategoryOCR.prefix -> GrammarCategoryOCR.getName(context)
                                                        TranslateCategoryOCR.prefix -> TranslateCategoryOCR.getName(context)
                                                        SummaryCategoryOCR.prefix -> SummaryCategoryOCR.getName(context)
                                                        else -> TextCategoryOCR.getName(context)
                                                    },
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            modifier = Modifier.size(14.dp),
                                            painter = when (conversation.category) {
                                                TextCategoryOCR.prefix -> painterResource(id = R.drawable.text_camera)
                                                MathCategoryOCR.prefix -> painterResource(id = R.drawable.math_camera)
                                                TranslateCategoryOCR.prefix -> painterResource(id = R.drawable.language_camera)
                                                else -> painterResource(id = R.drawable.text_camera)
                                            },
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
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

                }
            } // Animated visibility
        }
    }
}


