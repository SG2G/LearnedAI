package com.sginnovations.asked.viewmodel

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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

    val categoryId = mutableIntStateOf(0)

    // GET CATEGORIES
    /**
     * Get ALL Categories
     */
    fun getAllCategories(): List<LessonCategoryDataClass> {
        return lessonDataSource.getAllCategories()
    }
    /**
     * Get 1 Category
     */
    fun getCategoryById(): LessonCategoryDataClass {
        return lessonDataSource.getCategoryById(categoryId.intValue)
    }

    // GET LESSONS
    /**
     * All lessons on 1 Category
     */
    fun getAllLessonsByCategoryId(): List<LessonDataClass> {
        return lessonDataSource.getAllLessonsByCategoryId(categoryId.intValue)
    }

    /**
     * Get 1 Lesson
     */
    fun getLessonById(id: Int): LessonDataClass {
        return lessonDataSource.getLessonById(id)
    }

}
