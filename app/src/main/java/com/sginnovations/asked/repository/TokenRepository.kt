package com.sginnovations.asked.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.Constants
import com.sginnovations.asked.Constants.Companion.AD_REWARD_NUM_TOKEN
import com.sginnovations.asked.Constants.Companion.INVITE_REWARD_NUM_TOKEN
import com.sginnovations.asked.Constants.Companion.USERS_NAME
import com.sginnovations.asked.domain.token.GetTokensUseCase
import com.sginnovations.asked.domain.token.IncrementTokensUseCase
import com.sginnovations.asked.viewmodel.AuthViewModel
import com.sginnovations.asked.viewmodel.AuthViewModel.Companion.isPremium
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "TokenRepository"

private const val tokensOneLess = -1

@Singleton
class TokenRepository @Inject constructor(
    private val getTokensUseCase: GetTokensUseCase,
    private val incrementTokensUseCase: IncrementTokensUseCase,
    private val authRepository: AuthRepository,
) {
    private val documentReference: DocumentReference?
        get() = authRepository.getDocumentReference()

    suspend fun getTokens(): Flow<Long> = callbackFlow {
        Log.i(TAG, "getTokens")
        while (documentReference == null) {
            delay(500)
        }

        val listenerRegistration = documentReference!!.addSnapshotListener { documentReference, e ->
            Log.i(TAG, "documentRef ${documentReference.toString()}")
            launch(Dispatchers.IO) {
                getTokensUseCase(documentReference?.reference).collect { tokens ->
                    trySend(tokens)
                }
            }
        }
        awaitClose { listenerRegistration.remove() }
    }

    private suspend fun incrementTokens(numTokens: Int) {
        Log.i(TAG, "incrementTokens")
        if (documentReference != null) incrementTokensUseCase(documentReference!!, numTokens)
    }

    suspend fun giveAdReward() = incrementTokens(AD_REWARD_NUM_TOKEN.toInt())
    suspend fun giveRefCodeReward() = incrementTokens(INVITE_REWARD_NUM_TOKEN.toInt())
    suspend fun oneLessToken() {
        if (!isPremium.value) {
            incrementTokens(tokensOneLess)
        }
    }
}
