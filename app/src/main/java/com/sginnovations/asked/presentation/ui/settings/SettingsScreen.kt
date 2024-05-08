package com.sginnovations.asked.presentation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.sginnovations.asked.Constants.Companion.TEXT_SIZE_BIG
import com.sginnovations.asked.Constants.Companion.TEXT_SIZE_BIGGER
import com.sginnovations.asked.Constants.Companion.TEXT_SIZE_NORMAL
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.utils.ResetStatusBarColor
import com.sginnovations.asked.presentation.viewmodel.PreferencesViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsStateFul(
    vmPreferences: PreferencesViewModel,
) {
    val scope = rememberCoroutineScope()

    val themeValue = vmPreferences.theme.value
    val textSize = vmPreferences.fontSizeIncrease

    ResetStatusBarColor()

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

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        /**
         * Theme
         */
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.settings_theme),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(25.dp)
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
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
        }

        /**
         * Text Size
         */
        Text(
            text = stringResource(R.string.settings_text_size),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(25.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { onChangeTextSize(TEXT_SIZE_NORMAL) },
                    colors = ButtonDefaults.textButtonColors(
                        if (textSize.floatValue == TEXT_SIZE_NORMAL) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    )
                ) {
                    Text(
                        text = stringResource(R.string.standard),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                TextButton(
                    onClick = { onChangeTextSize(TEXT_SIZE_BIG) },
                    colors = ButtonDefaults.textButtonColors(
                        if (textSize.floatValue == TEXT_SIZE_BIG) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    )
                ) {
                    Text(
                        text = stringResource(R.string.large),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                TextButton(
                    onClick = { onChangeTextSize(TEXT_SIZE_BIGGER) },
                    colors = ButtonDefaults.textButtonColors(
                        if (textSize.floatValue == TEXT_SIZE_BIGGER) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    )
                ) {
                    Text(
                        text = stringResource(R.string.huge),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
