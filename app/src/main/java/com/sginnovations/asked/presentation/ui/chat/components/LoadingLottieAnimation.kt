package com.sginnovations.asked.presentation.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.ui_components.chat.IconAssistantMsg
import com.sginnovations.asked.presentation.ui.ui_components.chat.IconMsg

@Composable
fun LoadingLottieAnimation(
    isAssistant: Boolean = false,
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading2))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .background(backgroundColor)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (isAssistant) IconAssistantMsg() else IconMsg()
        ElevatedCard(
            modifier = Modifier.padding(horizontal = 16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(56.dp)
            )
        }
    }
}