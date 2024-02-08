package com.sginnovations.asked.presentation.ui.ui_components.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
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
import androidx.compose.ui.unit.dp

@Composable
fun DeleteAccountButton(
    onClick: () -> Unit,
) {
    var showConfirmation by remember { mutableStateOf(false) }

    if (showConfirmation) {
        ConfirmActionDialog(
            title = "Confirm Action",
            text = "Are you sure you want to Delete your account?",

            confirmText = "DELETE",
            dismissText = "Cancel",

            onConfirm = { onClick() },
            onDismiss = { showConfirmation = false }
        )
    }

    TextButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.DeleteForever,
                contentDescription = "DeleteForever",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(32.dp)
            )
            Text(
                text = "Delete Account",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "KeyboardArrowRight",
            )
        }
    }
}