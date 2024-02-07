package com.sginnovations.asked

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.sginnovations.asked.ui.theme.LearnedAITheme
import com.sginnovations.asked.ui.utils.UpdateScreen
import com.sginnovations.asked.viewmodel.PreferencesViewModel
import com.sginnovations.asked.viewmodel.RemoteConfigViewModel

@Composable
fun LearnedApp(
    vmRemoteConfig: RemoteConfigViewModel = hiltViewModel(),
    vmPreferences: PreferencesViewModel = hiltViewModel(),
) {
    val needToUpdate = vmRemoteConfig.needToUpdate
    val theme = vmPreferences.theme
    val fontSizeIncrease = vmPreferences.fontSizeIncrease

    Log.d("LearnedApp", "fontSizeMultiplier: ${fontSizeIncrease.floatValue} ")

    LearnedAITheme(
        darkTheme = theme.value,

        fontSizeIncrease = fontSizeIncrease,
    ) {
        if (needToUpdate.value) {
            UpdateScreen()
        } else {
            LearnedNavigation(
                vmPreferences = vmPreferences,
            )
        }
    }
}

