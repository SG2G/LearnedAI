package com.sginnovations.asked

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
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
    val fontSizeMultiplier = vmPreferences.fontSizeMultiplier

    Log.d("LearnedApp", "fontSizeMultiplier: ${fontSizeMultiplier.floatValue} ")

    LearnedAITheme(
        darkTheme = theme.value,

        fontSizeMultiplier = fontSizeMultiplier,
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

