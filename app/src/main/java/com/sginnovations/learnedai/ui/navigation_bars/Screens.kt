package com.sginnovations.learnedai.ui.navigation_bars

import android.content.Context

interface Screens {
    val route: String
    fun getName(context: Context): String
}

object Chat: Screens {
    override val route = "Chat"
    override fun getName(context: Context) = "Chat"
}

object NewConversation : Screens {
    override val route = "NewConversation"
    override fun getName(context: Context) = "New Chat"
}
object Crop : Screens {
    override val route = "Crop"
    override fun getName(context: Context) = "Crop"
}

object Auth : Screens {
    override val route = "Auth"
    override fun getName(context: Context) = "Auth"
}
object Points : Screens {
    override val route = "Points"
    override fun getName(context: Context) = "Points"
}