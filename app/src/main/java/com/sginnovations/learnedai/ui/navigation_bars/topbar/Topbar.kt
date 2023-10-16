package com.sginnovations.learnedai.ui.navigation_bars.topbar

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.sginnovations.learnedai.ui.navigation_bars.Camera
import com.sginnovations.learnedai.ui.navigation_bars.Chat
import com.sginnovations.learnedai.ui.navigation_bars.ChatsHistory
import com.sginnovations.learnedai.ui.navigation_bars.Points
import com.sginnovations.learnedai.ui.navigation_bars.Profile
import com.sginnovations.learnedai.ui.ui_components.topbars.CameraTopBar
import com.sginnovations.learnedai.ui.ui_components.topbars.ChatTopBar
import com.sginnovations.learnedai.ui.ui_components.topbars.ChatsHistoryTopBar
import com.sginnovations.learnedai.ui.ui_components.topbars.DefaultTopBar
import com.sginnovations.learnedai.ui.ui_components.topbars.PointsTopBar
import com.sginnovations.learnedai.ui.ui_components.topbars.ProfileTopBar
import com.sginnovations.learnedai.viewmodel.TokenViewModel

private const val TAG = "LearnedTopBar"

@Composable
fun LearnedTopBar(
    vmTokens: TokenViewModel,

    currentScreenTitle: String?,
    canNavigateBack: Boolean,

    onNavigatePoints: () -> Unit,
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    Log.i(TAG, "LearnedTopBar: ${currentScreenTitle.toString()}")

    when (currentScreenTitle) {
        Camera.route-> CameraTopBar(
            vmTokens,
            currentScreenTitle,
            onNavigatePoints
        )

        ChatsHistory.route -> ChatsHistoryTopBar(
            currentScreenTitle
        )

        Profile.route -> ProfileTopBar(
            currentScreenTitle
        )

        Chat.route -> ChatTopBar(
            vmTokens,
            currentScreenTitle,
            onNavigatePoints,
            navigateUp
        )

        Points.route-> PointsTopBar(
            currentScreenTitle,
            navigateUp
        )

        else -> DefaultTopBar(
            currentScreenTitle,
            navigateUp
        )
    }
}
