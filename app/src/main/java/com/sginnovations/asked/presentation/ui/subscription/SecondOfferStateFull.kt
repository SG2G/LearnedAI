package com.sginnovations.asked.presentation.ui.subscription

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.subscription.components.AskedSubscriptionTitle
import com.sginnovations.asked.presentation.ui.subscription.components.CountdownTimer
import com.sginnovations.asked.presentation.ui.subscription.components.SubscriptionButton
import com.sginnovations.asked.presentation.viewmodel.BillingViewModel
import com.sginnovations.asked.presentation.viewmodel.PreferencesViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime

private const val TAG = "SecondOfferStateFul"

@Composable
fun SecondOfferStateFul(
    vmBilling: BillingViewModel,
    vmPreference: PreferencesViewModel,

    onDismissRequest: () -> Unit,
) {
    val showComposable = remember { mutableStateOf(false) }

    val productMonthly = vmBilling.productMonthly
    val productAnnually = vmBilling.productAnnually
    val productAnnuallyRR = vmBilling.productAnnuallyRR2

    val priceSubAnnually = remember { mutableStateOf<String?>(null) }
    val priceSubAnnuallyDiscount = remember { mutableStateOf<String?>(null) }
    val priceSubAnnuallyRR = remember { mutableStateOf<String?>(null) }
    val priceSubMonthly = remember { mutableStateOf<String?>(null) }

    val priceMicrosSubAnnually = remember { mutableStateOf<Long?>(null) }
    val priceMicrosSubAnnuallyRR = remember { mutableStateOf<Long?>(null) }
    val priceMicrosSubMonthly = remember { mutableStateOf<Long?>(null) }

    val priceCurrencySubAnnually = remember { mutableStateOf<String?>(null) }

    Log.d(
        TAG,
        "productMonthly-> ${productMonthly.value.toString()} \n\n" +
                "productAnnually-> ${productAnnually.value.toString()} \n\n" +
                "productAnually RR -> ${productAnnuallyRR.value.toString()}"
    )

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun Context.getActivity(): Activity? {
        return when (this) {
            is Activity -> this
            is ContextWrapper -> baseContext.getActivity()
            else -> null
        }
    }

    val activity = context.getActivity()

    val backgroundColor = MaterialTheme.colorScheme.surface.toArgb()
    SideEffect { (context as Activity).window.navigationBarColor = backgroundColor }
    /**
     * No prices? No screen
     */
    LaunchedEffect(Unit) {
        var attempts = 0
        while (priceSubAnnually.value == null && priceSubMonthly.value == null && priceSubAnnuallyRR.value == null && attempts < 20) { // try up to 20 times
            delay(200)
            Log.d(TAG, "size: ${productAnnually.value?.subscriptionOfferDetails?.size} ")

            priceSubAnnuallyRR.value =
                productAnnuallyRR.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                    ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice

            priceMicrosSubAnnuallyRR.value =
                productAnnuallyRR.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                    ?.firstOrNull { it.priceAmountMicros > 0 }?.priceAmountMicros

            /**
             * If Not have Offer, size = 1
             */
            if (productAnnually.value?.subscriptionOfferDetails?.size == 1) {
                priceSubAnnually.value =
                    productAnnually.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                        ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice

                priceMicrosSubAnnually.value =
                    productAnnually.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                        ?.firstOrNull { it.priceAmountMicros > 0 }?.priceAmountMicros

                priceCurrencySubAnnually.value =
                    productAnnually.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                        ?.firstOrNull { it.priceAmountMicros > 0 }?.priceCurrencyCode
                /**
                 * If Not have Offer, size = 2
                 */
            } else {
                priceSubAnnuallyDiscount.value =
                    productAnnually.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                        ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice

                priceMicrosSubAnnually.value =
                    productAnnually.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                        ?.firstOrNull { it.priceAmountMicros > 0 }?.priceAmountMicros

                priceCurrencySubAnnually.value =
                    productAnnually.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                        ?.firstOrNull { it.priceAmountMicros > 0 }?.priceCurrencyCode

                priceSubAnnually.value =
                    productAnnually.value?.subscriptionOfferDetails?.getOrNull(1)?.pricingPhases?.pricingPhaseList
                        ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice
            }

            priceSubMonthly.value =
                productMonthly.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                    ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice

            priceMicrosSubMonthly.value =
                productMonthly.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                    ?.firstOrNull { it.priceAmountMicros > 0 }?.priceAmountMicros

            Log.i(
                TAG,
                "$attempts priceSubMonthly -> $priceSubMonthly \n" +
                        "\n priceSubAnnually -> $priceSubAnnually \n" +
                        "\n priceDiscountSubAnnually -> ${priceSubAnnuallyDiscount.value} \n" +
                        "\npriceAnnualRR -> $priceSubAnnuallyRR"
            )

            attempts++
        }

        // Check if priceInApp is not null before setting showComposable to true
        if (priceSubAnnually.value != null && priceSubMonthly.value != null && priceSubAnnuallyRR.value != null) {
            showComposable.value = true
        }
    }

    SecondOfferStateLess(
        vmPreference = vmPreference,
        priceFull = priceSubAnnually,
        priceDiscount = priceSubAnnuallyRR,

        priceAnnualMonthly = priceMicrosSubAnnuallyRR.value?.let { micro ->
            priceCurrencySubAnnually.value?.let { currency ->
                formatPriceAnnualToMonthly(
                    micro,
                    currency
                )
            }
        },

        onLaunchPurchaseFlow = {
            if (activity != null) {
                productAnnuallyRR.value?.let { productAnnuallyRR ->
                    onLaunchPurchaseFlow(
                        vmBilling = vmBilling,

                        scope = scope,
                        activity = activity,

                        productDetails = productAnnuallyRR,
                    )
                }
            }
        }

    ) { onDismissRequest() }
}

@Composable
fun SecondOfferStateLess(
    vmPreference: PreferencesViewModel,

    priceFull: MutableState<String?>,
    priceDiscount: MutableState<String?>,

    priceAnnualMonthly: String?,

    onLaunchPurchaseFlow: () -> Unit,

    onDismissRequest: () -> Unit,
) {
    val scrollState = rememberScrollState()

    val targetDate =
        LocalDateTime.of(2024, 3, 31, 0, 0) // Example: 31 March

    val showGiftContent = remember { mutableStateOf(false) }
    val offerFirstShow = vmPreference.showSubOffer

    LaunchedEffect(Unit) {
        if (offerFirstShow.value) {
            delay(3000)
        }
        showGiftContent.value = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp, topStart = 15.dp, topEnd = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                /**
                 * Title
                 */
                AskedSubscriptionTitle(onDismissRequest)
                /**
                 * CountDown
                 */
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.oferta_de_lanzamiento),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(R.string.only_until_march_31st),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        CountdownTimer(
                            targetDate = targetDate,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                        HappyLottieAnimation(animationId = R.raw.happy_mother)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                shape = RoundedCornerShape(5.dp),
                                colors = CardDefaults.cardColors(
                                    MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    modifier = Modifier.padding(16.dp),
                                    text = stringResource(R.string.save) + " " + "67%",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleSmall,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = priceAnnualMonthly + " " + stringResource(id = R.string.subscription_month),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = stringResource(R.string.only) + " " + priceDiscount.value + stringResource(
                                    id = R.string.subscription_year
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.was) + " " + priceFull.value + stringResource(
                                    id = R.string.subscription_year
                                ) + ")",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium,
                                textDecoration = TextDecoration.LineThrough,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    /**
                     * Confirm Button
                     */
                    SubscriptionButton(
                        textButton = stringResource(R.string.claim_offer),
                        onLaunchPurchaseFlow = { onLaunchPurchaseFlow() }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    /**
                     * Reject Offer
                     */
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.reject_offer),
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    val smallLetterPadding =
                        PaddingValues(bottom = 8.dp, start = 16.dp, end = 16.dp, top = 16.dp)

                    Text(
                        modifier = Modifier.fillMaxWidth().padding(smallLetterPadding),
                        text = stringResource(R.string.you_can_cancel_at_any_time_billing_is_annual),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}