package com.sginnovations.asked

import android.app.Activity
import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sginnovations.asked.ui.chat.ChatStateFul
import com.sginnovations.asked.ui.crop.CropStateFul
import com.sginnovations.asked.ui.earn_points.EarnPoints
import com.sginnovations.asked.ui.gallery.GalleryStateFull
import com.sginnovations.asked.ui.main_bottom_bar.camera.CameraStateFul
import com.sginnovations.asked.ui.main_bottom_bar.historychats.StateFulHistoryChats
import com.sginnovations.asked.ui.main_bottom_bar.profile.StateFulProfile
import com.sginnovations.asked.ui.newconversation.NewConversationStateFul
import com.sginnovations.asked.ui.ref_code.ReferralCodeStateFul
import com.sginnovations.asked.ui.sign_in.LearnedAuth
import com.sginnovations.asked.ui.subscription.SubscriptionStateFull
import com.sginnovations.asked.ui.top_bottom_bar.Auth
import com.sginnovations.asked.ui.top_bottom_bar.Camera
import com.sginnovations.asked.ui.top_bottom_bar.Chat
import com.sginnovations.asked.ui.top_bottom_bar.ChatsHistory
import com.sginnovations.asked.ui.top_bottom_bar.Crop
import com.sginnovations.asked.ui.top_bottom_bar.Gallery
import com.sginnovations.asked.ui.top_bottom_bar.NewConversation
import com.sginnovations.asked.ui.top_bottom_bar.Points
import com.sginnovations.asked.ui.top_bottom_bar.Profile
import com.sginnovations.asked.ui.top_bottom_bar.RefCode
import com.sginnovations.asked.ui.top_bottom_bar.Subscription
import com.sginnovations.asked.ui.top_bottom_bar.bottombar.LearnedBottomBar
import com.sginnovations.asked.ui.top_bottom_bar.topbar.LearnedTopBar
import com.sginnovations.asked.viewmodel.AdsViewModel
import com.sginnovations.asked.viewmodel.AuthViewModel
import com.sginnovations.asked.viewmodel.BillingViewModel
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.ReferralViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel
import kotlinx.coroutines.launch

private const val TAG = "LearnedNavigation"

@Composable
fun LearnedNavigation(
    vmChat: ChatViewModel = hiltViewModel(),
    vmCamera: CameraViewModel = hiltViewModel(),
    vmAuth: AuthViewModel = hiltViewModel(),
    vmToken: TokenViewModel = hiltViewModel(),
    vmAds: AdsViewModel = hiltViewModel(),
    vmReferral: ReferralViewModel = hiltViewModel(),
    vmBilling: BillingViewModel = hiltViewModel(),

    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val state by vmAuth.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    val intent = remember { (context as Activity).intent }
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen =
        when (navController.currentBackStackEntryAsState().value?.destination?.route) {
            Auth.getName(context) -> Auth

            Crop.getName(context) -> Crop
            ChatsHistory.getName(context) -> ChatsHistory
            Profile.getName(context) -> Profile

            Chat.getName(context) -> Chat
            Points.getName(context) -> Points
            else -> null
        }
    val currentScreenTitle = currentScreen?.route ?: ""

    LaunchedEffect(Unit) {
        if (vmAuth.userAuth.value != null) {
            // User its logged - set up
            navController.popBackStack(navController.graph.startDestinationId, true)
            navController.navigate(route = Camera.route)

            Log.i(TAG, "Calling SetUp")
            vmAuth.userJustLogged()
            vmToken.startTokenListener()
            vmReferral.handleDynamicLink(intent)
            vmAds.loadInterstitialAd(context)

        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            LearnedTopBar(
                vmTokens = vmToken,
                vmChat = vmChat,

                currentScreenTitle = currentScreenTitle,
                canNavigateBack = navController.previousBackStackEntry != null,

                onNavigate = { navController.navigate(it.route) },
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

                    ) {
                    navController.popBackStack(navController.graph.startDestinationId, true)
                    navController.navigate(route = Camera.route)

                    scope.launch {
                        Log.i(TAG, "Calling SetUp")
                        vmAuth.userJustLogged()
                        vmToken.startTokenListener()
                        vmReferral.handleDynamicLink(intent)
                        vmAds.loadInterstitialAd(context)

                    }
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
                    navController = navController,

                    vmCamera = vmCamera,
                    vmToken = vmToken,

                    onCropNavigation = { navController.navigate(route = Crop.route) },
                    onNavigateNewConversation  = {
                        navController.navigate(ChatsHistory.route) { //TODO NAVIGATOR CLASS NOW
                            // This ensures that the previous screen is removed from the backstack
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                        navController.navigate(NewConversation.route)
                    }
                )
                EarnPoints(vmToken,vmAds,navController)
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
                    vmAuth = vmAuth,

                    onNavigateUserNotLogged = {
                        navController.popBackStack(Profile.route, true)
                        navController.navigate(route = Auth.route)
                    },
                    onNavigateRefCode = { navController.navigate(route = RefCode.route) },
                    onNavigateSubscriptions = { navController.navigate(route = Subscription.route) }
                )
                EarnPoints(vmToken,vmAds,navController)
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
                        // This is where you handle navigation
                        navController.navigate(ChatsHistory.route) {
                            // This ensures that the previous screen is removed from the backstack
                            popUpTo(navController.currentDestination?.route ?: "") {
                                inclusive = true
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
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
                    vmAuth = vmAuth,
                )
                EarnPoints(vmToken,vmAds,navController)
            }
            composable(route = RefCode.route) {
                ReferralCodeStateFul(
                    vmAuth = vmAuth
                )
            }

            composable(route = Subscription.route) {
                SubscriptionStateFull(
                    vmBilling = vmBilling,
                )
            }
            composable(route = Gallery.route) {
                GalleryStateFull(
                    vmCamera = vmCamera,
                    onCropNavigation = { navController.navigate(route = Crop.route) }
                )
            }
        }
    }
}