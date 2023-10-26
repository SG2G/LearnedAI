package com.sginnovations.asked.domain.token

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

private const val TAG = "GetTokensUseCase"
class GetTokensUseCase @Inject constructor(){
    suspend operator fun invoke(
        documentReference: DocumentReference?
    ): Flow<Long> = callbackFlow {
        Log.i(TAG, "invoke")
        val listenerRegistration = documentReference?.addSnapshotListener { snapshot, e ->
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
        awaitClose { listenerRegistration?.remove() }
    }
}