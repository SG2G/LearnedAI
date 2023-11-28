package com.sginnovations.asked.ui.main_bottom_bar.historychats

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.data.All
import com.sginnovations.asked.data.MathCategoryOCR
import com.sginnovations.asked.data.TextCategoryOCR

private const val TAG = "CategoryCarousel"

@Composable
fun CategoryCarousel(
    onChangeCategory: (String) -> Unit,
) {
    val context = LocalContext.current

    val sliderList = listOf(All, TextCategoryOCR, MathCategoryOCR)
    var actualOption by remember { mutableStateOf(sliderList[0]) }
    val scale = remember { Animatable(1f) }

    val cardsWidth = 72.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(15.dp)
    ) {
        LazyRow {
            items(sliderList) { item ->
                val isSelected = actualOption == item
                Log.d(
                    TAG,
                    "isSelected -> $isSelected / actualOption -> $actualOption / item -> $item"
                )

                onChangeCategory(actualOption.root)

                LaunchedEffect(isSelected) {
                    scale.animateTo(
                        targetValue = if (isSelected) 0.9f else 1f,
                        animationSpec = tween(100, easing = LinearEasing)
                    )
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                }

                if (isSelected) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier
                            .scale(scale.value)
                            .padding(8.dp)
                            .width(cardsWidth),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(cardsWidth),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.getName(context),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                } else {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .width(cardsWidth),
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = {
                                            actualOption = item
                                            Log.d(TAG, "CategoryCarousel: Click")
                                        }
                                    )
                                }
                                .width(cardsWidth),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = item.getName(context),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

            }
        }
    }
}