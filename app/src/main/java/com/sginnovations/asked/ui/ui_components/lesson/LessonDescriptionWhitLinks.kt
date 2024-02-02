package com.sginnovations.asked.ui.ui_components.lesson

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.sginnovations.asked.R

@Composable
fun LessonDescriptionWithLinks(onYouTubeClick: () -> Unit, onTranscriptClick: () -> Unit) {
    val lessonDescriptionTypography = MaterialTheme.typography.bodyMedium.toSpanStyle()
    val lessonLinkColor = MaterialTheme.colorScheme.primary

    val annotatedText = buildAnnotatedString {
        withStyle(style = lessonDescriptionTypography) {
            append(stringResource(R.string.lesson_description_if_you_have_any_problem_watch_it_on))
        }

        // Annotation for "YouTube"
        pushStringAnnotation(tag = "URL_YOUTUBE", annotation = "youtube")
        withStyle(
            style = lessonDescriptionTypography.merge(
                SpanStyle(color = lessonLinkColor)
            )
        ) {
            append(stringResource(R.string.lesson_description_youtube))
        }
        pop()

        withStyle(style = lessonDescriptionTypography) {
            append(stringResource(R.string.lesson_description_or_you_can_see_the))
        }

        // Annotation for "transcript"
        pushStringAnnotation(tag = "URL_TRANSCRIPT", annotation = "transcript")
        withStyle(
            style = lessonDescriptionTypography.merge(
                SpanStyle(color = lessonLinkColor)
            )
        ) {
            append(stringResource(R.string.lesson_description_transcript))
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