package com.sginnovations.asked.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkUtils {
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        capabilities?.let {
            return it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
        return false
    }
}
