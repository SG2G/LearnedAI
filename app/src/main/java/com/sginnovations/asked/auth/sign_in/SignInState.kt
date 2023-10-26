package com.sginnovations.asked.auth.sign_in

data class SignInState (
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,

)