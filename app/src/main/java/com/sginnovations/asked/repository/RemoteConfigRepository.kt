package com.sginnovations.asked.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import javax.inject.Inject

private const val RC_DEFAULT_TOKENS = "defaultTokens"
private const val RC_AD_REWARD_TOKENS = "adRewardTokens"
private const val RC_INVITE_REWARD_TOKENS = "inviteRewardTokens"
private const val RC_OPENAI = "openAIAPIKey"
private const val RC_MATHPIX = "mathpixAPIKey"
private const val RC_CAMERA_MATH_TOKENS = "cameraMathCostTokens"
private const val RC_CAMERA_TEXT_TOKENS = "cameraTextCostTokens"
private const val RC_NEW_CAMERA_CONVERSATION_TOKENS = "newConversationCameraCostTokens"
private const val RC_NEW_ASSISTANT_CONVERSATION_TOKENS = "newConversationAssistantCostTokens"
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
    fun getCameraMathTokens() = getValue(RC_CAMERA_MATH_TOKENS)
    fun getCameraTextTokens() = getValue(RC_CAMERA_TEXT_TOKENS)
    fun getNewCameraConversationCostTokens() = getValue(RC_NEW_CAMERA_CONVERSATION_TOKENS)
    fun getNewAssistConversationCostTokens() = getValue(RC_NEW_ASSISTANT_CONVERSATION_TOKENS)

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
