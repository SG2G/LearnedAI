package com.sginnovations.asked.ui.top_bottom_bar.topbar

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.sginnovations.asked.Camera
import com.sginnovations.asked.Chat
import com.sginnovations.asked.ChatsHistory
import com.sginnovations.asked.Crop
import com.sginnovations.asked.Gallery
import com.sginnovations.asked.NewConversation
import com.sginnovations.asked.Profile
import com.sginnovations.asked.ScreensDestinations
import com.sginnovations.asked.Subscription
import com.sginnovations.asked.ui.ui_components.topbars.CameraTopBar
import com.sginnovations.asked.ui.ui_components.topbars.ChatTopBar
import com.sginnovations.asked.ui.ui_components.topbars.ChatsHistoryTopBar
import com.sginnovations.asked.ui.ui_components.topbars.CropTopBar
import com.sginnovations.asked.ui.ui_components.topbars.DefaultTopBar
import com.sginnovations.asked.ui.ui_components.topbars.GalleryTopBar
import com.sginnovations.asked.ui.ui_components.topbars.ProfileTopBar
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel

private const val TAG = "LearnedTopBar"

@Composable
fun LearnedTopBar(
    vmTokens: TokenViewModel,
    vmCamera: CameraViewModel,
    vmChat: ChatViewModel,

    currentScreen: ScreensDestinations?,
    canNavigateBack: Boolean,

    onNavigate: (ScreensDestinations) -> Unit,
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    Log.i(TAG, "LearnedTopBar: ${currentScreen.toString()}")

    when (currentScreen?.route) {
        Camera.route -> CameraTopBar(
            vmTokens,
        )

        Crop.route -> CropTopBar(
            vmCamera,

            navigateUp
        )

        ChatsHistory.route -> ChatsHistoryTopBar(
            ChatsHistory.getName(context)
        ) { onNavigate(it) }

        Profile.route -> ProfileTopBar(
            Profile.getName(context)
        )

        Chat.route -> ChatTopBar(
            vmTokens,
            navigateUp
        )

        Gallery.route -> GalleryTopBar(
            navigateUp
        )

        Subscription.route -> {}

        else ->
            if (canNavigateBack) {
                DefaultTopBar(
                    currentScreen?.getName(context),
                    navigateUp
                )
            }
    }
}
