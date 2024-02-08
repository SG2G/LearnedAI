package com.sginnovations.asked.domain.usecase.ref_code

import android.util.Log
import com.sginnovations.asked.domain.repository.TokenRepository
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
                Log.d(TAG, "invoke: Eligible")
                // Tokens for local user
                tokenRepository.giveRefCodeReward() //TODO UNIT TEST
                // Give user who invited Tokens
                tokenRepository.giveInvitorReward(inviteUserId)
            }
        }
    }
}