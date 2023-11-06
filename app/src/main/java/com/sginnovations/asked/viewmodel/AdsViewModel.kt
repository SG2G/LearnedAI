package com.sginnovations.asked.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.repository.AdManagerRepository
import com.sginnovations.asked.utils.CheckIsPremium.checkIsPremium
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdsViewModel @Inject constructor(
    private val adManagerRepository: AdManagerRepository,
) : ViewModel() {
    fun loadRewardedAd(context: Context) {
        viewModelScope.launch {
            if (!checkIsPremium()) {
                adManagerRepository.loadRewardedAd(context)
            }
        }
    }

    fun showRewardedAd(activity: Activity) {
        viewModelScope.launch {
            if (!checkIsPremium()) {
                adManagerRepository.showRewardedAd(activity)
            }
        }
    }

    fun loadInterstitialAd(context: Context) {
        viewModelScope.launch {
            if (!checkIsPremium()) {
                adManagerRepository.loadInterstitialAd(context)
            }
        }
    }

    fun showInterstitialAd(activity: Activity) {
        viewModelScope.launch {
            if (!checkIsPremium()) {
                adManagerRepository.showInterstitialAd(activity)
            }
        }
    }
}
