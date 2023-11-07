package com.sginnovations.asked.repository

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.sginnovations.asked.domain.token.GetTokensUseCase
import com.sginnovations.asked.domain.token.IncrementTokensUseCase
import com.sginnovations.asked.utils.CheckIsPremium.checkIsPremium
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    private val remoteConfigRepository: RemoteConfigRepository,
) {
    private val documentReference: DocumentReference?
        get() = authRepository.getDocumentReference()

    suspend fun getTokens(): Flow<Long> = callbackFlow {
        Log.i(TAG, "getTokens")
        while (documentReference == null) { delay(500) }

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

    suspend fun giveAdReward() = incrementTokens(
        remoteConfigRepository.getAdRewardTokens().toInt()
    )
    suspend fun giveRefCodeReward() = incrementTokens(
        remoteConfigRepository.getInviteRewardTokens().toInt()
    )

    suspend fun lessToken(num: Int) {
        if (!checkIsPremium()) {
            incrementTokens(num)
        }
    }
}
