package com.sginnovations.asked.ui.sign_in

import android.app.Activity.RESULT_OK
import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sginnovations.asked.R
import com.sginnovations.asked.auth.sign_in.data.SignInState
import com.sginnovations.asked.ui.ui_components.sign_in.GoogleSignInButton
import com.sginnovations.asked.utils.test_tags.TestTags
import com.sginnovations.asked.utils.test_tags.TestTags.EMAIL_TEXT_FIELD
import com.sginnovations.asked.utils.test_tags.TestTags.PASSWORD_TEXT_FIELD
import com.sginnovations.asked.utils.test_tags.TestTags.SIGN_IN_BUTTON
import com.sginnovations.asked.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LearnedAuth(
    vmAuth: AuthViewModel,

    onNavigationUserAlreadySigned: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val state by vmAuth.state.collectAsStateWithLifecycle()

    val auth: FirebaseAuth = Firebase.auth
    val userName = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }

    val googleAuthUiClient = vmAuth.getGoogleAuthUiClient()

    LaunchedEffect(state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()

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
                    vmAuth.onSignInResult(signInResult)
                }
            }
        }
    )

    LearnedAuthStateLess(
        state = state,
        context = context,

        userName = userName,
        userPassword = userPassword,

        onManualSignIn = {
            auth.signInWithEmailAndPassword(userName.value, userPassword.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.let { vmAuth.setDefaultTokens(it) }
                        onNavigationUserAlreadySigned()
                    } else {
                        // sign in failed
                    }
                }
        }
    ) {
        scope.launch {
            val signInIntentSender = googleAuthUiClient.signIn()
            launcher.launch(
                IntentSenderRequest.Builder(
                    signInIntentSender ?: return@launch
                ).build()
            )
        }
    }

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
                .clip(CircleShape).size(64.dp),
            painter = painterResource(id = R.drawable.asked30),
            contentDescription = "asked_logo",
        )
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
                        containerColor = MaterialTheme.colorScheme.primaryContainer
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
         * Component
         * Google Sign in Button
         */
        GoogleSignInButton(
            onClick = { onSignInClick() }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}