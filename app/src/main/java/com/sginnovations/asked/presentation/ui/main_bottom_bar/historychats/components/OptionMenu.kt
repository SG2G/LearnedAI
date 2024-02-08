package com.sginnovations.asked.presentation.ui.main_bottom_bar.historychats.components

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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.sginnovations.asked.R

@Composable
fun OptionMenu(
    showMenu: MutableState<Boolean>,
    indexMenu: MutableState<Int?>,

    scale: Float,

    conversationId: Int,

    onDeleteConversation: (Int) -> Unit,
    onClick: () -> Unit,
) {
    val menuWidth = 112.dp
    val menuRounded = 8.dp

    Popup(
        alignment = Alignment.TopEnd,
        offset = IntOffset(-48, 108),
        onDismissRequest = {
            showMenu.value = false
            indexMenu.value = null
        },
    ) {
        Box(
            modifier = Modifier
                .scale(scale)
                .width(menuWidth)
                .clip(RoundedCornerShape(menuRounded))
                .blur(15.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(menuRounded)
                )
                .border(1.dp, Color.DarkGray, RoundedCornerShape(menuRounded))
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .width(menuWidth)
                        .clip(
                            RoundedCornerShape(
                                topStart = menuRounded,
                                topEnd = menuRounded
                            )
                        )
                        .clickable {
                            showMenu.value = false
                            indexMenu.value = null
                        }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.delete),
                        color = Color.Transparent,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .weight(1f),
                    )
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        tint = Color.Transparent,
                        modifier = Modifier
                            .size(20.dp),
                    )
                }
//                Divider(modifier = Modifier.width(menuWidth), color = Color.Transparent)
//                Row(
//                    modifier = Modifier
//                        .width(menuWidth)
//                        .clip(
//                            RoundedCornerShape(
//                                bottomStart = menuRounded,
//                                bottomEnd = menuRounded
//                            )
//                        )
//                        .clickable {
//                            showMenu.value = false
//                            indexMenu.value = null
//                            //onClick()
//                        }
//                        .padding(8.dp),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Text(
//                        "Opción 2",
//                        color = Color.Transparent,
//                        style = MaterialTheme.typography.bodyMedium,
//                        modifier = Modifier.weight(1f)
//                    )
//                }
            }
        }
        /**
         * Real
         */
        Box(
            modifier = Modifier.scale(scale)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .width(menuWidth)
                        .clip(
                            RoundedCornerShape(
                                topStart = menuRounded,
                                topEnd = menuRounded
                            )
                        )
                        .clickable {
                            onDeleteConversation(conversationId)
                            showMenu.value = false
                            indexMenu.value = null
                        }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.delete),
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
//                Divider(modifier = Modifier.width(menuWidth), color = Color.DarkGray)
//                Row(
//                    modifier = Modifier
//                        .width(menuWidth)
//                        .clip(
//                            RoundedCornerShape(
//                                bottomStart = menuRounded,
//                                bottomEnd = menuRounded
//                            )
//                        )
//                        .clickable {
//                            showMenu.value = false
//                            indexMenu.value = null
//                            //onClick()
//                        }
//                        .padding(8.dp),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Text(
//                        "Opción 2",
//                        color = MaterialTheme.colorScheme.onBackground,
//                        style = MaterialTheme.typography.bodyMedium,
//                        modifier = Modifier.weight(1f)
//                    )
//                }
            }
        }
    }
}