package com.sginnovations.asked.domain.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.facebook.appevents.AppEventsLogger
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val TAG = "AppsFlyerEventRepo"

private const val EVENT_PHOTO_TAKEN = "photo_taken"
private const val EVENT_PHOTO_CROP = "photo_crop"
private const val EVENT_START_MESSAGE_PHOTO = "start_message_photo"
private const val EVENT_START_MESSAGE_ASSISTANT = "start_message_assistant"
private const val EVENT_CHAT_PHOTO = "chat_photo"
private const val EVENT_CHAT_ASSISTANT = "chat_assistant"
private const val EVENT_ALL_ENGAGEMENT = "all_engagement"

class AppsFlyerRepository @Inject constructor(
    @ApplicationContext val context: Context,
) {
    private val logger = AppEventsLogger.newLogger(context)

    fun allEngagementEvent() {
        Log.d(TAG, "allEngagementEvent")
        logger.logEvent("allEngagementEvent")

        AppsFlyerLib.getInstance().logEvent(
            context,
            EVENT_ALL_ENGAGEMENT,
            null,
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d(TAG, "Event sent successfully. allEngagementEvent")
                }

                override fun onError(errorCode: Int, errorDesc: String) {
                    Log.d(
                        TAG, "Event failed to be sent:\n" +
                                "Error code: " + errorCode + "\n" +
                                "Error description: " + errorDesc
                    )
                }
            }
        )
    }
    fun logPhotoTakenEvent(cameraType: String) {
        Log.d(TAG, "cameraType -> $cameraType")
        logger.logEvent("cameraType, $cameraType")

        val eventValues = HashMap<String, Any>().apply {
            put(AFInAppEventParameterName.CONTENT_TYPE, cameraType)
        }

        AppsFlyerLib.getInstance().logEvent(
            context,
            EVENT_PHOTO_TAKEN,
            eventValues,
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d(TAG, "Event sent successfully. camera - $cameraType")
                }

                override fun onError(errorCode: Int, errorDesc: String) {
                    Log.d(
                        TAG, "Event failed to be sent:\n" +
                                "Error code: " + errorCode + "\n" +
                                "Error description: " + errorDesc
                    )
                }
            }
        )
    }
    fun logPhotoCropEvent(cropType: String) {
        Log.d(TAG, "cropType -> $cropType")
        logger.logEvent("cropType, $cropType")

        val eventValues = HashMap<String, Any>().apply {
            put(AFInAppEventParameterName.CONTENT_TYPE, cropType)
        }

        AppsFlyerLib.getInstance().logEvent(
            context,
            EVENT_PHOTO_CROP,
            eventValues,
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d(TAG, "Event sent successfully. cropType - $cropType")
                }

                override fun onError(errorCode: Int, errorDesc: String) {
                    Log.d(
                        TAG, "Event failed to be sent:\n" +
                                "Error code: " + errorCode + "\n" +
                                "Error description: " + errorDesc
                    )
                }
            }
        )
    }
    fun logStartMessagePhotoEvent() {
        Log.d(TAG, "logStartMessagePhotoEvent")
        logger.logEvent("logStartMessagePhotoEvent")

        AppsFlyerLib.getInstance().logEvent(
            context,
            EVENT_START_MESSAGE_PHOTO,
            null,
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d(TAG, "Event sent successfully. logStartMessagePhotoEvent")
                }

                override fun onError(errorCode: Int, errorDesc: String) {
                    Log.d(
                        TAG, "Event failed to be sent:\n" +
                                "Error code: " + errorCode + "\n" +
                                "Error description: " + errorDesc
                    )
                }
            }
        )
    }
    fun logStartMessageAssistantEvent() {
        Log.d(TAG, "logStartMessageAssistantEvent")
        logger.logEvent("logStartMessageAssistantEvent")

        AppsFlyerLib.getInstance().logEvent(
            context,
            EVENT_START_MESSAGE_ASSISTANT,
            null,
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d(TAG, "Event sent successfully. logStartMessageAssistantEvent")
                }

                override fun onError(errorCode: Int, errorDesc: String) {
                    Log.d(
                        TAG, "Event failed to be sent:\n" +
                                "Error code: " + errorCode + "\n" +
                                "Error description: " + errorDesc
                    )
                }
            }
        )
    }
    fun logChatPhotoEvent() {
        Log.d(TAG, "logChatPhotoEvent")
        logger.logEvent("logChatPhotoEvent")

        AppsFlyerLib.getInstance().logEvent(
            context,
            EVENT_CHAT_PHOTO,
            null,
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d(TAG, "Event sent successfully. logChatPhotoEvent")
                }

                override fun onError(errorCode: Int, errorDesc: String) {
                    Log.d(
                        TAG, "Event failed to be sent:\n" +
                                "Error code: " + errorCode + "\n" +
                                "Error description: " + errorDesc
                    )
                }
            }
        )
    }
    fun logChatAssistantEvent() {
        Log.d(TAG, "logChatAssistantEvent")
        logger.logEvent("logChatAssistantEvent")

        AppsFlyerLib.getInstance().logEvent(
            context,
            EVENT_CHAT_ASSISTANT,
            null,
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d(TAG, "Event sent successfully. logChatAssistantEvent")
                }

                override fun onError(errorCode: Int, errorDesc: String) {
                    Log.d(
                        TAG, "Event failed to be sent:\n" +
                                "Error code: " + errorCode + "\n" +
                                "Error description: " + errorDesc
                    )
                }
            }
        )
    }
    fun logGuideLevelEvent(idLesson: Int) {
        Log.d(TAG, "logGuideLevelEvent")
        logger.logEvent("logGuideLevelEvent")

        val eventValues = HashMap<String, Any>()
        eventValues.put(AFInAppEventParameterName.LEVEL, idLesson)

        AppsFlyerLib.getInstance().logEvent(
            context,
            AFInAppEventType.LEVEL_ACHIEVED,
            eventValues,
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d(TAG, "Event sent successfully. logGuideLevelEvent")
                    Log.d(TAG, "Event Values: $eventValues")
                }

                override fun onError(errorCode: Int, errorDesc: String) {
                    Log.d(
                        TAG, "Launch failed to be sent:\n" +
                                "Error code: " + errorCode + "\n"
                                + "Error description: " + errorDesc
                    )
                }
            }
        )
    }
    fun logSubscriptionEvent(
        currentProductSKU: String?,
        currentProductPrice: Double?,
        applicationContext: Context
    ) {
        Log.d(TAG, "logSubscriptionEvent")
        logger.logEvent("logSubscriptionEvent")

        val eventValues = HashMap<String, Any>()
        eventValues.put(
            AFInAppEventParameterName.CONTENT_ID,
            currentProductSKU ?: ""
        )
        eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, "subscription")
        eventValues.put(
            AFInAppEventParameterName.REVENUE,
            currentProductPrice ?: 44.99
        )
        eventValues.put(AFInAppEventParameterName.CURRENCY, "USD")

        AppsFlyerLib.getInstance().logEvent(
            applicationContext,
            AFInAppEventType.SUBSCRIBE,
            eventValues,
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d(TAG, "Event sent successfully - logSubscriptionEvent")
                }

                override fun onError(errorCode: Int, errorDesc: String) {
                    Log.d(
                        TAG, "Launch failed to be sent:\n" +
                                "Error code: " + errorCode + "\n"
                                + "Error description: " + errorDesc
                    )
                }
            }
        )
    }

}
