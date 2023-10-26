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
        title = { Text(text = title, color = MaterialTheme.colorScheme.onBackground) },
        text = { Text(text = text) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = dismissText)
            }
        }
    )
}