package com.sginnovations.asked.data.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val PREFERENCES_NAME = "preferences"

private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

class Preferences @Inject constructor(
    private val context: Context,
) {

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
}