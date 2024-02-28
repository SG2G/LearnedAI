package com.sginnovations.asked.domain.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import org.json.JSONObject
import javax.inject.Inject

private const val RC_DEFAULT_TOKENS = "defaultTokens"
private const val RC_INVITE_REWARD_TOKENS = "inviteRewardTokens"
private const val RC_OPENAI = "openAIAPIKey"
private const val RC_MATHPIX = "mathpixAPIKey"
private const val RC_NEW_CAMERA_CONVERSATION_TOKENS = "newConversationCameraCostTokens"
private const val RC_NEW_ASSISTANT_CONVERSATION_TOKENS = "newConversationAssistantCostTokens"
private const val RC_JSON_PREMIUM_USERS = "jsonPremiumUsers"

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
    fun getInviteRewardTokens() = getValue(RC_INVITE_REWARD_TOKENS)
//    fun getCameraMathTokens() = getValue(RC_CAMERA_MATH_TOKENS)
//    fun getCameraTextTokens() = getValue(RC_CAMERA_TEXT_TOKENS)
    fun getNewCameraConversationCostTokens() = getValue(RC_NEW_CAMERA_CONVERSATION_TOKENS)
    fun getNewAssistConversationCostTokens() = getValue(RC_NEW_ASSISTANT_CONVERSATION_TOKENS)

    //API
    fun getOpenAIAPI() = getValue(RC_OPENAI)
    fun getMathpixAPI() = getValue(RC_MATHPIX)


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

    fun getPremiumUserUids(): List<String> {
        try {
            val jsonString = getValue(RC_JSON_PREMIUM_USERS)
            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray("premiumUsers")
            val uids = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                uids.add(jsonArray.getString(i))
            }

            return uids
        } catch (e: Exception) {
            e.printStackTrace()
            e.message

            return emptyList()
        }
    }
}
