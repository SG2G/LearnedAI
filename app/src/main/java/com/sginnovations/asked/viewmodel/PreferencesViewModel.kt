package com.sginnovations.asked.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.Constants.Companion.KEY_FIRST_TIME
import com.sginnovations.asked.data.preferences.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PreferencesViewModel"
@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferences: Preferences,
) : ViewModel() {

    val firstTimeLaunch = mutableStateOf(false)

    init {
        viewModelScope.launch {
            getIfIsFirstTime()
        }
    }

    /**
     * isFirstTime?
     * Execute just one time in the hole app
     */
    private suspend fun getIfIsFirstTime() {
        firstTimeLaunch.value = preferences.getIfIsFirstTime(KEY_FIRST_TIME)
    }

    suspend fun setNotFirstTime() {
        preferences.setNotFirstTime(KEY_FIRST_TIME)
        getIfIsFirstTime()
    }
}