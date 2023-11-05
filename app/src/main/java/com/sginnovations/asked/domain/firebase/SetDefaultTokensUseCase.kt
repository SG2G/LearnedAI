package com.sginnovations.asked.domain.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.Constants.Companion.USERS_NAME
import javax.inject.Inject

private const val TAG = "SetDefaultTokensUseCase"

private const val TOKENS = "tokens"
class SetDefaultTokensUseCase @Inject constructor() {
    operator fun invoke(firestore: FirebaseFirestore,user: FirebaseUser) {
        Log.d(TAG, "invoke: adding default tokens")
        firestore.collection(USERS_NAME).document(user.uid).set(mapOf(TOKENS to 7))
    }
}