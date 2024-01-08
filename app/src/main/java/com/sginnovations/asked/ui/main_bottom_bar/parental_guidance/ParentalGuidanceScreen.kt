package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sginnovations.asked.data.parental_guidance.LessonDataClass
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components.LessonCard
import com.sginnovations.asked.viewmodel.LessonViewModel
import com.sginnovations.asked.viewmodel.PreferencesViewModel

private const val TAG = "ParentalGuidanceStateFul"
@Composable
fun ParentalGuidanceStateFul(
    vmLesson: LessonViewModel,
    vmPreferences: PreferencesViewModel,

    onNavigateLesson: () -> Unit,
) {
    val lessons = vmLesson.getAllLessons()

    ParentalGuidanceStateLess(
        vmPreferences = vmPreferences,

        lessons = lessons

    ) { id ->
        Log.d(TAG, "id: $id")
        vmLesson.lessonId.intValue = id
        onNavigateLesson()
    }
}
@Composable
fun ParentalGuidanceStateLess(
    vmPreferences: PreferencesViewModel,

    lessons: List<LessonDataClass>,

    onNavigate: (Int) -> Unit,
) {

    Column(modifier = Modifier.fillMaxSize()
    ) {
        lessons.forEach { lesson ->

            LessonCard(
                vmPreferences = vmPreferences,

                lesson = lesson,

                onClick = { onNavigate(lesson.id) }
            )

        }
    }
}

