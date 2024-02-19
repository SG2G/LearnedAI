package com.sginnovations.asked

import android.app.Application
import android.util.Log
import androidx.annotation.Keep
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import dagger.hilt.android.HiltAndroidApp

private const val LOG_TAG = "AFApplication"
private const val DEV_ID = "K2Hr8qLCZaJfBb4KBLFz55"
@HiltAndroidApp
class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        val afInstance = AppsFlyerLib.getInstance()

        //afInstance.setDebugLog(true) //TODO DELETE

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