package com.sginnovations.asked.auth.sign_in.data

import androidx.annotation.Keep

@Keep
data class SignInResult(
    val data: UserData?,
    val errorMessage: String?,
)

@Keep
data class UserData(
    val userId: String,
    val userName: String?,
    val profilePictureUrl: String?,
)