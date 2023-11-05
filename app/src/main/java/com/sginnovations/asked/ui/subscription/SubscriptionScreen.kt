package com.sginnovations.asked.ui.subscription

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.ProductDetails
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.ui_components.subscription.SubscriptionCard
import com.sginnovations.asked.viewmodel.BillingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.firstOrNull

private const val TAG = "SubscriptionStateFull"

enum class Option { OptionWeekly, OptionLifetime }

@Composable
fun SubscriptionStateFull(
    vmBilling: BillingViewModel,
) {
    val userOption = remember { mutableStateOf(Option.OptionWeekly) }
    val showComposable = remember { mutableStateOf(false) }

    val productLifetime = vmBilling.productLifetime
    val productWeekly = vmBilling.productWeekly

    val priceInApp = remember { mutableStateOf<String?>(null) }
    val priceSub = remember { mutableStateOf<String?>(null) }


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

    LaunchedEffect(Unit) {
        var attempts = 0
        while (priceInApp.value == null && attempts < 5) { // try up to 5 times
            priceInApp.value = productLifetime.value?.oneTimePurchaseOfferDetails?.formattedPrice

            priceSub.value = productWeekly.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice

            delay(250)
            Log.i(TAG, "$attempts $priceSub / $priceInApp")

            attempts++
        }

        // Check if priceInApp is not null before setting showComposable to true
        if (priceInApp.value != null && priceSub.value != null) {
            showComposable.value = true
        }
    }


    if (showComposable.value) {
        SubscriptionStateLess(
            productLifetime,
            productWeekly,

            priceInApp,
            priceSub,

            userOption,

            ) { productDetails ->
            if (activity != null) {
                scope.launch {
                    Log.i(
                        TAG,
                        "SubscriptionStateFull: Activity not null, launching billing flow"
                    )
                    when (userOption.value) {
                        Option.OptionWeekly ->
                            vmBilling.launchBillingFlowSubs(
                                activity,
                                productDetails,
                            )

                        Option.OptionLifetime ->
                            vmBilling.launchBillingFlowInApp(
                                activity,
                                productDetails,
                            )
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionStateLess(
    productLifetime: MutableState<ProductDetails?>,
    productWeekly: MutableState<ProductDetails?>,

    priceInApp: MutableState<String?>,
    priceSub: MutableState<String?>,

    userOption: MutableState<Option>,

    onLaunchPurchaseFlow: (ProductDetails) -> Unit,
) {
    val selectedPlan = remember { mutableStateOf(productWeekly) }

    when (userOption.value) {
        Option.OptionWeekly -> selectedPlan.value = productWeekly
        Option.OptionLifetime -> selectedPlan.value = productLifetime
    }

    Log.i(TAG, "SubscriptionStateLess - $priceSub / $priceInApp")

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "AskedAI Pro",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Product 1 - Weekly
        priceSub.value?.let {
            SubscriptionCard(
                durationTime = stringResource(R.string.subscription_week),
                smallText = "3-day FREE TRIAL, Auto renewable",
                allPrice = it,
                subscriptionOption = Option.OptionWeekly,
                userOption = userOption.value
            ) { userOption.value = Option.OptionWeekly }
        }

        // Product 2 - LifeTime
        priceInApp.value?.let {
            SubscriptionCard(
                durationTime = stringResource(R.string.subscription_lifetime),
                smallText = "Full time, Lifetime",
                allPrice = it,
                subscriptionOption = Option.OptionLifetime,
                userOption = userOption.value
            ) { userOption.value = Option.OptionLifetime }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { selectedPlan.value.value?.let { onLaunchPurchaseFlow(it) } }, //TODO AD
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = "Buy AskedAI Pro", color = MaterialTheme.colorScheme.onBackground,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
