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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sginnovations.asked.Constants.Companion.START_DESTINATION
import com.sginnovations.asked.ui.chat.ChatStateFul
import com.sginnovations.asked.ui.crop.CropStateFul
import com.sginnovations.asked.ui.earn_points.EarnPoints
import com.sginnovations.asked.ui.gallery.GalleryStateFull
import com.sginnovations.asked.ui.main_bottom_bar.camera.CameraStateFul
import com.sginnovations.asked.ui.main_bottom_bar.historychats.StateFulHistoryChats
import com.sginnovations.asked.ui.main_bottom_bar.profile.StateFulProfile
import com.sginnovations.asked.ui.newconversation.NewConversationStateFul
import com.sginnovations.asked.ui.onboarding.onBoarding
import com.sginnovations.asked.ui.parental_guidance.StateFulParentalGuidance
import com.sginnovations.asked.ui.ref_code.ReferralCodeStateFul
import com.sginnovations.asked.ui.sign_in.LearnedAuth
import com.sginnovations.asked.ui.subscription.SubscriptionStateFull
import com.sginnovations.asked.ui.top_bottom_bar.bottombar.LearnedBottomBar
import com.sginnovations.asked.ui.top_bottom_bar.topbar.LearnedTopBar
import com.sginnovations.asked.utils.CheckIsPremium.checkIsPremium
import com.sginnovations.asked.viewmodel.AuthViewModel
import com.sginnovations.asked.viewmodel.BillingViewModel
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.IntentViewModel
import com.sginnovations.asked.viewmodel.NavigatorViewModel
import com.sginnovations.asked.viewmodel.PreferencesViewModel
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
    vmReferral: ReferralViewModel = hiltViewModel(),
    vmBilling: BillingViewModel = hiltViewModel(),
    vmIntent: IntentViewModel = hiltViewModel(),
    vmNavigator: NavigatorViewModel = hiltViewModel(),
    vmPreferences: PreferencesViewModel = hiltViewModel(),

    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val intent = remember { (context as Activity).intent }

    val firsTimeLaunch = vmPreferences.firstTimeLaunch

    LaunchedEffect(Unit) {
        if (vmAuth.userAuth.value != null) {
            // User its logged - set up
            vmNavigator.navigateAuthToCamera(navController)

            Log.i(TAG, "Calling SetUp")
            vmAuth.userJustLogged()
            vmToken.startTokenListener()
            vmReferral.handleDynamicLink(intent)
            vmBilling.connectToGooglePlay()

            checkIsPremium()
        }
    }

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen =
        when (navController.currentBackStackEntryAsState().value?.destination?.route) {
            Auth.route -> Auth

            Crop.route -> Crop

            ChatsHistory.route -> ChatsHistory
            ParentalGuidance.route -> ParentalGuidance
            Profile.route -> Profile

            NewConversation.route -> NewConversation
            Chat.route -> Chat

            Gallery.route -> Gallery
            Subscription.route -> Subscription
            RefCode.route -> RefCode
            else -> null
        }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            LearnedTopBar(
                vmTokens = vmToken,
                vmCamera = vmCamera,
                vmChat = vmChat,

                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,

                onNavigate = { navController.navigate(it.route) },
                navigateUp = { navController.navigateUp() },
            )
        },

        bottomBar = {
            if (!firsTimeLaunch.value) {
                LearnedBottomBar(
                    navController = navController,
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    backStackEntry = backStackEntry,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = START_DESTINATION,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Auth.route) {
                LearnedAuth(
                    vmAuth = vmAuth,
                ) {
                    scope.launch {
                        vmNavigator.navigateAuthToCamera(navController)
                        Log.i(TAG, "Calling SetUp when sign in")
                        vmAuth.userJustLogged()
                        vmToken.startTokenListener()
                        vmReferral.handleDynamicLink(intent)
                        vmBilling.connectToGooglePlay()

//                        if (!checkIsPremium()) {
//                            navController.navigate(route = Subscription.route)
//                        }
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

                if (firsTimeLaunch.value) {
                    Log.d(TAG, "LearnedNavigation: ${vmPreferences.firstTimeLaunch.value}")
                    onBoarding(
                        onFinish = { scope.launch { vmPreferences.setNotFirstTime() } },
                        onSkip = { scope.launch { vmPreferences.setNotFirstTime() } },
                    )
                } else {
                    CameraStateFul(
                        vmCamera = vmCamera,
                        vmToken = vmToken,

                        onGetPhotoGallery = { navController.navigate(route = Gallery.route) },
                        onCropNavigation = { navController.navigate(route = Crop.route) },
                    )
                    EarnPoints(vmToken, navController)
                }
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
                route = ParentalGuidance.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                StateFulParentalGuidance()
            }

            composable(
                route = Profile.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                StateFulProfile(
                    vmToken = vmToken,
                    vmAuth = vmAuth,
                    vmIntent = vmIntent,

                    onNavigateUserNotLogged = {
                        navController.popBackStack(route = Profile.route, true)
                        navController.navigate(route = Auth.route)
                    },
                    onNavigateRefCode = { navController.navigate(route = RefCode.route) },
                    onNavigateSubscriptions = { navController.navigate(route = Subscription.route) }
                )
                EarnPoints(vmToken, navController)
            }
            /**
             * Camera Crop
             */
            composable(route = Crop.route) {
                CropStateFul(
                    vmCamera = vmCamera,
                    vmChat = vmChat,
                    vmToken = vmToken,

                    navController = navController,

                    onNavigateChat = { scope.launch { vmNavigator.navigateChat(navController) } },
                    onNavigateNewChat = { scope.launch { vmNavigator.navigateNewChat(navController) } }
                )
            }
            /**
             * NewConversation
             */
            composable(route = NewConversation.route) {
                NewConversationStateFul(
                    vmChat = vmChat,
                    vmCamera = vmCamera,

                    onNavigateChat = { scope.launch { vmNavigator.navigateChat(navController) } }
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
                    vmCamera = vmCamera,
                    vmChat = vmChat,
                    vmToken = vmToken,
                    vmAuth = vmAuth,
                )
                EarnPoints(vmToken, navController)
            }
            composable(route = RefCode.route) {
                ReferralCodeStateFul(
                    vmAuth = vmAuth
                )
            }

            composable(route = Subscription.route) {
                SubscriptionStateFull(
                    vmBilling = vmBilling,
                    onNavigateUp = { navController.navigateUp() }
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