package com.sginnovations.learnedai.ui.points

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.learnedai.ui.ui_components.points.PointsDisplay
import com.sginnovations.learnedai.ui.ui_components.points.TokenIcon
import com.sginnovations.learnedai.viewmodel.AdsViewModel
import com.sginnovations.learnedai.viewmodel.TokenViewModel

private const val TAG = "PointsStateFul"
@Composable
fun PointsStateFul(
    vmToken: TokenViewModel,
    vmAds: AdsViewModel
) {
    val context = LocalContext.current
    fun Context.getActivity(): Activity? {
        return when(this) {
            is Activity -> this
            is ContextWrapper -> baseContext.getActivity()
            else -> null
        }
    }
    val activity = context.getActivity()

    val tokens = vmToken.tokens.collectAsState()

    LaunchedEffect(Unit) {
        Log.i(TAG, "Loading Ad in LauncherEffect")
        vmAds.loadRewardedAd(context)
    }

    PointsStateLess(
        tokens = tokens

    ) {
        if (activity != null) {
            vmAds.showRewardedAd(activity)
        }
    }
}

@Composable
fun PointsStateLess(
    tokens: State<Long>,

    onShowAd: () -> Unit,
) {

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            PointsDisplay(
                tokens = tokens,
                showPlus = false
            ) {}
        }
        Text(text = "Hola")

        Space(n = 16)
        PointsCard(
            text = "Unlimited Points",
            buttonText = "See more",
            borderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            buttonColor = MaterialTheme.colorScheme.background,
            cardContainerColor = MaterialTheme.colorScheme.primaryContainer,
            onClick = {  }
        )
        Space(16)
        PointsCard(
            text = "+2 Watch ad",
            buttonText = "Watch",
            borderColor = Color.Transparent,
            buttonColor = MaterialTheme.colorScheme.background,
            cardContainerColor = MaterialTheme.colorScheme.primaryContainer,
            onClick = { onShowAd() }
        )
        Space(16)
        PointsCard(
            text = "Daily Redward",
            buttonText = "Claim",
            borderColor = Color.Transparent,
            buttonColor = MaterialTheme.colorScheme.background,
            cardContainerColor = MaterialTheme.colorScheme.primaryContainer,
            onClick = {}
        )
    }
}

@Composable
fun Space(n: Int) {
    Spacer(modifier = Modifier.height(n.dp))
}

@Composable
fun PointsCard(
    text: String,
    buttonText: String,

    borderColor: Color,
    buttonColor: Color,

    cardContainerColor: Color,

    onClick: () -> Unit,
) {
    val cardsPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .border(3.dp, borderColor, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardContainerColor
        )
    ) {
        Column {
            Row(
                modifier = Modifier.padding(cardsPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TokenIcon()
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = text, modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                Button(
                    onClick = { onClick() },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor
                    )
                ) {
                    Text(text = buttonText, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}

