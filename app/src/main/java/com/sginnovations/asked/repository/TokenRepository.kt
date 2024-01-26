package com.sginnovations.asked.repository

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.Constants.Companion.TOKENS_NAME
import com.sginnovations.asked.Constants.Companion.USERS_NAME
import com.sginnovations.asked.domain.token.EnsureMinimumTokensUseCase
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

@Singleton
class TokenRepository @Inject constructor(
    private val getTokensUseCase: GetTokensUseCase,
    private val incrementTokensUseCase: IncrementTokensUseCase,
    private val ensureMinimumTokensUseCase: EnsureMinimumTokensUseCase,

    private val authRepository: AuthRepository,

    private val remoteConfigRepository: RemoteConfigRepository,
) {
    private val documentReference: DocumentReference?
        get() = authRepository.getDocumentReference()

    suspend fun getTokens(): Flow<Long> = callbackFlow {
        Log.i(TAG, "getTokens")
        while (documentReference == null) { delay(500) }

        val listenerRegistration = documentReference!!.addSnapshotListener { documentReference, _ ->
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
    private suspend fun ensureMinimumTokensUseCase() {
        Log.i(TAG, "ensureMinimumTokensUseCase")
        if (documentReference != null) ensureMinimumTokensUseCase(documentReference!!)
    }

    suspend fun giveRefCodeReward() = incrementTokens(
        remoteConfigRepository.getInviteRewardTokens().toInt()
    )

    suspend fun lessTokenCheckPremium(num: Int) {
        if (!checkIsPremium()) incrementTokens(num)
    }
    suspend fun ensureMinimumTokensUseCaseCheckPremium() {
        if (!checkIsPremium()) ensureMinimumTokensUseCase()
    }

    fun giveInvitorReward(inviteUserId: String) { // TODO USE CASE
        val firestore = FirebaseFirestore.getInstance()
        val plusTokens = remoteConfigRepository.getInviteRewardTokens().toInt()

        firestore.collection(USERS_NAME).document(inviteUserId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val currentTokens = document[TOKENS_NAME] as Long
                    val updatedTokens = currentTokens + plusTokens
                    firestore.collection(USERS_NAME).document(inviteUserId)
                        .set(mapOf(TOKENS_NAME to updatedTokens))
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully updated!")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error updating document", e)
                        }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }
}
