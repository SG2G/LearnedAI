package com.sginnovations.learnedai.ui.navigation.bottombar

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.sginnovations.learnedai.R

interface BottomBarDestinations {
    val route: String
    fun getLabel(context: Context): String
    val icon: ImageVector // val icon: @Composable () -> Painter
}

object Home : BottomBarDestinations {
    override val route = "Home"
    override fun getLabel(context: Context) = context.getString(R.string.bottom_bar_label_home)
    override val icon = Icons.Default.Home
}

object Search : BottomBarDestinations {
    override val route = "search"
    override fun getLabel(context: Context) = context.getString(R.string.bottom_bar_label_search)
    override val icon = Icons.Default.Search
}

object Profile : BottomBarDestinations {
    override val route = "profile"
    override fun getLabel(context: Context) = context.getString(R.string.bottom_bar_label_profile)
    override val icon = Icons.Default.Person
}
