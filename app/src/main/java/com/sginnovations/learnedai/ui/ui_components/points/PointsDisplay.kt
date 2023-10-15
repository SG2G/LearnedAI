package com.sginnovations.learnedai.ui.ui_components.points

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun PointsDisplay(
    tokens: State<Long>,

    showPlus: Boolean,

    onNavigatePoints: () -> Unit,
) {
    val scale: MutableState<Float> = remember { mutableStateOf(1f) }


    Box(
        modifier = Modifier
            .padding(end = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onNavigatePoints()
                    },
                    onPress = {
                        scale.value = 0.9f
                        tryAwaitRelease()
                        scale.value = 1f
                    },
                )
            }
            .scale(animateFloatAsState(scale.value, label = "").value)
    ) {
        Row(
            modifier = Modifier
                .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showPlus) {
                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .padding(end = 8.dp)
                        .background(Color.LightGray, RoundedCornerShape(5.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Icon",
                        tint = Color.DarkGray
                    )
                }
            }

            Text(
                text = tokens.value.toString(), modifier = Modifier
            )
            Spacer(modifier = Modifier.width(2.dp))
            TokenIcon()
        }
    }
}