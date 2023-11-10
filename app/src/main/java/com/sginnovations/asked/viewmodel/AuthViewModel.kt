package com.sginnovations.asked.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sginnovations.asked.Constants.Companion.IS_PREMIUM
import com.sginnovations.asked.Constants.Companion.USERS_NAME
import com.sginnovations.asked.auth.sign_in.GoogleAuthUiClient
import com.sginnovations.asked.auth.sign_in.data.SignInResult
import com.sginnovations.asked.auth.sign_in.data.SignInState
import com.sginnovations.asked.auth.sign_in.data.UserData
import com.sginnovations.asked.domain.firebase.setters.SetDefaultTokensUseCase
import com.sginnovations.asked.utils.CheckIsPremium.checkIsPremium
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val googleAuthUiClient: GoogleAuthUiClient,

    private val setDefaultTokensUseCase: SetDefaultTokensUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _userAuth = MutableStateFlow<UserData?>(null)
    val userAuth = _userAuth.asStateFlow()

    private val firestore = Firebase.firestore
    init {
        viewModelScope.launch {
            userJustLogged()

            checkIsPremium()
        }
    }


    fun userJustLogged() {
        _userAuth.value = googleAuthUiClient.getSignedInUser()

        Log.i(TAG, "userJustLogged: $userAuth")
    }

    fun getGoogleAuthUiClient(): GoogleAuthUiClient {
        return googleAuthUiClient
    }

    /**
     * Testing ? :(
     */
    fun setDefaultTokens(user: FirebaseUser) {
        setDefaultTokensUseCase(firestore, user)
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
        resetState()
        googleAuthUiClient.signOut()
    }

    private fun resetState() {
        _state.update { SignInState() }
    }
}