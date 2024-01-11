package com.sginnovations.asked.ui.main_bottom_bar.parental_chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.newconversation.components.SubTitleChatUseExample
import com.sginnovations.asked.ui.newconversation.components.TitleChatUseExample

val myCards = listOf(
    CardContent(R.drawable.lesson_0, "Título 1", "Subtítulo 1"),
    CardContent(R.drawable.lesson_0, "Título 2", "Subtítulo 2"),
    CardContent(R.drawable.lesson_0, "Título 1", "Subtítulo 1"),
    CardContent(R.drawable.lesson_0, "Título 2", "Subtítulo 2"),
)

@Composable
fun NewAssistantSuggestions(
    onUseSuggestion: (String) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        itemsIndexed(myCards) { _, card ->
            ElevatedCard(
                modifier = Modifier
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
                        TitleChatUseExample(
                            painterResource = painterResource(id = card.imageResId),
                            text = card.title
                        )
                        SubTitleChatUseExample(text = card.subtitle)
                    }
                }
            }
        }
    }
}

data class CardContent(
    val imageResId: Int,
    val title: String,
    val subtitle: String,
)