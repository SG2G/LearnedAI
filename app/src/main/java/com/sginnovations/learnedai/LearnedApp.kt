package com.sginnovations.learnedai

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.identity.Identity
import com.sginnovations.learnedai.presentation.sign_in.GoogleAuthUiClient

@Composable
fun LearnedApp(

) {
    val context = LocalContext.current

    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    LearnedNavigation(
        googleAuthUiClient = googleAuthUiClient,
    )
}