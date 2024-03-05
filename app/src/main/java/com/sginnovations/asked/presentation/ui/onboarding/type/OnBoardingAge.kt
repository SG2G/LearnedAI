package com.sginnovations.asked.presentation.ui.onboarding.type

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingPage
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingType

data class AgeRange(val range: String)

@Composable
fun OnBoardingAge(
    onBoardingPage: OnBoardingPage,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.select_your_age_range),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        AgeSelection(onBoardingPage)
    }
}

@Composable
fun AgeSelection(
    onBoardingPage: OnBoardingPage,
) {
    val scrollState = rememberScrollState()

    val ageRanges = listOf(
        AgeRange("18-24"),
        AgeRange("25-34"),
        AgeRange("35-44"),
        AgeRange("45-54"),
        AgeRange("+55"),
    )

    var selectedAgeRange by remember { mutableStateOf<AgeRange?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(8.dp)
    ) {
        ageRanges.forEach { ageRange ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 4.dp)
                    .clickable {
                        selectedAgeRange = ageRange
                    },
                shape = RoundedCornerShape(10.dp),
                border = if (selectedAgeRange == ageRange) BorderStroke(
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
                        .background(if (selectedAgeRange == ageRange) Color(0xFFE6E8F4) else Color.Transparent)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = ageRange.range,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

object AgeSelect : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.SingleSelect
    override fun getTitle(context: Context) = ""
    override fun getSubTitle(context: Context) = ""
}

@Preview(showBackground = true)
@Composable
fun PreviewOnBoardingAge() {
    MaterialTheme {
        OnBoardingAge(AgeSelect)
    }
}
