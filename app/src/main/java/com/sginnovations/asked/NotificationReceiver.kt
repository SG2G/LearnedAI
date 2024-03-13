package com.sginnovations.asked

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sginnovations.asked.data.AskedNotificationService

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Here the service or action to show the notification is started.
        // Assuming you have an instance of your notification service.
        // Note: If you are using Hilt for dependency injection, you might need to use HiltWorkerFactory
        // or alternative methods to obtain the instance of your service if it requires injection.
        val notificationService = AskedNotificationService(context)
        notificationService.showBasicNotification()
    }
}