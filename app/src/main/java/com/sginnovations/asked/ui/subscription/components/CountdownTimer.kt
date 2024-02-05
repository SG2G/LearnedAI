package com.sginnovations.asked.ui.subscription.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getTimeRemaining(targetDate: LocalDateTime): Duration {
    val now = LocalDateTime.now()
    return Duration.between(now, targetDate)
}

@Composable
fun CountdownTimer(targetDate: LocalDateTime) {
    var timeRemaining by remember { mutableStateOf(getTimeRemaining(targetDate)) }

    LaunchedEffect(key1 = timeRemaining) {
        if (!timeRemaining.isZero && !timeRemaining.isNegative) {
            delay(1000L)
            timeRemaining = getTimeRemaining(targetDate)
        }
    }

    Text(
        modifier = Modifier.fillMaxSize(),
        text = if (timeRemaining.isZero || timeRemaining.isNegative) "Time over" else formatDuration(timeRemaining),
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
    )
}

fun formatDuration(duration: Duration): String {
    val days = duration.toDays()
    val hours = duration.toHours() % 24
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60
    return "%d:%02d:%02d:%02d".format(days, hours, minutes, seconds)
}
