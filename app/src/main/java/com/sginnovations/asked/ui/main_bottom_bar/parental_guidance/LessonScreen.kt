package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.data.lessons.LessonDataClass
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components.ComposeYouTubePlayer
import com.sginnovations.asked.viewmodel.IntentViewModel
import com.sginnovations.asked.viewmodel.LessonViewModel
import com.sginnovations.asked.viewmodel.PreferencesViewModel

private const val TAG = "LessonStateFul"

@Composable
fun LessonStateFul(
    vmLesson: LessonViewModel,
    vmIntent: IntentViewModel,
    vmPreferences: PreferencesViewModel,

    onOpenTranscript: () -> Unit,
) {
    val context = LocalContext.current
    val lessonId = vmLesson.lessonId

    val lesson = vmLesson.getLessonById(lessonId.intValue)

    Log.d(TAG, "lessonId: ${lessonId.intValue} lesson -> ${lesson.toString()}")

    LessonStateLess(
        lesson = lesson,

        onOpenTranscript = { onOpenTranscript() },
        onYouTubeClick = { vmIntent.openYouTubeVideo(context, lesson.videoId) },

        onLessonRead = {
            vmPreferences.markLessonAsRead(lessonId.intValue)
        },
    )
}


@Composable
fun LessonStateLess(
    lesson: LessonDataClass,

    onOpenTranscript: () -> Unit,
    onYouTubeClick: () -> Unit,

    onLessonRead: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = lesson.introduction,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            ElevatedCard(
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                ComposeYouTubePlayer(videoId = lesson.videoId)

                Box(modifier = Modifier.padding(8.dp)) {
                    LessonDescriptionWithLinks(
                        onYouTubeClick = { onYouTubeClick() },
                        onTranscriptClick = { onOpenTranscript() }
                    )
                }
            }

        }

//        Text(text = "If you have any problem watch it on")
//        TextButton(onClick = { onOpenTranscript() }) {
//            Text(text = "Transcript")
//        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    onLessonRead()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Next")
            }
        }
    }
}

@Composable
fun LessonDescriptionWithLinks(onYouTubeClick: () -> Unit, onTranscriptClick: () -> Unit) {
    val lessonDescriptionTypography = MaterialTheme.typography.bodyLarge.toSpanStyle()
    val lessonLinkColor = MaterialTheme.colorScheme.primary

    val annotatedText = buildAnnotatedString {
        withStyle(style = lessonDescriptionTypography) {
            append("If you have any problem watch it on ")
        }

        // Annotation for "YouTube"
        pushStringAnnotation(tag = "URL_YOUTUBE", annotation = "youtube")
        withStyle(
            style = lessonDescriptionTypography.merge(
                SpanStyle(color = lessonLinkColor)
            )
        ) {
            append("YouTube")
        }
        pop()

        withStyle(style = lessonDescriptionTypography) {
            append(" or you can see the ")
        }

        // Annotation for "transcript"
        pushStringAnnotation(tag = "URL_TRANSCRIPT", annotation = "transcript")
        withStyle(
            style = lessonDescriptionTypography.merge(
                SpanStyle(color = lessonLinkColor)
            )
        ) {
            append("Transcript")
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "URL_YOUTUBE", start = offset, end = offset)
                .firstOrNull()?.let { onYouTubeClick() }
            annotatedText.getStringAnnotations(tag = "URL_TRANSCRIPT", start = offset, end = offset)
                .firstOrNull()?.let { onTranscriptClick() }
        }
    )
}
