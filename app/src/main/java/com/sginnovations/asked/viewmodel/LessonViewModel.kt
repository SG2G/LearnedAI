package com.sginnovations.asked.viewmodel

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import com.sginnovations.asked.data.lessons.LessonCategoryDataClass
import com.sginnovations.asked.data.lessons.LessonDataClass
import com.sginnovations.asked.data.lessons.LessonDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val lessonDataSource: LessonDataSource
) : ViewModel() {

    val lessonId = mutableIntStateOf(0)
    val lessonCategoryId = mutableIntStateOf(0)

    fun getLessonById(id: Int): LessonDataClass {
        return lessonDataSource.getLessonById(id)
    }
    fun getAllLessons(): List<LessonDataClass> {
        return lessonDataSource.getAllLessons()
    }
    fun getAllLessonsCategory(): List<LessonCategoryDataClass> {
        return lessonDataSource.getAllLessonsCategory()
    }

}
