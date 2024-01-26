package com.sginnovations.asked.domain.token

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.sginnovations.asked.Constants
import com.sginnovations.asked.Constants.Companion.LAST_TOKEN_UPDATE
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "EnsureMinimumTokensUseCase"
class EnsureMinimumTokensUseCase @Inject constructor() {

    suspend operator fun invoke(documentReference: DocumentReference) {
        val today = LocalDate.now().toString() // Actual date, format: "YYYY-MM-DD"

        documentReference.get().addOnSuccessListener { documentSnapshot ->
            val currentTokens = documentSnapshot.getLong(Constants.TOKENS_NAME) ?: 0
            val lastUpdateDate = documentSnapshot.getString("lastTokensUpdate")

            if (today != lastUpdateDate && currentTokens < 3) {
                val updates = mapOf(
                    Constants.TOKENS_NAME to 3,
                    LAST_TOKEN_UPDATE to today
                )
                documentReference.update(updates)
                    .addOnSuccessListener {
                        Log.i(TAG, "Tokens set to 3 for the new day.")
                    }
                    .addOnFailureListener { e ->
                        Log.i(TAG, "Error updating tokens: ${e.printStackTrace()}")
                    }
            }
        }.addOnFailureListener { e ->
            Log.i(TAG, "Error getting document: ", e)
        }
    }
}


