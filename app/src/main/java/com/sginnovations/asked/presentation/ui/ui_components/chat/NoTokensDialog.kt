package com.sginnovations.asked.presentation.ui.ui_components.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.no_tokens_dialog_tittle),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                //ResetDailyTokens()
                Text(text = stringResource(R.string.no_tokens_dialog_subtitle))
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
                    text = stringResource(R.string.no_tokens_dialog_cancel),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
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
                    text = stringResource(R.string.no_tokens_dialog_view_premium_subscription),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
    )

}

@Composable
fun ResetDailyTokens() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.if_you_fall_below_3_tokens),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
//    Text(
//        modifier = Modifier.fillMaxWidth(),
//        text = stringResource(R.string.they_reset_to_3_daily),
//        style = MaterialTheme.typography.bodySmall,
//        color = MaterialTheme.colorScheme.onSurfaceVariant,
//        textAlign = TextAlign.Center
//    )
    Spacer(modifier = Modifier.height(16.dp))
}
