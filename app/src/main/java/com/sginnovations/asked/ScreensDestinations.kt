package com.sginnovations.asked

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

interface ScreensDestinations {
    val route: String
    fun getName(context: Context): String
    fun getBottomName(context: Context): String = ""
    val icon: Int?
        get() = null
    val selectedIcon: Int?
        get() = null
}

/**
 * Bottom
 */
object Camera : ScreensDestinations {
    override val route = "Camera"
    override fun getName(context: Context) = context.getString(R.string.bottom_bar_label_camera)
    override fun getBottomName(context: Context) = context.getString(R.string.bottom_bar_label_camera)
    override val icon: Int = R.drawable.camera_svgrepo_outlined
    override val selectedIcon: Int = R.drawable.camera_svgrepo_filled
}

object ChatsHistory : ScreensDestinations {
    override val route = "ChatsHistory"
    override fun getName(context: Context) = context.getString(R.string.topbar_chats_history)
    override fun getBottomName(context: Context) = context.getString(R.string.topbar_chats_history)
    override val icon: Int = R.drawable.chat_svgrepo_outlined
    override val selectedIcon: Int = R.drawable.chat_svgrepo_filled
}
object ParentalAssist : ScreensDestinations {
    override val route = "ParentalAssist"
    override fun getName(context: Context) = context.getString(R.string.topbar_parentalassist)
    override fun getBottomName(context: Context) = context.getString(R.string.assistant)
    override val icon: Int = R.drawable.sofa_svgrepo_outlined
    override val selectedIcon: Int = R.drawable.sofa_svgrepo_filled
}
object ParentalGuidance : ScreensDestinations {
    override val route = "ParentalGuidance"
    override fun getName(context: Context) = context.getString(R.string.topbar_parental_guidance)
    override fun getBottomName(context: Context) = context.getString(R.string.guide)
    override val icon: Int = R.drawable.book_bookmark_svgrepo_outlined
    override val selectedIcon: Int = R.drawable.book_bookmark_svgrepo_filled
}

object Profile : ScreensDestinations {
    override val route = "Profile"
    override fun getName(context: Context) = context.getString(R.string.topbar_profile)
    override fun getBottomName(context: Context) = context.getString(R.string.topbar_profile)
    override val icon: Int = R.drawable.profile_svgrepo_outlined
    override val selectedIcon: Int = R.drawable.profile_svgrepo_filled
}

/**
 * Can Navigate Back
 */
object Chat: ScreensDestinations {
    override val route = "Chat"
    override fun getName(context: Context) = "Chat"
}

object NewConversation : ScreensDestinations {
    override val route = "NewConversation"
    override fun getName(context: Context) = context.getString(R.string.topbar_new_chat)
}
object Crop : ScreensDestinations {
    override val route = "Crop"
    override fun getName(context: Context) = context.getString(R.string.topbar_crop)
}
object OnBoarding : ScreensDestinations {
    override val route = "OnBoarding"
    override fun getName(context: Context) = "OnBoarding"
}

object Auth : ScreensDestinations {
    override val route = "Auth"
    override fun getName(context: Context) = "Auth"
}
object RefCode : ScreensDestinations {
    override val route = "Share"
    override fun getName(context: Context) = context.getString(R.string.topbar_share)
}
object CategoryLesson : ScreensDestinations {
    override val route = "CategoryLesson"
    override fun getName(context: Context) = context.getString(R.string.topbar_lessons)
}
object Lesson : ScreensDestinations {
    override val route = "Lesson"
    override fun getName(context: Context) = context.getString(R.string.topbar_lesson)
}
object Transcript : ScreensDestinations {
    override val route = "Transcript"
    override fun getName(context: Context) = context.getString(R.string.topbar_transcript)
}
object Subscription : ScreensDestinations {
    override val route = "Subscription"
    override fun getName(context: Context) = context.getString(R.string.topbar_subscription)
}
object Gallery : ScreensDestinations {
    override val route = "Gallery"
    override fun getName(context: Context) = context.getString(R.string.topbar_gallery)
}

object Settings : ScreensDestinations {
    override val route = "Settings"
    override fun getName(context: Context) = context.getString(R.string.topbar_settings)
}
object AssistantNewConversation : ScreensDestinations {
    override val route = "AssistantNewConversation"
    override fun getName(context: Context) = context.getString(R.string.topbar_new_chat)
}
object AssistantChat : ScreensDestinations {
    override val route = "AssistantChat"
    override fun getName(context: Context) = context.getString(R.string.topbar_assistant_chat)
}