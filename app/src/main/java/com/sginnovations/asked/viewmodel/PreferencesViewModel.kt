package com.sginnovations.asked.viewmodel

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.Constants.Companion.KEY_FIRST_TIME
import com.sginnovations.asked.Constants.Companion.KEY_FONT_SIZE_MULTIPLIER
import com.sginnovations.asked.Constants.Companion.KEY_THEME
import com.sginnovations.asked.data.preferences.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PreferencesViewModel"
@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferences: Preferences,
) : ViewModel() {

    val firstTimeLaunch = mutableStateOf(false)
    val theme = mutableStateOf(false)
    val fontSizeMultiplier = mutableFloatStateOf(1f)

    init {
        viewModelScope.launch {
            getIfIsFirstTime()
            getTheme()
            getFontSizeMultiplier()
        }
    }

    /**
     * Text Size
     */
    private suspend fun getFontSizeMultiplier() {
        fontSizeMultiplier.floatValue = preferences.getFontSizeMultiplier(KEY_FONT_SIZE_MULTIPLIER)
    }

    suspend fun setFontSizeMultiplier(multiplier: Float) {
        preferences.setFontSizeMultiplier(KEY_FONT_SIZE_MULTIPLIER, multiplier)
        getFontSizeMultiplier()
    }

    /**
     * Theme
     */
    private suspend fun getTheme() {
        theme.value = preferences.getTheme(KEY_THEME)
    }

    suspend fun setTheme(value: Boolean) {
        viewModelScope.launch {
            preferences.setTheme(KEY_THEME, value)
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