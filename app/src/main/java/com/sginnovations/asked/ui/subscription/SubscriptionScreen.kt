package com.sginnovations.asked.ui.subscription

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.FiberDvr
import androidx.compose.material.icons.outlined.Token
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.ProductDetails
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.ui_components.subscription.SubscriptionCard
import com.sginnovations.asked.ui.ui_components.tokens.TokenIcon
import com.sginnovations.asked.viewmodel.BillingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.firstOrNull

private const val TAG = "SubscriptionStateFull"

enum class Option { OptionWeekly, OptionLifetime }

@Composable
fun SubscriptionStateFull(
    vmBilling: BillingViewModel,

    onNavigateUp: () -> Unit,
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
            delay(250)
            priceInApp.value = productLifetime.value?.oneTimePurchaseOfferDetails?.formattedPrice

            priceSub.value =
                productWeekly.value?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList
                    ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice

            delay(25)
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

                        Option.OptionLifetime ->
                            vmBilling.launchBillingFlowInApp(
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
                    painter = painterResource(id = R.drawable.subscription_background_school2_modifier2),
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

    priceInApp: MutableState<String?>,
    priceSub: MutableState<String?>,

    userOption: MutableState<Option>,

    onNavigateUp: () -> Unit,

    onLaunchPurchaseFlow: (ProductDetails) -> Unit,
) {
    val selectedPlan = remember { mutableStateOf(productWeekly) }

    when (userOption.value) {
        Option.OptionWeekly -> selectedPlan.value = productWeekly
        Option.OptionLifetime -> selectedPlan.value = productLifetime
    }

    Log.i(TAG, "SubscriptionStateLess - $priceSub / $priceInApp")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.subscription_background_school2_modifier2),
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
                Column(
                    modifier = Modifier.padding(horizontal = 64.dp, vertical = 32.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TitleBenefit(
                            painterResource = painterResource(id = R.drawable.subscription_infinity),
                            text = "Unlimited"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TokenIcon()
                    }

                    TitleBenefit(
                        painterResource = painterResource(id = R.drawable.subscription_star),
                        text = "Exclusive Functions"
                    )
                    SubTitleBenefit(text = "- Load PDF\n- GPT 4 Turbo")
                    TitleBenefit(
                        painterResource = painterResource(id = R.drawable.subscription_morechat),
                        text = "Higher Word Limit"
                    )
                    TitleBenefit(
                        painterResource = painterResource(id = R.drawable.subscription_noad),
                        text = "No ads"
                    )
                }
            }
        }

        /**
         * Products
         */
        // Product 1 - Weekly
        priceSub.value?.let {
            SubscriptionCard(
                durationTime = stringResource(R.string.subscription_week),
                smallText = "3-day FREE TRIAL, Cancel anytime,\nAuto renewable",
                allPrice = it,
                subscriptionOption = Option.OptionWeekly,
                userOption = userOption.value
            ) { userOption.value = Option.OptionWeekly }
        }

        // Product 2 - LifeTime
        priceInApp.value?.let {
            SubscriptionCard(
                durationTime = stringResource(R.string.subscription_lifetime),
                smallText = "Billed once",
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
                    .height(58.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                if (userOption.value == Option.OptionWeekly) {
                    Text(
                        text = "Start Free Trial & Plan",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                } else {
                    Text(
                        text = "Unlock Asked AI Pro",
                        color = MaterialTheme.colorScheme.onBackground,
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
        if (userOption.value == Option.OptionWeekly) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .padding(bottom = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Text(
                        text = "Cancel Anytime",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row {
                    Icon(
                        imageVector = Icons.Filled.Shield,
                        contentDescription = "Shield",
                        tint = Color.Green
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "No Payment Now",
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
                    .padding(bottom = 8.dp, end = 16.dp),
            ) {}
        }
    }
}

/**
 * Components
 *
 */
@Composable
fun TitleBenefit(painterResource: Painter, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource,
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(38.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text, color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun SubTitleBenefit(text: String) {
    Text(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 44.dp),
        text = text, color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.titleSmall
    )
}

