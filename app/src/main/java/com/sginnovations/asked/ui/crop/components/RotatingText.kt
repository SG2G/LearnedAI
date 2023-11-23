package com.sginnovations.asked.ui.crop.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.sginnovations.asked.R
import kotlinx.coroutines.delay

@Composable
fun RotatingText() {
    val phrases = listOf(
        stringResource(R.string.loading_phrase_1),
        stringResource(R.string.loading_phrase_2),
        stringResource(R.string.loading_phrase_3),
        stringResource(R.string.loading_phrase_4),
        stringResource(R.string.loading_phrase_5)
    )

    val index = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000L)
            index.value = (index.value + 1) % phrases.size
        }
    }

    Text(
        text = phrases[index.value],
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
}