package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.data.parental_guidance.LessonDataClass
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components.ComposeYouTubePlayer
import com.sginnovations.asked.viewmodel.LessonViewModel
import com.sginnovations.asked.viewmodel.PreferencesViewModel

private const val TAG = "LessonStateFul"

@Composable
fun LessonStateFul(
    vmLesson: LessonViewModel,
    vmPreferences: PreferencesViewModel,

    onOpenTranscript: () -> Unit,
) {
    val lessonId = vmLesson.lessonId

    val lesson = vmLesson.getLessonById(lessonId.intValue)

    Log.d(TAG, "lessonId: ${lessonId.intValue} lesson -> ${lesson.toString()}")

    LessonStateLess(
        lesson = lesson,

        onOpenTranscript = { onOpenTranscript() },

        onLessonRead = {
            vmPreferences.markLessonAsRead(lessonId.intValue)
        },
    )
}


@Composable
fun LessonStateLess(
    lesson: LessonDataClass,

    onOpenTranscript: () -> Unit,
    onLessonRead: () -> Unit,
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = lesson.introduction)

        ComposeYouTubePlayer(videoId = lesson.videoId)
        Text(text = "If you have any problem idk")
        TextButton(onClick = { onOpenTranscript() }) {
            Text(text = "Transcript")
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    onLessonRead()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
            ) {
                Text(text = "Next")
            }
        }
    }
}