package com.sginnovations.asked.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.Constants
import javax.inject.Inject

private const val TAG = "AuthRepository"

class AuthRepository @Inject constructor(
    private val firebaseStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) {
    private var documentReference: DocumentReference? = null

    fun getDocumentReference(): DocumentReference? {
        Log.i(TAG, "getDocumentReference: ${firebaseAuth.currentUser?.displayName}")
        return firebaseAuth.currentUser?.let { user ->
            firebaseStore.collection(Constants.USERS_NAME).document(user.uid)
        }
    }

    init {
        firebaseAuth.addAuthStateListener {
            documentReference = firebaseAuth.currentUser?.let { user ->
                firebaseStore.collection(Constants.USERS_NAME).document(user.uid)
            }
        }
    }
}

