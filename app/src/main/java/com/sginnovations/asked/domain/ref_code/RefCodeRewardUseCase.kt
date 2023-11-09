package com.sginnovations.asked.domain.ref_code

import android.util.Log
import com.sginnovations.asked.repository.TokenRepository
import javax.inject.Inject

private const val TAG = "RefCodeRewardUseCase"
class RefCodeRewardUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val isActualDeviceNewAccountUseCase: NewAccountUseCase,
    private val isActualDeviceEligibleForRewardUseCase: EligibleForRewardUseCase,
) {
    suspend operator fun invoke(inviteUserId: String) {
        Log.i(TAG, "Ref Code reward")
        if (isActualDeviceNewAccountUseCase()) {
            if (isActualDeviceEligibleForRewardUseCase(inviteUserId)) {
                // Tokens for local user
                tokenRepository.giveRefCodeReward()
                // Give user who invited Tokens
                tokenRepository.giveInvitorReward(inviteUserId)
            }
        }
    }
}