package com.sginnovations.asked

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.sginnovations.asked.presentation.ui.theme.LearnedAITheme
import com.sginnovations.asked.presentation.ui.utils.UpdateScreen
import com.sginnovations.asked.presentation.viewmodel.PreferencesViewModel
import com.sginnovations.asked.presentation.viewmodel.RemoteConfigViewModel


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

