package com.sginnovations.asked.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.sginnovations.asked.auth.sign_in.data.UserData
import com.sginnovations.asked.repository.IntentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntentViewModel @Inject constructor(
    private val intentRepository: IntentRepository,
) : ViewModel() {

    fun sendEmail(context: Context, userAuth: State<UserData?>) =
        intentRepository.sendEmail(context, userAuth)

    fun rateUs(context: Context) =
        intentRepository.rateUs(context)
}
