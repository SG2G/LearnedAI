package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sginnovations.asked.data.lessons.LessonDataClass
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components.LessonCard
import com.sginnovations.asked.viewmodel.LessonViewModel
import com.sginnovations.asked.viewmodel.PreferencesViewModel

@Composable
fun CategoryLessonsStateFul(
    vmLesson: LessonViewModel,
    vmPreferences: PreferencesViewModel,

    onNavigateLesson: () -> Unit,
) {
    val lessons = vmLesson.getAllLessons()

    CategoryLessonsStateLess(
        lessons = lessons,

        vmPreferences = vmPreferences,

        onNavigateLesson = { id ->

            vmLesson.lessonId.intValue = id
            onNavigateLesson()
        }
    )
}
@Composable
fun CategoryLessonsStateLess(
    lessons: List<LessonDataClass>,

    vmPreferences: PreferencesViewModel,

    onNavigateLesson: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        lessons.forEach { lesson ->
            LessonCard(
                vmPreferences = vmPreferences,

                lesson = lesson,

                onClick = { onNavigateLesson(lesson.idLesson) }
            )
        }
    }
}