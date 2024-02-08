package com.sginnovations.asked.domain.usecase.ref_code

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.Constants.Companion.USERS_NAME
import com.sginnovations.asked.domain.usecase.firebase.setters.SetUserInvitedUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "EligibleForReward"
class EligibleForRewardUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,

    private val setUserInvitedUseCase: SetUserInvitedUseCase
) {
    /**
     * If user is not EligibleForReward is because the data is already created
     * If user is EligibleForReward the data will be set here
     */
    suspend operator fun invoke(invitationUserId: String): Boolean {
        var currentUser = firebaseAuth.currentUser
        var attempts = 0
        while (currentUser == null && attempts < 5) {
            delay(1000)  // Wait for 1 second
            currentUser = firebaseAuth.currentUser
            attempts++
        }

        return currentUser?.let { currentUser ->
            Log.i(TAG, "currentUser ${currentUser.uid} invitationUserId: $invitationUserId")
            if (currentUser.uid == invitationUserId) {
                Log.i(TAG, "same uid")
                false
            } else {
                val userDocument = firestore.collection(USERS_NAME).document(currentUser.uid)
                val invitationsCollection = userDocument.collection("invitations")
                val querySnapshot = invitationsCollection.get().await()
                if (querySnapshot.isEmpty) {
                    setUserInvitedUseCase(invitationsCollection, invitationUserId)
                } else {
                    Log.i(TAG, "invitationsCollection NOT empty, User already invited")
                    // The collection exists and is not empty.
                    false
                }
            }
        } ?: false  // Returns false if currentUser is still null after waiting
    }

}