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
    private val tokenRepository: TokenRepository,
) : ViewModel() {

    private val _tokens = MutableStateFlow<Long>(0)
    val tokens = _tokens.asStateFlow()

    private val _pointsScreenVisible = MutableStateFlow(false)
    val pointsScreenVisible = _pointsScreenVisible.asStateFlow()

    fun startTokenListener() {
        viewModelScope.launch {
            tokenRepository.getTokens().collect { tokens ->
                Log.i(TAG, "Launching token listener")
                _tokens.value = tokens
            }
        }
    }

    fun testGivePoints() { //TODO DELETE
        viewModelScope.launch {
            Log.i(TAG, "testGivePoints: 10")
            _tokens.value += 10
        }
    }

    fun oneLessToken() {
        viewModelScope.launch {
            Log.i(TAG, "oneLessToken")
            tokenRepository.oneLessToken()
        }
    }

    fun switchPointsVisibility() {
        Log.i(TAG, "switchPointsVisibility")
        _pointsScreenVisible.value = !_pointsScreenVisible.value
    }
}