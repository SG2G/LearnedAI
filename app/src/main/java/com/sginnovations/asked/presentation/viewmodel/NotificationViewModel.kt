package com.sginnovations.asked.presentation.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.NotificationReceiver
import com.sginnovations.asked.R
import com.sginnovations.asked.data.AskedNotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val application: Application
) : AndroidViewModel(application) {

    private val askedNotificationService by lazy { AskedNotificationService(context) }

//    fun scheduleWaterReminder() {
//        // 5 days in milliseconds
////        val fiveDaysInMillis = 5 * 24 * 60 * 60 * 1000L
//        val fiveMinutesInMillis = 1 * 60 * 1000L // 5 minutos * 60 segundos * 1000 milisegundos
//        askedNotificationService.scheduleNotification(fiveMinutesInMillis)
//    }

    fun showBasicNotification() {
        askedNotificationService.showBasicNotification()
    }
}