package com.sginnovations.asked.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.domain.repository.AppsFlyerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsFlyerViewModel @Inject constructor(
    private val appsFlyerRepository: AppsFlyerRepository
) : ViewModel() {

    fun logPhotoTakenEvent(cameraType: String) {
        viewModelScope.launch {
            appsFlyerRepository.logPhotoTakenEvent(cameraType)
            appsFlyerRepository.allEngagementEvent()
        }
    }
    fun logPhotoCropEvent(cropType: String) {
        viewModelScope.launch {
            appsFlyerRepository.logPhotoCropEvent(cropType)
            appsFlyerRepository.allEngagementEvent()
        }
    }
    fun logStartMessagePhotoEvent() {
        viewModelScope.launch {
            appsFlyerRepository.logStartMessagePhotoEvent()
            appsFlyerRepository.allEngagementEvent()
        }
    }
    fun logStartMessageAssistantEvent() {
        viewModelScope.launch {
            appsFlyerRepository.logStartMessageAssistantEvent()
            appsFlyerRepository.allEngagementEvent()
        }
    }
    fun logChatPhotoEvent() {
        viewModelScope.launch {
            appsFlyerRepository.logChatPhotoEvent()
            appsFlyerRepository.allEngagementEvent()
        }
    }
    fun logChatAssistantEvent() {
        viewModelScope.launch {
            appsFlyerRepository.logChatAssistantEvent()
            appsFlyerRepository.allEngagementEvent()
        }
    }
    fun logGuideLevelEvent(idLesson: Int) {
        viewModelScope.launch {
            appsFlyerRepository.logGuideLevelEvent(idLesson)
            appsFlyerRepository.allEngagementEvent()
        }
    }

}