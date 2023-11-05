package com.sginnovations.asked.domain.ref_code

import android.util.Log
import com.sginnovations.asked.repository.TokenRepository
import javax.inject.Inject

private const val TAG = "RefCodeRewardUseCase"
class RefCodeRewardUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val isNewAccountUseCase: NewAccountUseCase,
    private val eligibleForRewardUseCase: EligibleForRewardUseCase,
) {
    suspend fun invoke(userId: String) {
        Log.i(TAG, "Ref Code reward")
        if (isNewAccountUseCase.invoke()) {
            if (eligibleForRewardUseCase.invoke(userId)) {
                tokenRepository.giveRefCodeReward()
            }
        }
    }
}