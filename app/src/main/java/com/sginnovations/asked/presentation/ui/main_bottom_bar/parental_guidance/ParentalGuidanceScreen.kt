package com.sginnovations.asked.presentation.ui.main_bottom_bar.parental_guidance

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.data.lessons.LessonCategoryDataClass
import com.sginnovations.asked.presentation.ui.main_bottom_bar.parental_guidance.components.CategoryLessonCard
import com.sginnovations.asked.presentation.ui.rss.RssFeedScreen
import com.sginnovations.asked.presentation.ui.utils.ResetStatusBarColor
import com.sginnovations.asked.presentation.viewmodel.LessonViewModel
import com.sginnovations.asked.presentation.viewmodel.RssFeedViewModel

private const val TAG = "ParentalGuidanceStateFul"


enum class Tabs {
    Lessons,
    ComingSoon;

    fun getTitle(context: Context): String {
        return when (this) {
            Lessons -> context.getString(R.string.tabs_lessons)
            ComingSoon -> context.getString(R.string.tabs_content)
        }
    }
}

@Composable
fun ParentalGuidanceStateFul(
    vmLesson: LessonViewModel,
    vmRss: RssFeedViewModel,

    onNavigateCategoryLessons: () -> Unit,
) {
    val context = LocalContext.current

    val lessonsCategory = vmLesson.getAllCategories()

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
                    text = { Text(tab.getTitle(context)) },
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
                    vmLesson = vmLesson,

                    lessonsCategory = lessonsCategory,

                    onNavigateCategoryLessons = { category ->
                        Log.d(TAG, "category id: ${category.idCategory}")

                        vmLesson.categoryId.intValue = category.idCategory
                        onNavigateCategoryLessons()
                    }
                )

            Tabs.ComingSoon ->
                RssFeedScreen(vmRss = vmRss)
        }
    }

}

@Composable
fun ParentalGuidanceStateLess(
    vmLesson: LessonViewModel,

    lessonsCategory: List<LessonCategoryDataClass>,

    onNavigateCategoryLessons: (LessonCategoryDataClass) -> Unit,
) {
    ResetStatusBarColor()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        lessonsCategory.forEach { category ->
            CategoryLessonCard(
                vmLesson = vmLesson,

                category = category,

                onNavigateCategoryLessons = {
                    onNavigateCategoryLessons(category)
                }
            )

        }
        Text(
            text = stringResource(R.string.more_lessons_coming_soon),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

