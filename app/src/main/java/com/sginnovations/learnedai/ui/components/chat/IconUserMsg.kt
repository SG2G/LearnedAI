package com.sginnovations.learnedai.ui.components.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sginnovations.learnedai.data.database.util.Assistant

@Composable
fun IconUserMsg(name: String, chatTitleColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Info Icon"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, color = chatTitleColor)
    }
}