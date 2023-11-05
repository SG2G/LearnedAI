package com.sginnovations.asked.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sginnovations.asked.model.rewarded_ad.AdManager
import com.sginnovations.asked.viewmodel.AuthViewModel.Companion.isPremium
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdsViewModel @Inject constructor(
    private val adManager: AdManager
): ViewModel() {

    fun loadRewardedAd(context: Context) {
        if (!isPremium.value) {
            viewModelScope.launch {
                adManager.loadRewardedAd(context)
            }
        }
    }

    fun showRewardedAd(activity: Activity) {
        if (!isPremium.value) {
            viewModelScope.launch {
                adManager.showRewardedAd(activity)
            }
        }
    }

    fun loadInterstitialAd(context: Context) {
        if (!isPremium.value) {
            viewModelScope.launch {
                adManager.loadInterstitialAd(context)
            }
        }
    }

    fun showInterstitialAd(activity: Activity) {
        if (!isPremium.value) {
            viewModelScope.launch {
                adManager.showInterstitialAd(activity)
            }
        }
    }
}
