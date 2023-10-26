package com.sginnovations.asked.ui.ui_components.points

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R

@Composable
fun TokenIcon() {
    Icon(
        painter = painterResource(id = R.drawable.token_fill0_wght400_grad0_opsz24),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
    )
}