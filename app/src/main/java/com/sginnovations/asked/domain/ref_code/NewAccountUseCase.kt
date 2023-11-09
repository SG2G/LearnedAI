package com.sginnovations.asked.domain.ref_code

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "NewAccountUseCase"

class NewAccountUseCase @Inject constructor() {
    suspend operator fun invoke(): Boolean {
        val auth = Firebase.auth
        val db = Firebase.firestore

        val user = auth.currentUser
        val creationTime = user?.metadata?.creationTimestamp

        val userDoc = user?.let { db.collection("users").document(it.uid) }

        return suspendCoroutine { continuation ->
            userDoc?.get()?.addOnSuccessListener { document ->
                if (document.exists()) {
                    val currentTime = System.currentTimeMillis()
                    val timeDifference = currentTime - creationTime!!

                    val timeDifferenceInHours =
                        TimeUnit.MILLISECONDS.toHours(timeDifference)

                    continuation.resume(timeDifferenceInHours <= 72)
                } else {
                    continuation.resume(true)
                }
            }?.addOnFailureListener {
                Log.w(TAG, "failure")
                continuation.resume(false)
            }
        }
    }
}
