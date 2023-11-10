package com.sginnovations.asked.ui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import kotlinx.coroutines.launch

@Composable
fun UpdateScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.update_critical_updates_required),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(R.string.update_there_are_critical_updates_avaliable_please_update_the_app_to_apply_these_changes),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        Image(
            painter = painterResource(id = R.drawable.update_app),
            contentDescription = "update_app",
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                scope.launch {
                    openAppInPlayStore("com.sginnovations.asked", context)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(
                stringResource(R.string.update_btn_update),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
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