package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance

import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.TextView
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.sginnovations.asked.Constants
import com.sginnovations.asked.Constants.Companion.PURPLE_COLOR
import com.sginnovations.asked.R
import com.sginnovations.asked.data.lessons.LessonDataClass
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components.ComposeYouTubePlayer
import com.sginnovations.asked.ui.ui_components.lesson.EndLessonDialog
import com.sginnovations.asked.ui.ui_components.lesson.LessonDescriptionWithLinks
import com.sginnovations.asked.viewmodel.AssistantViewModel
import com.sginnovations.asked.viewmodel.IntentViewModel
import com.sginnovations.asked.viewmodel.LessonViewModel
import com.sginnovations.asked.viewmodel.PreferencesViewModel
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val TAG = "LessonStateFul"

@Composable
fun LessonStateFul(
    vmLesson: LessonViewModel,
    vmAssistant: AssistantViewModel,
    vmIntent: IntentViewModel,
    vmPreferences: PreferencesViewModel,

    onOpenTranscript: () -> Unit,

    onNavigateAssistant: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val lessonId = vmLesson.lessonId

    val lesson = vmLesson.getLessonById(lessonId.intValue)

    val showEndLesson = remember { mutableStateOf(false) }

    LessonStateLess(
        lesson = lesson,

        onOpenTranscript = { onOpenTranscript() },
        onYouTubeClick = { vmIntent.openYouTubeVideo(context, lesson.videoId) },

        onExampleButton = {
            scope.launch {
                vmAssistant.firstMessage.value = lesson.questionAsked

                onNavigateAssistant()
            }
        },

        onLessonReadAndFinish = {
            Log.d(TAG, "lessonId: ${lessonId.intValue} lesson -> ${lesson.toString()}")
            vmPreferences.markLessonAsRead(lessonId.intValue)
            showEndLesson.value = true
        },
    )
    if (showEndLesson.value) {
        EndLessonDialog(
            onDismissRequest = {
                showEndLesson.value = false
            },
            onConfirmRequest = {
                showEndLesson.value = false
                onNavigateBack()
            },
        )
    }
}

@Composable
fun LessonStateLess(
    lesson: LessonDataClass,

    onOpenTranscript: () -> Unit,
    onYouTubeClick: () -> Unit,

    onExampleButton: () -> Unit,

    onLessonReadAndFinish: () -> Unit,
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    HorizontalPager(
        count = if (lesson.conclusion.isNullOrEmpty()) 1 else 2,
        state = pagerState
    ) { page ->
        Log.d(TAG, "page: $page  pagerState -> ${pagerState.currentPage}")
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (page) {
                0 -> Page1(
                    lesson = lesson,

                    onYouTubeClick = { onYouTubeClick() },
                    onOpenTranscript = { onOpenTranscript() },
                )

                1 -> Page2(
                    lesson = lesson,

                    onExampleButton = { onExampleButton() }
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                        val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp

                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .animateContentSize()
                                .size(width, 8.dp)
                                .clip(
                                    if (pagerState.currentPage == iteration) RoundedCornerShape(
                                        10.dp
                                    ) else CircleShape
                                )
                                .background(color)
                        )

                    }
                }

                Button(
                    onClick = {
                        if (page == lesson.lessonPages) {
                            // page actual == page count
                            onLessonReadAndFinish()

                        } else {
                            scope.launch {
                                pagerState.scrollToPage(pagerState.currentPage + 1 % pagerState.pageCount)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text =
                        if (page == lesson.lessonPages) {
                            stringResource(R.string.lesson_finish)
                        } else {
                            stringResource(R.string.lesson_next)
                        },
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

        }
    }
}

@Composable
fun Page2(
    lesson: LessonDataClass,

    onExampleButton: () -> Unit,
) {
    if (lesson.conclusion != null) {
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
                .padding(bottom = 108.dp)
                .fillMaxSize()
                .verticalScroll(verticalScroll)

        ) {
            AndroidView(
                modifier = Modifier
                    .padding(Constants.CHAT_MSG_PADDING),
                factory = { ctx ->
                    TextView(ctx).apply {
                        setBackgroundColor(android.graphics.Color.TRANSPARENT)
                        setTextColor(textColor)
                        textSize = textSizee
                        typeface = ResourcesCompat.getFont(ctx, R.font.monasans_regular)
                        movementMethod = LinkMovementMethod.getInstance()
                    }
                },
                update = { view ->
                    val node = markwon.parse(lesson.conclusion)
                    val renderedMarkdown = markwon.render(node)
                    markwon.setParsedMarkdown(view, renderedMarkdown)
                }
            )

            if (!lesson.buttonText.isNullOrEmpty()) {
                Button(
                    onClick = { onExampleButton() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 72.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(PURPLE_COLOR)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.asked_assistant),
                            contentDescription = "Icon",
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape),
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = lesson.buttonText ?: "See Example",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        ) //TODO TRANSLATE MAYBE
                    }
                }
            }
        }
    }
}

@Composable
fun Page1(
    lesson: LessonDataClass,

    onYouTubeClick: () -> Unit,
    onOpenTranscript: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = lesson.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.introduction),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = lesson.introduction,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

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
}
