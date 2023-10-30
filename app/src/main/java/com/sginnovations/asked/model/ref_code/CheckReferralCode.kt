package com.sginnovations.asked.model.ref_code

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.Executor
import javax.inject.Inject

private const val TAG = "HandleDynamicLink"
class HandleDynamicLink @Inject constructor(
    private val executor: Executor,
) {
    suspend operator fun invoke(
        intent: Intent,
        onRewardUser: (String) -> Unit
    ) {
        // When the app starts, check if it was opened from a Dynamic Link
        Log.d(TAG, "invoke: Final step, checking referral code")
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(executor) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link

                    // Extract referrer ID from the link
                    val referrerId = deepLink?.getQueryParameter("invitedby")

                    // Check if a new account was created and reward the referrer
                    if (referrerId != null) {
                        onRewardUser(referrerId)
                    }
                }
            }
            .addOnFailureListener(executor) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }

    }
}