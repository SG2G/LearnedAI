package com.sginnovations.learnedai

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sginnovations.learnedai.ui.camera.CameraStateFul
import com.sginnovations.learnedai.ui.camera.crop.CropStateFul
import com.sginnovations.learnedai.ui.chat.StateFulChat
import com.sginnovations.learnedai.ui.historychats.StateFulHistoryChats
import com.sginnovations.learnedai.ui.navigation.Chat
import com.sginnovations.learnedai.ui.navigation.Crop
import com.sginnovations.learnedai.ui.navigation.NewConversation
import com.sginnovations.learnedai.ui.navigation.bottombar.Camera
import com.sginnovations.learnedai.ui.navigation.bottombar.Chats
import com.sginnovations.learnedai.ui.navigation.bottombar.LearnedBottomBar
import com.sginnovations.learnedai.ui.navigation.bottombar.Profile
import com.sginnovations.learnedai.ui.navigation.topbar.LearnedTopBar
import com.sginnovations.learnedai.ui.newconversation.NewConversationStateFul
import com.sginnovations.learnedai.viewmodel.CameraViewModel
import com.sginnovations.learnedai.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnedNavigation(
    vmChat: ChatViewModel = hiltViewModel(),
    vmCamera: CameraViewModel = hiltViewModel(),


    navController: NavHostController = rememberNavController(),
) {

    val context = LocalContext.current
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen =
        when (navController.currentBackStackEntryAsState().value?.destination?.route) {
            NewConversation.route -> NewConversation
            Chat.route -> Chat
            else -> null
        }
    val currentScreenTitle = currentScreen?.getName(context) ?: ""

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            LearnedTopBar(
                currentScreenTitle = currentScreenTitle,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        },

        bottomBar = {
            LearnedBottomBar(
                navController = navController,
                canNavigateBack = navController.previousBackStackEntry != null,
                backStackEntry = backStackEntry
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Camera.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            /**
             *  Bottom Bar Destinations
             */
            composable(route = Camera.route) {
                CameraStateFul(
                    vmCamera = vmCamera,

                    onCropNavigation = { navController.navigate(route = Crop.route) }
                )
            }
            composable(route = Chats.route) {
                StateFulHistoryChats(
                    vmChat = vmChat,

                    onNavigateMessages = { navController.navigate(route = "Chat") },
                    onNavigateNewConversation = {
                        navController.navigate(route = NewConversation.route)
                    }
                )
            }
            composable(route = Profile.route) {

            }
            /**
             * Crop
             */
            composable(route = Crop.route) {
                CropStateFul(
                    vmCamera = vmCamera,

                    navController = navController,
                )
            }
            /**
             * NewConversation
             */
            composable(route = NewConversation.route) {
                NewConversationStateFul(
                    vmChat = vmChat,
                    vmCamera = vmCamera,

                    onNavigateChat = { navController.navigate(route = Chat.route) }
                )
            }
            /**
             * Chat
             */
            composable(route = Chat.route) {
                StateFulChat(
                    vmChat = vmChat
                )
            }
        }
    }
}