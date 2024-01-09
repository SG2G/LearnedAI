package com.sginnovations.asked.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.Constants.Companion.TEXT_SIZE_BIG
import com.sginnovations.asked.Constants.Companion.TEXT_SIZE_BIGGER
import com.sginnovations.asked.Constants.Companion.TEXT_SIZE_NORMAL
import com.sginnovations.asked.R
import com.sginnovations.asked.viewmodel.PreferencesViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsStateFul(
    vmPreferences: PreferencesViewModel,
) {
    val scope = rememberCoroutineScope()

    val themeValue = vmPreferences.theme.value
    val textSize = vmPreferences.fontSizeIncrease

    SettingsStateLess(
        themeValue = themeValue,
        textSize = textSize,

        onSwitchTheme = {
            scope.launch {
                vmPreferences.theme.value = !vmPreferences.theme.value
                vmPreferences.setTheme(vmPreferences.theme.value)
            }
        },
    ) { increase ->
        scope.launch {
            vmPreferences.setFontSizeIncrease(increase)
        }
    }
}

@Composable
fun SettingsStateLess(
    themeValue: Boolean,
    textSize: MutableFloatState,

    onSwitchTheme: () -> Unit,
    onChangeTextSize: (Float) -> Unit,
) {

    Column {
        Text(
            text = stringResource(R.string.settings_theme), color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Card(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.settings_dark_mode),
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = themeValue,
                    onCheckedChange = { onSwitchTheme() },
                    thumbContent = {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            painter = painterResource(
                                id = if (!themeValue)
                                    R.drawable.sun_2_svgrepo_filled else R.drawable.moon_svgrepo_filled
                            ),
                            contentDescription = null,
                            tint = if (!themeValue) Color.Yellow else Color.White,
                        )
                    }
                )
            }
        }

        Text(
            text = stringResource(R.string.settings_text_size),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Card(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row {
                TextButton(
                    onClick = { onChangeTextSize(TEXT_SIZE_NORMAL) },
                    colors = ButtonDefaults.textButtonColors(
                        if (textSize.floatValue == TEXT_SIZE_NORMAL) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                    )
                ) {
                    Text(
                        text = "aA", color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 16.sp)
                    )
                }

                TextButton(
                    onClick = { onChangeTextSize(TEXT_SIZE_BIG) },
                    colors = ButtonDefaults.textButtonColors(
                        if (textSize.floatValue == TEXT_SIZE_BIG) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                    )
                ) {
                    Text(
                        text = "aA", color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp)
                    )
                }

                TextButton(
                    onClick = { onChangeTextSize(TEXT_SIZE_BIGGER) },
                    colors = ButtonDefaults.textButtonColors(
                        if (textSize.floatValue == TEXT_SIZE_BIGGER) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                    )
                ) {
                    Text(
                        text = "aA", color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp)
                    )
                }
            }
        }
    }
}
