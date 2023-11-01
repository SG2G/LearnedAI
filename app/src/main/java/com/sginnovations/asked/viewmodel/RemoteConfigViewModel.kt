package com.sginnovations.asked.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.sginnovations.asked.repository.RemoteConfigRepository
import com.sginnovations.asked.utils.CheckMinVersion.needToUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "RemoteConfigViewModel"

@HiltViewModel
class RemoteConfigViewModel @Inject constructor(
    @ApplicationContext private val context: Context,

    private val remoteConfigRepository: RemoteConfigRepository,
) : ViewModel() {

    val needToUpdate = mutableStateOf(false)

    init {
        viewModelScope.launch {
            val updated = remoteConfigRepository.fetchAndActivate().await()
            if (updated) {
                val minVersion = remoteConfigRepository.getValue("minVersion")
                needToUpdate.value = needToUpdate(context, minVersion)
            }
        }

        remoteConfigRepository.addUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains("minVersion")) {
                    remoteConfigRepository.fetchAndActivate().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i(TAG, "onUpdate: minVersion")
                            remoteConfigRepository.remoteConfigActivate().addOnCompleteListener {
                                val minVersion = remoteConfigRepository.getValue("minVersion")
                                viewModelScope.launch {
                                    needToUpdate.value = needToUpdate(context, minVersion)
                                }
                            }
                        }
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.w(TAG, "Config update error with code: " + error.code, error)
            }
        })
    }

}
