package com.sginnovations.asked.ui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun UpdateApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Critical Updates Required",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "There are critical updates avaliable. Please update the app to apply these changes.",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = {
            scope.launch {
                openAppInPlayStore("com.sginnovations.asked", context)
            }
        }) {
            Text("Update")
        }
    }

}

fun openAppInPlayStore(appId: String, context: Context) {
    val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appId"))
    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://play.google.com/store/apps/details?id=$appId")
    )

    try {
        context.startActivity(playStoreIntent)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(browserIntent)
    }
}