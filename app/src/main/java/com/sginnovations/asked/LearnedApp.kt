package com.sginnovations.asked

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.sginnovations.asked.ui.utils.UpdateApp
import com.sginnovations.asked.utils.CheckMinVersion.needToUpdate
import com.sginnovations.asked.viewmodel.RemoteConfigViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "LearnedApp"

@Composable
fun LearnedApp(
    vmRemoteConfig: RemoteConfigViewModel = hiltViewModel(),
) {
    val needToUpdate = vmRemoteConfig.needToUpdate

    if (needToUpdate.value) {
        UpdateApp()
    } else {
        LearnedNavigation()
    }
}

