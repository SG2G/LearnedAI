package com.sginnovations.asked.domain.usecase

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sginnovations.asked.R


private const val PERMISSION_REQUEST_CODE = 100

fun checkPermissionAndRequest(
    activity: Activity,
    context: Context,

    permission: String,
    permName: String,

    onPermissionGranted: () -> Unit
) {
    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted()
    } else {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.permission_required, permName))
                .setMessage(context.getString(R.string.this_app_requires_access_to_your, permName))
                .setPositiveButton("Ok") { dialog, which ->
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(permission),
                        PERMISSION_REQUEST_CODE
                    )
                }
                .create()
                .show()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(permission),
                PERMISSION_REQUEST_CODE
            )
        }
    }
}
