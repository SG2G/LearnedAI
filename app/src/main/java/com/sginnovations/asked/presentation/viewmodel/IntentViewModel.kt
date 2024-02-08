package com.sginnovations.asked.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.auth.sign_in.data.UserData
import com.sginnovations.asked.domain.repository.IntentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntentViewModel @Inject constructor(
    private val intentRepository: IntentRepository,
) : ViewModel() {

    fun openYouTubeVideo(context: Context, videoId: String) {
        viewModelScope.launch {
            intentRepository.openYouTubeVideo(context, videoId)
        }
    }

    fun sendEmail(context: Context, userAuth: State<UserData?>) =
        intentRepository.sendEmail(context, userAuth)

    fun rateUs(context: Context) =
        intentRepository.rateUs(context)

    fun manageSubscription(context: Context) = intentRepository.manageSubscription(context)
}
