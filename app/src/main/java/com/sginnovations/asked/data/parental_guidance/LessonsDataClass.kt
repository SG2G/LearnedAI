package com.sginnovations.asked.data.parental_guidance

import android.content.Context
import com.sginnovations.asked.R

data class LessonDataClass(
    val id: Int,

    val imageId: Int,

    val title: String,
    val subtitle: String,
    val description: String,

    val transcription: String,

    val introduction: String,
    val videoId: String,
    val conclusion: String,
)

