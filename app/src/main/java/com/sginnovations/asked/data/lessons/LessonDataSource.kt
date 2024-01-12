package com.sginnovations.asked.data.lessons

import android.content.Context
import com.sginnovations.asked.R
import javax.inject.Inject

class LessonDataSource @Inject constructor(
    private val context: Context
) {
    fun getAllCategories(): List<LessonCategoryDataClass> { return categories }

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
        LessonDataClass(
            0,
            1000,
            R.drawable.lesson_0,
            "Gestión de las Emociones y Resiliencia",
            "Como estudiar",
            "Gestión de las Emociones y Resiliencia",
            "**Enseñando a los Niños a Comprender y Manejar sus Emociones**\n" +
                    "\n" +
                    "Elena, madre de dos niños pequeños, se dio cuenta de la importancia de la gestión emocional cuando su hijo menor, Lucas, comenzó a tener berrinches frecuentes. En lugar de reprenderlo o ignorar sus explosiones emocionales, Elena decidió adoptar un enfoque más empático y educativo.\n" +
                    "\n" +
                    "Cuando Lucas se frustraba, Elena se arrodillaba a su altura y le decía con calma: \"Veo que estás muy enojado ahora. ¿Puedes decirme qué te molesta?\" Esta simple pregunta ayudaba a Lucas a empezar a identificar y poner palabras a sus emociones. Con el tiempo, Lucas aprendió a expresar sus sentimientos de manera más clara, reduciendo la frecuencia y la intensidad de sus berrinches.\n" +
                    "\n" +
                    "Elena también introdujo \"tiempos de calma\" en lugar de \"tiempos fuera\". Durante estos momentos, Lucas podía retirarse a un lugar tranquilo en la casa para calmarse, con la opción de hablar sobre sus sentimientos después. Este enfoque no solo ayudó a Lucas a calmarse, sino que también le enseñó técnicas útiles de autoregulación.\n" +
                    "\n" +
                    "**Conclusión: Fomentando la Resiliencia desde la Infancia**\n" +
                    "\n" +
                    "La historia de Elena y Lucas demuestra que enseñar a los niños a gestionar sus emociones y a ser resilientes es un proceso continuo que requiere paciencia y comprensión. Al reconocer y validar las emociones de los niños, y al proporcionarles herramientas para manejarlas, los padres les están equipando con habilidades cruciales para la vida.",
            "La gestión de emociones y el desarrollo de la resiliencia son habilidades esenciales que los padres pueden fomentar en sus hijos desde una edad temprana. Estas habilidades son fundamentales para el bienestar emocional y la capacidad de los niños para enfrentar desafíos en la vida. Inspirado por los conceptos presentados en \"El niño emocionalmente inteligente\" de John Gottman, este artículo explora cómo los padres pueden guiar a sus hijos jóvenes en el reconocimiento, la expresión y la gestión de sus emociones de manera saludable.",
            "54fjHJlMoNY",
            "**Consejos Prácticos**\n" +
                    "\n" +
                    "1. **Validación Emocional**: Reconoce y valida las emociones de tu hijo. Esto les enseña que todas las emociones son válidas y parte de la experiencia humana.\n" +
                    "2. **Modelado de Comportamiento**: Los niños aprenden observando. Muestra cómo gestionar tus propias emociones de manera saludable.\n" +
                    "3. **Técnicas de Autoregulación**: Enseña técnicas simples como la respiración profunda o la visualización para ayudar a los niños a calmarse cuando se sienten abrumados."
        ),
        LessonDataClass(
            0,
            1001,
            R.drawable.burro,
            "Gestión de las Emociones y Resiliencia",
            "Como estudiar",
            "Gestión de las Emociones y Resiliencia gestión de las Emociones y Resilienciaión de las Emociones y Resiliencia gestión de las Emociones y Resiliencia",
            "**Enseñando a los Niños a Comprender y Manejar sus Emociones**\n" +
                    "\n" +
                    "Elena, madre de dos niños pequeños, se dio cuenta de la importancia de la gestión emocional cuando su hijo menor, Lucas, comenzó a tener berrinches frecuentes. En lugar de reprenderlo o ignorar sus explosiones emocionales, Elena decidió adoptar un enfoque más empático y educativo.\n" +
                    "\n" +
                    "Cuando Lucas se frustraba, Elena se arrodillaba a su altura y le decía con calma: \"Veo que estás muy enojado ahora. ¿Puedes decirme qué te molesta?\" Esta simple pregunta ayudaba a Lucas a empezar a identificar y poner palabras a sus emociones. Con el tiempo, Lucas aprendió a expresar sus sentimientos de manera más clara, reduciendo la frecuencia y la intensidad de sus berrinches.\n" +
                    "\n" +
                    "Elena también introdujo \"tiempos de calma\" en lugar de \"tiempos fuera\". Durante estos momentos, Lucas podía retirarse a un lugar tranquilo en la casa para calmarse, con la opción de hablar sobre sus sentimientos después. Este enfoque no solo ayudó a Lucas a calmarse, sino que también le enseñó técnicas útiles de autoregulación.\n" +
                    "\n" +
                    "**Conclusión: Fomentando la Resiliencia desde la Infancia**\n" +
                    "\n" +
                    "La historia de Elena y Lucas demuestra que enseñar a los niños a gestionar sus emociones y a ser resilientes es un proceso continuo que requiere paciencia y comprensión. Al reconocer y validar las emociones de los niños, y al proporcionarles herramientas para manejarlas, los padres les están equipando con habilidades cruciales para la vida.",
            "La gestión de emociones y el desarrollo de la resiliencia son habilidades esenciales que los padres pueden fomentar en sus hijos desde una edad temprana. Estas habilidades son fundamentales para el bienestar emocional y la capacidad de los niños para enfrentar desafíos en la vida. Inspirado por los conceptos presentados en \"El niño emocionalmente inteligente\" de John Gottman, este artículo explora cómo los padres pueden guiar a sus hijos jóvenes en el reconocimiento, la expresión y la gestión de sus emociones de manera saludable.",
            "54fjHJlMoNY",
            "**Consejos Prácticos**\n" +
                    "\n" +
                    "1. **Validación Emocional**: Reconoce y valida las emociones de tu hijo. Esto les enseña que todas las emociones son válidas y parte de la experiencia humana.\n" +
                    "2. **Modelado de Comportamiento**: Los niños aprenden observando. Muestra cómo gestionar tus propias emociones de manera saludable.\n" +
                    "3. **Técnicas de Autoregulación**: Enseña técnicas simples como la respiración profunda o la visualización para ayudar a los niños a calmarse cuando se sienten abrumados."
        ),
        LessonDataClass(
            0,
            1002,
            R.drawable.burro,
            "Gestión de gestiones",
            "Como estudiar",
            "Gestión de las Emociones y Resiliencia",
            "**Enseñando a los Niños a Comprender y Manejar sus Emociones**\n" +
                    "\n" +
                    "Elena, madre de dos niños pequeños, se dio cuenta de la importancia de la gestión emocional cuando su hijo menor, Lucas, comenzó a tener berrinches frecuentes. En lugar de reprenderlo o ignorar sus explosiones emocionales, Elena decidió adoptar un enfoque más empático y educativo.\n" +
                    "\n" +
                    "Cuando Lucas se frustraba, Elena se arrodillaba a su altura y le decía con calma: \"Veo que estás muy enojado ahora. ¿Puedes decirme qué te molesta?\" Esta simple pregunta ayudaba a Lucas a empezar a identificar y poner palabras a sus emociones. Con el tiempo, Lucas aprendió a expresar sus sentimientos de manera más clara, reduciendo la frecuencia y la intensidad de sus berrinches.\n" +
                    "\n" +
                    "Elena también introdujo \"tiempos de calma\" en lugar de \"tiempos fuera\". Durante estos momentos, Lucas podía retirarse a un lugar tranquilo en la casa para calmarse, con la opción de hablar sobre sus sentimientos después. Este enfoque no solo ayudó a Lucas a calmarse, sino que también le enseñó técnicas útiles de autoregulación.\n" +
                    "\n" +
                    "**Conclusión: Fomentando la Resiliencia desde la Infancia**\n" +
                    "\n" +
                    "La historia de Elena y Lucas demuestra que enseñar a los niños a gestionar sus emociones y a ser resilientes es un proceso continuo que requiere paciencia y comprensión. Al reconocer y validar las emociones de los niños, y al proporcionarles herramientas para manejarlas, los padres les están equipando con habilidades cruciales para la vida.",
            "La gestión de emociones y el desarrollo de la resiliencia son habilidades esenciales que los padres pueden fomentar en sus hijos desde una edad temprana. Estas habilidades son fundamentales para el bienestar emocional y la capacidad de los niños para enfrentar desafíos en la vida. Inspirado por los conceptos presentados en \"El niño emocionalmente inteligente\" de John Gottman, este artículo explora cómo los padres pueden guiar a sus hijos jóvenes en el reconocimiento, la expresión y la gestión de sus emociones de manera saludable.",
            "54fjHJlMoNY",
            "**Consejos Prácticos**\n" +
                    "\n" +
                    "1. **Validación Emocional**: Reconoce y valida las emociones de tu hijo. Esto les enseña que todas las emociones son válidas y parte de la experiencia humana.\n" +
                    "2. **Modelado de Comportamiento**: Los niños aprenden observando. Muestra cómo gestionar tus propias emociones de manera saludable.\n" +
                    "3. **Técnicas de Autoregulación**: Enseña técnicas simples como la respiración profunda o la visualización para ayudar a los niños a calmarse cuando se sienten abrumados."
        ),
    )

    private val categories = listOf(
        LessonCategoryDataClass(
            0,
            R.drawable.lesson_0,
            "Crecimiento y Evolución Infantil",
            "3 lessons",
            "Descripción y noseque Descripción y noseque noseque Descripción y noseque",

        ),
        LessonCategoryDataClass(
            1,
            R.drawable.lesson_0,
            "Hola que pasa",
            "4 lessons",
            "Descripción y noseque Descripción y noseque noseque Descripción y noseque",

            )
    )

    private fun defaultLesson(): LessonDataClass {
        // Return a default LessonDataClass object
        return LessonDataClass(
            // Set default values
            idCategory = -1,
            idLesson = -1000,
            imageId = R.drawable.burro,
            title = "Default Title",
            subtitle = "Default Subtitle",
            description = "Default Description",
            transcription = "Default Transcription",
            introduction = "Default Introduction",
            videoId = "DefaultVideoID",
            conclusion = "Default Conclusion"
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
