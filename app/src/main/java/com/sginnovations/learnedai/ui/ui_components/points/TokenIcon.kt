package com.sginnovations.learnedai.ui.ui_components.points

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.sginnovations.learnedai.R

@Composable
fun TokenIcon() {
    Icon(
        painter = painterResource(id = R.drawable.token_fill0_wght400_grad0_opsz24),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary
    )
}