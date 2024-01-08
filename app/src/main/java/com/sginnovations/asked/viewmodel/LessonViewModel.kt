package com.sginnovations.asked.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import com.sginnovations.asked.data.parental_guidance.LessonDataClass
import com.sginnovations.asked.data.parental_guidance.LessonDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val lessonDataSource: LessonDataSource
) : ViewModel() {

    val lessonId = mutableIntStateOf(0)

    fun getLessonById(id: Int): LessonDataClass {
        return lessonDataSource.getLessonById(id)
    }
    fun getAllLessons(): List<LessonDataClass> {
        return lessonDataSource.getAllLessons()
    }

    // Other ViewModel logic...
}
