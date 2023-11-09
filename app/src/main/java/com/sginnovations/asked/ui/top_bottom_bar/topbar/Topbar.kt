package com.sginnovations.asked.ui.top_bottom_bar.topbar

import android.util.Log
import androidx.compose.runtime.Composable
import com.sginnovations.asked.Camera
import com.sginnovations.asked.Chat
import com.sginnovations.asked.ChatsHistory
import com.sginnovations.asked.NewConversation
import com.sginnovations.asked.Points
import com.sginnovations.asked.Profile
import com.sginnovations.asked.ScreensDestinations
import com.sginnovations.asked.Subscription
import com.sginnovations.asked.ui.ui_components.topbars.CameraTopBar
import com.sginnovations.asked.ui.ui_components.topbars.ChatTopBar
import com.sginnovations.asked.ui.ui_components.topbars.ChatsHistoryTopBar
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

        Subscription.route -> {}

        else ->
            if (canNavigateBack) {
                DefaultTopBar(
                    currentScreenTitle,
                    navigateUp
                )
            }
    }
}
