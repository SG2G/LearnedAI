package com.sginnovations.asked.presentation.ui.chat.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun LaunchedEffectSnackBarAnimation(
    snackbarHostState: SnackbarHostState,
    snackbarOffset: Animatable<Float, AnimationVector1D>
) {
    LaunchedEffect(snackbarHostState.currentSnackbarData) {
        if (snackbarHostState.currentSnackbarData != null) {
            // Animate snackbar in
            snackbarOffset.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
        } else {
            // Animate snackbar out
            snackbarOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        }
    }
}