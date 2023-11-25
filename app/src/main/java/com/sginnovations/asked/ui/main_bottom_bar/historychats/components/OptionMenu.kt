package com.sginnovations.asked.ui.main_bottom_bar.historychats.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

@Composable
fun OptionMenu(
    showMenu: MutableState<Boolean>,

    index: Int,
    indexMenu: MutableState<Int?>,

    onDelete: () -> Unit,
    onClick: () -> Unit,
) {

    AnimatedVisibility(
        visible = showMenu.value,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandIn(
            animationSpec = tween(
                durationMillis = 1000
            )
        ),
        exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + shrinkOut(
            animationSpec = tween(
                durationMillis = 1000
            )
        )
    ) {
        Popup(
            alignment = Alignment.TopEnd,
            offset = IntOffset(0, 56),
            onDismissRequest = {
                showMenu.value = false
                indexMenu.value = null
            },
        ) {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                            .copy(alpha = 0.95f),
                        RoundedCornerShape(10.dp)
                    )
                    .border(
                        1.dp,
                        Color.DarkGray,
                        RoundedCornerShape(10.dp)
                    )
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .width(128.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 10.dp,
                                    topEnd = 10.dp
                                )
                            )
                            .clickable {
                                showMenu.value = false
                                indexMenu.value = null
                                onDelete()
                            }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Delete",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .weight(1f),
                        )
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .size(20.dp),
                        )
                    }
                    Divider(modifier = Modifier.width(128.dp))
                    Row(
                        modifier = Modifier
                            .width(128.dp)
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 10.dp,
                                    bottomEnd = 10.dp
                                )
                            )
                            .clickable {
                                showMenu.value = false
                                indexMenu.value = null
                                onClick()
                            }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "Opción 2",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}