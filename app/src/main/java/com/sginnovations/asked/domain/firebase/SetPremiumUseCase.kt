package com.sginnovations.asked.domain.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.Constants.Companion.IS_PREMIUM
import com.sginnovations.asked.Constants.Companion.USERS_NAME
import kotlinx.coroutines.delay
import javax.inject.Inject

private const val TAG = "SetPremiumUseCase"
class SetPremiumUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) {
    suspend operator fun invoke(value: Boolean) {
        val user = firebaseAuth.currentUser
        Log.d(TAG, "invoke starting ")
        while (user == null) { delay(250) }

        Log.d(TAG, "invoke Adding IS_PREMIUM ")
        firestore.collection(USERS_NAME).document(user.uid).update(mapOf(IS_PREMIUM to value))
    }
}