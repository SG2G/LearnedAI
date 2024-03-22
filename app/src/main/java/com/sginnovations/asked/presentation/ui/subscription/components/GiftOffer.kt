package com.sginnovations.asked.presentation.ui.subscription.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.R
import com.sginnovations.asked.utils.CheckIsPremium
import kotlinx.coroutines.async

@Composable
fun GiftOffer(
    modifier: Modifier = Modifier,

    onNavigateFirstOffer: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val isPremium = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) { isPremium.value = scope.async { CheckIsPremium.checkIsPremium() }.await() }

    if (!isPremium.value) {
        val scale: MutableState<Float> = remember { mutableFloatStateOf(1f) }

        Box(
            modifier = modifier
                .padding(end = 16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onNavigateFirstOffer() },
                        onPress = {
                            scale.value = 0.9f
                            tryAwaitRelease()
                            scale.value = 1f
                        },
                    )
                }
                .scale(animateFloatAsState(scale.value, label = "").value)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    //.border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.TopEnd) {
                    Icon(
                        painter = painterResource(id = R.drawable.gift_svgrepo_com__1_),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .offset(x = (2.dp), y = (0.dp))
                            .background(color = Color.Red, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("1", color = Color.White, fontSize = 10.sp)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.gift_for_you),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}