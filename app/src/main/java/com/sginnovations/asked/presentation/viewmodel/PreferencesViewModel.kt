package com.sginnovations.asked.presentation.viewmodel

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.Constants.Companion.KEY_FIRST_TIME
import com.sginnovations.asked.Constants.Companion.KEY_FONT_SIZE_MULTIPLIER
import com.sginnovations.asked.Constants.Companion.KEY_OFFER
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
    val showSubOffer = mutableStateOf(false)
    val theme = mutableStateOf(false)
    val fontSizeIncrease = mutableFloatStateOf(1f)

    private val readLessons = mutableStateOf<Set<Int>>(setOf())

    init {
        viewModelScope.launch {
            getIfIsFirstTime()
            getShowSubOffer()
            getTheme()
            getFontSizeIncrease()

            readLessons.value = preferences.getReadLessons()
        }
    }

    /**
     * Show Subscription Offer
     */
    private suspend fun getShowSubOffer() {
        showSubOffer.value = preferences.getShowSubOffer(KEY_OFFER)
    }

    suspend fun setShowSubOffer() {
        preferences.setShowSubOffer(KEY_OFFER)
        getShowSubOffer()
    }

    /**
     * Reads
     */

    fun markLessonAsRead(lessonId: Int) {
        viewModelScope.launch {
            preferences.markLessonAsRead(lessonId)
            readLessons.value = preferences.getReadLessons()
        }
    }

    fun isLessonRead(lessonId: Int): Boolean {
        return readLessons.value.contains(lessonId)
    }

    /**
     * Text Size
     */
    private suspend fun getFontSizeIncrease() {
        fontSizeIncrease.floatValue = preferences.getFontSizeIncrease(KEY_FONT_SIZE_MULTIPLIER)
    }

    suspend fun setFontSizeIncrease(increase: Float) {
        preferences.setFontSizeIncrease(KEY_FONT_SIZE_MULTIPLIER, increase)
        getFontSizeIncrease()
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