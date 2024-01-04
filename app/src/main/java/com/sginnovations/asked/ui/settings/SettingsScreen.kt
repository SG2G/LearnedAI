package com.sginnovations.asked.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.R
import com.sginnovations.asked.viewmodel.PreferencesViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsStateFul(
    vmPreferences: PreferencesViewModel,
) {
    val scope = rememberCoroutineScope()

    Column {
        Text(
            text = "Theme", color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        Card(
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Dark Mode"
                )
                Switch(
                    checked = vmPreferences.theme.value,
                    onCheckedChange = {
                        scope.launch {
                            vmPreferences.theme.value = !vmPreferences.theme.value
                            vmPreferences.setTheme(vmPreferences.theme.value)
                        }
                    },
                    thumbContent = {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            painter = painterResource(id = if (!vmPreferences.theme.value)
                                R.drawable.sun_2_svgrepo_filled else R.drawable.moon_svgrepo_filled),
                            contentDescription = null,
                            tint = if (!vmPreferences.theme.value) Color.Yellow else Color.DarkGray,
                        )
                    }
                )
            }
        }

        Text(
            text = "Text Size", color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = "aA", color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 16.sp)
                    )
                }
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = "aA", color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp)
                    )
                }
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = "aA", color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp)
                    )
                }
            }
        }
    }

}