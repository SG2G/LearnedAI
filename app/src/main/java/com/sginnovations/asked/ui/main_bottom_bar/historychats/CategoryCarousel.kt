package com.sginnovations.asked.ui.main_bottom_bar.historychats

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.data.All
import com.sginnovations.asked.data.GrammarCategoryOCR
import com.sginnovations.asked.data.MathCategoryOCR
import com.sginnovations.asked.data.SummaryCategoryOCR
import com.sginnovations.asked.data.TextCategoryOCR
import com.sginnovations.asked.data.TranslateCategoryOCR

private const val TAG = "CategoryCarousel"

@Composable
fun CategoryCarousel(
    onChangeCategory: (String) -> Unit,
) {
    val context = LocalContext.current

    val sliderList = listOf(
        All,
        TextCategoryOCR,
        MathCategoryOCR,
        TranslateCategoryOCR,
        SummaryCategoryOCR,
        GrammarCategoryOCR
    )
    var actualOption by remember { mutableStateOf(sliderList[0]) }
    val scale = remember { Animatable(1f) }

    //TODO ANIMATE ALL CLICK
    LazyRow {
        items(sliderList) { item ->
            val isSelected = actualOption == item
            Log.d(TAG, "isSelected -> $isSelected / actualOption -> $actualOption / item -> $item")

            onChangeCategory(actualOption.prefix)

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

            val shape = RoundedCornerShape(15.dp)
            if (isSelected) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .scale(scale.value)
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    shape = shape
                ) {
                    Box(
                        modifier = Modifier
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.getName(context),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            } else {
                OutlinedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    shape = shape
                ) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        actualOption = item
                                        Log.d(TAG, "CategoryCarousel: Click")
                                    }
                                )
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = item.getName(context),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

        }
    }
}