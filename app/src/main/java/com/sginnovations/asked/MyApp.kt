package com.sginnovations.asked

import android.app.Application
import android.util.Log
import androidx.annotation.Keep
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.appsflyer.deeplink.DeepLink
import com.appsflyer.deeplink.DeepLinkListener
import com.appsflyer.deeplink.DeepLinkResult
import com.sginnovations.asked.Constants.Companion.DEV_ID
import dagger.hilt.android.HiltAndroidApp

private const val LOG_TAG = "AFApplication"
@HiltAndroidApp
class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        val afInstance = AppsFlyerLib.getInstance()

        //afInstance.setDebugLog(true) //TODO DELETE

        AppsFlyerLib.getInstance().subscribeForDeepLink(object : DeepLinkListener {
            override fun onDeepLinking(deepLinkResult: DeepLinkResult) {
                when (deepLinkResult.status) {
                    DeepLinkResult.Status.FOUND -> {
                        Log.d(
                            LOG_TAG,"Deep link found"
                        )
                    }
                    DeepLinkResult.Status.NOT_FOUND -> {
                        Log.d(
                            LOG_TAG,"Deep link not found"
                        )
                        return
                    }
                    else -> {
                        // dlStatus == DeepLinkResult.Status.ERROR
                        val dlError = deepLinkResult.error
                        Log.d(
                            LOG_TAG,"There was an error getting Deep Link data: $dlError"
                        )
                        return
                    }
                }
                var deepLinkObj: DeepLink = deepLinkResult.deepLink
                try {
                    Log.d(
                        LOG_TAG,"The DeepLink data is: $deepLinkObj"
                    )
                } catch (e: Exception) {
                    Log.d(
                        LOG_TAG,"DeepLink data came back null"
                    )
                    return
                }

                // An example for using is_deferred
                if (deepLinkObj.isDeferred == true) {
                    Log.d(LOG_TAG, "This is a deferred deep link");
                } else {
                    Log.d(LOG_TAG, "This is a direct deep link");
                }

                try {
                    val fruitName = deepLinkObj.deepLinkValue
                    Log.d(LOG_TAG, "The DeepLink will route to: $fruitName")
                } catch (e:Exception) {
                    Log.d(LOG_TAG, "There's been an error: $e");
                    return;
                }
            }
        })

        afInstance.init(DEV_ID, null, this)

        afInstance.start(this, DEV_ID, object :
            AppsFlyerRequestListener {
            override fun onSuccess() {
                Log.d(LOG_TAG, "Launch sent successfully")
            }

            override fun onError(errorCode: Int, errorDesc: String) {
                Log.d(
                    LOG_TAG, "Launch failed to be sent:\n" +
                            "Error code: " + errorCode + "\n"
                            + "Error description: " + errorDesc
                )
            }
        })

    }
}