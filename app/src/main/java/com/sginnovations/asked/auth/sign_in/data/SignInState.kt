package com.sginnovations.asked.auth.sign_in.data

data class SignInState (
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,

)