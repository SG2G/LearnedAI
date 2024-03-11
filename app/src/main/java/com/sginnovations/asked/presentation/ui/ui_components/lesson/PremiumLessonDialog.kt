package com.sginnovations.asked.presentation.ui.ui_components.lesson

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.sginnovations.asked.R

@Composable
fun PremiumLessonDialog(
    onDismissRequest: () -> Unit,
    onSeePremiumSubscription: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.lesson_dialog_title),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(text = stringResource(R.string.lesson_dialog_subtitle))
        },
        confirmButton = {
            Button(
                onClick = { onSeePremiumSubscription() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.lesson_dialog_confirm_button), color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismissRequest() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.lesson_dialog_dimiss_button),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )

}