package com.sginnovations.asked.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.sginnovations.asked.repository.TokenRepository
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
) : ViewModel() {

    private val _tokens = MutableStateFlow<Long>(0)
    val tokens = _tokens.asStateFlow()

    private val _isPointsVisible = MutableStateFlow<Boolean>(false)
    val isPointsVisible = _isPointsVisible.asStateFlow()

    init {
        viewModelScope.launch {
            tokenRepository.getTokens().collect { tokens ->
                Log.i(TAG, "Launching token listener")
                _tokens.value = tokens
            }
        }
    }

    fun testGivePoints() {
        viewModelScope.launch {
            _tokens.value += 10
        }
    }

    suspend fun oneLessToken() {
        viewModelScope.launch {
            Log.i(TAG, "oneLessToken")
            tokenRepository.oneLessToken()
        }
    }

    fun switchPointsVisibility() {
        Log.i(TAG, "switchPointsVisibility")
        _isPointsVisible.value = !_isPointsVisible.value
    }
}