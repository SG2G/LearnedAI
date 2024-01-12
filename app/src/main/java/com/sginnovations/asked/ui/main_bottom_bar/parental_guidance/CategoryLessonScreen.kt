package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.data.lessons.LessonCategoryDataClass
import com.sginnovations.asked.data.lessons.LessonDataClass
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components.SmallLessonCard
import com.sginnovations.asked.viewmodel.LessonViewModel
import com.sginnovations.asked.viewmodel.PreferencesViewModel

@Composable
fun CategoryLessonsStateFul(
    vmLesson: LessonViewModel,
    vmPreferences: PreferencesViewModel,

    onNavigateLesson: () -> Unit,
) {
    val category = vmLesson.getCategoryById()
    val lessons = vmLesson.getAllLessonsByCategoryId()

    CategoryLessonsStateLess(
        lessons = lessons,
        category = category,

        vmPreferences = vmPreferences

    ) { id ->

        vmLesson.lessonId.intValue = id
        onNavigateLesson()
    }
}

@Composable
fun CategoryLessonsStateLess(
    lessons: List<LessonDataClass>,
    category: LessonCategoryDataClass,

    vmPreferences: PreferencesViewModel,

    onNavigateLesson: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                text = category.title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = category.description,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        lessons.forEachIndexed { index, lesson ->
            SmallLessonCard(
                lesson = lesson,
                lessonNumber = index + 1,

                isRead = vmPreferences.isLessonRead(lesson.idLesson),
                onClick = { onNavigateLesson(lesson.idLesson) }
            )
        }
    }
}