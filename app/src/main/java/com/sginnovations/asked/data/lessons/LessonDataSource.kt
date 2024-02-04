package com.sginnovations.asked.data.lessons

import android.content.Context
import com.sginnovations.asked.R
import javax.inject.Inject

class LessonDataSource @Inject constructor(
    private val context: Context,
) {
    fun getAllCategories(): List<LessonCategoryDataClass> {
        return categories
    }

    fun getCategoryById(id: Int): LessonCategoryDataClass {
        return categories.find { it.idCategory == id } ?: defaultCategory()
    }

    fun getAllLessonsByCategoryId(idCategory: Int): List<LessonDataClass> {
        return lessons.filter { it.idCategory == idCategory }
    }

    fun getLessonById(id: Int): LessonDataClass {
        return lessons.find { it.idLesson == id } ?: defaultLesson()
    }

    fun countLessonsByCategory(idCategory: Int): Int {
        return lessons.count { it.idCategory == idCategory }
    }

    private val lessons = listOf(
        /**
         * Intro
         */
        LessonDataClass(
            idCategory = 0,
            idLesson = 1000,
            title = context.getString(R.string.lesson_title_1000),
            subtitle = context.getString(R.string.lesson_subtitle_1000),
            transcription = context.getString(R.string.lesson_transcription_1000),
            introduction = context.getString(R.string.lesson_introduction_1000),
            videoId = context.getString(R.string.lesson_videoid_1000),
            reference = "",
            conclusion = context.getString(R.string.lesson_conclusion_1000),
            buttonText = null,
            questionAsked = null,
            isPremium = false,
            lessonPages = 1
        ),
        /**
         * Self esteem
         */
        LessonDataClass(
            idCategory = 0,
            idLesson = 1001,
            title = context.getString(R.string.lesson_title_1001),
            subtitle = context.getString(R.string.lesson_subtitle_1001),
            transcription = context.getString(R.string.lesson_transcription_1001),
            introduction = context.getString(R.string.lesson_introduction_1001),
            videoId = context.getString(R.string.lesson_videoid_1001),
            reference = context.getString(R.string.lesson_reference_1001),
            conclusion = context.getString(R.string.lesson_conclusion_1001),
            buttonText = context.getString(R.string.lesson_button_text_1001),
            questionAsked = context.getString(R.string.lesson_question_asked_1001),
            isPremium = false,
            lessonPages = 1
        ),
        /**
         * Effective Communication
         */
        LessonDataClass(
            idCategory = 0,
            idLesson = 1002,
            title = context.getString(R.string.lesson_title_1002),
            subtitle = context.getString(R.string.lesson_subtitle_1002),
            transcription = context.getString(R.string.lesson_transcription_1002),
            introduction = context.getString(R.string.lesson_introduction_1002),
            videoId = context.getString(R.string.lesson_videoid_1002),
            reference = context.getString(R.string.lesson_reference_1002),
            conclusion = context.getString(R.string.lesson_conclusion_1002),
            buttonText = context.getString(R.string.lesson_button_text_1002),
            questionAsked = context.getString(R.string.lesson_question_asked_1002),
            isPremium = false,
            lessonPages = 1
        ),
        /**
         * Help Kids WHit Homework
         */
        LessonDataClass(
            idCategory = 1,
            idLesson = 2000,
            title = context.getString(R.string.lesson_title_2000),
            subtitle = context.getString(R.string.lesson_subtitle_2000),
            transcription = context.getString(R.string.lesson_transcription_2000),
            introduction = context.getString(R.string.lesson_introduction_2000),
            videoId = context.getString(R.string.lesson_videoid_2000),
            reference = context.getString(R.string.lesson_reference_2000),
            conclusion = context.getString(R.string.lesson_conclusion_2000),
            buttonText = context.getString(R.string.lesson_button_text_2000),
            questionAsked = context.getString(R.string.lesson_question_asked_2000),
            isPremium = false,
            lessonPages = 1
        ),
        /**
         * Time Management
         */
        LessonDataClass(
            idCategory = 1,
            idLesson = 2001,
            title = context.getString(R.string.lesson_title_2001),
            subtitle = context.getString(R.string.lesson_subtitle_2001),
            transcription = context.getString(R.string.lesson_transcription_2001),
            introduction = context.getString(R.string.lesson_introduction_2001),
            videoId = context.getString(R.string.lesson_videoid_2001),
            reference = context.getString(R.string.lesson_reference_2001),
            conclusion = context.getString(R.string.lesson_conclusion_2001),
            buttonText = context.getString(R.string.lesson_button_text_2001),
            questionAsked = context.getString(R.string.lesson_question_asked_2001),
            isPremium = true, //TODO CHANGE
            lessonPages = 1
        ),
        /**
         * Health and well-being Food
         */
        LessonDataClass(
            idCategory = 2,
            idLesson = 3000,
            title = context.getString(R.string.lesson_title_3000),
            subtitle = context.getString(R.string.lesson_subtitle_3000),
            transcription = context.getString(R.string.lesson_transcription_3000),
            introduction = context.getString(R.string.lesson_introduction_3000),
            videoId = context.getString(R.string.lesson_videoid_3000),
            reference = context.getString(R.string.lesson_reference_3000),
            conclusion = context.getString(R.string.lesson_conclusion_3000),
            buttonText = context.getString(R.string.lesson_button_text_3000),
            questionAsked = context.getString(R.string.lesson_question_asked_3000),
            isPremium = true,
            lessonPages = 1
        ),
        /**
         * Important of Sports
         */
        LessonDataClass(
            idCategory = 2,
            idLesson = 3001,
            title = context.getString(R.string.lesson_title_3001),
            subtitle = context.getString(R.string.lesson_subtitle_3001),
            transcription = context.getString(R.string.lesson_transcription_3001),
            introduction = context.getString(R.string.lesson_introduction_3001),
            videoId = context.getString(R.string.lesson_videoid_3001),
            reference = context.getString(R.string.lesson_reference_3001),
            conclusion = context.getString(R.string.lesson_conclusion_3001),
            buttonText = context.getString(R.string.lesson_button_text_3001),
            questionAsked = context.getString(R.string.lesson_question_asked_3001),
            isPremium = true,
            lessonPages = 1
        ),
        /**
         * Sleep zzZZZ
         */
        LessonDataClass(
            idCategory = 2,
            idLesson = 3002,
            title = context.getString(R.string.lesson_title_3002),
            subtitle = context.getString(R.string.lesson_subtitle_3002),
            transcription = context.getString(R.string.lesson_transcription_3002),
            introduction = context.getString(R.string.lesson_introduction_3002),
            videoId = context.getString(R.string.lesson_videoid_3002),
            reference = context.getString(R.string.lesson_reference_3002),
            conclusion = context.getString(R.string.lesson_conclusion_3002),
            buttonText = context.getString(R.string.lesson_button_text_3002),
            questionAsked = context.getString(R.string.lesson_question_asked_3002),
            isPremium = true,
            lessonPages = 1
        ),

        /**
         * Empathy
         */
        LessonDataClass(
            idCategory = 3,
            idLesson = 4000,
            title = context.getString(R.string.lesson_title_4000),
            subtitle = context.getString(R.string.lesson_subtitle_4000),
            transcription = context.getString(R.string.lesson_transcription_4000),
            introduction = context.getString(R.string.lesson_introduction_4000),
            videoId = context.getString(R.string.lesson_videoid_4000),
            reference = context.getString(R.string.lesson_reference_4000),
            conclusion = context.getString(R.string.lesson_conclusion_4000),
            buttonText = context.getString(R.string.lesson_button_text_4000),
            questionAsked = context.getString(R.string.lesson_question_asked_4000),
            isPremium = true,
            lessonPages = 1
        ),

        /**
         * Tech
         */
//        LessonDataClass(
//            idCategory = 4,
//            idLesson = 5000,
//            title = "lesson",
//            subtitle = "lesson",
//            transcription = "lesson",
//            introduction = "lesson",
//            videoId = "54fjHJlMoNY",
//            reference = "lesson",
//            conclusion = "lesson",
//            buttonText = "lesson",
//            questionAsked = "lesson",
//            isPremium = false,
//            lessonPages = 1
//        ),
    )

    private val categories = listOf(
        LessonCategoryDataClass(
            0,
            R.drawable._1,
            context.getString(R.string.guide_category_title_0),
            context.getString(R.string.lessons),
            context.getString(R.string.guide_category_description_0),

            ),
        LessonCategoryDataClass(
            1,
            R.drawable._2,
            context.getString(R.string.guide_category_title_1),
            context.getString(R.string.lessons),
            context.getString(R.string.guide_category_description_1),

            ),
        LessonCategoryDataClass(
            2,
            R.drawable._3,
            context.getString(R.string.guide_category_title_2),
            context.getString(R.string.lessons),
            context.getString(R.string.guide_category_description_2),

            ),
        LessonCategoryDataClass(
            3,
            R.drawable._4,
            context.getString(R.string.guide_category_title_3),
            context.getString(R.string.lessons),
            context.getString(R.string.guide_category_description_3),

            ),
        LessonCategoryDataClass(
            4,
            R.drawable._5,
            context.getString(R.string.guide_category_title_4),
            context.getString(R.string.lessons),
            context.getString(R.string.guide_category_description_4),

            ),
    )

    private fun defaultLesson(): LessonDataClass {
        // Return a default LessonDataClass object
        return LessonDataClass(
            // Set default values
            idCategory = -1,
            idLesson = -1000,
            title = "Default Title",
            subtitle = "Default Subtitle",
            transcription = "Default Transcription",
            introduction = "Default Introduction",
            videoId = "DefaultVideoID",
            conclusion = "Default Conclusion",
            reference = "Default Reference",
            buttonText = "Default Button",
            questionAsked = "Default Question",
            isPremium = false,
            lessonPages = 1
        )
    }

    private fun defaultCategory(): LessonCategoryDataClass {
        // Return a default LessonDataClass object
        return LessonCategoryDataClass(
            -1,
            R.drawable.lesson_0, //TODO DEFAULT PHOTO
            "Default Title",
            "Default Subtitle",
            "Default Description",

            )
    }
}
