package com.sginnovations.asked.domain.repository

import android.content.Intent
import android.util.Log
import com.sginnovations.asked.domain.usecase.ref_code.RefCodeRewardUseCase
import com.sginnovations.asked.presentation.model.ref_code.HandleDynamicLink
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HandleDynamicLink"
class ReferralCodeRepository @Inject constructor(
    private val handleDynamicLink: HandleDynamicLink,

    private val refCodeRewardUseCase: RefCodeRewardUseCase,
) {
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun handleDynamicLink(intent: Intent) {
        Log.d("handleDynamicLink", "checkReferralCodeInvite: ")
        handleDynamicLink(
            intent = intent,
            onRewardUser = { inviteUserId ->
                Log.d(TAG, "handleDynamicLink: Reward granted and given yesyes")
                GlobalScope.launch {
                    refCodeRewardUseCase(inviteUserId)
                }
            }
        )
    }
}