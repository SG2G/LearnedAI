package com.sginnovations.asked.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.sginnovations.asked.presentation.sign_in.GoogleAuthUiClient
import com.sginnovations.asked.presentation.sign_in.SignInResult
import com.sginnovations.asked.presentation.sign_in.SignInState
import com.sginnovations.asked.presentation.sign_in.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TAG = "SignInViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val googleAuthUiClient: GoogleAuthUiClient,
    @ApplicationContext val context: Context
):ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _userAuth = MutableStateFlow<UserData?>(null)
    val userAuth = _userAuth.asStateFlow()

    init {
        val user = googleAuthUiClient.getSignedInUser()
        _userAuth.value = user
    }

    fun getGoogleAuthUiClient(): GoogleAuthUiClient {
        return googleAuthUiClient
    }

    /**
     * Old shit
     */
    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }
    suspend fun signOut() {
        googleAuthUiClient.signOut()
    }
    fun resetState() {
        _state.update { SignInState() }
    }
}