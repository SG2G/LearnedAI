package com.sginnovations.asked.domain.usecase.firebase.setters

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.Constants.Companion.IS_PREMIUM
import com.sginnovations.asked.Constants.Companion.USERS_NAME
import com.sginnovations.asked.domain.repository.RemoteConfigRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

private const val TAG = "SetPremiumUseCase"

class SetPremiumUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,

//    private val remoteConfigRepository: RemoteConfigRepository,
) {

    suspend operator fun invoke(value: Boolean) {
        val user = firebaseAuth.currentUser
        Log.d(TAG, "invoke starting ")
        while (user == null) {
            delay(250)
        }

        Log.d(TAG, "invoke Adding IS_PREMIUM ")
        if (user.uid != "P635nMFvPJVI05RqiQ9jvpuR8Vp2") {
            firestore.collection(USERS_NAME).document(user.uid).update(mapOf(IS_PREMIUM to value))
        }
    }
//    suspend operator fun invoke(value: Boolean) {
//        val user = firebaseAuth.currentUser
//        Log.d(TAG, "invoke starting ")
//        while (user == null) {
//            delay(250)
//        }
//
//        Log.d(TAG, "invoke Adding IS_PREMIUM ")
//
//        val premiumUserUids = remoteConfigRepository.getPremiumUserUids()
//        Log.d(TAG, "premium uid $premiumUserUids")
//
//        // Check if user.uid is in the premiumUserUids list
//        val isUserPremium = premiumUserUids.contains(user.uid)
//
//        // Update the document for user.uid based on whether it is in the list
//        firestore.collection(USERS_NAME).document(user.uid)//TODO ALL THE TIME SENDING REQUEST
//            .update(mapOf(IS_PREMIUM to isUserPremium))
//            .addOnSuccessListener {
//                Log.d(TAG, "User premium status updated successfully")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error updating user premium status", e)
//            }
//    }
}