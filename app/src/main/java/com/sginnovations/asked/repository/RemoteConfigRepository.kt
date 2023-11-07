package com.sginnovations.asked.repository

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.sginnovations.asked.viewmodel.RemoteConfigViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.math.log

private const val RC_DEFAULT_TOKENS = "defaultTokens"
private const val RC_AD_REWARD_TOKENS = "adRewardTokens"
private const val RC_INVITE_REWARD_TOKENS = "inviteRewardTokens"
private const val RC_OPENAI = "openAIAPIKey"
private const val RC_MATHPIX = "mathpixAPIKey"
private const val RC_CAMERA_MATH = "cameraMathCostTokens"
private const val RC_ADS_ALLOWED = "isAdsAllowed"

private const val TAG = "RemoteConfigRepository"

class RemoteConfigRepository @Inject constructor() {

    private val remoteConfig = Firebase.remoteConfig

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    //TOKENS
    fun getDefaultTokens()= getValue(RC_DEFAULT_TOKENS)
    fun getAdRewardTokens() = getValue(RC_AD_REWARD_TOKENS)
    fun getInviteRewardTokens() = getValue(RC_INVITE_REWARD_TOKENS)
    fun getCameraMathTokens() = getValue(RC_CAMERA_MATH)

    //API
    fun getOpenAIAPI() = getValue(RC_OPENAI)
    fun getMathpixAPI() = getValue(RC_MATHPIX)
    // ADS
    fun isAdsAllowed() = getValue(RC_ADS_ALLOWED)


    /**
     * Remote Config SetUp
     */
    fun addUpdateListener(listener: ConfigUpdateListener) {
        remoteConfig.addOnConfigUpdateListener(listener)
    }

    fun remoteConfigActivate(): Task<Boolean> {
        return remoteConfig.activate()
    }

    fun remoteConfigFetchAndActivate(): Task<Boolean> {
        return remoteConfig.fetchAndActivate()
    }

    fun getValue(key: String): String {
        val remoteConfig = remoteConfig.getValue(key).asString()
        Log.d(TAG, "getValue: $remoteConfig")
        return remoteConfig
    }
}
