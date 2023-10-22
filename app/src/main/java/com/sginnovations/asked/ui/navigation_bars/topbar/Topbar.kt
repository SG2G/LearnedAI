package com.sginnovations.asked.ui.navigation_bars.topbar

import android.util.Log
import androidx.compose.runtime.Composable
import com.sginnovations.asked.ui.navigation_bars.Camera
import com.sginnovations.asked.ui.navigation_bars.Chat
import com.sginnovations.asked.ui.navigation_bars.ChatsHistory
import com.sginnovations.asked.ui.navigation_bars.Points
import com.sginnovations.asked.ui.navigation_bars.Profile
import com.sginnovations.asked.ui.ui_components.topbars.CameraTopBar
import com.sginnovations.asked.ui.ui_components.topbars.ChatTopBar
import com.sginnovations.asked.ui.ui_components.topbars.ChatsHistoryTopBar
import com.sginnovations.asked.ui.ui_components.topbars.DefaultTopBar
import com.sginnovations.asked.ui.ui_components.topbars.PointsTopBar
import com.sginnovations.asked.ui.ui_components.topbars.ProfileTopBar
import com.sginnovations.asked.viewmodel.TokenViewModel

private const val TAG = "LearnedTopBar"

@Composable
fun LearnedTopBar(
    vmTokens: TokenViewModel,

    currentScreenTitle: String?,
    canNavigateBack: Boolean,

    onNavigatePoints: () -> Unit,
    navigateUp: () -> Unit,
) {
    Log.i(TAG, "LearnedTopBar: ${currentScreenTitle.toString()}")

    when (currentScreenTitle) {
        Camera.route -> CameraTopBar(
            vmTokens,
            currentScreenTitle,
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

        Points.route -> PointsTopBar(
            currentScreenTitle,
            navigateUp
        )

        else ->
            if (canNavigateBack) {
                DefaultTopBar(
                    currentScreenTitle,
                    navigateUp
                )
            }
    }
}
