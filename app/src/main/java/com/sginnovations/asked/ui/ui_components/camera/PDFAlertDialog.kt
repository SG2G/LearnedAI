package com.sginnovations.asked.ui.ui_components.camera

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PDFAlertDialog(onDismiss: () -> Unit
) { // TODO DELETE
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Work in Progress", color = MaterialTheme.colorScheme.onBackground) },
        text = { Text(text = "PDF reader feature for premium users.") },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(text = "Cool!", color = MaterialTheme.colorScheme.onBackground)
            }
        },
    )
}