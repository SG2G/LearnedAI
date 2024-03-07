package com.sginnovations.asked.presentation.ui.crop.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.ui_components.chat.IconAssistantMsg
import com.sginnovations.asked.presentation.ui.ui_components.chat.IconMsg

@Composable
fun IsLoadingCrop() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_crop))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Log.d("IsLoadingCrop", "CircularProgressIndicator")
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .zIndex(10f),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
//                ),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
////            CircularProgressIndicator()
////            RotatingText()
//        }
//    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(10f),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(224.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}