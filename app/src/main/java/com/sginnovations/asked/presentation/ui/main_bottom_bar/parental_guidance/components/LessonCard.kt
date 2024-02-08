package com.sginnovations.asked.presentation.ui.main_bottom_bar.parental_guidance.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.data.lessons.LessonDataClass


@Composable
fun LessonCard(
    lesson: LessonDataClass,
    lessonNumber: Int,

    isPremium: Boolean,

    isRead: Boolean,
    onNavigateLesson: () -> Unit,
    onNavigatePremium: () -> Unit,
) {
    val elevatedCardShape = RoundedCornerShape(10.dp)

    if (lesson.isPremium) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .shadow(1.dp, elevatedCardShape)
                .alpha(
                    when (isPremium) {
                        true -> 1f
                        else -> 0.6f
                    }
                ),
            shape = elevatedCardShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
        ) {
            Box(modifier = Modifier.clickable {
                when (isPremium) {
                    true -> onNavigateLesson()
                    else -> onNavigatePremium()
                }
            }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lessonNumber.toString().padStart(2, '0'),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(16.dp)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = lesson.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    when (isPremium) {
                        true ->
                            Icon(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .size(if (isRead) 24.dp else 28.dp)
                                    .padding(end = 8.dp)
                                    .weight(0.2f),
                                imageVector = if (isRead) Icons.Filled.CheckCircle else Icons.Default.PlayArrow,
                                contentDescription = "check or play",
                                tint = if (isRead) Color(0xFF469C29) else MaterialTheme.colorScheme.primary,
                            )

                        else -> Icon(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(if (isRead) 24.dp else 28.dp)
                                .padding(end = 8.dp)
                                .weight(0.2f),
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Lock",
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }

                    //if (isRead) Color(0xFF5bb93b) else MaterialTheme.colorScheme.primary,
                }
            }
        }
    } else {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .shadow(1.dp, elevatedCardShape),
            shape = elevatedCardShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
        ) {
            Box(modifier = Modifier.clickable { onNavigateLesson() }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lessonNumber.toString().padStart(2, '0'),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(16.dp)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = lesson.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(if (isRead) 24.dp else 28.dp)
                            .padding(end = 8.dp)
                            .weight(0.2f),
                        imageVector = if (isRead) Icons.Filled.CheckCircle else Icons.Default.PlayArrow,
                        contentDescription = "check",
                        tint = if (isRead) Color(0xFF469C29) else MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}