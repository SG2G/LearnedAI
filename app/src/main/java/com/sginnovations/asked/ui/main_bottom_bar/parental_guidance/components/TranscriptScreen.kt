package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sginnovations.asked.data.parental_guidance.LessonDataClass
import com.sginnovations.asked.viewmodel.LessonViewModel

@Composable
fun TranscriptStateFul(
    vmLesson: LessonViewModel,
) {
    val lessonId = vmLesson.lessonId

    val lesson = vmLesson.getLessonById(lessonId.intValue)

    TranscriptStateFul(
        lesson = lesson
    )
}

@Composable
fun TranscriptStateFul(
    lesson: LessonDataClass,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = lesson.transcription)
    }
}