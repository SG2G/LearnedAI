package com.sginnovations.learnedai.ui.navigation.bottombar

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.sginnovations.learnedai.R

interface BottomBarDestinations {
    val route: String
    fun getLabel(context: Context): String
    val icon: @Composable () -> Painter
    val selectedIcon: @Composable () -> Painter
}

object Camera : BottomBarDestinations {
    override val route = "Camera"
    override fun getLabel(context: Context) = context.getString(R.string.bottom_bar_label_camera)
    override val icon: @Composable () -> Painter = {
        painterResource(id = R.drawable.photo_camera_fill0_wght400_grad0_opsz24)
    }
    override val selectedIcon: @Composable () -> Painter = {
        painterResource(id = R.drawable.photo_camera_fill1_wght400_grad0_opsz24)
    }
}

object Chats : BottomBarDestinations {
    override val route = "Chats"
    override fun getLabel(context: Context) = context.getString(R.string.bottom_bar_label_search)
    override val icon: @Composable () -> Painter = {
        painterResource(id = R.drawable.chat_fill0_wght400_grad0_opsz24)
    }
    override val selectedIcon: @Composable () -> Painter = {
        painterResource(id = R.drawable.chat_fill1_wght400_grad0_opsz24)
    }
}

object Profile : BottomBarDestinations {
    override val route = "Profile"
    override fun getLabel(context: Context) = context.getString(R.string.bottom_bar_label_profile)
    override val icon: @Composable () -> Painter = {
        painterResource(id = R.drawable.person_fill0_wght400_grad0_opsz24)
    }
    override val selectedIcon: @Composable () -> Painter = {
        painterResource(id = R.drawable.person_fill1_wght400_grad0_opsz24)
    }
}
