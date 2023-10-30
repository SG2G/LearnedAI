@file:OptIn(ExperimentalPermissionsApi::class)

package com.sginnovations.asked.ui.main_bottom_bar.camera

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermissions(
    permsAsked: String,
    
    onPermissionGranted: () -> Unit,
) {
    val permissionState = rememberPermissionState(permission = permsAsked)

    val context = LocalContext.current

    LaunchedEffect(permissionState) {
        permissionState.launchPermissionRequest()
    }
    when {
        permissionState.status.isGranted -> {
            // Permission is granted. Show the camera preview.
            onPermissionGranted()
        }
        permissionState.status.shouldShowRationale -> {
            // Permission is not granted, but the user hasn't denied it permanently.
            // Show a rationale message and request the permission again.
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Camera Permission Required",color = MaterialTheme.colorScheme.onBackground) },
                text = { Text("This app requires access to your camera to take photos.",color = MaterialTheme.colorScheme.onSurfaceVariant) },
                confirmButton = {
                    Button(
                        onClick = {
                            permissionState.launchPermissionRequest()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
        else -> {
            // Permission is denied permanently. Show a message and navigate to app settings.
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Camera Permission Denied",color = MaterialTheme.colorScheme.onBackground) },
                text = {
                    Text(
                        "This app requires access to your camera to take photos. Please enable the camera permission in your device settings.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            openAppSettings(context)
                        }
                    ) {
                        Text("Open Settings")
                    }
                }
            )
        }
    }
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri: Uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    context.startActivity(intent)
}