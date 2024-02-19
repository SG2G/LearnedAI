package com.sginnovations.asked.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerLib
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sginnovations.asked.auth.sign_in.GoogleAuthUiClient
import com.sginnovations.asked.auth.sign_in.data.SignInResult
import com.sginnovations.asked.auth.sign_in.data.SignInState
import com.sginnovations.asked.auth.sign_in.data.UserData
import com.sginnovations.asked.domain.usecase.firebase.setters.SetDefaultTokensUseCase
import com.sginnovations.asked.utils.CheckIsPremium.checkIsPremium
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "AskedSignIn"

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

    val showSignInScreen = mutableStateOf(false)

    init {
        viewModelScope.launch {
            userJustLogged()

            checkIsPremium()
        }
    }

    suspend fun getAuth(): FirebaseAuth? {
        val auth: FirebaseAuth = Firebase.auth

        return suspendCoroutine { continuation ->
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    Log.d(TAG, "getAuth: $auth")
                    if (task.isSuccessful) {
                        // Sign in success
                        Log.d(TAG, "isSuccessful: $auth")
                        continuation.resume(auth)
                        showSignInScreen.value = false
                    } else {
                        viewModelScope.launch {
                            delay(500)
                            Log.d(TAG, "else: $auth")
                            // Sign in fails
                            continuation.resume(null)
                            showSignInScreen.value = true
                        }
                    }
                }
        }
    }


    fun userJustLogged() {
        _userAuth.value = googleAuthUiClient.getSignedInUser()

        try {
            Log.d(TAG, "userJustLogged: Customer Id Successfully Set")
            AppsFlyerLib.getInstance().setCustomerUserId(_userAuth.value?.userId)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "userJustLogged: Customer Id Not Set")
        }

        Log.i(TAG, "userJustLogged: ${userAuth.value}")
    }

    fun getGoogleAuthUiClient() = googleAuthUiClient

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
        Log.d(TAG, "onSignInResult: result -> ${result.data}")
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