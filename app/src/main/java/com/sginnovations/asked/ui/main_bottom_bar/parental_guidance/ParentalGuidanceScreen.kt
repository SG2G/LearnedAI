package com.sginnovations.asked.ui.main_bottom_bar.parental_guidance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components.CustomCard

@Composable
fun ParentalGuidanceStateFul(

    onNavigate: (String) -> Unit,
) {

    ParentalGuidanceStateLess(
        onNavigate = { destination ->
            onNavigate(destination)
        }
    )
}
@Composable
fun ParentalGuidanceStateLess(

    onNavigate: (String) -> Unit,
) {

    Column(modifier = Modifier.fillMaxSize()
    ) {
        CustomCard(
            title = "Como estudiar",
            subtitle = "12 lecciones",
            description = "Aqui te enseñare mucha cosa",
            imageUrl = "https://img.freepik.com/foto-gratis/primer-plano-lindo-burro-pastando-prado-generado-ia_188544-14334.jpg?w=1380&t=st=1704470903~exp=1704471503~hmac=8a5d95f79d6a8d36ac995ee5b29aead3a1cb33d97a2d0b14cf461d6dedfd0cfb",

            onClick = {  }
        )
    }
}