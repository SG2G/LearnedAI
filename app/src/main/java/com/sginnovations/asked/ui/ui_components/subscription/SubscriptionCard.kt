package com.sginnovations.asked.ui.ui_components.subscription

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.subscription.Option

@Composable
fun SubscriptionCard(
    durationTime: String,
    smallText: String,
    allPrice: String,
    subscriptionOption: Option,
    userOption: Option,

    onUserSelected: () -> Unit,
) {
    val isSelected = userOption.name == subscriptionOption.name
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
        label = "",
        animationSpec = tween(250)
    )
    val circleColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray

    val scale = remember { Animatable(1f) }

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

    Box(modifier = Modifier.padding(8.dp)) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, top = 16.dp, end = 14.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onUserSelected()
                        }
                    )
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            border = BorderStroke(2.dp, color = borderColor)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = smallText,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = TextStyle(
                            fontSize = 12.sp
                        ),
                    )
                    Text(
                        text = allPrice + durationTime,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                }
                if (subscriptionOption.name == Option.OptionMonthly.name) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.scale(scale.value)
                        ) {
                            Text(
                                text = stringResource(R.string.subscription_save_50),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        /**
         * Blue Select Tick
         */
        Box(modifier = Modifier.padding(start = 32.dp, top = 4.dp)) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .height(24.dp)
                    .padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(circleColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(3.dp)
                        )
                    }
                }
//                Spacer(modifier = Modifier.width(8.dp))
//                ElevatedCard(
//                    elevation = CardDefaults.elevatedCardElevation(
//                        defaultElevation = 4.dp
//                    ),
//                    colors = CardDefaults.elevatedCardColors(
//                        containerColor = MaterialTheme.colorScheme.surface
//                    )
//                ) {
//                    Text(
//                        modifier = Modifier.padding(4.dp),
//                        text = "Save 50%",
//                        style = TextStyle(
//                            fontSize = 12.sp
//                        ),
//                        color = MaterialTheme.colorScheme.onPrimaryContainer
//                    )
//                }
            }
        } // Box 2
    } // Box 1
}