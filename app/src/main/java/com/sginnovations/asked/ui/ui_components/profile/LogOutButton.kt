package com.sginnovations.asked.ui.ui_components.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R

@Composable
fun LogOutButton(
    onClick: () -> Unit,
) {
    var showConfirmation by remember { mutableStateOf(false) }

    val iconModifier = Modifier.size(28.dp).padding(end = 8.dp)
    val iconTint = MaterialTheme.colorScheme.error

    val textColor = MaterialTheme.colorScheme.error
    val textStyle = MaterialTheme.typography.bodyMedium

    val arrowIconModifier = Modifier.size(24.dp)
    val arrowIconTint = MaterialTheme.colorScheme.onSurfaceVariant

    if (showConfirmation) {
        ConfirmActionDialog(
            title = stringResource(R.string.log_out_button_confirm_action),
            text = stringResource(R.string.log_out_button_are_you_sure_you_want_to_log_out),

            confirmText = stringResource(R.string.log_out_button_log_out),
            dismissText = stringResource(R.string.log_out_button_cancel),

            onConfirm = { onClick() },
            onDismiss = { showConfirmation = false }
        )
    }

    ElevatedCard(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {
        TextButton(
            onClick = {
                showConfirmation = !showConfirmation
            },
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
                    painter = painterResource(id = R.drawable.logout_fill0_wght400_grad0_opsz48),
                    contentDescription = "ExitToApp",
                    modifier = iconModifier,
                    tint = iconTint,
                )
                Text(
                    text = stringResource(R.string.log_out_button_log_out),
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
}