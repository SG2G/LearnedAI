package com.sginnovations.asked.presentation.ui.ui_components.subscription

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.subscription.Option

@Composable
fun SubscriptionCard(
    subscriptionDuration: String,
    allPrice: String,
    priceWithDiscount: String?,
    priceAnnualMonthly: String? = null,
    savingsPercentage : Int? = null,
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
    val selectedColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray

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
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(2.dp, color = borderColor)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
            ) {
                /**
                 * Small Text
                 */
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = subscriptionDuration,
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (subscriptionOption.name == Option.OptionAnnually.name) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(5.dp),
                                modifier = Modifier.scale(scale.value)
                            ) {
                                Text(
                                    text = stringResource(R.string.save) + " $savingsPercentage%",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                    /**
                     * Money
                     */
                    if (subscriptionOption.name == Option.OptionAnnually.name) {
//                        Log.d(
//                            "SubscriptionCard",
//                            "priceDiscount-> $priceWithDiscount allPrice -> $allPrice "
//                        )
                        if (priceWithDiscount.equals(allPrice) || priceWithDiscount.isNullOrEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = allPrice + " " + stringResource(R.string.subscription_year),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "($priceAnnualMonthly" + " " + stringResource(id = R.string.subscription_monthly) + ")",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = allPrice,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = priceWithDiscount + " " + stringResource(R.string.subscription_year),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "($priceAnnualMonthly" + " " + stringResource(id = R.string.subscription_monthly) + ")",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }

                        }
                    }
                    if (subscriptionOption.name == Option.OptionMonthly.name) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = allPrice + " " + stringResource(R.string.subscription_month),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        }

        /**
         * Blue Select Tick & Most Popular Badge
         */
        Box(modifier = Modifier.padding(start = 32.dp, top = 4.dp)) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .height(24.dp)
                    .padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(selectedColor),
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
                if (subscriptionOption.name == Option.OptionAnnually.name) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Card(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = selectedColor
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                            text = stringResource(R.string.subscription_most_popular),
                            style = TextStyle(
                                fontSize = 12.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        } // Box 2
    } // Box 1
}