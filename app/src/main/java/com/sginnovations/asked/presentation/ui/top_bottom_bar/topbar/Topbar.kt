package com.sginnovations.asked.presentation.ui.top_bottom_bar.topbar

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.sginnovations.asked.AssistantChat
import com.sginnovations.asked.AssistantNewConversation
import com.sginnovations.asked.Camera
import com.sginnovations.asked.CategoryLesson
import com.sginnovations.asked.Chat
import com.sginnovations.asked.ChatsHistory
import com.sginnovations.asked.Crop
import com.sginnovations.asked.FirstOfferScreen
import com.sginnovations.asked.Gallery
import com.sginnovations.asked.MainScreen
import com.sginnovations.asked.NewConversation
import com.sginnovations.asked.ParentalAssist
import com.sginnovations.asked.ParentalGuidance
import com.sginnovations.asked.Profile
import com.sginnovations.asked.ScreensDestinations
import com.sginnovations.asked.SecondOfferScreen
import com.sginnovations.asked.Subscription
import com.sginnovations.asked.presentation.ui.ui_components.topbars.CameraTopBar
import com.sginnovations.asked.presentation.ui.ui_components.topbars.CategoryTopBar
import com.sginnovations.asked.presentation.ui.ui_components.topbars.ChatsHistoryTopBar
import com.sginnovations.asked.presentation.ui.ui_components.topbars.CropTopBar
import com.sginnovations.asked.presentation.ui.ui_components.topbars.DefaultTopBar
import com.sginnovations.asked.presentation.ui.ui_components.topbars.GalleryTopBar
import com.sginnovations.asked.presentation.ui.ui_components.topbars.MainTopBar
import com.sginnovations.asked.presentation.ui.ui_components.topbars.NameAndTokensTopBar
import com.sginnovations.asked.presentation.ui.ui_components.topbars.ParentalGuidanceTopBar
import com.sginnovations.asked.presentation.ui.ui_components.topbars.ProfileTopBar
import com.sginnovations.asked.presentation.viewmodel.CameraViewModel
import com.sginnovations.asked.presentation.viewmodel.LessonViewModel
import com.sginnovations.asked.presentation.viewmodel.TokenViewModel

private const val TAG = "LearnedTopBar"

@Composable
fun LearnedTopBar(
    vmTokens: TokenViewModel,
    vmCamera: CameraViewModel,
    vmLesson: LessonViewModel,

    currentScreen: ScreensDestinations?,
    canNavigateBack: Boolean,

    onNavigateProfile: () -> Unit,
    onNavigateSettings: () -> Unit,

    onNavigate: (ScreensDestinations) -> Unit,
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    Log.i(TAG, "LearnedTopBar: ${currentScreen.toString()}")

    when (currentScreen?.route) {

        MainScreen.route -> MainTopBar(
            onNavigateProfile,
            onNavigateSettings
        )
        /**
         * 4
         */
        Camera.route -> CameraTopBar(
            Camera.getName(context),
            navigateUp
        ) { onNavigate(it) }

        ChatsHistory.route -> ChatsHistoryTopBar(
            ChatsHistory.getName(context),
            navigateUp
        ) { onNavigate(it) }

        /**
         * Same
         */
        ParentalGuidance.route -> ParentalGuidanceTopBar(
            ParentalGuidance.getName(context),
            navigateUp
        ) { onNavigate(it) }

        ParentalAssist.route -> ParentalGuidanceTopBar(
            ParentalAssist.getName(context),
            navigateUp,
        ) { onNavigate(it) }

        Profile.route -> ProfileTopBar(
            Profile.getName(context),
            navigateUp,
        )

        /**
         * Other
         */
        Crop.route -> CropTopBar(
            vmCamera,
            navigateUp,
        )
        /**
         * Token
         */
        Chat.route -> NameAndTokensTopBar(
            vmTokens,
            currentScreen.getName(context),
            { onNavigate(FirstOfferScreen) },
            navigateUp
        )
        NewConversation.route -> NameAndTokensTopBar(
            vmTokens,
            currentScreen.getName(context),
            { onNavigate(FirstOfferScreen) },
            navigateUp
        )
        AssistantNewConversation.route -> NameAndTokensTopBar(
            vmTokens,
            currentScreen.getName(context),
            { onNavigate(FirstOfferScreen) },
            navigateUp
        )
        AssistantChat.route -> NameAndTokensTopBar(
            vmTokens,
            currentScreen.getName(context),
            { onNavigate(FirstOfferScreen) },
            navigateUp
        )

        Gallery.route -> GalleryTopBar(
            navigateUp
        )

        Subscription.route -> {}
        FirstOfferScreen.route -> {}
        SecondOfferScreen.route -> {}

        CategoryLesson.route -> CategoryTopBar(
            vmLesson,

            navigateUp
        )

        else ->
            if (canNavigateBack) {
                DefaultTopBar(
                    currentScreen?.getName(context),
                    navigateUp
                )
            }
    }
}
