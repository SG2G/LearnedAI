package com.sginnovations.asked.domain.usecase.token

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.sginnovations.asked.Constants
import javax.inject.Inject

private const val TAG = "IncrementTokensUseCase"
class IncrementTokensUseCase @Inject constructor() {
    suspend operator fun invoke(
        documentReference: DocumentReference,
        numTokens: Int,
    ) {
        Log.i(TAG, "invoke")
        documentReference.update(Constants.TOKENS_NAME, FieldValue.increment(numTokens.toLong()))
            .addOnSuccessListener {
                Log.i(TAG, "IncrementTokensUseCase correct")
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "IncrementTokensUseCase ERROR -> ${e.printStackTrace()}")
            }
    }
}