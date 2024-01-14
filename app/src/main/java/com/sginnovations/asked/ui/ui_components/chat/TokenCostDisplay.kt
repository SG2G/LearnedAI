package com.sginnovations.asked.ui.ui_components.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.ui_components.tokens.TokenIcon

@Composable
fun TokenCostDisplay(
    tokenCost: String,
) {
    Row(
        modifier = Modifier
            .scale(0.8f)
            .padding(start = 16.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = tokenCost)
        TokenIcon()
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = stringResource(R.string.message))
    }
}