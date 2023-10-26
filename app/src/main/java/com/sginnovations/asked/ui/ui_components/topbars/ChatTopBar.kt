@file:OptIn(ExperimentalMaterial3Api::class)

package com.sginnovations.asked.ui.ui_components.topbars

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.ui.ui_components.points.PointsDisplay
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel

@Composable
fun ChatTopBar(
    vmTokens: TokenViewModel,
    vmChat: ChatViewModel,
    currentScreenTitle: String,

    navigateUp: () -> Unit,
) {
    val tokens = vmTokens.tokens.collectAsState()
    val id = vmChat.idConversation.intValue - 1
    val conversations = vmChat.conversations.value

    Log.i("ChatTopBar", "ChatTopBar: $conversations $id")
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = conversations[id].name,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = TextStyle(
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "ArrowBack"
                )
            }
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                PointsDisplay(
                    tokens = tokens,
                    showPlus = true
                ) { vmTokens.switchPointsVisibility() }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
        )
    )
}
