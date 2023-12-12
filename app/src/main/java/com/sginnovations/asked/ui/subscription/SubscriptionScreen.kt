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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

enum class Option { OptionWeekly, OptionMonthly }

@Composable
fun SubscriptionStateFull(
    vmBilling: BillingViewModel,

    onNavigateUp: () -> Unit,
) {
    val userOption = remember { mutableStateOf(Option.OptionWeekly) }
    val showComposable = remember { mutableStateOf(false) }

    val productMonthly = vmBilling.productMonthly
    val productWeekly = vmBilling.productWeekly

    val priceSubMonthly = remember { mutableStateOf<String?>(null) }
    val priceSubWeekly = remember { mutableStateOf<String?>(null) }

    Log.d(
        TAG,
        "productMonthly-> ${productMonthly.value.toString()} productWeekly-> ${productWeekly.value.toString()} "
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
        while (priceSubMonthly.value == null && priceSubWeekly.value == null && attempts < 20) { // try up to 20 times
            delay(200)
            priceSubMonthly.value =
                productMonthly.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice

            priceSubWeekly.value =
                productWeekly.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                    ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice

            Log.i(TAG, "$attempts $priceSubWeekly / $priceSubMonthly")

            attempts++
        }

        // Check if priceInApp is not null before setting showComposable to true
        if (priceSubMonthly.value != null && priceSubWeekly.value != null) {
            showComposable.value = true
        }
    }

    if (showComposable.value) {
        SubscriptionStateLess(
            productMonthly,
            productWeekly,

            priceSubMonthly,
            priceSubWeekly,

            userOption,

            onNavigateUp,
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

                        Option.OptionMonthly ->
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
    productLifetime: MutableState<ProductDetails?>,
    productWeekly: MutableState<ProductDetails?>,

    priceSubMonthly: MutableState<String?>,
    priceSubWeekly: MutableState<String?>,

    userOption: MutableState<Option>,

    onNavigateUp: () -> Unit,

    onLaunchPurchaseFlow: (ProductDetails) -> Unit,
) {
    val selectedPlan = remember { mutableStateOf(productWeekly) }
    val cardAlpha = 0.8f

    when (userOption.value) {
        Option.OptionWeekly -> selectedPlan.value = productWeekly
        Option.OptionMonthly -> selectedPlan.value = productLifetime
    }

    Log.i(TAG, "SubscriptionStateLess - $priceSubWeekly / $priceSubMonthly")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), Orientation.Vertical),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.subscription_background_school),
                contentDescription = "subscription_background_school",
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.1f)
            )
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
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TitleBenefit(
                            painterResource = painterResource(id = R.drawable.subscription_infinite2),
                            text = stringResource(R.string.subscription_unlimited)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TokenIcon()
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TitleBenefit(
                            painterResource = painterResource(id = R.drawable.subscription_chat2),
                            text = stringResource(R.string.subscription_higher_word_limit)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TitleBenefit(
                            painterResource = painterResource(id = R.drawable.subscription_ad2),
                            text = stringResource(R.string.subscription_no_ads)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            TitleBenefit(
                                painterResource = painterResource(id = R.drawable.subscription_star2),
                                text = stringResource(R.string.subscription_exclusive_functions)
                            )
                            SubTitleBenefit(text = stringResource(R.string.subscription_subtitle_load_pdf_gpt_4_turbo))
                        }
                    }
                }
            }
        }


        /**
         * Products
         */
        // Product 1 - Weekly
        priceSubWeekly.value?.let {
            SubscriptionCard(
                durationTime = stringResource(R.string.subscription_week),
                smallText = stringResource(R.string.subscription_3_day_free_trial_cancel_anytime_auto_renewable),
                allPrice = it,
                subscriptionOption = Option.OptionWeekly,
                userOption = userOption.value
            ) { userOption.value = Option.OptionWeekly }
        }

        // Product 2 - LifeTime
        priceSubMonthly.value?.let {
            SubscriptionCard(
                durationTime = stringResource(R.string.subscription_monthly),
                smallText = stringResource(R.string.subscription2_cancel_anytime),
                allPrice = it,
                subscriptionOption = Option.OptionMonthly,
                userOption = userOption.value
            ) { userOption.value = Option.OptionMonthly }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { selectedPlan.value.value?.let { onLaunchPurchaseFlow(it) } }, //TODO AD
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(58.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                if (userOption.value == Option.OptionWeekly) {
                    Text(
                        text = stringResource(R.string.subscription_start_free_trial_plan),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                } else {
                    Text(
                        text = stringResource(R.string.subscription_unlock_asked_ai_pro),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Row(
                        modifier = Modifier.height(24.dp)
                    ) {

                    }
                }
            }
        }
        /**
         * Small Letter
         */
        val smallLetterPadding = PaddingValues(bottom = 8.dp, start = 16.dp, end = 16.dp)
        if (userOption.value == Option.OptionWeekly) {
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
