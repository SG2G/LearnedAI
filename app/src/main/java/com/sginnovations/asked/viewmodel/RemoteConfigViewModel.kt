package com.sginnovations.asked.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.sginnovations.asked.repository.RemoteConfigRepository
import com.sginnovations.asked.utils.CheckMinVersion.checkMinVersion
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val RC_MIN_VERSION = "minVersion"
private const val RC_DEFAULT_TOKENS = "defaultTokens"

private const val TAG = "RemoteConfigViewModel"

@HiltViewModel
class RemoteConfigViewModel @Inject constructor(
    @ApplicationContext private val context: Context,

    private val remoteConfigRepository: RemoteConfigRepository,
) : ViewModel() {

    val needToUpdate = mutableStateOf(false)

    init { setUp() }

    private fun setUp() {
        viewModelScope.launch {
            try {
                remoteConfigRepository.remoteConfigFetchAndActivate().await()
                val minVersion = remoteConfigRepository.getValue(RC_MIN_VERSION)
                needToUpdate.value = checkMinVersion(context, minVersion)
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the exception here, e.g., log it or take appropriate action
                // This catch block will catch any exception that occurs during remoteConfigFetchAndActivate
            }
        }

        remoteConfigRepository.addUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains(RC_MIN_VERSION)) {
                    remoteConfigRepository.remoteConfigFetchAndActivate()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.i(TAG, "onUpdate: minVersion")
                                remoteConfigRepository.remoteConfigActivate()
                                    .addOnCompleteListener {
                                        viewModelScope.launch {
                                            val minVersion =
                                                remoteConfigRepository.getValue(RC_MIN_VERSION)

                                            needToUpdate.value =
                                                checkMinVersion(context, minVersion)
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

    fun getAdRewardTokens(): String {
        return remoteConfigRepository.getAdRewardTokens()
    }
    fun getInviteRewardTokens(): String {
        return remoteConfigRepository.getInviteRewardTokens()
    }
}