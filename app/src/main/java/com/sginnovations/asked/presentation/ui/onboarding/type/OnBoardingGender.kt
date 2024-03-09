package com.sginnovations.asked.presentation.ui.onboarding.type

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingPage

data class GenderOption(
    val name: String,
    @DrawableRes val imageRes: Int,
)

@Composable
fun OnBoardingGender(
    onBoardingPage: OnBoardingPage,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.select_your_gender),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        GenderSelection()
    }
}

@Composable
fun GenderSelection(
) {
    val genders = listOf(
        GenderOption(stringResource(R.string.male), R.drawable.male),
        GenderOption(stringResource(R.string.female), R.drawable.female),
        GenderOption(stringResource(R.string.other), R.drawable.other)
    )

    var selectedGender by remember { mutableStateOf<GenderOption?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        genders.forEach { gender ->
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .clickable {
                        selectedGender = gender
                    },
                shape = RoundedCornerShape(10.dp),
                border = if (selectedGender == gender) BorderStroke(
                    2.dp,
                    Color(0xFF155CE9)
                ) else BorderStroke(1.dp, Color.LightGray),
                colors = CardDefaults.cardColors(
                    MaterialTheme.colorScheme.surface
                ),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(158.dp)
                        .background(if (selectedGender == gender) Color(0xFFE6E8F4) else Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier.padding(16.dp),
                            painter = painterResource(id = gender.imageRes),
                            contentDescription = "${gender.name} image",
                        )
                        Text(text = gender.name, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

//object GenderSelect : OnBoardingPage {
//    override fun getType(context: Context): OnBoardingType = OnBoardingType.GenderSelect
//    override fun getTitle(context: Context) = ""
//    override fun getSubTitle(context: Context) = ""
//
//}

//@Preview(showBackground = true)
//@Composable
//fun PreviewOnBoardingGender() {
//    MaterialTheme {
//        OnBoardingGender(
//            onBoardingPage = GenderSelect,
//        )
//    }
//}
