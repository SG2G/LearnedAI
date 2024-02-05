package com.sginnovations.asked.ui.ui_components.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.utils.CheckIsPremium.checkIsPremium
import kotlinx.coroutines.async

@Composable
fun ProfileName(userName: String = "User Name") {
    val scope = rememberCoroutineScope()
    var isPremium by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isPremium = scope.async { checkIsPremium() }.await()
    }


    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        Text(
            text = userName.capitalizeFirstLetter(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        if (!isPremium) {
            Text(
                text = stringResource(R.string.profile_base_plan),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        } else {
            Text(
                text = stringResource(R.string.profile_2_plan),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(R.string.profile_1_premium),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

fun String.capitalizeFirstLetter(): String {
    if (this.isEmpty()) return ""
    return this[0].uppercase() + this.substring(1)
}