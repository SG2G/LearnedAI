package com.sginnovations.asked.model.rewarded_ad

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.sginnovations.asked.Constants.Companion.INTERSTITIAL_AD_UNIT
import com.sginnovations.asked.Constants.Companion.REWARD_AD_UNIT
import com.sginnovations.asked.repository.TokenRepository
import javax.inject.Inject


private const val TAG = "AdManager"

class AdManager @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    private var rewardedAd: RewardedAd? = null
    private val rewardAdUnit = REWARD_AD_UNIT

    private var interstitialAd: InterstitialAd? = null
    private val interstitialAdUnit = INTERSTITIAL_AD_UNIT

    fun loadInterstitialAd(context: Context) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(context, interstitialAdUnit,
            adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    interstitialAd = null
                    Log.d(TAG, "Interstitial Ad failed, trying again.")
                    loadInterstitialAd(context)
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Interstitial Ad was loaded.")
                    interstitialAd = ad
                }
            })
    }
    fun showInterstitialAd(activity: Activity) {
        if (interstitialAd != null) {
            interstitialAd?.show(activity)
            Log.d(TAG, "showInterstitialAd: Interstitial ad show")
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

    fun loadRewardedAd(context: Context) {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(context, rewardAdUnit,
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    rewardedAd = null
                    loadRewardedAd(context)
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Rewarded Ad was loaded.")
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
                    Log.d(TAG, "Rewarded Ad was dismissed.")
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d(TAG, "Rewarded Ad failed to show.")
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Rewarded Ad showed fullscreen content.")
                    // Se llama después de que se muestra el anuncio a pantalla completa.
                    // Recargamos el anuncio para que esté listo para la próxima vez.
                    loadRewardedAd(activity)
                }
            }
        } ?: run {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
            Toast.makeText(activity, "Try again later!", Toast.LENGTH_SHORT).show()
        }
    }
}