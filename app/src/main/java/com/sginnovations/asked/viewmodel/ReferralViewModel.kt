package com.sginnovations.asked.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.sginnovations.asked.repository.ReferralCodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "ReferralViewModel"
@HiltViewModel
class ReferralViewModel @Inject constructor(
    private val referralCodeRepository: ReferralCodeRepository
) : ViewModel() {

    suspend fun handleDynamicLink(intent: Intent) {
        Log.d(TAG, "ViewModel -> checkReferralInvite")
        referralCodeRepository.handleDynamicLink(intent)
    }
}