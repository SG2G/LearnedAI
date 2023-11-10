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

        val docRef = firestore.collection(USERS_NAME).document(user.uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.contains(TOKENS)) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    // If TOKENS does not exist
                    firestore.collection(USERS_NAME).document(user.uid).set(mapOf(TOKENS to value))
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }
}
