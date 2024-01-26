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
            videoId = "54fjHJlMoNY",
            reference = "",
            conclusion = null,
            buttonText = null,
            questionAsked = null,
            isPremium = false,
            lessonPages = 0
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
            videoId = "54fjHJlMoNY",
            reference = "Waters, L. (2017). The strength switch: how the new science of strength-based parenting helps your child and your teen flourish. Scribe Publications.",
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
            title = "Comunicación efectiva",
            subtitle = "algo",
            transcription = context.getString(R.string.lesson_transcription_1002),
            introduction = "¿Sabías que la manera en que hablas con tus hijos puede influir en su confianza y éxito futuro? Descubre cómo la comunicación efectiva puede ser tu herramienta más poderosa como padre.",
            videoId = "54fjHJlMoNY",
            reference = "Rolland, R. (2023). El arte de hablar con niños: Claves sencillas para fomentar su autonomía, creatividad y confianza. Diana Editorial.",
            conclusion = context.getString(R.string.lesson_conclusion_1002),
            buttonText = "Desbloquea la Magia de Escuchar a tu Hijo",
            questionAsked = "¿Cómo puedo mejorar mis habilidades de escucha activa para tener una comunicación más efectiva con mi hijo?",
            isPremium = false,
            lessonPages = 1
        ),
        /**
         * Help Kids WHit Homework
         */
        LessonDataClass(
            idCategory = 1,
            idLesson = 2000,
            title = "Cómo los Padres Pueden Ayudar a sus Hijos con los Deberes",
            subtitle = "Estrategias para una Educación Colaborativa",
            transcription = context.getString(R.string.lesson_transcription_2000),
            introduction = "Es la hora de hacer los deberes y tu hijo está frustrado, no sabe por dónde empezar. ¿Intervienes o lo dejas enfrentarse solo a este reto? Esta es una pregunta común que muchos padres se hacen. Hoy, vamos a explorar cómo puedes ser un apoyo efectivo sin quitarles a tus hijos la oportunidad de aprender por sí mismos.",
            videoId = "54fjHJlMoNY",
            reference = "Kohn, A. (2007). The homework myth: Why Our Kids Get Too Much of a Bad Thing. Hachette UK.",
            conclusion = context.getString(R.string.lesson_conclusion_2000),
            buttonText = "Descubre el Arte de Apoyar sin Interferir",
            questionAsked = "¿Cómo puedo identificar el momento adecuado para intervenir y ayudar a mi hijo con sus deberes sin quitarle la oportunidad de aprender por sí mismo?",
            isPremium = false,
            lessonPages = 1
        ),
        /**
         * Time Management
         */
        LessonDataClass(
            idCategory = 1,
            idLesson = 2001,
            title = "Comunicación efectiva",
            subtitle = "algo",
            transcription = "context.getString(R.string.lesson_transcription_1002)",
            introduction = "¿Te has preguntado cómo algo tan simple como la gestión del tiempo puede ser un superpoder oculto en la educación de tus hijos? Descubre cómo convertirlo en una aventura emocionante y fundamental para su éxito futuro",
            videoId = "54fjHJlMoNY",
            reference = "Dawson, P., & Guare, R. (2011). Smart but scattered: The Revolutionary «Executive Skills» Approach to Helping Kids Reach Their Potential. Guilford Press.",
            conclusion = context.getString(R.string.lesson_conclusion_2001),
            buttonText = "Juega y Organiza: Aprende Cómo",
            questionAsked = "¿Puedes darme más ejemplos de juegos o actividades lúdicas que los padres pueden usar para enseñar a los niños sobre la gestión del tiempo y la organización?",
            isPremium = true,
            lessonPages = 1
        ),
        /**
         * Health and well-being
         */
        LessonDataClass(
            idCategory = 2,
            idLesson = 3000,
            title = "Nutrición Equilibrada y Alimentación Saludable para Niños.",
            subtitle = "algo",
            transcription = "",
            introduction = "¿Sabies que si si?",
            videoId = "54fjHJlMoNY",
            reference = "",
            conclusion = "",
            buttonText = "",
            questionAsked = "",
            isPremium = true,
            lessonPages = 1
        ),
    )

    private val categories = listOf(
        LessonCategoryDataClass(
            0,
            R.drawable.lesson_0,
            context.getString(R.string.guide_category_title_0),
            "3 lessons",
            context.getString(R.string.guide_category_description_0),

            ),
        LessonCategoryDataClass(
            1,
            R.drawable.lesson_1,
            context.getString(R.string.guide_category_title_1),
            "2 lessons",
            context.getString(R.string.guide_category_description_1),

            ),
        LessonCategoryDataClass(
            2,
            R.drawable.lesson_2,
            context.getString(R.string.guide_category_title_2),
            "2 lessons",
            context.getString(R.string.guide_category_description_2),

            ),
        LessonCategoryDataClass(
            3,
            R.drawable.lesson_3,
            context.getString(R.string.guide_category_title_3),
            "2 lessons",
            context.getString(R.string.guide_category_description_3),

            ),
        LessonCategoryDataClass(
            4,
            R.drawable.lesson_4,
            context.getString(R.string.guide_category_title_4),
            "2 lessons",
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
