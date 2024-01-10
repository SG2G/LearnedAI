package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.data.lessons.LessonDataClass
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components.LessonCard
import com.sginnovations.asked.viewmodel.LessonViewModel
import com.sginnovations.asked.viewmodel.PreferencesViewModel

private const val TAG = "ParentalGuidanceStateFul"

enum class Tabs(val title: String) {
    Lessons("Lessons"),
    ComingSoon("Content")
}

@Composable
fun ParentalGuidanceStateFul(
    vmLesson: LessonViewModel,
    vmPreferences: PreferencesViewModel,

    onNavigateLesson: () -> Unit,
) {
    val lessons = vmLesson.getAllLessons()

    val selectedTab = remember { mutableStateOf(Tabs.Lessons) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab.value.ordinal,
            containerColor = MaterialTheme.colorScheme.background,
            indicator = { tabPositions ->
                val modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTab.value.ordinal])
                    .padding(horizontal = 64.dp)
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp))

                Box(modifier = modifier)
            }
        ) {
            Tabs.values().forEach { tab ->
                Tab(
                    text = { Text(tab.title) },
                    selected = tab == selectedTab.value,
                    onClick = { selectedTab.value = tab },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Divider()

        // Content of the selected tab
        when (selectedTab.value) {
            Tabs.Lessons -> ParentalGuidanceStateLess(
                vmPreferences = vmPreferences,

                lessons = lessons
            ) { id ->
                Log.d(TAG, "id: $id")
                vmLesson.lessonId.intValue = id
                onNavigateLesson()
            }

            Tabs.ComingSoon -> ComingSoonTabContent()
        }
    }

}

@Composable
fun ParentalGuidanceStateLess(
    vmPreferences: PreferencesViewModel,

    lessons: List<LessonDataClass>,

    onNavigate: (Int) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize()
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

@Composable
fun ComingSoonTabContent() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Coming Soon...", style = MaterialTheme.typography.titleMedium)
    }
}

