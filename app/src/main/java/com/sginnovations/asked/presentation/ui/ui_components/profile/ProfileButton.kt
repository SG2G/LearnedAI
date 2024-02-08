package com.sginnovations.asked.presentation.ui.ui_components.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ProfileButton(
    text: String,
    painterResource: Painter,

    onClick: () -> Unit,
) {
    val iconModifier = Modifier.size(28.dp).padding(end = 8.dp)
    val iconTint = MaterialTheme.colorScheme.onSurface

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.bodyMedium

    val arrowIconModifier = Modifier.size(24.dp)
    val arrowIconTint = MaterialTheme.colorScheme.onSurfaceVariant

    TextButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .fillMaxWidth().padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource,
                contentDescription = "ProfileButton",
                modifier = iconModifier,
                tint = iconTint,
            )
            Text(
                text = text,
                color = textColor,
                style = textStyle,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "KeyboardArrowRight",
                modifier = arrowIconModifier,
                tint = arrowIconTint,
            )
        }
    }
}

@Composable
fun ProfileButton(
    text: String,
    imageVector: ImageVector,

    onClick: () -> Unit,
) {
    val iconModifier = Modifier.size(28.dp).padding(end = 8.dp)
    val iconTint = MaterialTheme.colorScheme.onSurface

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.bodyMedium

    val arrowIconModifier = Modifier.size(24.dp)
    val arrowIconTint = MaterialTheme.colorScheme.onSurfaceVariant

    TextButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "ProfileButton",
                modifier = iconModifier,
                tint = iconTint,
            )
            Text(
                text = text,
                color = textColor,
                style = textStyle,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "KeyboardArrowRight",
                modifier = arrowIconModifier,
                tint = arrowIconTint,
            )
        }
    }
}