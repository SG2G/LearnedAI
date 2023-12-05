package com.sginnovations.asked.ui.chat.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cabin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sginnovations.asked.R

@Composable
fun ConfidenceDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icons.Filled.Cabin },
        title = {
            Text(
                text = stringResource(R.string.confidence_dialog_title), color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                Divider()
                Text(
                    text = stringResource(R.string.confidence_dialog_text),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.confidence_dialog_tip1),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.confidence_dialog_tip2),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = stringResource(R.string.pdf_button_cool),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        },
    )
}