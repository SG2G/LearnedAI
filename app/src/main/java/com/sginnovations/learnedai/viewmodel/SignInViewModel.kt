package com.sginnovations.learnedai.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sginnovations.learnedai.presentation.sign_in.GoogleAuthUiClient
import com.sginnovations.learnedai.presentation.sign_in.SignInResult
import com.sginnovations.learnedai.presentation.sign_in.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TAG = "SignInViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor():ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }
    fun resetState() {
        _state.update { SignInState() }
    }
}