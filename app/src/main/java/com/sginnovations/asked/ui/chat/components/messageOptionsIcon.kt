package com.sginnovations.asked.ui.chat.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun messageOptionsIcon(
    onSetClip: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val activeIconColor = MaterialTheme.colorScheme.onBackground
    val noActiveIconColor = androidx.compose.ui.graphics.Color.Gray

    var targetColor by remember { mutableStateOf(noActiveIconColor) }
    val iconColor by animateColorAsState(targetColor, label = "")

    IconButton(
        onClick = { onSetClip() },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.copy_svgrepo_com),
            contentDescription = "Copy text",
            tint = iconColor,
            modifier = Modifier
                .size(20.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            scope.launch {
                                targetColor = activeIconColor
                                delay(200)
                                targetColor = noActiveIconColor
                                onSetClip()
                            }
                        },
                        onPress = {
                            targetColor = activeIconColor
                            tryAwaitRelease()
                            targetColor = noActiveIconColor
                            onSetClip()
                        }
                    )
                }
        )
    } // Copy IconButton
}
