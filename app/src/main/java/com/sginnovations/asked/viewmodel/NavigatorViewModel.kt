package com.sginnovations.asked.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.sginnovations.asked.AssistantChat
import com.sginnovations.asked.Camera
import com.sginnovations.asked.Chat
import com.sginnovations.asked.ChatsHistory
import com.sginnovations.asked.NewConversation
import com.sginnovations.asked.ParentalAssist
import com.sginnovations.asked.ScreensDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "NavigatorViewModel"
@HiltViewModel
class NavigatorViewModel @Inject constructor(): ViewModel() {

    suspend fun navigateChat(navController: NavController) {
        Log.i(TAG, "Navigating navigateChat")

        withContext(Dispatchers.Main) {
            navController.navigate(ChatsHistory.route) {
                // This ensures that the previous screen is removed from the backstack
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
            navController.navigate(Chat.route)
        }
    }
    suspend fun navigateAssistantChat(navController: NavController) {
        Log.i(TAG, "Navigating navigateChat")

        withContext(Dispatchers.Main) {
            navController.navigate(ParentalAssist.route) {
                // This ensures that the previous screen is removed from the backstack
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
            navController.navigate(AssistantChat.route)
        }
    }
    suspend fun navigateNewChat(navController: NavController) {
        withContext(Dispatchers.Main) {
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
    }
    suspend fun navigateAuthToX(navController: NavController, screen: ScreensDestinations) {
        withContext(Dispatchers.Main) {
            try {
                navController.navigate(screen.route) {
                    // This ensures that the previous screen is removed from the backstack
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}