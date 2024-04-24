package com.sginnovations.asked.presentation.ui.sign_in

import android.app.Activity.RESULT_OK
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import com.sginnovations.asked.R
import com.sginnovations.asked.auth.sign_in.data.SignInState
import com.sginnovations.asked.presentation.ui.crop.components.IsLoadingCrop
import com.sginnovations.asked.presentation.ui.ui_components.sign_in.GoogleSignInButton
import com.sginnovations.asked.presentation.viewmodel.AuthViewModel
import com.sginnovations.asked.utils.test_tags.TestTags.EMAIL_TEXT_FIELD
import com.sginnovations.asked.utils.test_tags.TestTags.PASSWORD_TEXT_FIELD
import com.sginnovations.asked.utils.test_tags.TestTags.SIGN_IN_BUTTON
import kotlinx.coroutines.launch

private const val TAG = "AskedSignIn"

@Composable
fun LearnedAuth(
    vmAuth: AuthViewModel,

    onNavigationUserAlreadySigned: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val state by vmAuth.state.collectAsStateWithLifecycle()

    val auth = remember { mutableStateOf(FirebaseAuth.getInstance()) }
    val userName = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }
    val isLoading = vmAuth.loadingAuth

    val googleAuthUiClient = vmAuth.getGoogleAuthUiClient()

    LaunchedEffect(Unit) { vmAuth.getAuth()}
    LaunchedEffect(state.isSignInSuccessful) {
        Log.d(TAG, "LearnedAuth: isSignInSuccessful")
        if (state.isSignInSuccessful) {
            onNavigationUserAlreadySigned()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                scope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    Log.d(TAG, "LearnedAuth: launcher")
                    vmAuth.onSignInResult(signInResult)
                }
            } else {
                vmAuth.loadingAuth.value = false
            }
        }
    )

    Log.d(TAG, "showSignInScreen: ${vmAuth.showSignInScreen}")
    if (vmAuth.showSignInScreen.value) {
        LearnedAuthStateLess(
            state = state,
            context = context,

            userName = userName,
            userPassword = userPassword,

            onManualSignIn = {
                try {
                    auth.value.signInWithEmailAndPassword(userName.value, userPassword.value)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "LearnedAuth: task.isSuccessful")
                                val user = auth.value.currentUser
                                user?.let { vmAuth.setDefaultTokens(it) }
                                onNavigationUserAlreadySigned()
                            } else {
                                // sign in failed
                            }
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        ) { // onSignInClick =
            scope.launch {
                vmAuth.loadingAuth.value = true
                val signInIntentSender = googleAuthUiClient.signIn()
                launcher.launch(
                    IntentSenderRequest.Builder(
                        signInIntentSender ?: return@launch
                    ).build()
                )
            }
        }
    }

    if (isLoading.value) IsLoadingCrop()

}

@Composable
fun LearnedAuthStateLess(
    state: SignInState,
    context: Context,

    userName: MutableState<String>,
    userPassword: MutableState<String>,

    onManualSignIn: () -> Unit,

    onSignInClick: () -> Unit,
) {
    val showAdminSignIn = remember { mutableStateOf(false) }

    LaunchedEffect(state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.sign_in_title_sign_in),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp),
            painter = painterResource(id = R.drawable.asked30),
            contentDescription = "asked_logo",
        )
        /**
         * TEST MANUAL SIGN IN
         */
        if (showAdminSignIn.value) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = userName.value,
                    onValueChange = { userName.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag(EMAIL_TEXT_FIELD),
                    colors = TextFieldDefaults.colors(),
                    placeholder = { Text(text = "Email") }
                )
                TextField(
                    value = userPassword.value,
                    onValueChange = { userPassword.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag(PASSWORD_TEXT_FIELD),
                    colors = TextFieldDefaults.colors(),
                    placeholder = { Text(text = "Password") }
                )
                Button(
                    onClick = {
                        if (userName.value.isNotEmpty() && userPassword.value.isNotEmpty()) {
                            onManualSignIn()
                        }
                    },
                    shape = RoundedCornerShape(40),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .testTag(SIGN_IN_BUTTON),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = "Admin Sign In",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        /**
         * Sign in Image
         */
        Image(
            painter = painterResource(id = R.drawable.sign_in),
            contentDescription = "sign_in",
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            showAdminSignIn.value = !showAdminSignIn.value
                        }
                    )
                }
        )
        /**
         * Component
         * Google Sign in Button
         */
        GoogleSignInButton(
            onClick = { onSignInClick() }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}