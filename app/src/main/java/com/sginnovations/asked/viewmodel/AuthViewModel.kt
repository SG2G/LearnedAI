package com.sginnovations.asked.viewmodel

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.Constants
import com.sginnovations.asked.Constants.Companion.IS_PREMIUM
import com.sginnovations.asked.Constants.Companion.USERS_NAME
import com.sginnovations.asked.auth.sign_in.GoogleAuthUiClient
import com.sginnovations.asked.auth.sign_in.SignInResult
import com.sginnovations.asked.auth.sign_in.SignInState
import com.sginnovations.asked.auth.sign_in.UserData
import com.sginnovations.asked.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val googleAuthUiClient: GoogleAuthUiClient,

    ) : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _userAuth = MutableStateFlow<UserData?>(null)
    val userAuth = _userAuth.asStateFlow()

    private val executor = Executors.newSingleThreadScheduledExecutor()
    companion object {
        val isPremium = mutableStateOf(false)
    }

    override fun onCleared() {
        super.onCleared()
        executor.shutdown()
    }

    init {
        viewModelScope.launch {
            executor.scheduleAtFixedRate(::checkIsPremium, 0, 1, TimeUnit.MINUTES)

            userJustLogged()
            checkIsPremium()
        }
    }

    fun checkIsPremium() {
        val firestore = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val docRef = user?.uid?.let { firestore.collection(USERS_NAME).document(it) }

        docRef?.get()?.addOnSuccessListener { document ->
            if (document != null) {
                isPremium.value = document.getBoolean(IS_PREMIUM) ?: false
            }
        }?.addOnFailureListener { e ->
            Log.d(TAG, "checkIsPremium: error")
            e.printStackTrace()
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

    fun resetState() {
        _state.update { SignInState() }
    }
}