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
    override val icon: Int = R.drawable.camera_svgrepo_outlined
    override val selectedIcon: Int = R.drawable.camera_svgrepo_filled
}

object ChatsHistory : ScreensDestinations {
    override val route = "Chats History"
    override fun getName(context: Context) = context.getString(R.string.topbar_chats_history)
    override val icon: Int = R.drawable.chat_svgrepo_outlined
    override val selectedIcon: Int = R.drawable.chat_svgrepo_filled
}
object ParentalGuidance : ScreensDestinations {
    override val route = "Parental Guidance"
    override fun getName(context: Context) = "Parental Guidance"
    override val icon: Int = R.drawable.book_bookmark_svgrepo_outlined
    override val selectedIcon: Int = R.drawable.book_bookmark_svgrepo_filled
}

object Profile : ScreensDestinations {
    override val route = "Profile"
    override fun getName(context: Context) = context.getString(R.string.topbar_profile)
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

object Auth : ScreensDestinations {
    override val route = "Auth"
    override fun getName(context: Context) = "Auth"
}
object RefCode : ScreensDestinations {
    override val route = "Share"
    override fun getName(context: Context) = context.getString(R.string.topbar_share)
}
object Subscription : ScreensDestinations {
    override val route = "Subscription"
    override fun getName(context: Context) = context.getString(R.string.topbar_subscription)
}
object Gallery : ScreensDestinations {
    override val route = "Gallery"
    override fun getName(context: Context) = context.getString(R.string.topbar_gallery)
}