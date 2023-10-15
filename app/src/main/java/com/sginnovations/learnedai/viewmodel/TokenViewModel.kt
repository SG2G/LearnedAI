package com.sginnovations.learnedai.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.learnedai.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TokenViewModel"
@HiltViewModel
class TokenViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val tokenRepository: TokenRepository,
): ViewModel() {

    private val _tokens = MutableStateFlow<Long>(0)
    val tokens = _tokens.asStateFlow()

    init {
        viewModelScope.launch {
            tokenRepository.getTokens().collect { tokens ->
                _tokens.value = tokens
            }
        }
    }

    suspend fun oneLessToken() {
        viewModelScope.launch {
            tokenRepository.oneLessToken()
        }
    }
}