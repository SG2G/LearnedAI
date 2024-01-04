package com.sginnovations.asked.ui.main_bottom_bar.camera.components.top

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.sginnovations.asked.R
import com.sginnovations.asked.utils.LanguageName.languageName

@Composable
fun TranslateSelector(
    translateLanguage: MutableState<String>,

    onChangeTranslateLanguage: (String) -> Unit,
) {
    val context = LocalContext.current

    var selectedLanguage by remember { mutableStateOf(translateLanguage.value) }

    var expanded by remember { mutableStateOf(false) }
    var showCustomLanguageDialog by remember { mutableStateOf(false) }

    val availableLanguages = listOf("en", "es", "fr").filterNot { it == selectedLanguage } + "other"

    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.language_translate_to) + languageName(context,selectedLanguage),
            modifier = Modifier.clickable { expanded = true },
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            availableLanguages.forEach { languageCode ->
                DropdownMenuItem(
                    onClick = {
                        if (languageCode == "other") {
                            showCustomLanguageDialog = true
                        } else {
                            selectedLanguage = languageCode
                            onChangeTranslateLanguage(languageName(context,languageCode))
                        }
                        expanded = false
                    },
                    text = {
                        Text(
                            text = languageName(context,languageCode),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                )
            }
        }
    }

    if (showCustomLanguageDialog) {
        CustomLanguageDialog(
            onLanguageSelected = { customLanguage ->
                selectedLanguage = customLanguage
                onChangeTranslateLanguage(customLanguage)
                showCustomLanguageDialog = false
            },
            onDismiss = { showCustomLanguageDialog = false }
        )
    }
}

@Composable
fun CustomLanguageDialog(
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var textState by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                stringResource(R.string.language_alert_enter_language),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                label = { Text(stringResource(R.string.language_alert_language)) },
                colors = OutlinedTextFieldDefaults.colors(
                    MaterialTheme.colorScheme.onBackground
                )
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onLanguageSelected(textState)
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.language_alert_ok))
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(stringResource(R.string.language_alert_cancel))
            }
        }
    )
}


