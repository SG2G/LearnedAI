package com.sginnovations.asked.presentation.ui.onboarding.type

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.viewmodel.OnBoardingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "OnBoardingCrafting"
private const val NUM_PROGRESSBAR = 2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingCrafting(
    vmOnBoarding: OnBoardingViewModel,
    pagerState: PagerState,
) {
    val questions = listOf(
        stringResource(R.string.pb_question_1),
        stringResource(R.string.pb_question_2),
        stringResource(R.string.pb_question_3)
    )
    val progressBarText = listOf(
        stringResource(R.string.pb_text_1),
        stringResource(R.string.pb_text_2),
        stringResource(R.string.pb_text_3)
    )

    var progressBarsState by remember { mutableStateOf(listOf(0f)) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var showQuestionState by remember { mutableStateOf(List(questions.size) { false }) }
    val coroutineScope = rememberCoroutineScope()

    val allCompleted = progressBarsState.all { it >= 1f }

    val currentPage = vmOnBoarding.currentPage.intValue
    val isLastPage = remember { mutableStateOf(currentPage == pagerState.pageCount - 1) }

    LaunchedEffect(currentPage) {
        isLastPage.value = currentPage == pagerState.pageCount - 1
        Log.d(
            TAG,
            "isLastPage: $isLastPage currentPage $currentPage count ${pagerState.pageCount - 1}"
        )
    }
    LaunchedEffect(isLastPage.value) {
        Log.d(
            TAG,
            "isLastPage: $isLastPage currentPage $currentPage count ${pagerState.pageCount - 1}"
        )
        if (isLastPage.value) {
            vmOnBoarding.btnEnable.value = false
            Log.d(TAG, "vmOnBoarding.btnEnable.value: ${vmOnBoarding.btnEnable.value}")
            delay(500)
        }
    }
    LaunchedEffect(allCompleted) {
        if (isLastPage.value) {
            while (!allCompleted) {
                delay(500)
            }
            Log.d(TAG, "progressBarsState: $progressBarsState")
            Log.d(TAG, "currentQuestionIndex: $currentQuestionIndex")
            Log.d(TAG, "showQuestionState: $showQuestionState")
            if (currentQuestionIndex == NUM_PROGRESSBAR) {
                vmOnBoarding.btnEnable.value = true
            }
        }
    }
    LaunchedEffect(isLastPage.value) {
        if (currentPage < questions.size) {
            if (progressBarsState.getOrNull(currentPage) ?: 0f < 1f) {
                coroutineScope.launch {
                    var newProgress = progressBarsState.getOrNull(currentPage) ?: 0f
                    while (newProgress < 1f) {
                        delay(30)
                        newProgress += 0.02f
                        progressBarsState = progressBarsState.toMutableList().apply {
                            if (size > currentPage) {
                                set(currentPage, newProgress)
                            }
                        }
                    }
                    showQuestionState = showQuestionState.toMutableList().apply {
                        if (size > currentPage) {
                            set(currentPage, true)
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.crafting_title),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(128.dp))

        progressBarsState.forEachIndexed { index, progress ->
            ProgressBarWithQuestion(
                progressBarText = progressBarText.getOrNull(index) ?: "Personalizing",
                progress = progress,
                question = if (showQuestionState.getOrNull(index) == true) questions.getOrNull(index) else null,
                onAnswer = {
                    coroutineScope.launch {
                        while (!isLastPage.value) {
                            delay(1000)
                        }
                        if (isLastPage.value) {
                            showQuestionState =
                                showQuestionState.toMutableList().also { it[index] = false }
                            if (index == currentQuestionIndex) {
                                var newProgress = progress
                                while (newProgress < 1f) {
                                    delay(50)
                                    newProgress += 0.02f
                                    progressBarsState = progressBarsState.toMutableList()
                                        .apply { set(index, newProgress) }
                                }
                                if (currentQuestionIndex < questions.size - 1) {
                                    delay(500)
                                    currentQuestionIndex++
                                    progressBarsState =
                                        progressBarsState + 0f
                                    showQuestionState =
                                        showQuestionState.toMutableList().apply { add(false) }
                                }
                            }
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    LaunchedEffect(key1 = currentQuestionIndex) {
        while (!isLastPage.value) {
            delay(1000)
        }
        if (!showQuestionState[currentQuestionIndex] && progressBarsState.getOrNull(
                currentQuestionIndex
            ) ?: 0f < 0.5f
        ) {
            coroutineScope.launch {
                if (isLastPage.value) {
                    var newProgress = progressBarsState.getOrNull(currentQuestionIndex) ?: 0f
                    while (newProgress < 0.5f) {
                        delay(50)
                        newProgress += 0.02f
                        if (newProgress >= 0.5f) {
                            showQuestionState = showQuestionState.toMutableList()
                                .also { it[currentQuestionIndex] = true }
                        }
                        progressBarsState = progressBarsState.toMutableList()
                            .apply { set(currentQuestionIndex, newProgress) }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressBarWithQuestion(
    progressBarText: String,
    progress: Float,
    question: String?,
    onAnswer: (Boolean) -> Unit,
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                progressBarText,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
            ProgressTextOrIcon(progress = progress)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp)),
        )
        if (question != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.to_move_forward_specify),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = question,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { onAnswer(true) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Text(stringResource(id = R.string.yes))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { onAnswer(true) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Text(stringResource(id = R.string.no))
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProgressTextOrIcon(progress: Float) {
    if ((progress * 100).toInt() < 100) {
        Text(
            text = "${(progress * 100).toInt().coerceIn(0, 100)}%",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall
        )
    } else {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "Completed",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewCrafting() {
//    OnBoardingCrafting()
//}