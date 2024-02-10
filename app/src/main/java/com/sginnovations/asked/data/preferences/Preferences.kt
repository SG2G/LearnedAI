package com.sginnovations.asked.data.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val PREFERENCES_NAME = "preferences"

private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

class Preferences @Inject constructor(
    private val context: Context,
) {

    /**
     * Reads
     */
    private val READ_LESSONS_KEY = stringPreferencesKey("read_lessons")

    suspend fun getReadLessons(): Set<Int> {
        val preferences = context.dataStore.data.first()
        val lessonsString = preferences[READ_LESSONS_KEY] ?: ""
        return if (lessonsString.isEmpty()) emptySet() else lessonsString.split(",").map { it.toInt() }.toSet()
    }

    suspend fun markLessonAsRead(lessonId: Int) {
        val currentLessons = getReadLessons().toMutableSet()
        currentLessons.add(lessonId)
        setReadLessons(currentLessons)
    }

    private suspend fun setReadLessons(lessons: Set<Int>) {
        val lessonsString = lessons.joinToString(separator = ",")
        context.dataStore.edit { preferences ->
            preferences[READ_LESSONS_KEY] = lessonsString
        }
    }

    /**
     * Show Sub Offer
     */
    suspend fun setShowSubOffer(key: String) {
        val preferenceKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferenceKey] = false
        }
    }
    suspend fun getShowSubOffer(key: String): Boolean {
        val preferenceKey = booleanPreferencesKey(key)
        val showSubOffer = context.dataStore.data.first()[preferenceKey] ?: true
        Log.d(PREFERENCES_NAME, "getShowSubOffer: $showSubOffer")
        return showSubOffer
    }

    /**
     * Text Size
     */
    suspend fun setFontSizeIncrease(key: String, increase: Float) {
        val preferenceKey = floatPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferenceKey] = increase
        }
    }
    suspend fun getFontSizeIncrease(key: String): Float {
        val preferenceKey = floatPreferencesKey(key)
        return context.dataStore.data.first()[preferenceKey] ?: 1f
    }

    /**
     * OnBoarding
     */
    suspend fun setNotFirstTime(key: String) {
        val preferenceKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferenceKey] = false
        }
    }
    suspend fun getIfIsFirstTime(key: String): Boolean {
        val preferenceKey = booleanPreferencesKey(key)
        val firsTime = context.dataStore.data.first()[preferenceKey] ?: true
        Log.d(PREFERENCES_NAME, "getIfIsFirstTime: $firsTime")
        return firsTime
    }

    /**
     * Theme
     */
    suspend fun setTheme(key: String, value: Boolean) {
        val preferenceKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }
    suspend fun getTheme(key: String): Boolean {
        val preferenceKey = booleanPreferencesKey(key)
        return context.dataStore.data.first()[preferenceKey] ?: false
    }

}