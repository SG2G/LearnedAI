package com.sginnovations.asked

import android.app.Activity
import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import com.sginnovations.asked.ui.main_bottom_bar.parental_chat.AssistantChatStateFul
import com.sginnovations.asked.ui.main_bottom_bar.parental_chat.AssistantNewConversationStateFul
import com.sginnovations.asked.ui.main_bottom_bar.parental_chat.ParentalAssistantStateFul
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.CategoryLessonsStateFul
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.LessonStateFul
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.ParentalGuidanceStateFul
import com.sginnovations.asked.ui.main_bottom_bar.parental_guidance.components.TranscriptStateFul
import com.sginnovations.asked.ui.main_bottom_bar.profile.StateFulProfile
import com.sginnovations.asked.ui.newconversation.NewConversationStateFul
import com.sginnovations.asked.ui.onboarding.onBoarding
import com.sginnovations.asked.ui.ref_code.ReferralCodeStateFul
import com.sginnovations.asked.ui.settings.SettingsStateFul
import com.sginnovations.asked.ui.sign_in.LearnedAuth
import com.sginnovations.asked.ui.subscription.SubscriptionStateFull
import com.sginnovations.asked.ui.top_bottom_bar.bottombar.LearnedBottomBar
import com.sginnovations.asked.ui.top_bottom_bar.topbar.LearnedTopBar
import com.sginnovations.asked.utils.CheckIsPremium.checkIsPremium
import com.sginnovations.asked.viewmodel.AssistantViewModel
import com.sginnovations.asked.viewmodel.AuthViewModel
import com.sginnovations.asked.viewmodel.BillingViewModel
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.ChatViewModel
import com.sginnovations.asked.viewmodel.IntentViewModel
import com.sginnovations.asked.viewmodel.LessonViewModel
import com.sginnovations.asked.viewmodel.NavigatorViewModel
import com.sginnovations.asked.viewmodel.PreferencesViewModel
import com.sginnovations.asked.viewmodel.ReferralViewModel
import com.sginnovations.asked.viewmodel.ReportViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "LearnedNavigation"

/**
 * Animations
 */
val enterTransitionHorizontalSlide = slideInHorizontally(
    initialOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(
        durationMillis = 300,
        easing = LinearEasing
    )
)
val exitTransitionHorizontalSlide = slideOutHorizontally(
    targetOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(
        durationMillis = 300,
        easing = LinearEasing
    )
)
val enterTransitionVerticalSlide = slideInVertically(
    initialOffsetY = { fullHeight -> fullHeight },
    animationSpec = tween(durationMillis = 300)
)
val exitTransitionVerticalSlide = slideOutVertically(
    targetOffsetY = { fullHeight -> -fullHeight },
    animationSpec = tween(durationMillis = 300)
)

@Composable
fun LearnedNavigation(
    vmPreferences: PreferencesViewModel,

    vmChat: ChatViewModel = hiltViewModel(),
    vmCamera: CameraViewModel = hiltViewModel(),
    vmAuth: AuthViewModel = hiltViewModel(),
    vmToken: TokenViewModel = hiltViewModel(),
    vmReferral: ReferralViewModel = hiltViewModel(),
    vmBilling: BillingViewModel = hiltViewModel(),
    vmIntent: IntentViewModel = hiltViewModel(),
    vmNavigator: NavigatorViewModel = hiltViewModel(),
    vmLesson: LessonViewModel = hiltViewModel(),
    vmAssistant: AssistantViewModel = hiltViewModel(),
    vmReport: ReportViewModel = hiltViewModel(),

    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val intent = remember { (context as Activity).intent }

    val firstBottomScreen = ParentalAssist
    val firsTimeLaunch = vmPreferences.firstTimeLaunch

    LaunchedEffect(Unit) {
        if (vmAuth.userAuth.value != null) {
            // User its logged - set up
            vmNavigator.navigateAuthToX(navController, firstBottomScreen)

            Log.i(TAG, "Calling SetUp")
            vmAuth.userJustLogged()
            vmToken.startTokenListener()
            vmReferral.handleDynamicLink(intent)
            vmBilling.connectToGooglePlay()

            checkIsPremium()

            vmToken.ensureMinimumTokensUseCaseCheckPremium()
        }
    }

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen =
        when (navController.currentBackStackEntryAsState().value?.destination?.route) {
            Auth.route -> Auth

            Crop.route -> Crop
            OnBoarding.route -> OnBoarding

            ChatsHistory.route -> ChatsHistory
            ParentalAssist.route -> ParentalAssist
            ParentalGuidance.route -> ParentalGuidance
            Profile.route -> Profile

            NewConversation.route -> NewConversation
            AssistantNewConversation.route -> AssistantNewConversation
            Chat.route -> Chat
            AssistantChat.route -> AssistantChat

            Gallery.route -> Gallery
            Subscription.route -> Subscription
            RefCode.route -> RefCode
            Settings.route -> Settings

            Lesson.route -> Lesson
            Transcript.route -> Transcript

            CategoryLesson.route -> CategoryLesson

            else -> null
        }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            LearnedTopBar(
                vmTokens = vmToken,
                vmCamera = vmCamera,
                vmChat = vmChat,
                vmLesson = vmLesson,

                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,

                onNavigate = { navController.navigate(it.route) },
                navigateUp = { navController.navigateUp() },
            )
        },

        bottomBar = {
            LearnedBottomBar(
                navController = navController,
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                backStackEntry = backStackEntry,
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
                ) {
                    scope.launch {
                        // IMPORTANT
                        if (firsTimeLaunch.value) {
                            vmNavigator.navigateAuthToX(
                                navController,
                                OnBoarding
                            )
                        } else {
                            vmNavigator.navigateAuthToX(
                                navController,
                                firstBottomScreen
                            )
                        }

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
                CameraStateFul(
                    vmCamera = vmCamera,
                    vmToken = vmToken,

                    onNavigateSubscriptions = { navController.navigate(route = Subscription.route) },

                    onGetPhotoGallery = { navController.navigate(route = Gallery.route) },
                    onCropNavigation = { navController.navigate(route = Crop.route) },
                )
                EarnPoints(vmToken, navController)
            }

            composable(
                route = ChatsHistory.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                StateFulHistoryChats(
                    vmChat = vmChat,
                    vmPreferences = vmPreferences,

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
                ParentalGuidanceStateFul(
                    vmLesson = vmLesson,

                    onNavigateCategoryLessons = { navController.navigate(route = CategoryLesson.route) }
                )
            }
            composable(
                route = ParentalAssist.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                /**
                 * ONBOARDING
                 */
                ParentalAssistantStateFul(
                    vmAssistant = vmAssistant,
                    vmPreferences = vmPreferences,

                    onNavigateNewMessage = { navController.navigate(route = AssistantNewConversation.route) },
                    onNavigateMessages = { navController.navigate(route = AssistantChat.route) },
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
             * Category Lessons
             */
            composable(
                route = CategoryLesson.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                CategoryLessonsStateFul(
                    vmLesson = vmLesson,
                    vmPreferences = vmPreferences,

                    onNavigateLesson = { navController.navigate(route = Lesson.route) },
                    onNavigateSubscriptionScreen = { navController.navigate(route = Subscription.route) }
                )
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

                    onNavigateSubscriptionScreen = { navController.navigate(route = Subscription.route) },

                    onNavigateChat = { scope.launch { vmNavigator.navigateChat(navController) } },
                    onNavigateNewChat = { scope.launch { vmNavigator.navigateNewChat(navController) } }
                )
            }
            composable(
                route = OnBoarding.route
            ) {
                suspend fun endOnBoarding() {
                    vmPreferences.setNotFirstTime()
                    vmNavigator.navigateAuthToX(navController, firstBottomScreen)
                }
                Log.d(TAG, "LearnedNavigation: ${vmPreferences.firstTimeLaunch.value}")
                onBoarding(
                    onFinish = { scope.launch { endOnBoarding() } },
                    onSkip = { scope.launch { endOnBoarding() } },
                )

            }
            /**
             * NewConversation
             */
            composable(
                route = NewConversation.route,
            ) {
                NewConversationStateFul(
                    vmChat = vmChat,
                    vmCamera = vmCamera,
                    vmToken = vmToken,

                    onNavigateChat = { scope.launch { vmNavigator.navigateChat(navController) } },
                    onNavigateSubscriptionScreen = { navController.navigate(route = Subscription.route) }
                )
            }
            /**
             * AssistantNewConversation
             */
            composable(
                route = AssistantNewConversation.route,
            ) {
                AssistantNewConversationStateFul(
                    vmAssistant = vmAssistant,
                    vmToken = vmToken,

                    onNavigateChat = {
                        scope.launch {
                            vmNavigator.navigateAssistantChat(navController)
                        }
                    },
                    onNavigateSubscriptionScreen = { navController.navigate(route = Subscription.route) }
                )
                EarnPoints(vmToken, navController)
            }
            /**
             * AssistantChat
             */
            composable(
                route = AssistantChat.route,
                enterTransition = { enterTransitionHorizontalSlide },
                exitTransition = { exitTransitionHorizontalSlide },
            ) {
                AssistantChatStateFul(
                    vmAssistant = vmAssistant,
                    vmToken = vmToken,
                    vmAuth = vmAuth,
                    vmReport = vmReport,

                    onNavigateSubscriptionScreen = { navController.navigate(route = Subscription.route) }
                )
                EarnPoints(vmToken, navController)
            }

            /**
             * Chat
             */
            composable(
                route = Chat.route,
                enterTransition = { enterTransitionHorizontalSlide },
                exitTransition = { exitTransitionHorizontalSlide },
            ) {
                ChatStateFul(
                    vmCamera = vmCamera,
                    vmChat = vmChat,
                    vmToken = vmToken,
                    vmAuth = vmAuth,
                    vmReport = vmReport,

                    onNavigateSubscriptionScreen = { navController.navigate(route = Subscription.route) }
                )
                EarnPoints(vmToken, navController)
            }

            composable(
                route = Lesson.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                LessonStateFul(
                    vmLesson = vmLesson,
                    vmAssistant = vmAssistant,
                    vmIntent = vmIntent,
                    vmPreferences = vmPreferences,

                    onOpenTranscript = { navController.navigate(route = Transcript.route) },

                    onNavigateAssistant = {
                        scope.launch {
                            vmNavigator.navigateAssistantNewChat(
                                navController
                            )
                        }
                    },
                    onNavigateBack = { navController.navigateUp() }
                )
            }
            composable(
                route = Transcript.route,
                enterTransition = { enterTransitionVerticalSlide },
                exitTransition = { exitTransitionVerticalSlide },
            ) {
                TranscriptStateFul(
                    vmLesson = vmLesson,
                )
            }

            composable(route = RefCode.route) {
                ReferralCodeStateFul(
                    vmAuth = vmAuth
                )
            }

            composable(route = Subscription.route) {
                SubscriptionStateFull(
                    vmBilling = vmBilling,
                    vmIntent = vmIntent,
                    vmAuth = vmAuth,

                    onNavigateUp = { navController.navigateUp() }
                )
            }
            composable(route = Gallery.route) {
                GalleryStateFull(
                    vmCamera = vmCamera,
                    vmToken = vmToken,

                    onNavigateSubscriptionScreen = { navController.navigate(route = Subscription.route) },
                    onCropNavigation = { navController.navigate(route = Crop.route) }
                )
            }
            composable(route = Settings.route) {
                SettingsStateFul(
                    vmPreferences = vmPreferences
                )
            }
        }
    }
}