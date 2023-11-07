package com.sginnovations.asked.domain.firebase.setters

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.Constants.Companion.USERS_NAME
import com.sginnovations.asked.repository.RemoteConfigRepository
import javax.inject.Inject

private const val TAG = "SetDefaultTokensUseCase"

private const val TOKENS = "tokens"
class SetDefaultTokensUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) {
    operator fun invoke(firestore: FirebaseFirestore, user: FirebaseUser) {
        val defaultTokens = remoteConfigRepository.getDefaultTokens()
        Log.d(TAG, "invoke: value $defaultTokens")
        val value = defaultTokens.toIntOrNull()
        Log.d(TAG, "invoke: Integer value $value")
        firestore.collection(USERS_NAME).document(user.uid).set(mapOf(TOKENS to value))

    }
}