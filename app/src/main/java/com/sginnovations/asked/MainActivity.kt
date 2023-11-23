package com.sginnovations.asked

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.sginnovations.asked.ui.theme.LearnedAITheme
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import java.util.regex.Pattern

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
                    LearnedApp()

//                    Column(
//                        modifier = Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        CauchySchwarzInequality()
//                    }
                }
            }
        }
    }
}
//
//@Composable
//fun CauchySchwarzInequality() {
//    val context = LocalContext.current
//
//
//    val markwon = remember {
//        Markwon.builder(context)
//            .usePlugin(MarkwonInlineParserPlugin.create())
//            .usePlugin(
//                JLatexMathPlugin.create(
//                    60f,
//                    JLatexMathPlugin.BuilderConfigure { builder ->
//                        builder.inlinesEnabled(true)
//                    }
//                )
//            )
//            .build()
//    }
//
////    val texto =
////        """Certainly! I can help you with that equation. It looks like a proportion equation." +
////                " To simplify it, we can cancel out the common term of \frac{3}{4} on both sides:"""
////    //val markdownText = surroundOperationsWithDollars(texto)
////
////    val que = "hola <br><br>√(m_A)(0.3 - x) = x√(m_B)<br><br> que pasa cerdo <br><br> adhola <br><br>"
//
//    val inputText = "hey a) \\\\( h(x)=\\\\frac{x^{2}-2 x-15}{x+3} \\\\)"
//
//    val markdownText1 = replaceMathParenthesisSymbols(inputText)
//    Log.d(TAG, "MASON - markdownText1: $markdownText1 ")
//    val markdownText = cleanMathExpression(markdownText1)
//
////    val markdownText = "\$\$\\\\( \\\\log (3 x+5)+\\\\log (x+5)=3 \\\\)\$\$"
//
//    Log.d(TAG, "MASON - inputText: $inputText")
//    Log.d(TAG, "MASON - markdownText: $markdownText")
//
//    AndroidView(
//        modifier = Modifier.fillMaxWidth(),
//        factory = { ctx ->
//            TextView(ctx).apply {
//                setBackgroundColor(Color.TRANSPARENT)
//                setTextColor(Color.WHITE)
//                textSize = 24f
//                movementMethod = LinkMovementMethod.getInstance()
//            }
//        },
//        update = { view ->
//            val node = markwon.parse(markdownText)
//            val renderedMarkdown = markwon.render(node)
//            markwon.setParsedMarkdown(view, renderedMarkdown)
//        }
//    )
//}
//
///**
// * Replace
// */
//
///**
// * Replace "\\(" to "$$"
// */
//fun replaceMathParenthesisSymbols(text: String): String {
//    return text.replace("\\\\(", "$$").replace("\\\\)", "$$")
//}
//
///**
// * Replace "\\" to "\"
// */
//fun cleanMathExpression(expression: String): String {
//    return expression.replace("\\\\", "\\").replace("$$\\(", "$$").replace("\\)$$", "$$")
//}
//
//
//
///**
// * OLD
// */
//fun rodearFuncionesMatematicasConDolares(texto: String): String {
//    // Esta regex busca bloques de texto entre <br> que contienen una función matemática
//    val regex = Pattern.compile("<br><br>([^<]*\\([^\\)]+\\)[^<]*)<br><br>")
//    val matcher = regex.matcher(texto)
//    val sb = StringBuilder()
//
//    var lastEnd = 0
//    while (matcher.find()) {
//        sb.append(texto.substring(lastEnd, matcher.start()))
//        val match = matcher.group(1)
//        // Comprobamos si el bloque de texto contiene principalmente texto o una función matemática
//        val words = match.split("\\s+".toRegex())
//        var wordCount = 0
//        var nonWordCount = 0
//        for (word in words) {
//            if (word.matches("\\w+".toRegex())) {
//                wordCount++
//            } else {
//                nonWordCount++
//            }
//        }
//        // Si el bloque de texto contiene principalmente una función matemática, lo rodeamos con $$
//        if (nonWordCount > wordCount) {
//            sb.append("<br><br>\n$$\n")
//            sb.append(match)
//            sb.append("\n$$\n<br><br>")
//        } else {
//            // Si no, lo dejamos como está
//            sb.append("<br><br>")
//            sb.append(match)
//            sb.append("<br><br>")
//        }
//        lastEnd = matcher.end()
//    }
//    sb.append(texto.substring(lastEnd))
//
//    return sb.toString()
//}