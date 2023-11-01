package com.sginnovations.asked.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteConfigRepository @Inject constructor() {
    private val remoteConfig = Firebase.remoteConfig

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    fun addUpdateListener(listener: ConfigUpdateListener) {
        remoteConfig.addOnConfigUpdateListener(listener)
    }
    fun remoteConfigActivate(): Task<Boolean> {
        return remoteConfig.activate()
    }

    fun fetchAndActivate(): Task<Boolean> {
        return remoteConfig.fetchAndActivate()
    }

    fun getValue(key: String): String {
        return remoteConfig.getValue(key).asString()
    }
}
