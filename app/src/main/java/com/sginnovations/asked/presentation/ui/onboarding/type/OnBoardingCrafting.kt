package com.sginnovations.asked.presentation.ui.onboarding.type

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OnBoardingCrafting() {
    val questions = listOf("¿Pregunta 1?", "¿Pregunta 2?", "¿Pregunta 3?")
    var progressBarsState by remember { mutableStateOf(listOf(0f)) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var showQuestion by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Título", modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(20.dp))

        progressBarsState.forEachIndexed { index, progress ->
            ProgressBarWithQuestion(progress, if (index == currentQuestionIndex && showQuestion) questions[index] else null) { answerGiven ->
                if (answerGiven && index == currentQuestionIndex) {
                    showQuestion = false
                    coroutineScope.launch {
                        // Simula el progreso después de responder a la pregunta
                        var newProgress = progressBarsState[index]
                        while (newProgress < 1f) {
                            delay(30)
                            newProgress += 0.02f
                            progressBarsState = progressBarsState.toMutableList().apply { set(index, newProgress) }
                        }

                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                            progressBarsState = progressBarsState + 0f // Añade una nueva barra de progreso
                            showQuestion = true
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    LaunchedEffect(key1 = currentQuestionIndex) {
        if (!showQuestion && currentQuestionIndex < questions.size && progressBarsState[currentQuestionIndex] < 0.5f) {
            coroutineScope.launch {
                // Inicia el progreso de la barra actual
                var newProgress = progressBarsState[currentQuestionIndex]
                while (newProgress < 0.5f) {
                    delay(30)
                    newProgress += 0.01f
                    progressBarsState = progressBarsState.toMutableList().apply { set(currentQuestionIndex, newProgress) }
                }
                showQuestion = true // Muestra la pregunta cuando la barra alcanza el 50%
            }
        }
    }
}

@Composable
fun ProgressBarWithQuestion(progress: Float, question: String?, onAnswer: (Boolean) -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Inicio")
            Text("${(progress * 100).toInt()}%")
        }
        LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
        if (question != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Card(modifier = Modifier.padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(question)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(onClick = { onAnswer(true) }) {
                            Text("Sí")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { onAnswer(false) }) {
                            Text("No")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCrafting() {
    OnBoardingCrafting()
}