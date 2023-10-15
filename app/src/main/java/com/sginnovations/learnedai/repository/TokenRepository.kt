package com.sginnovations.learnedai.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.learnedai.Constants.Companion.TOKENS_NAME
import com.sginnovations.learnedai.Constants.Companion.USERS_NAME
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "TokenRepository"

private const val tokensRewardQuantity = 2
private const val tokensOneLess = -1
@Singleton
class TokenRepository @Inject constructor(
    private val db: FirebaseFirestore,
) {
    private val user = FirebaseAuth.getInstance().currentUser
    private val document = db.collection(USERS_NAME).document(user!!.uid)

    fun giveReward() {
        incrementTokens(tokensRewardQuantity)
    }
    fun oneLessToken() {
        incrementTokens(tokensOneLess)
    }

    /**
     * Model
     */
    suspend fun getTokens(): Flow<Long> = callbackFlow {
        val listenerRegistration = document.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val tokens = snapshot.getLong("tokens")
                if (tokens != null) {
                    trySend(tokens)
                } else {
                    trySend(0)
                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
        awaitClose { listenerRegistration.remove() }
    }
    private fun incrementTokens(increment: Int) {
        document.update(TOKENS_NAME, FieldValue.increment(increment.toLong()))
            .addOnSuccessListener {
                Log.i(TAG, "oneMoreToken correct")
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "oneMoreToken ERROR")
            }
    }
}
