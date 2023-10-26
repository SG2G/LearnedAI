package com.sginnovations.asked

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.sginnovations.asked.ui.utils.UpdateApp
import com.sginnovations.asked.utils.CheckMinVersion.needToUpdate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "LearnedApp"

@Composable
fun LearnedApp(
    activity: Activity,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val needToUpdate = remember { mutableStateOf(false) }

    /**
     * Version controller - Remote config
     */
    val remoteConfig = Firebase.remoteConfig
    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 60
    }
    remoteConfig.setConfigSettingsAsync(configSettings)

    // Fetch
    remoteConfig.fetchAndActivate()
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                val updated = task.result

                scope.launch {
                    val minVersion = remoteConfig.getValue("minVersion")
                    delay(5000)

                    needToUpdate.value = needToUpdate(context, minVersion)
                }
            }
        }

    // Listener
    remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
        override fun onUpdate(configUpdate: ConfigUpdate) {

            if (configUpdate.updatedKeys.contains("minVersion")) {
                remoteConfig.activate().addOnCompleteListener {

                    scope.launch {
                        val minVersion = remoteConfig.getValue("minVersion")
                        delay(5000)

                        needToUpdate.value = needToUpdate(context, minVersion)
                    }
                }
            }
        }

        override fun onError(error: FirebaseRemoteConfigException) {
            Log.w(TAG, "Config update error with code: " + error.code, error)
        }
    })

    if (needToUpdate.value) {
        UpdateApp()
    } else {
        LearnedNavigation()
    }
}

