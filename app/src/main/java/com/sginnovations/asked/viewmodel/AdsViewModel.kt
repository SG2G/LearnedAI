package com.sginnovations.asked.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.sginnovations.asked.model.rewarded_ad.AdManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdsViewModel @Inject constructor(
    private val adManager: AdManager
): ViewModel() {

    fun loadRewardedAd(context: Context) {
        adManager.loadRewardedAd(context)
    }
    fun showRewardedAd(activity: Activity) {
        adManager.showRewardedAd(activity)
    }
    fun loadInterstitialAd(context: Context) {
        adManager.loadInterstitialAd(context)
    }
    fun showInterstitialAd(activity: Activity) {
        adManager.showInterstitialAd(activity)
    }

}