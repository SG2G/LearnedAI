package com.sginnovations.learnedai

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sginnovations.learnedai.presentation.sign_in.GoogleAuthUiClient
import com.sginnovations.learnedai.ui.camera.CameraStateFul
import com.sginnovations.learnedai.ui.chat.ChatStateFul
import com.sginnovations.learnedai.ui.crop.CropStateFul
import com.sginnovations.learnedai.ui.historychats.StateFulHistoryChats
import com.sginnovations.learnedai.ui.navigation_bars.Auth
import com.sginnovations.learnedai.ui.navigation_bars.Camera
import com.sginnovations.learnedai.ui.navigation_bars.Chat
import com.sginnovations.learnedai.ui.navigation_bars.ChatsHistory
import com.sginnovations.learnedai.ui.navigation_bars.Crop
import com.sginnovations.learnedai.ui.navigation_bars.NewConversation
import com.sginnovations.learnedai.ui.navigation_bars.Points
import com.sginnovations.learnedai.ui.navigation_bars.Profile
import com.sginnovations.learnedai.ui.navigation_bars.bottombar.LearnedBottomBar
import com.sginnovations.learnedai.ui.navigation_bars.topbar.LearnedTopBar
import com.sginnovations.learnedai.ui.newconversation.NewConversationStateFul
import com.sginnovations.learnedai.ui.points.PointsStateFul
import com.sginnovations.learnedai.ui.profile.StateFulProfile
import com.sginnovations.learnedai.ui.sign_in.LearnedAuth
import com.sginnovations.learnedai.viewmodel.AdsViewModel
import com.sginnovations.learnedai.viewmodel.AuthViewModel
import com.sginnovations.learnedai.viewmodel.CameraViewModel
import com.sginnovations.learnedai.viewmodel.ChatViewModel
import com.sginnovations.learnedai.viewmodel.TokenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnedNavigation(
    googleAuthUiClient: GoogleAuthUiClient,

    vmChat: ChatViewModel = hiltViewModel(),
    vmCamera: CameraViewModel = hiltViewModel(),
    vmAuth: AuthViewModel = hiltViewModel(),
    vmToken: TokenViewModel = hiltViewModel(),
    vmAds: AdsViewModel = hiltViewModel(),

    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val state by vmAuth.state.collectAsStateWithLifecycle()

    //TODO NAVIGATOR CLASS
    fun navigateUserLogged() {
        navController.popBackStack(navController.graph.startDestinationId, true)
        navController.navigate(route = Camera.route)
    }

    fun navigateUserNotLogged() {
        navController.popBackStack(Profile.route, true)
        navController.navigate(route = Auth.route)

    }

    LaunchedEffect(Unit) {
        if (googleAuthUiClient.getSignedInUser() != null) {
            navigateUserLogged()
        }
        // Load and ad
        vmAds.loadInterstitialAd(context) //TODO SET MSG IF AD NOT LOADED
    }

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen =
        when (navController.currentBackStackEntryAsState().value?.destination?.route) {
            Auth.getName(context) -> Auth

            Camera.getName(context) -> Camera
            ChatsHistory.getName(context) -> ChatsHistory
            Profile.getName(context) -> Profile

            Chat.getName(context) -> Chat
            Points.getName(context) -> Points
            else -> null
        }
    val currentScreenTitle = currentScreen?.route ?: ""

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            LearnedTopBar(
                vmTokens = vmToken,
                currentScreenTitle = currentScreenTitle,
                canNavigateBack = navController.previousBackStackEntry != null,

                onNavigatePoints = { navController.navigate(Points.route) },
                navigateUp = { navController.navigateUp() },
            )
        },

        bottomBar = {
            LearnedBottomBar(
                navController = navController,
                currentScreenTitle = currentScreenTitle,
                canNavigateBack = navController.previousBackStackEntry != null,
                backStackEntry = backStackEntry
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Auth.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Auth.route) {
                LearnedAuth(
                    vmAuth = vmAuth,
                    state = state,

                    googleAuthUiClient = googleAuthUiClient,
                ) {
                    navigateUserLogged()
                }
            }

            /**
             *  Bottom Bar Destinations
             */
            composable(
                route = Camera.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                CameraStateFul(
                    vmCamera = vmCamera,

                    onCropNavigation = { navController.navigate(route = Crop.route) }
                )
            }
            composable(
                route = ChatsHistory.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                StateFulHistoryChats(
                    vmChat = vmChat,

                    onNavigateMessages = { navController.navigate(route = Chat.route) },
                    onNavigateNewConversation = {
                        navController.navigate(route = NewConversation.route)
                    }
                )
            }
            composable(
                route = Profile.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                StateFulProfile(
                    vmToken = vmToken,

                    googleAuthUiClient = googleAuthUiClient

                ) {
                    navigateUserNotLogged()
                }
            }
            /**
             * Camera Crop
             */
            composable(route = Crop.route) {
                CropStateFul(
                    vmCamera = vmCamera,
                    vmChat = vmChat,

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
                    vmAds = vmAds,

                    onNavigateChat = {
                        navController.popBackStack(navController.graph.startDestinationId, true)
                        navController.navigate(ChatsHistory.route) {
                            popUpTo(ChatsHistory.route) { inclusive = true }
                            launchSingleTop = true
                        }
                        navController.navigate(Chat.route)
                    }
                )
            }
            /**
             * Chat
             */
            composable(
                route = Chat.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    )
                },
            ) {
                ChatStateFul(
                    vmChat = vmChat,
                    vmToken = vmToken,

                    googleAuthUiClient = googleAuthUiClient
                )
            }
            /**
             * Get more Points
             */
            composable(route = Points.route) {
                PointsStateFul(
                    vmToken = vmToken,
                    vmAds = vmAds,
                )
            }
        }
    }
}