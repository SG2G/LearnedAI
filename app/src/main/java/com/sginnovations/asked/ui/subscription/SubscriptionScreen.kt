package com.sginnovations.asked.ui.subscription

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlin.collections.firstOrNull

private const val TAG = "SubscriptionStateFull"

enum class Option {
    OptionWeekly,
    OptionLifetime,
}

@Composable
fun SubscriptionStateFull(
    vmBilling: BillingViewModel,
) {
    val userOption = remember { mutableStateOf(Option.OptionWeekly) }
    val productDetailsList = vmBilling.productDetails

    val context = LocalContext.current

    fun Context.getActivity(): Activity? {
        return when (this) {
            is Activity -> this
            is ContextWrapper -> baseContext.getActivity()
            else -> null
        }
    }

    val activity = context.getActivity()

    Log.i(TAG, "SubscriptionStateFull: ${productDetailsList.value}")
    if (productDetailsList.value.isNotEmpty()) {
        SubscriptionStateLess(
            productDetailsList.value[0],
            productDetailsList.value[1],

            userOption,

            ) { productDetails ->
            if (activity != null) {
                Log.i(TAG, "SubscriptionStateFull: Activity not null, launching billing flow")
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

@Composable
fun SubscriptionStateLess(
    productLifetime: ProductDetails,
    productWeekly: ProductDetails,

    userOption: MutableState<Option>,

    onLaunchPurchaseFlow: (ProductDetails) -> Unit,
) {
    val priceInApp = remember { mutableStateOf(productLifetime.oneTimePurchaseOfferDetails?.formattedPrice ?: "null")}
    val priceSub = remember { mutableStateOf(getProductPrice(productWeekly)) }

    val selectedPlan = remember { mutableStateOf(productWeekly) }

    when (userOption.value) {
        Option.OptionWeekly -> selectedPlan.value = productWeekly
        Option.OptionLifetime -> selectedPlan.value = productLifetime
    }

    Log.i(TAG, "$priceSub / $priceInApp")

    Column(
        Modifier.padding(vertical = 16.dp)
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


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Product 1 - Weekly
            SubscriptionCard(
                durationTime = stringResource(R.string.subscription_week),
                allPrice = priceSub.value,
                subscriptionOption = Option.OptionWeekly,
                userOption = userOption.value
            ) { userOption.value = Option.OptionWeekly }

            // Product 2 - LifeTime
            SubscriptionCard(
                durationTime = "/ Lifetime",
                allPrice = priceInApp.value,
                subscriptionOption = Option.OptionLifetime,
                userOption = userOption.value
            ) { userOption.value = Option.OptionLifetime }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { onLaunchPurchaseFlow(selectedPlan.value) }, //TODO AD
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
}
fun getProductPrice(productDetails: ProductDetails): String {
    val subscriptionOfferDetailsList = productDetails.subscriptionOfferDetails
    return subscriptionOfferDetailsList
        ?.firstOrNull()?.pricingPhases?.pricingPhaseList
        ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice.toString()
}

