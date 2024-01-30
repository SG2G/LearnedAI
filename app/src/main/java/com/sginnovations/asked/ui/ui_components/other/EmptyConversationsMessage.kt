package com.sginnovations.asked.ui.ui_components.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R

@Composable
fun EmptyConversationsMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.thinking2),
            contentDescription = "thinking head",
            modifier = Modifier.fillMaxWidth(0.4f)
        )
        Text(
            text = stringResource(R.string.chats_history_hmm_it_seems_like_there_s_nothing_here),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall
        )
    }
}