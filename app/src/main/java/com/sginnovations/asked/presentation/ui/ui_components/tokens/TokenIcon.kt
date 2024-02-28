package com.sginnovations.asked.presentation.ui.ui_components.tokens

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R

@Composable
fun TokenIcon(
    modifier: Modifier = Modifier.scale(MaterialTheme.typography.titleMedium.fontSize.value/17),
) {
    Icon(
        painter = painterResource(id = R.drawable.token_fill0_wght400_grad0_opsz24),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}