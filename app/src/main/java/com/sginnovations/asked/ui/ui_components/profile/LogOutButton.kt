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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R

@Composable
fun LogOutButton(
    onClick: () -> Unit,
) {
    var showConfirmation by remember { mutableStateOf(false) }

    if (showConfirmation) {
        ConfirmActionDialog(
            title = "Confirm Action",
            text = "Are you sure you want to Log Out?",

            confirmText = "Log out",
            dismissText = "Cancel",

            onConfirm = { onClick() },
            onDismiss = { showConfirmation = false }
        )
    }

    TextButton(
        onClick = {
            showConfirmation = !showConfirmation
        },
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
                painter = painterResource(id = R.drawable.logout_fill0_wght400_grad0_opsz48),
                contentDescription = "ExitToApp",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(32.dp)
            )
            Text(
                text = "Log out",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleSmall
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "KeyboardArrowRight",
            )
        }
    }
}