package com.sginnovations.asked.presentation.ui.main

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import com.sginnovations.asked.ChatsHistory
import com.sginnovations.asked.ParentalAssist
import com.sginnovations.asked.ParentalGuidance
import com.sginnovations.asked.R
import com.sginnovations.asked.ScreensDestinations
import com.sginnovations.asked.presentation.ui.utils.StatusBarColorBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.sqrt

private val backGroundColor = Color(0xFF3C5AFA)

@Composable
fun ExpandingCircleScreen(showAnimation: MutableState<Boolean>) {
    val maxRadius = remember { Animatable(0f) }

    if (showAnimation.value) {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp.value
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp.value
        val diagonal = hypot(screenWidth, screenHeight) * 2

        LaunchedEffect(key1 = showAnimation) {
            maxRadius.animateTo(
                targetValue = diagonal,
                animationSpec = tween(durationMillis = 250)
            )
            delay(2000)  // Mantén el círculo expandido por 2 segundos
            showAnimation.value = false
            maxRadius.snapTo(0f)
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                drawCircle(Color.White, radius = maxRadius.value, center = center)
            }
        }
    }
}

//FF3C5AFA
@Composable
fun MainScreenStateFul(
    onClick: (String) -> Unit,
) {
    val showAnimation = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    MainScreenStateLess(
        onClick = {
            scope.launch {
                showAnimation.value = true
                delay(150)
                onClick(it)
            }
        },
    )
    ExpandingCircleScreen(showAnimation)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenStateLess(
    onClick: (String) -> Unit,
) {
    val context = LocalContext.current

    StatusBarColorBlue()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = backGroundColor,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 128.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.mountain_white),
                contentDescription = null
            )
        }
        BoxWithConstraints {
            val numberOfCards = 3

            val spacing = 4.dp
            val totalSpacing = (numberOfCards) * spacing
            val availableWidth = maxWidth - totalSpacing

            val cardWidth = availableWidth / numberOfCards

            Log.d("ResponsiveCardRow", "Total Spacing: ${totalSpacing.value} px")
            Log.d("ResponsiveCardRow", "Available Width: $availableWidth px")
            Log.d("ResponsiveCardRow", "Card Width: ${cardWidth.value} dp")

            val xOffset = cardWidth * 0.2f
            val yOffset = cardWidth * 0.5f

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MainCard(
                    onClick = onClick,
                    context = context,
                    feature = ChatsHistory,
                    cardSize = cardWidth,
//                    cardColor = Color(0xFFE0995E),
                    cardColor = Color.White,
                    modifier = Modifier
//                            .offset(x = (xOffset))  // Adjust this value as needed
                        .zIndex(1f)
                )
                MainCard(
                    onClick = onClick,
                    context = context,
                    feature = ParentalAssist,
                    cardSize = cardWidth,
//                    cardColor = Color(0xFF76C2AF),
                    cardColor = Color.White,
                    modifier = Modifier
//                            .offset(y = (-yOffset))
                        .zIndex(2f) // Highest zIndex for the middle card
                )
                MainCard(
                    onClick = onClick,
                    context = context,
                    feature = ParentalGuidance,
                    cardSize = cardWidth,
//                    cardColor = Color(0xFF77B3D4),
                    cardColor = Color.White,
                    modifier = Modifier
//                            .offset(x = (-xOffset))  // Adjust this value as needed
                        .zIndex(1f)
                )
            }
        }
    }
}

@Composable
private fun MainCard(
    onClick: (String) -> Unit,
    context: Context,

    feature: ScreensDestinations,
    cardSize: Dp,
    cardColor: Color,
    modifier: Modifier = Modifier,
) {

    Card(
        shape = CircleShape,
        colors = CardDefaults.cardColors(cardColor),
        modifier = modifier
            .size(cardSize)  // Aplica el tamaño calculado
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onClick(feature.route) })
            }
            .border(1.dp, Color.DarkGray, CircleShape)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            feature.icon?.let { painterResource(id = it) }?.let {
                Icon(
                    painter = it,
                    contentDescription = null,
                    modifier = Modifier.size(cardSize * 0.4f),
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.height(cardSize * 0.05f))
            Text(
                text = feature.getBottomName(context),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}