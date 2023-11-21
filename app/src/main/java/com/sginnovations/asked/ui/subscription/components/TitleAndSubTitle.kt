package com.sginnovations.asked.ui.subscription.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

/**
 * Components
 */
@Composable
fun TitleBenefit(painterResource: Painter, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource,
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(38.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text, color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun SubTitleBenefit(text: String) {
    Text(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 44.dp),
        text = text, color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.titleSmall
    )
}