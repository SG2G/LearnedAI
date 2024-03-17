package com.sginnovations.asked.presentation.ui.subscription.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Doorbell
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R

data class CardInfo(
    val title: String,
    val description: String,

    val icon: Int,
    val softColor: Color,
    val strongColor: Color,
)


@Composable
fun SubscriptionFlow() {
    val context = LocalContext.current

    val cardsInfo = listOf(
        CardInfo(
            title = context.getString(R.string.sub_card_title_1),
            description = context.getString(R.string.sub_card_description_1),

            icon = R.drawable.tick_svgrepo_com,
            softColor = Color(0xFFE7F1E8),
            strongColor = Color(0xFF36AA7D),
        ),
        CardInfo(
            title = context.getString(R.string.sub_card_title_2),
            description = context.getString(R.string.sub_card_description_2),

            icon = R.drawable.play_svgrepo_com,
            softColor = Color(0xFFE7F1E8),
            strongColor = Color(0xFF36AA7D),
        ),
        CardInfo(
            title = context.getString(R.string.sub_card_title_3),
            description = context.getString(R.string.sub_card_description_3),

            icon = R.drawable.bell_svgrepo_com,
            softColor = Color(0xFFFFEDE1),
            strongColor = Color(0xFFfd8528),
        ),
        CardInfo(
            title = context.getString(R.string.sub_card_title_4),
            description = context.getString(R.string.sub_card_description_4),

            icon = R.drawable.star_svgrepo_com,
            softColor = Color(0xFFFFEDE1),
            strongColor = Color(0xFFfd8528),
        )
    )

    Column(modifier = Modifier.padding(16.dp)) {
        cardsInfo.forEachIndexed { index, cardInfo ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                CardWithTitleAndIcon(
                    index = index,
                    icon = cardInfo.icon,
                    softColor = cardInfo.softColor,
                    strongColor = cardInfo.strongColor,
                )
                Column {
                    Text(
                        text = cardInfo.title,
                        modifier = Modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (index == 0) TextDecoration.LineThrough else TextDecoration.None
                    )
                    Text(
                        text = cardInfo.description,
                        modifier = Modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (index != cardsInfo.size -1) {
                VerticalFadeDivider(index)
            }
        }
    }
}
@Composable
fun VerticalFadeDivider(index: Int) {
    val brush = when (index) {
        0 -> Brush.verticalGradient(colors = listOf(Color(0xFFE7F1E8), Color(0xFFE7F1E8)))
        1 -> Brush.verticalGradient(colors = listOf(Color(0xFF36AA7D), Color(0xFFFFEDE1)))
        2 -> Brush.verticalGradient(colors = listOf(Color(0xFFFFEDE1), Color(0xFFFFEDE1)))
        else -> Brush.verticalGradient(colors = listOf(Color(0xFFFFEDE1), Color(0xFFFFEDE1)))
    }

    Canvas(modifier = Modifier
        .padding(start = 22.dp)
        .size(width = 1.dp, height = 16.dp)) {
        drawLine(
            brush = brush,
            start = Offset(x = 0f, y = -60f),
            end = Offset(x = 0f, y = size.height + 60f),
            strokeWidth = 2.dp.toPx()
        )
    }
}
@Composable
fun CardWithTitleAndIcon(
    modifier: Modifier = Modifier,

    index: Int,
    icon: Int,

    softColor: Color,
    strongColor: Color,
) {
    Card(
        modifier = modifier
            .width(44.dp)
            .height(44.dp)
            .alpha(2f)
            .border(
                width = 2.dp,
                color =
                if (index == 1) {
                    strongColor
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(10.dp)
            ),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(softColor)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(28.dp),
            tint = strongColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomLayoutWithTextPerCard() {
    MaterialTheme {
        Surface {
            SubscriptionFlow()
        }
    }
}
