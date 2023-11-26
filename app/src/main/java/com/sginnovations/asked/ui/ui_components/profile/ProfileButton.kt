package com.sginnovations.asked.ui.ui_components.profile

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R

@Composable
fun ProfileButton(
    text: String,
    painterResource: Painter,

    onClick: () -> Unit,
) {

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
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(28.dp)
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "KeyboardArrowRight",
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
                imageVector = imageVector,
                contentDescription = "ProfileButton",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(28.dp)
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "KeyboardArrowRight",
            )
        }
    }
}