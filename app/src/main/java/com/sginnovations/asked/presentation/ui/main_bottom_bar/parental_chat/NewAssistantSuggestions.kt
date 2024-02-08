package com.sginnovations.asked.presentation.ui.main_bottom_bar.parental_chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R

@Composable
fun NewAssistantSuggestions(
    onUseSuggestion: (String) -> Unit,
) {
    val myCards = listOf(
        CardSuggestionContent(stringResource(R.string.suggestion_title_1),
            stringResource(R.string.suggestion_text_1)),
        CardSuggestionContent(stringResource(R.string.suggestion_title_2),
            stringResource(R.string.suggestion_text_2)),
        CardSuggestionContent(stringResource(R.string.suggestion_title_3),
            stringResource(R.string.suggestion_text_3)),
        CardSuggestionContent(stringResource(R.string.suggestion_title_4),
            stringResource(R.string.suggestion_text_4)),
        CardSuggestionContent(stringResource(R.string.suggestion_title_5),
            stringResource(R.string.suggestion_text_5)),
        CardSuggestionContent(stringResource(R.string.suggestion_title_6),
            stringResource(R.string.suggestion_text_6)),
        CardSuggestionContent(stringResource(R.string.suggestion_title_7),
            stringResource(R.string.suggestion_text_7)),
        CardSuggestionContent(stringResource(R.string.suggestion_title_8),
            stringResource(R.string.suggestion_text_8)),
        CardSuggestionContent(stringResource(R.string.suggestion_title_9),
            stringResource(R.string.suggestion_text_9)),
        CardSuggestionContent(stringResource(R.string.suggestion_title_10),
            stringResource(R.string.suggestion_text_10)),
        CardSuggestionContent(stringResource(R.string.suggestion_title_11),
            stringResource(R.string.suggestion_text_11))
    )

    val randomCards = myCards.shuffled().take(4)

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(randomCards) { _, card ->
            ElevatedCard(
                modifier = Modifier
                    .width(304.dp)
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.clickable {
                        onUseSuggestion(card.subtitle)
                    },
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = card.title,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = card.subtitle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

data class CardSuggestionContent(
    val title: String,
    val subtitle: String,
)