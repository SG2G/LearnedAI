package com.sginnovations.asked.domain.usecase.token

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.sginnovations.asked.Constants
import com.sginnovations.asked.Constants.Companion.LAST_TOKEN_UPDATE
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "EnsureMinimumTokensUseCase"
private const val DAILY_TOKENS = 2
class EnsureMinimumTokensUseCase @Inject constructor() {

    suspend operator fun invoke(documentReference: DocumentReference) {
        val today = LocalDate.now().toString() // Actual date, format: "YYYY-MM-DD"

        documentReference.get().addOnSuccessListener { documentSnapshot ->
            val currentTokens = documentSnapshot.getLong(Constants.TOKENS_NAME) ?: 0
            val lastUpdateDate = documentSnapshot.getString("lastTokensUpdate")

            if (today != lastUpdateDate && currentTokens < DAILY_TOKENS) {
                val updates = mapOf(
                    Constants.TOKENS_NAME to DAILY_TOKENS,
                    LAST_TOKEN_UPDATE to today
                )
                documentReference.update(updates)
                    .addOnSuccessListener {
                        Log.i(TAG, "Tokens set to 2 for the new day.")
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


