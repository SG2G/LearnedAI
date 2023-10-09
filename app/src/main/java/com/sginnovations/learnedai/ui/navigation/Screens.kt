package com.sginnovations.learnedai.ui.navigation

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