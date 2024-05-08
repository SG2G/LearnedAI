package com.sginnovations.asked.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.google.android.play.core.review.ReviewManagerFactory

private const val TAG = "showFeedBackDialog"
object FeedBack {

    fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
    fun showFeedBackDialog(activity: Activity) {
        val reviewManager = ReviewManagerFactory.create(activity)
        Log.d(TAG, "Entering")
        reviewManager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d(TAG, "isSuccessful")
                reviewManager.launchReviewFlow(activity, it.result)
            }
        }
    }
}