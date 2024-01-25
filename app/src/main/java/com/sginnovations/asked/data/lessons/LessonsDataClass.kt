package com.sginnovations.asked.data.lessons

data class LessonDataClass(
    val idCategory: Int,

    val idLesson: Int,

    val title: String,
    val subtitle: String,

    val transcription: String,

    val introduction: String,
    val videoId: String,
    val reference: String,


    val conclusion: String?,
    val buttonText: String?,
    val questionAsked: String?,

    val isPremium: Boolean,

    val lessonPages: Int,
)
data class LessonCategoryDataClass(
    val idCategory: Int,

    val imageId: Int,

    val title: String,
    val subtitle: String,
    val description: String,
)

