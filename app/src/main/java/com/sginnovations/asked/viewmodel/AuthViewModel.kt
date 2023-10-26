package com.sginnovations.asked.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.sginnovations.asked.auth.sign_in.GoogleAuthUiClient
import com.sginnovations.asked.auth.sign_in.SignInResult
import com.sginnovations.asked.auth.sign_in.SignInState
import com.sginnovations.asked.auth.sign_in.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val googleAuthUiClient: GoogleAuthUiClient,
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _userAuth = MutableStateFlow<UserData?>(null)
    val userAuth = _userAuth.asStateFlow()

    init {
        userJustLogged()
    }
    fun userJustLogged() {
        val userAuth = googleAuthUiClient.getSignedInUser()
        _userAuth.value = userAuth
        Log.i(TAG, "userJustLogged: ${userAuth.toString()}")
    }

    fun getGoogleAuthUiClient(): GoogleAuthUiClient {
        return googleAuthUiClient
    }

    suspend fun deleteAccount() { //TODO DELETE

        googleAuthUiClient.user?.delete()
            ?.addOnCompleteListener {
                Log.i(TAG, "deleteAccount: Account deleted")
            }
            ?.addOnFailureListener {
                Log.i(TAG, "deleteAccount: Failure to delete acc")
            }
    }

    /**
     * Old shit
     */
    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    suspend fun signOut() {
        googleAuthUiClient.signOut()
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}