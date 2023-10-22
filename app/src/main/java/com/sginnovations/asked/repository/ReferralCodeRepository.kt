package com.sginnovations.asked.repository

import android.content.Intent
import android.util.Log
import com.sginnovations.asked.domain.RefCodeRewardUseCase
import com.sginnovations.asked.model.ref_code.CheckReferralCode
import javax.inject.Inject

private const val TAG = "ReferralCodeRepository"
class ReferralCodeRepository @Inject constructor(
    private val checkReferralCode: CheckReferralCode,
    private val refCodeReward: RefCodeRewardUseCase,
) {
    suspend fun checkReferralCodeInvite(intent: Intent) {
        Log.d("checkReferralCodeInvite", "checkReferralCodeInvite: ")
        checkReferralCode(
            intent,
            onRewardUser = {
                Log.d(TAG, "checkReferralCodeInvite: Reward granted and given yesyes")
                refCodeReward
            }
        )
    }
}