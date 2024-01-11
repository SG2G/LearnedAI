package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.data.lessons.LessonCategoryDataClass
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components.CategoryLessonCard
import com.sginnovations.asked.viewmodel.LessonViewModel

private const val TAG = "ParentalGuidanceStateFul"

enum class Tabs(val title: String) {
    Lessons("Lessons"),
    ComingSoon("Content")
}

@Composable
fun ParentalGuidanceStateFul(
    vmLesson: LessonViewModel,

    onNavigateCategory: () -> Unit,
) {
    val lessonsCategory = vmLesson.getAllLessonsCategory()

    val selectedTab = remember { mutableStateOf(Tabs.Lessons) }

    Column(modifier = Modifier.fillMaxSize()) {
        /**
         * Tab row
         */
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

        /**
         * StateLess
         */
        when (selectedTab.value) {
            Tabs.Lessons ->
                ParentalGuidanceStateLess(
                    lessonsCategory = lessonsCategory,

                    onNavigateCategory = { id ->
                        Log.d(TAG, "category id: $id")

                        vmLesson.lessonCategoryId.intValue = id
                        onNavigateCategory()
                    }
                )

            Tabs.ComingSoon ->
                ComingSoonTabContent()
        }
    }

}

@Composable
fun ParentalGuidanceStateLess(
    lessonsCategory: List<LessonCategoryDataClass>,

    onNavigateCategory: (Int) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        lessonsCategory.forEach { category ->

            CategoryLessonCard(
                imagePainter = painterResource(id = R.drawable.burro),
                title = category.title,
                subtitle = category.subtitle,
                description = category.description,

                onNavigateCategory = { onNavigateCategory(category.idCategory) }
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

