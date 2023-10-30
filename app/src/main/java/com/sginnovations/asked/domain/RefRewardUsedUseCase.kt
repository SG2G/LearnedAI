package com.sginnovations.asked.domain

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.Constants.Companion.USERS_NAME
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "EligibleForReward"
class EligibleForReward @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) {
    suspend fun invoke(invitationUserId: String): Boolean {
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
                    Log.i(TAG, "invitationsCollection empty")
                    // The collection doesn't exist or is empty.
                    // Here you can create the collection and add the document.
                    val invitationData = mapOf("invited" to true, "invitedBy" to invitationUserId)
                    invitationsCollection.document().set(invitationData).await()
                    true
                } else {
                    Log.i(TAG, "invitationsCollection NOT empty, User already invited")
                    // The collection exists and is not empty.
                    // Here you can perform the other action you mentioned.
                    false
                }
            }
        } ?: false  // Returns false if currentUser is still null after waiting
    }

}