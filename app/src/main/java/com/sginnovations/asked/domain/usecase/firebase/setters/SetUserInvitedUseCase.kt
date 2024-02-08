package com.sginnovations.asked.domain.usecase.firebase.setters

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "SetUserInvitedUseCase"

private const val INVITED = "invited"
private const val INVITED_BY = "invitedBy"
class SetUserInvitedUseCase @Inject constructor() {
    suspend operator fun invoke(invitationsCollection: CollectionReference, invitationUserId: String): Boolean {
        Log.i(TAG, "invitationsCollection empty")
        // The collection doesn't exist or is empty.
        // Here you can create the collection and add the document.
        return try {
            val invitationData = mapOf(INVITED to true, INVITED_BY to invitationUserId)
            invitationsCollection.document().set(invitationData).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}