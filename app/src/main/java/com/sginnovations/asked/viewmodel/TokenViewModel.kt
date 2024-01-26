package com.sginnovations.asked.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.Constants.Companion.ONE_LESS_TOKEN
import com.sginnovations.asked.repository.RemoteConfigRepository
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

    private val remoteConfigRepository: RemoteConfigRepository,
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

    fun ensureMinimumTokensUseCaseCheckPremium() {
        viewModelScope.launch {
            tokenRepository.ensureMinimumTokensUseCaseCheckPremium()
        }
    }
    fun switchPointsVisibility() {
        Log.i(TAG, "switchPointsVisibility")
        _pointsScreenVisible.value = !_pointsScreenVisible.value
    }
}

//fun getCameraMathTokens(): String {
//        Log.i(TAG, "getCameraMathTokens")
//        return remoteConfigRepository.getCameraMathTokens()
//    }
//    fun getCameraTextTokens(): String {
//        Log.i(TAG, "getCameraMathTokens")
//        return remoteConfigRepository.getCameraTextTokens()
//    }
//    fun oneLessToken() = lessTokenCheckPremium(ONE_LESS_TOKEN)
//    fun xLessToken(num: Int) = lessTokenCheckPremium(num)
//
//    private fun lessTokenCheckPremium(num: Int) {
//        viewModelScope.launch {
//            Log.i(TAG, "oneLessToken")
//            tokenRepository.lessTokenCheckPremium(num)
//        }
//    }