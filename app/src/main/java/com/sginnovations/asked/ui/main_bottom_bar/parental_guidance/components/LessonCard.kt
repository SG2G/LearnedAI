package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.data.lessons.LessonCategoryDataClass
import com.sginnovations.asked.data.lessons.LessonDataClass
import com.sginnovations.asked.viewmodel.PreferencesViewModel


@Composable
fun SmallLessonCard(
    lesson: LessonDataClass,
    lessonNumber: Int,

    isRead: Boolean,
    onClick: () -> Unit,
) {
    val elevatedCardShape = RoundedCornerShape(10.dp)

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
        Box(modifier = Modifier.clickable { onClick() }) {
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(28.dp)
                        .padding(end = 8.dp)
                        .weight(0.2f),
                    imageVector = if (isRead) Icons.Filled.CheckCircle else Icons.Default.PlayArrow,
                    contentDescription = "check",
                    tint = if (isRead) Color(0xFF469C29) else MaterialTheme.colorScheme.primary,
                )
                //if (isRead) Color(0xFF5bb93b) else MaterialTheme.colorScheme.primary,
            }
        }
    }
}

@Composable
fun CategoryLessonCard(
    category: LessonCategoryDataClass,
//    isRead: Boolean,
    onNavigateCategoryLessons: () -> Unit,
) {
    val elevatedCardShape = RoundedCornerShape(15.dp)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(1.dp, elevatedCardShape),
        shape = elevatedCardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.clickable { onNavigateCategoryLessons() }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = category.imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(92.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = category.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = category.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

//                Icon(
//                    imageVector = if (isRead) Icons.Filled.CheckCircle else Icons.Default.Cancel,
//                    contentDescription = "check",
//                    tint = if (isRead) Color(0xFF5bb93b) else Color.Transparent,
//                    modifier = Modifier.align(Alignment.Top)
//                )
            }
        }
    }
}
