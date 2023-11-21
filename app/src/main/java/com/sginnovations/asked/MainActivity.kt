package com.sginnovations.asked

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.sginnovations.asked.ui.theme.LearnedAITheme
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin

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
                    //LearnedApp()

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val markdown = """
                    # Hello, GeeksForGeeks!
                    This is a sample markdown text in **Jetpack Compose**.
                    - Kotlin
                    - Jetpack Compose
                    - Markdown
                    
**The Cauchy-Schwarz Inequality**
${'$'}${'$'}\left( \sum_{k=1}^n a_k b_k \right)^2 \leq \left( \sum_{k=1}^n a_k^2 \right) \left( \sum_{k=1}^n b_k^2 \right)${'$'}${'$'}
                    
                """.trimIndent()

//                        MarkdownText(markdown = markdown)
                        CauchySchwarzInequality()
                    }
                }
            }
        }
    }
}

@Composable
fun CauchySchwarzInequality() {
    val context = LocalContext.current
    val markdownText = """
        $$
        ${'$'}${'$'}\lim_{x \rightarrow 3} \frac{x^2 + 9}{x - 3}${'$'}${'$'}
        $$
    """.trimIndent()

    val markwon = remember {
        Markwon.builder(context)
            .usePlugin(JLatexMathPlugin.create(70f)) // replace 24f with your desired text size
            .build()
    }

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { ctx ->
            TextView(ctx).apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(Color.WHITE)
                textSize = 70f
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = { view ->
            val node = markwon.parse(markdownText)
            val renderedMarkdown = markwon.render(node)
            markwon.setParsedMarkdown(view, renderedMarkdown)
        }
    )
}


//@Composable
//fun MarkdownText(markdown: String) {
//    val context = LocalContext.current
//    val markwon = remember { Markwon.builder(context).build() }
//
//    AndroidView(
//        factory = { ctx ->
//            TextView(ctx).apply {
//                movementMethod = LinkMovementMethod.getInstance()
//            }
//        },
//        update = { view ->
//            val node = markwon.parse(markdown)
//            val markdownText = markwon.render(node)
//            markwon.setParsedMarkdown(view, markdownText)
//        }
//    )
//}
