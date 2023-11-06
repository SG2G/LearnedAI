package com.sginnovations.asked.utils

import android.content.Context
import android.os.Build
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue

private const val TAG = "RemoteConfigViewModelVersionCheck"

object CheckMinVersion {
    suspend fun checkMinVersion(
        context: Context,
        minVersion: String,
    ): Boolean {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo.longVersionCode
        } else {
            pInfo.versionCode
        }

        Log.i(TAG, "ControllerVersionCheck: actual version: $versionCode, minVersion: $minVersion")
        return versionCode.toString() < minVersion
    }
}