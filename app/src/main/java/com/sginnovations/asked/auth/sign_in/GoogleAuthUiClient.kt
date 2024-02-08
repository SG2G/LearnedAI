package com.sginnovations.asked.auth.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sginnovations.asked.R
import com.sginnovations.asked.auth.sign_in.data.SignInResult
import com.sginnovations.asked.auth.sign_in.data.UserData
import com.sginnovations.asked.domain.usecase.firebase.setters.SetDefaultTokensUseCase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

private const val TAG = "GoogleAuthUiClient"
class GoogleAuthUiClient @Inject constructor (
    private val context: Context,
    private val oneTapClient: SignInClient,

    private val setDefaultTokensUseCase: SetDefaultTokensUseCase
) {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    val user = FirebaseAuth.getInstance().currentUser

    suspend fun signIn(): IntentSender? {
        Log.i(TAG, "signIn:")
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        Log.i(TAG, "buildSignInRequest: ")
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken,null)

        return try {
            Log.i(TAG, "signInWithIntent: ")
            val result = auth.signInWithCredential(googleCredential).await()
            val user = result.user

            if (result.additionalUserInfo?.isNewUser == true) {
                if (user != null) {
                    Log.d(TAG, "signInWithIntent: setDefaultTokensUseCase")
                    setDefaultTokensUseCase(firestore, user)
                }
            }
            Log.d(TAG, "Calling signinResult ${user?.displayName}")

            SignInResult(
                data = user?.run {
                    Log.d(TAG, "Sign in result done")
                    UserData(
                        userId = uid,
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        Log.i(TAG, "getSignedInUser: ")
        UserData(
            userId = uid,
            userName = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }
}