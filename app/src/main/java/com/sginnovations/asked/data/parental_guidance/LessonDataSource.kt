package com.sginnovations.asked.data.parental_guidance

import android.content.Context
import com.sginnovations.asked.R
import javax.inject.Inject

class LessonDataSource @Inject constructor(
    private val context: Context
) {

    fun getLessonById(id: Int): LessonDataClass {
        return lessons.find { it.id == id } ?: defaultLesson()
    }
    fun getAllLessons(): List<LessonDataClass> {
        return lessons ?: emptyList()
    }

    private val lessons = listOf(
        LessonDataClass(
            0,
            R.drawable.burro,
            "Como estudiar y sacarse la ESO?? que loco",
            "Como estudiar",
            "Como estudiar y noseque vamos a poner un texto to largo pa que quepa",
            context.getString(R.string.lessons_transcription1),
            "introduction1",
            "54fjHJlMoNY",
            "conclusion1"
        ),
    )

    private fun defaultLesson(): LessonDataClass {
        // Return a default LessonDataClass object
        return LessonDataClass(
            // Set default values
            id = -1,  // Indicative of a default or non-existent lesson
            imageId = R.drawable.burro,  // Assuming you have a default drawable
            title = "Default Title",
            subtitle = "Default Subtitle",
            description = "Default Description",
            transcription = "Default Transcription",
            introduction = "Default Introduction",
            videoId = "DefaultVideoID",
            conclusion = "Default Conclusion"
        )
    }
}
