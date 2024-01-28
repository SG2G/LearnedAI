package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components

import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.sginnovations.asked.Constants
import com.sginnovations.asked.R
import com.sginnovations.asked.data.lessons.LessonDataClass
import com.sginnovations.asked.viewmodel.LessonViewModel
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin

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
    val context = LocalContext.current
    val verticalScroll = rememberScrollState()

    val markwon = remember {
        Markwon.builder(context)
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(
                JLatexMathPlugin.create(
                    42f,
                    JLatexMathPlugin.BuilderConfigure { builder ->
                        builder.inlinesEnabled(true)
                    }
                )
            )
            .build()
    }

    val textColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val textSizee = MaterialTheme.typography.bodyMedium.fontSize.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
    ) {
        AndroidView(
            modifier = Modifier
                .padding(Constants.CHAT_MSG_PADDING),
            factory = { ctx ->
                TextView(ctx).apply {
                    setBackgroundColor(Color.TRANSPARENT)
                    setTextColor(textColor)
                    textSize = textSizee
                    typeface = ResourcesCompat.getFont(ctx, R.font.monasans_regular)
                    movementMethod = LinkMovementMethod.getInstance()
                }
            },
            update = { view ->
                val node = markwon.parse(lesson.transcription)
                val renderedMarkdown = markwon.render(node)
                markwon.setParsedMarkdown(view, renderedMarkdown)
            }
        )

        Text(
            modifier = Modifier.padding(16.dp),
            text = lesson.reference,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}