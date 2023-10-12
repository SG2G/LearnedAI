package com.sginnovations.learnedai.ui.profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.sginnovations.learnedai.presentation.sign_in.GoogleAuthUiClient
import com.sginnovations.learnedai.presentation.sign_in.SignInState
import com.sginnovations.learnedai.viewmodel.SignInViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "StateFulProfile"
@Composable
fun StateFulProfile(
    vmAuth: SignInViewModel,
    state: SignInState,

    googleAuthUiClient: GoogleAuthUiClient,

    onNavigateUserNotLogged: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    StateLessProfile(

        onSignOut = {
            scope.launch {
                googleAuthUiClient.signOut()
                onNavigateUserNotLogged()
            }
        }
    )
}

@Composable
fun StateLessProfile(
    onSignOut: () -> Unit,
) {
    Column {
        Button(onClick = { onSignOut() }
        ) {
            Text(text = "Sing out")
        }
    }
}