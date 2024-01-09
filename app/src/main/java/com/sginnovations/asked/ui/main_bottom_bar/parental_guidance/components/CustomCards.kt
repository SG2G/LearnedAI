package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.data.lessons.LessonDataClass
import com.sginnovations.asked.viewmodel.PreferencesViewModel


@Composable
fun LessonCard(
    vmPreferences: PreferencesViewModel,

    lesson: LessonDataClass,

    onClick: () -> Unit,
) {

    SmallLessonCard(
        lesson = lesson,
        isRead = vmPreferences.isLessonRead(lesson.id),
        onClick = { onClick() }
    )

}


@Composable
fun SmallLessonCard(
    lesson: LessonDataClass,

    isRead: Boolean,

    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Box(
            modifier = Modifier.clickable { onClick() },
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = lesson.imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(92.dp),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = lesson.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (isRead) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "check",
                        tint = Color.Green,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.Top)
                    )
                }
            }
        }
    }
}

//@Composable
//fun ExpandedLessonCard(
//    lesson: LessonDataClass,
//
//    isRead: Boolean,
//
//    onClick: () -> Unit,
//) {
//    ElevatedCard(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        shape = RoundedCornerShape(15.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
//    ) {
//        Box(
//            modifier = Modifier.clickable { onClick() },
//        ) {
//            Column {
//                Image(
//                    painter = painterResource(id = lesson.imageId),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(128.dp),
//                    contentScale = ContentScale.Crop
//                )
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Row(
//                        Modifier.fillMaxWidth()
//                    ) {
//                        Text(
//                            text = lesson.title,
//                            style = MaterialTheme.typography.headlineMedium,
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                        Spacer(modifier = Modifier.weight(1f))
//                        if (isRead) {
//                            Icon(
//                                imageVector = Icons.Filled.CheckCircle,
//                                contentDescription = "check",
//                                tint = Color.Green
//                            )
//                        }
//                    }
//
//                    Text(
//                        text = lesson.subtitle,
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        maxLines = 2,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Text(
//                        text = lesson.description,
//                        style = MaterialTheme.typography.bodyLarge,
//                        color = MaterialTheme.colorScheme.onPrimaryContainer
//                    )
//                }
//
//            }
//        }
//    }
//}
