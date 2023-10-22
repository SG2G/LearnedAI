package com.sginnovations.asked.ui.ui_components.chat

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

@Composable
fun TypingTextAnimation(
    message: String,
    msgPadding: PaddingValues,

    onStopTextAnimation: () -> Unit,
) {
    val typingState = remember { mutableStateOf("") }

    val vibrator = LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    LaunchedEffect(message) {
        var counter = 0
        message.forEach { char ->
            if (counter % 10 == 0) {
                delay(1)
            }
            typingState.value += char

            // Vibration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // For API 26 and higher
                val effect =
                    VibrationEffect.createOneShot(1, 5)
                vibrator.vibrate(effect)
            }

            counter++
        }
        onStopTextAnimation()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(msgPadding)
    ) {
        Text(text = typingState.value)
        Icon(Icons.Default.Info, contentDescription = null)
    }
}