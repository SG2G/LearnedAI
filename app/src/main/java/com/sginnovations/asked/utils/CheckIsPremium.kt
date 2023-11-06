package com.sginnovations.asked.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.Constants
import kotlinx.coroutines.tasks.await

private const val TAG = "CheckIsPremium"

object CheckIsPremium {

    private var lastCheckTime: Long = 0
    private var isPremiumCache: Boolean = false

    suspend fun checkIsPremium(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastCheckTime < 60 * 60 * 1000) {
            // If less than an hour has passed since the last check, returns the stored value
            Log.d(TAG, "checkIsPremium: isPremiumCache")
            return isPremiumCache
        }

        val firestore = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val docRef = user?.uid?.let { firestore.collection(Constants.USERS_NAME).document(it) }

        return try {
            Log.d(TAG, "checkIsPremium: getting value for firebase")
            val document = docRef?.get()?.await()
            val isPremium = document?.getBoolean(Constants.IS_PREMIUM) ?: false
            // Stores the value and the time it was obtained
            isPremiumCache = isPremium
            lastCheckTime = currentTime
            isPremium
        } catch (e: Exception) {
            Log.d(TAG, "checkIsPremium: error")
            e.printStackTrace()
            false
        }
    }
}