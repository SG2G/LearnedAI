package com.sginnovations.asked.presentation.ui.onboarding.type

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingPage
import com.sginnovations.asked.presentation.ui.onboarding.OnBoardingType

@Composable
fun OnBoardingQuote(
    onBoardingPage: OnBoardingPage,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            text = stringResource(R.string.do_you_agree_with_this_statement),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    Color(0xFFE6E8F4)
                )
            ) {
                Icon(
                    modifier = Modifier.height(32.dp).padding(start = 16.dp, top = 16.dp),
                    painter = painterResource(id = R.drawable.quotes_end_svgrepo_com),
                    contentDescription = null,
                    tint = Color(0xFF3C5AFA)
                )
                onBoardingPage.getDescription(context)?.let { text ->
                    Text(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }

}

object QuoteScreen : OnBoardingPage {
    override fun getType(context: Context): OnBoardingType = OnBoardingType.Quote
    override fun getTitle(context: Context) = ""
    override fun getSubTitle(context: Context) = ""
    override fun getDescription(context: Context) = "hola esto es una quote"
}

@Preview(showBackground = true)
@Composable
fun PreviewQuoteScreen() {
    OnBoardingQuote(QuoteScreen)
}
