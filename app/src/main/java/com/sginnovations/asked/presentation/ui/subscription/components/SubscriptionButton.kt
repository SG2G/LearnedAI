package com.sginnovations.asked.presentation.ui.subscription.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R

@Composable
fun SubscriptionButton(
    textButton: String,
    onLaunchPurchaseFlow: () -> Unit,
) {
    val animationColor = MaterialTheme.colorScheme.primary
    val infiniteTransition = rememberInfiniteTransition()

    val animatedWidth by infiniteTransition.animateFloat(
        initialValue = 1.1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val animatedHeight by infiniteTransition.animateFloat(
        initialValue = 1.1f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .height(48.dp)
                .width(294.dp)
        ) {
            val width = size.width * animatedWidth
            val height = size.height * animatedHeight
            val topLeftX = (size.width - width) / 2
            val topLeftY = (size.height - height) / 2

            drawRoundRect(
                color = animationColor.copy(alpha = animatedAlpha),
                topLeft = Offset(topLeftX, topLeftY),
                size = Size(width, height),
                cornerRadius = CornerRadius(
                    x = 200f * animatedWidth,
                    y = 200f * animatedHeight
                ) // Ensure the corner radius scales with the animation
            )
        }

        // The button
        Button(
            onClick = { onLaunchPurchaseFlow() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(
                text = textButton,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}