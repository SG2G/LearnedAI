package com.sginnovations.asked

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.sginnovations.asked.ui.theme.LearnedAITheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicBoolean

private const val TAG = "Main"
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private lateinit var consentInformation: ConsentInformation
//    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
//    private var isMobileAdsInitializeCalled = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val debugSettings = ConsentDebugSettings.Builder(this)
//            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
//            .build()
//        // Set tag for under age of consent. false means users are not under age
//        // of consent.
//        val params = ConsentRequestParameters
//            .Builder()
//            .setConsentDebugSettings(debugSettings)
//            //.setTagForUnderAgeOfConsent(true)
//            .build()
//
//        consentInformation = UserMessagingPlatform.getConsentInformation(this)
//        consentInformation.requestConsentInfoUpdate(
//            this,
//            params,
//            ConsentInformation.OnConsentInfoUpdateSuccessListener {
//                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
//                    this@MainActivity,
//                    ConsentForm.OnConsentFormDismissedListener {
//                            loadAndShowError ->
//                        // Consent gathering failed.
//                        Log.w(TAG, String.format("OnConsentFormDismissedListener"+"%s: %s",
//                            loadAndShowError?.errorCode,
//                            loadAndShowError?.message
//                        ))
//
//                        // Consent has been gathered.
//                        if (consentInformation.canRequestAds()) {
//                            initializeMobileAdsSdk()
//                        }
//                    }
//                )
//            },
//            ConsentInformation.OnConsentInfoUpdateFailureListener {
//                    requestConsentError ->
//                // Consent gathering failed.
//                Log.w(TAG, String.format("OnConsentInfoUpdateFailureListener"+"%s: %s",
//                    requestConsentError.errorCode,
//                    requestConsentError.message
//                ))
//            })
//        // Check if you can initialize the Google Mobile Ads SDK in parallel
//        // while checking for new consent information. Consent obtained in
//        // the previous session can be used to request ads.
//        if (consentInformation.canRequestAds()) {
//            initializeMobileAdsSdk()
//        }

        setContent {
//            LaunchedEffect(Unit) {
//                delay(5000)
//                consentInformation.reset() //TODO DELETE
//                Log.d(TAG, "consentInformation.reset()")
//            }
            LearnedAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LearnedApp()
                }
            }
        }
    }

//    private fun initializeMobileAdsSdk() {
//        if (isMobileAdsInitializeCalled.getAndSet(true)) {
//            return
//        }
//
//        // Initialize the Google Mobile Ads SDK.
//        MobileAds.initialize(this)
//
//        // TODO: Request an ad.
//        // InterstitialAd.load(...)
//    }
}