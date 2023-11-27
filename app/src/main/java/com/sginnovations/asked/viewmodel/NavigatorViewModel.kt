package com.sginnovations.asked.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.sginnovations.asked.Camera
import com.sginnovations.asked.Chat
import com.sginnovations.asked.ChatsHistory
import com.sginnovations.asked.NewConversation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "NavigatorViewModel"
@HiltViewModel
class NavigatorViewModel @Inject constructor(): ViewModel() {

    fun navigateChat(navController: NavController) {
        Log.i(TAG, "Navigating navigateChat")

        navController.navigate(ChatsHistory.route) {
            // This ensures that the previous screen is removed from the backstack
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
        navController.navigate(Chat.route)
    }
    fun navigateNewChat(navController: NavController) {
        if (navController.currentDestination?.route != NewConversation.route) {
            Log.i(TAG, "Navigating navigateNewChat")

            navController.navigate(ChatsHistory.route) {
                // This ensures that the previous screen is removed from the backstack
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
            navController.navigate(NewConversation.route)
        }
    }
    fun navigateAuthToCamera(navController: NavController) {
        try {
            navController.popBackStack(navController.graph.startDestinationId, true)
            navController.navigate(route = Camera.route)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}