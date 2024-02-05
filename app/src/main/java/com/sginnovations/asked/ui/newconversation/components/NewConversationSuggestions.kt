package com.sginnovations.asked.ui.newconversation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R

@Composable
fun NewConversationSuggestions(

) {
    val elevatedCardPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ElevatedCard(
            modifier = Modifier.padding(elevatedCardPadding),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
            ) {
                TitleChatUseExample(
                    painterResource = painterResource(id = R.drawable.icons8_brain_96),
                    text = stringResource(R.string.new_conversation_resolve)
                )
                SubTitleChatUseExample(text = stringResource(R.string.new_conversation_take_photos_of_your_text_or_mathematical_problems_and_learn_by_talking_to_the_chat))
            }
        }
        ElevatedCard(
            modifier = androidx.compose.ui.Modifier.padding(elevatedCardPadding),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
            ) {
                TitleChatUseExample(
                    painterResource = painterResource(id = R.drawable.icons8_write_64),
                    text = stringResource(R.string.new_conversation_write)
                )
                SubTitleChatUseExample(text = stringResource(R.string.new_conversation_give_me_a_summary_of_the_book_to_kill_a_mockingbird))
            }
        }

        ElevatedCard(
            modifier = androidx.compose.ui.Modifier.padding(elevatedCardPadding),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
            ) {
                TitleChatUseExample(
                    painterResource = painterResource(id = R.drawable.icons8_magic_wand_64),
                    text = stringResource(R.string.new_conversation_creativity)
                )
                SubTitleChatUseExample(text = stringResource(R.string.new_conversation_create_a_slogan_for_a_dogs_social_media))
            }
        }
    }
}