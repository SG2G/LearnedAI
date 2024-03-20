package com.sginnovations.asked.presentation.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sginnovations.asked.Constants.Companion.RC_ONBOARDING_EXPERIMENT
import com.sginnovations.asked.domain.repository.RemoteConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    val btnEnable = mutableStateOf(true)
    val currentPage = mutableIntStateOf(0)
    val quoteResponse = mutableStateOf(true)

    val childName = mutableStateOf("")

    val onBoardingExperiment = mutableStateOf("")

    fun getOnBoardingExperimentNum() {
        onBoardingExperiment.value =  remoteConfigRepository.getValue(RC_ONBOARDING_EXPERIMENT)
    }

}