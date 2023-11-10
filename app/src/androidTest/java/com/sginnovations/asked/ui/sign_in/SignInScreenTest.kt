package com.sginnovations.asked.ui.sign_in

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sginnovations.asked.Auth
import com.sginnovations.asked.Camera
import com.sginnovations.asked.MainActivity
import com.sginnovations.asked.ui.main_bottom_bar.camera.CameraStateFul
import com.sginnovations.asked.ui.theme.LearnedAITheme
import com.sginnovations.asked.utils.test_tags.TestTags
import com.sginnovations.asked.viewmodel.AuthViewModel
import com.sginnovations.asked.viewmodel.CameraViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SignInScreenTest {

    //@get:Rule(order = 0)
    //val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        //hiltRule.inject()
        composeRule.activity.setContent {
            val vmAuth: AuthViewModel = hiltViewModel()
            val vmCamera: CameraViewModel = hiltViewModel()
            val vmToken: TokenViewModel = hiltViewModel()

            val navController = rememberNavController()
            LearnedAITheme {
                NavHost(
                    navController = navController,
                    startDestination = Auth.route
                ) {
                    composable(route = Auth.route) {
                        LearnedAuth(
                            vmAuth = vmAuth,
                        ) {
                            navController.popBackStack(navController.graph.startDestinationId, true)
                            navController.navigate(route = Camera.route)
                        }
                    }
                    composable(route = Camera.route) {
                        CameraStateFul(
                            vmCamera = vmCamera,
                            vmToken = vmToken,

                            onGetPhotoGallery = {},
                            onCropNavigation = {}
                        )
                    }
                }
            }
        }
    }

    @Test
    fun testSignIn() {
        val emailField = composeRule.onNodeWithTag(TestTags.EMAIL_TEXT_FIELD)
        val passwordField = composeRule.onNodeWithTag(TestTags.PASSWORD_TEXT_FIELD)
        val signInButton = composeRule.onNodeWithTag(TestTags.SIGN_IN_BUTTON)

        emailField.performTextInput("apptestingbysg@gmail.com")
        passwordField.performTextInput("123testing")
        signInButton.performClick()

    }

}