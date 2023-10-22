package com.sginnovations.asked.ui.sign_in

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.sign_in.GoogleAuthUiClient
import com.sginnovations.asked.presentation.sign_in.SignInState
import com.sginnovations.asked.ui.ui_components.sign_in.GoogleSignInButton
import com.sginnovations.asked.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LearnedAuth(
    vmAuth: AuthViewModel,
    state: SignInState,

    onNavigationUserAlreadySigned: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val googleAuthUiClient = vmAuth.getGoogleAuthUiClient()

    LaunchedEffect(Unit) {
        if(googleAuthUiClient.getSignedInUser() != null) {
            onNavigationUserAlreadySigned()
        }
    }

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if(state.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()

            onNavigationUserAlreadySigned()
            vmAuth.resetState()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if(result.resultCode == RESULT_OK) {
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
        onSignInClick = {
            scope.launch {
                val signInIntentSender = googleAuthUiClient.signIn()
                launcher.launch(
                    IntentSenderRequest.Builder(
                        signInIntentSender ?: return@launch
                    ).build()
                )
            }
        }
    )

}

@Composable
fun LearnedAuthStateLess(
    state: SignInState,
    context: Context,

    onSignInClick: () -> Unit,
) {

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
        /**
         * Logo Image
         */

        Image(
            painter = painterResource(id = R.drawable.person_fill1_wght400_grad0_opsz24),
            contentDescription = "Icon Image",
            modifier = Modifier
                .size(48.dp)
                .padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        /**
         * Component
         * Google Sign in Button
         */
        GoogleSignInButton(
            onClick = { onSignInClick() }
        )
    }
}