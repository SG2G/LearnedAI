package com.sginnovations.asked.domain

import com.sginnovations.asked.repository.TokenRepository
import javax.inject.Inject

class RefCodeRewardUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend fun invoke() {
        tokenRepository.giveRefCodeReward()
    }
}