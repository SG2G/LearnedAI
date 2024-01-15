package com.sginnovations.asked.ui.subscription

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.billingclient.api.ProductDetails
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.subscription.components.SubTitleBenefit
import com.sginnovations.asked.ui.subscription.components.TitleBenefit
import com.sginnovations.asked.ui.ui_components.subscription.SubscriptionCard
import com.sginnovations.asked.ui.ui_components.tokens.TokenIcon
import com.sginnovations.asked.viewmodel.BillingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "SubscriptionStateFull"

enum class Option { OptionMonthly, OptionAnnually }

@Composable
fun SubscriptionStateFull(
    vmBilling: BillingViewModel,

    onNavigateUp: () -> Unit,
) {
    val userOption = remember { mutableStateOf(Option.OptionMonthly) }
    val showComposable = remember { mutableStateOf(false) }

    val productMonthly = vmBilling.productMonthly
    val productAnnually = vmBilling.productAnnually

    val priceSubAnnually = remember { mutableStateOf<String?>(null) }
    val priceSubMonthly = remember { mutableStateOf<String?>(null) }

    Log.d(
        TAG,
        "productMonthly-> ${productMonthly.value.toString()} productWeekly-> ${productAnnually.value.toString()} "
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

    /**
     * No prices? No screen
     */
    LaunchedEffect(Unit) {
        var attempts = 0
        while (priceSubAnnually.value == null && priceSubMonthly.value == null && attempts < 20) { // try up to 20 times
            delay(200)
            priceSubAnnually.value =
                productMonthly.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                    ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice

            priceSubMonthly.value =
                productAnnually.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                    ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice

            Log.i(TAG, "$attempts $priceSubMonthly / $priceSubAnnually")

            attempts++
        }

        // Check if priceInApp is not null before setting showComposable to true
        if (priceSubAnnually.value != null && priceSubMonthly.value != null) {
            showComposable.value = true
        }
    }

    if (showComposable.value) {
        SubscriptionStateLess(
            productMonthly,
            productAnnually,

            priceSubAnnually,
            priceSubMonthly,

            userOption,

            onNavigateUp,
        ) { productDetails ->
            if (activity != null) {
                scope.launch {
                    Log.i(
                        TAG,
                        "SubscriptionStateFull: Activity not null, launching billing flow"
                    )
                    when (userOption.value) { //TODO ¿?¿?
                        Option.OptionMonthly ->
                            vmBilling.launchBillingFlowSubs(
                                activity,
                                productDetails,
                            )

                        Option.OptionAnnually ->
                            vmBilling.launchBillingFlowSubs(
                                activity,
                                productDetails,
                            )
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.subscription_background_school),
                    contentDescription = "subscription_background_school",
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.1f)
                )
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        IconButton(onClick = { onNavigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = "Cancel"
                            )
                        }
                        Text(
                            text = "AskedAI Pro",
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                        IconButton(onClick = { }) {}
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionStateLess(
    productMonthly: MutableState<ProductDetails?>,
    productAnnually: MutableState<ProductDetails?>,

    priceSubAnnually: MutableState<String?>,
    priceSubMonthy: MutableState<String?>,

    userOption: MutableState<Option>,

    onNavigateUp: () -> Unit,

    onLaunchPurchaseFlow: (ProductDetails) -> Unit,
) {
    val selectedPlan = remember { mutableStateOf(productAnnually) }
    val cardAlpha = 0.8f

    when (userOption.value) {
        Option.OptionMonthly -> selectedPlan.value = productAnnually
        Option.OptionAnnually -> selectedPlan.value = productMonthly
    }

    Log.i(TAG, "SubscriptionStateLess - $priceSubMonthy / $priceSubAnnually")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), Orientation.Vertical),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "Cancel"
                        )
                    }
                    Text(
                        text = "AskedAI Pro",
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    IconButton(onClick = { }) {}
                }
                /**
                 * Benfits
                 */
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TitleBenefit(
                            painterResource = painterResource(id = R.drawable.subscription_infinite2),
                            text = stringResource(R.string.subscription_unlimited)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TokenIcon()
                    }

                    Divider()

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TitleBenefit(
                            painterResource = painterResource(id = R.drawable.subscription_chat2),
                            text = stringResource(R.string.subscription_higher_word_limit)
                        )
                    }

                    Divider()

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TitleBenefit(
                            painterResource = painterResource(id = R.drawable.subscription_star2),
                            text = "Access to Full Guide"
                        )
                    }

                    Divider()

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            TitleBenefit(
                                painterResource = painterResource(id = R.drawable.subscription_star2),
                                text = stringResource(R.string.subscription_exclusive_functions)
                            )
                            SubTitleBenefit(text = "")
                        }
                    }

                }
            }
        }


        /**
         * Products
         */
        // Product 1 - Weekly
        priceSubMonthy.value?.let { price ->
            SubscriptionCard(
                durationTime = stringResource(R.string.subscription_monthly),
                smallText = stringResource(R.string.subscription_monthly_small_text),
                allPrice = price,
                subscriptionOption = Option.OptionMonthly,
                userOption = userOption.value
            ) { userOption.value = Option.OptionMonthly }
        }

        // Product 2 - LifeTime
        priceSubAnnually.value?.let { price ->
            SubscriptionCard(
                durationTime = stringResource(R.string.subscription_annually),
                smallText = stringResource(R.string.subscription_annually_small_text),
                allPrice = price,
                subscriptionOption = Option.OptionAnnually,
                userOption = userOption.value
            ) { userOption.value = Option.OptionAnnually }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { selectedPlan.value.value?.let { onLaunchPurchaseFlow(it) } }, //TODO AD
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(58.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = stringResource(R.string.subscription_unlock_asked_ai_pro),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    modifier = Modifier.height(24.dp)
                ) {

                }
            }
        }
        /**
         * Small Letter
         */
        val smallLetterPadding = PaddingValues(bottom = 8.dp, start = 16.dp, end = 16.dp)
        if (userOption.value == Option.OptionAnnually) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .padding(smallLetterPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Text(
                        text = stringResource(R.string.subscription_cancel_anytime),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Icon(
                        imageVector = Icons.Filled.Shield,
                        contentDescription = "Shield",
                        tint = Color(0xFF8dad63)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.subscription_no_payment_now),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .padding(smallLetterPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Text(
                        text = stringResource(R.string.subscription_cancel_anytime),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Text(
                        text = "",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
