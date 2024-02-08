package com.sginnovations.asked.presentation.viewmodel

//@HiltViewModel
//class AdsViewModel @Inject constructor(
//    private val adManagerRepository: AdManagerRepository,
//    private val remoteConfigRepository: RemoteConfigRepository,
//) : ViewModel() {
//    fun loadRewardedAd(context: Context) {
//        viewModelScope.launch {
//            if (!checkIsPremium()) {
//                adManagerRepository.loadRewardedAd(context)
//            }
//        }
//    }
//
//    fun showRewardedAd(activity: Activity) {
//        viewModelScope.launch {
//            if (!checkIsPremium()) {
//                adManagerRepository.showRewardedAd(activity)
//            }
//        }
//    }
//
//    fun loadInterstitialAd(context: Context) {
//        viewModelScope.launch {
//            try {
//                if (remoteConfigRepository.isAdsAllowed().toBoolean()) {
//                    if (!checkIsPremium()) {
//                        adManagerRepository.loadInterstitialAd(context)
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    fun showInterstitialAd(activity: Activity) {
//        viewModelScope.launch {
//            try {
//                if (remoteConfigRepository.isAdsAllowed().toBoolean()) {
//                    if (!checkIsPremium()) {
//                        adManagerRepository.showInterstitialAd(activity)
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//}
