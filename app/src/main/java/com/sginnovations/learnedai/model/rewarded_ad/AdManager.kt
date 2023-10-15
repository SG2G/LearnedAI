package com.sginnovations.learnedai.model.rewarded_ad

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.sginnovations.learnedai.repository.TokenRepository
import javax.inject.Inject


private const val TAG = "RewardedAd"

class AdManager @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    private var rewardedAd: RewardedAd? = null
    private val rewardAdUnit = "ca-app-pub-3940256099942544/5224354917"

    fun loadRewardedAd(context: Context) {
        var adRequest = AdRequest.Builder().build()

        RewardedAd.load(context, rewardAdUnit,
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    rewardedAd = null
                    loadRewardedAd(context)
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedAd = ad
                }
            })
    }

    fun showRewardedAd(activity: Activity) {
        rewardedAd?.let { ad ->
            ad.show(activity, object : OnUserEarnedRewardListener {
                override fun onUserEarnedReward(rewardItem: RewardItem) {
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type

                    // Reward
                    tokenRepository.giveReward()

                    Log.d(TAG, "User earned the reward. Amount: $rewardAmount, Type: $rewardType")
                }
            })
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed.")
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d(TAG, "Ad failed to show.")
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                    // Se llama después de que se muestra el anuncio a pantalla completa.
                    // Recargamos el anuncio para que esté listo para la próxima vez.
                    loadRewardedAd(activity)
                }
            }
        } ?: run {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }
    }
}