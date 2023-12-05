package com.sginnovations.asked.ui.ui_components.tokens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.R
import com.sginnovations.asked.utils.CheckIsPremium.checkIsPremium
import kotlinx.coroutines.async

@Composable
fun TokenDisplay(
    modifier: Modifier = Modifier,

    tokens: State<Long>,

    showPlus: Boolean,

    onNavigatePoints: () -> Unit,
) {
    val scale: MutableState<Float> = remember { mutableFloatStateOf(1f) }
    val scope = rememberCoroutineScope()
    var isPremium by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isPremium = scope.async { checkIsPremium() }.await()
    }

    Box(
        modifier = modifier
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
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        Row(
            modifier = Modifier
                //.border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showPlus) {
//                Box(
//                    modifier = Modifier
//                        .padding(end = 4.dp)
//                        .background(Color.LightGray, RoundedCornerShape(5.dp))
//                        .size(16.dp),
//                    contentAlignment = Alignment.Center,
//                ) {
                    Icon(
                        modifier = Modifier.size(24.dp).padding(end = 6.dp),
                        painter = painterResource(id = R.drawable.add_square_svgrepo_com),
                        contentDescription = "Add Icon",
                    )
            }

            Text(
                text = if (isPremium) stringResource(R.string.infinite) else tokens.value.toString(),
                modifier = Modifier
                    .padding(bottom = if (isPremium) 4.dp else 0.dp),
                color = MaterialTheme.colorScheme.primary,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
            )
            Spacer(modifier = Modifier.width(2.dp))
            TokenIcon()
        }
    }
}