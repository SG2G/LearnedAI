package com.sginnovations.learnedai.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.sginnovations.learnedai.presentation.sign_in.GoogleAuthUiClient
import com.sginnovations.learnedai.viewmodel.TokenViewModel
import kotlinx.coroutines.launch

private const val TAG = "StateFulProfile"

@Composable
fun StateFulProfile(
    vmTokens: TokenViewModel,

    googleAuthUiClient: GoogleAuthUiClient,

    onNavigateUserNotLogged: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    StateLessProfile(
        vmTokens = vmTokens,

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
    vmTokens: TokenViewModel,

    onSignOut: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val tokens by vmTokens.tokens.collectAsState()

    Column {
        Text(text = "Tokens: ${tokens}")
        Button(onClick = {
            scope.launch {
                vmTokens.oneLessToken()
            }
        }
        ) {
            Text(text = "Rest 1")
        }
        
        Button(onClick = { onSignOut() }) {
            Text(text = "Sign Out")
        }
    }
}