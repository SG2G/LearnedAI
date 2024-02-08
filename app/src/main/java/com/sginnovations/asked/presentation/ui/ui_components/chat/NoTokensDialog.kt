package com.sginnovations.asked.presentation.ui.ui_components.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.ui_components.tokens.TokenIcon

@Composable
fun NoTokensDialog(
    onDismissRequest: () -> Unit,
    onSeePremiumSubscription: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = stringResource(R.string.no_tokens_dialog_tittle),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.no_tokens_dialog_subtitle))
                Spacer(modifier = Modifier.width(8.dp))
                TokenIcon()
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
                    text = stringResource(R.string.no_tokens_dialog_cancel), color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
                    text = stringResource(R.string.no_tokens_dialog_view_premium_subscription), color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
    )

}