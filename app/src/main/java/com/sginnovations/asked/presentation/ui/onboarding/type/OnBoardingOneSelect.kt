package com.sginnovations.asked.presentation.ui.onboarding.type

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingOneSelection
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingPage

@Composable
fun OnBoardingOneSelect(
    onBoardingPage: OnBoardingPage,
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = onBoardingPage.getTitle(context),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        OneSelection(onBoardingPage)
    }
}

@Composable
fun OneSelection(
    onBoardingPage: OnBoardingPage,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val options = remember { onBoardingPage.getOneOptions(context) }

    var selectedOption by remember { mutableStateOf<OnBoardingOneSelection?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(8.dp)
    ) {
        options?.forEach { option ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 4.dp)
                    .clickable {
                        selectedOption = option
                    },
                shape = RoundedCornerShape(10.dp),
                border = if (selectedOption == option) BorderStroke(
                    2.dp,
                    Color(0xFF155CE9)
                ) else BorderStroke(1.dp, Color(0xFFC0BFBF)),
                colors = CardDefaults.cardColors(
                    MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .background(if (selectedOption == option) Color(0xFFE6E8F4) else Color.Transparent)
                ) {
                    if (option.text2.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            text = option.text1,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(16.dp),
                                text = option.text1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(16.dp),
                                text = option.text2,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                    }
                }
            }
        }
    }
}

//object AgeSelect : OnBoardingPage {
//    override fun getType(context: Context): OnBoardingType = OnBoardingType.SingleSelect
//    override fun getTitle(context: Context) = ""
//    override fun getSubTitle(context: Context) = ""
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewOnBoardingAge() {
//    MaterialTheme {
//        OnBoardingAge(AgeSelect)
//    }
//}
