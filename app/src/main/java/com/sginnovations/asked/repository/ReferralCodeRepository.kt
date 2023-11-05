package com.sginnovations.asked.repository

import android.content.Intent
import android.util.Log
import com.sginnovations.asked.domain.ref_code.RefCodeRewardUseCase
import com.sginnovations.asked.model.ref_code.HandleDynamicLink
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HandleDynamicLink"
class ReferralCodeRepository @Inject constructor(
    private val handleDynamicLink: HandleDynamicLink,

    private val refCodeReward: RefCodeRewardUseCase,
) {
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun handleDynamicLink(intent: Intent) {
        Log.d("handleDynamicLink", "checkReferralCodeInvite: ")
        handleDynamicLink(
            intent = intent,
            onRewardUser = { userId ->
                Log.d(TAG, "handleDynamicLink: Reward granted and given yesyes")
                GlobalScope.launch {
                    refCodeReward.invoke(userId)
                }
            }
        )
    }
}