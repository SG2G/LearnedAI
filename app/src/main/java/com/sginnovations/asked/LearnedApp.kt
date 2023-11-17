package com.sginnovations.asked

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.sginnovations.asked.ui.utils.UpdateScreen
import com.sginnovations.asked.viewmodel.RemoteConfigViewModel

@Composable
fun LearnedApp(
    vmRemoteConfig: RemoteConfigViewModel = hiltViewModel(),
) {
    val needToUpdate = vmRemoteConfig.needToUpdate
    Log.d("LearnedApp", "needToUpdate: ${needToUpdate.value} ")

    if (needToUpdate.value) {
        UpdateScreen()
    } else {
        LearnedNavigation()
    }
}

