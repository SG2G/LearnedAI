package com.sginnovations.asked.ui.main_bottom_bar.camera.components.other

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sginnovations.asked.R

@Composable
fun PDFAlertDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.pdf_title_work_in_progress), color = MaterialTheme.colorScheme.onBackground) },
        text = { Text(text = stringResource(R.string.pdf_subtitle_pdf_reader_feature_for_premium_users)) },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(text = stringResource(R.string.pdf_button_cool), color = MaterialTheme.colorScheme.onBackground)
            }
        },
    )
}