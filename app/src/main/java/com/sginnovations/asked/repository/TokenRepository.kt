package com.sginnovations.asked.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.Constants.Companion.USERS_NAME
import com.sginnovations.asked.domain.token.GetTokensUseCase
import com.sginnovations.asked.domain.token.IncrementTokensUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "TokenRepository"

private const val tokensAdRewardQuantity = 2
private const val tokensRefCodeReward = 10
private const val tokensOneLess = -1
@Singleton
class TokenRepository @Inject constructor(
    private val db: FirebaseFirestore,

    private val getTokensUseCase: GetTokensUseCase,
    private val incrementTokensUseCase: IncrementTokensUseCase,
) {

    private val user = FirebaseAuth.getInstance().currentUser
    private val documentReference = user?.let { db.collection(USERS_NAME).document(it.uid) }

    suspend fun getTokens(): Flow<Long> = callbackFlow {
        Log.i(TAG, "getTokens")
        val listenerRegistration = documentReference?.addSnapshotListener { documentReference, e ->
            launch(Dispatchers.IO) {
                getTokensUseCase(documentReference?.reference).collect { tokens ->
                    trySend(tokens)
                }
            }
        }
        awaitClose { listenerRegistration?.remove() }
    }

    private suspend fun incrementTokens(numTokens: Int) {
        Log.i(TAG, "incrementTokens")
        if (documentReference != null) incrementTokensUseCase(documentReference,numTokens)
    }

    suspend fun giveAdReward() = incrementTokens(tokensAdRewardQuantity)
    suspend fun giveRefCodeReward() = incrementTokens(tokensRefCodeReward)
    suspend fun oneLessToken() = incrementTokens(tokensOneLess)
}
