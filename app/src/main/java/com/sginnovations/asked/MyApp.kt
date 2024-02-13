package com.sginnovations.asked

import android.app.Application
import androidx.annotation.Keep
import dagger.hilt.android.HiltAndroidApp

@Keep
@HiltAndroidApp
class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}