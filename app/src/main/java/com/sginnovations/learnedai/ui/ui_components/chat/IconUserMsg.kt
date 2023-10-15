package com.sginnovations.learnedai.ui.ui_components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.sginnovations.learnedai.Constants.Companion.ASSISTANT_PROFILE_URL

/**
 * Photo
 */
@Composable
fun IconUserMsg(
    name: String,
    photoUrl: String,
) {
    val chatTitleColor = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = photoUrl,
            contentDescription = null,
            modifier = Modifier
                .background(Color.Transparent)
                .clip(CircleShape)
                .size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name,
            color = chatTitleColor
        )
    }
}

/**
 * No Photo
 */
@Composable
fun IconAssistantMsg(
    name: String
) {
    val chatTitleColor = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ASSISTANT_PROFILE_URL,
            contentDescription = null,
            modifier = Modifier
                .background(Color.Transparent)
                .clip(CircleShape)
                .size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name,
            color = chatTitleColor
        )
    }
}
