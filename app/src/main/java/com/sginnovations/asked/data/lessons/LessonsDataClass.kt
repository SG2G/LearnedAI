package com.sginnovations.asked.data.lessons

data class LessonDataClass(
    val idCategory: Int,

    val idLesson: Int,

    val imageId: Int,

    val title: String,
    val subtitle: String,
    val description: String,

    val transcription: String,

    val introduction: String,
    val videoId: String,
    val conclusion: String,

    val isPremium: Boolean,
)
data class LessonCategoryDataClass(
    val idCategory: Int,

    val imageId: Int,

    val title: String,
    val subtitle: String,
    val description: String,
)

