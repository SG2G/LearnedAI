package com.sginnovations.asked.ui.ui_components.profile

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ConfirmActionDialog(
    title: String,
    text: String,

    confirmText: String,
    dismissText: String,

    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = { Text(text = text) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text(
                    text = confirmText,
                    color = MaterialTheme.colorScheme.onError,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        },
        dismissButton = {
            Button(onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer)) {
                Text(
                    text = dismissText,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    )
}