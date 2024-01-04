package com.sginnovations.asked

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Log.d("LearnedApp", "needToUpdate: ${needToUpdate.value} ")

    val theme = vmPreferences.theme

    LearnedAITheme(
        darkTheme = theme.value
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

