package com.sginnovations.asked.presentation.ui.ui_components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sginnovations.asked.Constants.Companion.DEFAULT_PROFILE_URL

@Composable
fun ProfilePicture(userAuthPhotoUrl: String? = DEFAULT_PROFILE_URL) {
    Box(
        modifier = Modifier
            .size(width = 65.dp, height = 65.dp)
    ) {
        AsyncImage(
            model = userAuthPhotoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
    }
}