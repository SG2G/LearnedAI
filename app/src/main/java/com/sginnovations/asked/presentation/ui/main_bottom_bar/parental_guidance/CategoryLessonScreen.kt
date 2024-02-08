package com.sginnovations.asked.presentation.ui.main_bottom_bar.parental_guidance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.data.lessons.LessonCategoryDataClass
import com.sginnovations.asked.data.lessons.LessonDataClass
import com.sginnovations.asked.presentation.ui.main_bottom_bar.parental_guidance.components.LessonCard
import com.sginnovations.asked.presentation.ui.ui_components.lesson.PremiumLessonDialog
import com.sginnovations.asked.presentation.viewmodel.LessonViewModel
import com.sginnovations.asked.presentation.viewmodel.PreferencesViewModel
import com.sginnovations.asked.utils.CheckIsPremium
import kotlinx.coroutines.async

@Composable
fun CategoryLessonsStateFul(
    vmLesson: LessonViewModel,
    vmPreferences: PreferencesViewModel,

    onNavigateLesson: () -> Unit,
    onNavigateSubscriptionScreen: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val category = vmLesson.getCategoryById()
    val lessons = vmLesson.getAllLessonsByCategoryId()

    var isPremium by remember { mutableStateOf(false) }
    val showLessonDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isPremium = scope.async { CheckIsPremium.checkIsPremium() }.await()
    }

    CategoryLessonsStateLess(
        lessons = lessons,
        category = category,

        isPremium = isPremium,
        showLessonDialog = showLessonDialog,

        vmPreferences = vmPreferences

    ) { id ->

        vmLesson.lessonId.intValue = id
        onNavigateLesson()
    }

    if (showLessonDialog.value) {
        PremiumLessonDialog(
            onDismissRequest = { showLessonDialog.value = false },
            onSeePremiumSubscription = {
                showLessonDialog.value = false
                onNavigateSubscriptionScreen()
            }
        )
    }
}

@Composable
fun CategoryLessonsStateLess(
    lessons: List<LessonDataClass>,
    category: LessonCategoryDataClass,

    isPremium: Boolean,
    showLessonDialog: MutableState<Boolean>,

    vmPreferences: PreferencesViewModel,

    onNavigateLesson: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                text = category.title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = category.description,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        lessons.forEachIndexed { index, lesson ->
            LessonCard(
                lesson = lesson,
                lessonNumber = index + 1,

                isPremium = isPremium,

                isRead = vmPreferences.isLessonRead(lesson.idLesson),
                onNavigateLesson = { onNavigateLesson(lesson.idLesson) },
                onNavigatePremium = { showLessonDialog.value = true },
            )
        }
    }
}