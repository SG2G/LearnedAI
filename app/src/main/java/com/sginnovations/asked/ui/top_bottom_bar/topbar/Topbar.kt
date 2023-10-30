package com.sginnovations.asked.ui.top_bottom_bar.topbar

import android.util.Log
import androidx.compose.runtime.Composable
import com.sginnovations.asked.ui.top_bottom_bar.Camera
import com.sginnovations.asked.ui.top_bottom_bar.Chat
import com.sginnovations.asked.ui.top_bottom_bar.ChatsHistory
import com.sginnovations.asked.ui.top_bottom_bar.Crop
import com.sginnovations.asked.ui.top_bottom_bar.Points
import com.sginnovations.asked.ui.top_bottom_bar.Profile
import com.sginnovations.asked.ui.top_bottom_bar.ScreensDestinations
import com.sginnovations.asked.ui.ui_components.topbars.CameraTopBar
import com.sginnovations.asked.ui.ui_components.topbars.ChatTopBar
import com.sginnovations.asked.ui.ui_components.topbars.ChatsHistoryTopBar
import com.sginnovations.asked.ui.ui_components.topbars.CropTopBar
import com.sginnovations.asked.ui.ui_components.topbars.DefaultTopBar
import com.sginnovations.asked.ui.ui_components.topbars.PointsTopBar
import com.sginnovations.asked.ui.ui_components.topbars.ProfileTopBar
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel

private const val TAG = "LearnedTopBar"

@Composable
fun LearnedTopBar(
    vmTokens: TokenViewModel,
    vmChat: ChatViewModel,

    currentScreenTitle: String?,
    canNavigateBack: Boolean,

    onNavigate: (ScreensDestinations) -> Unit,
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
        ) { onNavigate(it) }

        Profile.route -> ProfileTopBar(
            currentScreenTitle
        )

        Chat.route -> ChatTopBar(
            vmTokens,
            vmChat,
            currentScreenTitle,
            navigateUp
        )

        Points.route -> PointsTopBar(
            currentScreenTitle,
            navigateUp
        )

        Crop.route -> CropTopBar(navigateUp)

        else ->
            if (canNavigateBack) {
                DefaultTopBar(
                    currentScreenTitle,
                    navigateUp
                )
            }
    }
}
