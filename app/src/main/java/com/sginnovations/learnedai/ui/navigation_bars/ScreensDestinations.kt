package com.sginnovations.learnedai.ui.navigation_bars

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector
import com.sginnovations.learnedai.R

interface ScreensDestinations {
    val route: String
    fun getName(context: Context): String
    val icon: ImageVector?
    val selectedIcon: ImageVector?
}

/**
 * Bottom
 */
object Camera : ScreensDestinations {
    override val route = "Camera"
    override fun getName(context: Context) = context.getString(R.string.bottom_bar_label_camera)
    override val icon = Icons.Outlined.CameraAlt
    override val selectedIcon = Icons.Filled.CameraAlt
}

object ChatsHistory : ScreensDestinations {
    override val route = "Chats History"
    override fun getName(context: Context) = "Chats History"
    override val icon = Icons.Outlined.Chat
    override val selectedIcon = Icons.Filled.Chat
}

object Profile : ScreensDestinations {
    override val route = "Profile"
    override fun getName(context: Context) = "Profile"
    override val icon = Icons.Outlined.PersonOutline
    override val selectedIcon = Icons.Filled.Person
}

/**
 * Can Navigate Back
 */
object Chat: ScreensDestinations {
    override val route = "Chat"
    override fun getName(context: Context) = "Chat"
    override val icon: ImageVector? = null
    override val selectedIcon: ImageVector? = null
}

object NewConversation : ScreensDestinations {
    override val route = "NewConversation"
    override fun getName(context: Context) = "New Chat"
    override val icon: ImageVector? = null
    override val selectedIcon: ImageVector? = null
}
object Crop : ScreensDestinations {
    override val route = "Crop"
    override fun getName(context: Context) = "Crop"
    override val icon: ImageVector? = null
    override val selectedIcon: ImageVector? = null
}

object Auth : ScreensDestinations {
    override val route = "Auth"
    override fun getName(context: Context) = "Auth"
    override val icon: ImageVector? = null
    override val selectedIcon: ImageVector? = null
}
object Points : ScreensDestinations {
    override val route = "Points"
    override fun getName(context: Context) = "Points"
    override val icon: ImageVector? = null
    override val selectedIcon: ImageVector? = null
}