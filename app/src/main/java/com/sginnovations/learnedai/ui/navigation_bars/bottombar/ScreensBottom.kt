package com.sginnovations.learnedai.ui.navigation_bars.bottombar

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

interface BottomBarDestinations {
    val route: String
    fun getLabel(context: Context): String
    val icon: ImageVector
    val selectedIcon: ImageVector
}

object Camera : BottomBarDestinations {
    override val route = "Camera"
    override fun getLabel(context: Context) = context.getString(R.string.bottom_bar_label_camera)
    override val icon = Icons.Outlined.CameraAlt
    override val selectedIcon = Icons.Filled.CameraAlt
}

object Chats : BottomBarDestinations {
    override val route = "Chats"
    override fun getLabel(context: Context) = context.getString(R.string.bottom_bar_label_search)
    override val icon = Icons.Outlined.Chat
    override val selectedIcon = Icons.Filled.Chat
}

object Profile : BottomBarDestinations {
    override val route = "Profile"
    override fun getLabel(context: Context) = context.getString(R.string.bottom_bar_label_profile)
    override val icon = Icons.Outlined.PersonOutline
    override val selectedIcon = Icons.Filled.Person
}
