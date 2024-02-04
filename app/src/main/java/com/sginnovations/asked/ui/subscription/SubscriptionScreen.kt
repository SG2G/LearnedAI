package com.sginnovations.asked.ui.subscription

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.android.billingclient.api.ProductDetails
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.subscription.components.CountdownTimer
import com.sginnovations.asked.ui.subscription.components.Feature
import com.sginnovations.asked.ui.subscription.components.SubTitleBenefit
import com.sginnovations.asked.ui.subscription.components.SubscriptionComparisonTable
import com.sginnovations.asked.ui.subscription.components.TitleBenefit
import com.sginnovations.asked.ui.ui_components.subscription.SubscriptionCard
import com.sginnovations.asked.viewmodel.AuthViewModel
import com.sginnovations.asked.viewmodel.BillingViewModel
import com.sginnovations.asked.viewmodel.IntentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.Currency
import kotlin.math.roundToInt

private const val TAG = "SubscriptionStateFull"

enum class Option { OptionMonthly, OptionAnnually }

fun formatPriceAnnualToMonthly(priceAmountMicros: Long, priceCurrencyCode: String): String {
    val amount = (priceAmountMicros / 1_000_000.0) / 12
    val format = NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance(priceCurrencyCode)
        maximumFractionDigits = 2
    }
    return format.format(amount)
}

fun calculateSavingsPercentage(microsMonth: Long, microsAnnual: Long): Int {
    val month = (microsMonth / 1_000_000.0) * 12
    val annual = microsAnnual / 1_000_000.0
    return (((month - annual) / month) * 100).roundToInt()
}

@Composable
fun SubscriptionStateFull(
    vmBilling: BillingViewModel,
    vmIntent: IntentViewModel,
    vmAuth: AuthViewModel,

    onNavigateUp: () -> Unit,
) {
    val userAuth = vmAuth.userAuth.collectAsState()

    val userOption = remember { mutableStateOf(Option.OptionMonthly) }
    val showComposable = remember { mutableStateOf(false) }

    val productMonthly = vmBilling.productMonthly
    val productAnnually = vmBilling.productAnnually

    val priceSubAnnually = remember { mutableStateOf<String?>(null) }
    val priceDiscountSubAnnually = remember { mutableStateOf<String?>(null) }
    val priceSubMonthly = remember { mutableStateOf<String?>(null) }

    val priceMicrosSubAnnually = remember { mutableStateOf<Long?>(null) }
    val priceMicrosSubMonthly = remember { mutableStateOf<Long?>(null) }
    val priceCurrencySubAnnually = remember { mutableStateOf<String?>(null) }

    Log.d(
        TAG,
        "productMonthly-> ${productMonthly.value.toString()} productAnnually-> ${productAnnually.value.toString()} "
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
            Log.d(TAG, "size: ${productAnnually.value?.subscriptionOfferDetails?.size} ")

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
            } else {
                priceDiscountSubAnnually.value =
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
                "$attempts priceSubMonthly -> $priceSubMonthly / priceSubAnnually -> $priceSubAnnually / priceDiscountSubAnnually -> ${priceDiscountSubAnnually.value}"
            )

            attempts++
        }

        // Check if priceInApp is not null before setting showComposable to true
        if (priceSubAnnually.value != null && priceSubMonthly.value != null) {
            showComposable.value = true
        }
    }

    if (showComposable.value) { //TODO CHANGE
        SubscriptionStateLess(
            productMonthly,
            productAnnually,

            priceSubAnnually,
            priceDiscountSubAnnually,
            priceSubMonthly,

            priceMicrosSubAnnually,
            priceMicrosSubMonthly,
            priceCurrencySubAnnually,

            userOption,

            onNavigateUp,

            onSendEmail = { vmIntent.sendEmail(context, userAuth) },

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
        ) {
            Box {
                Column(
                    verticalArrangement = Arrangement.Top
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
                            text = "Asked Premium",
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                        IconButton(onClick = { }) {}
                    }
                }
//                Column(
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "If you still seeing this screen, send a email to:",
//                        style = MaterialTheme.typography.bodyMedium,
//                        modifier = Modifier.padding(16.dp)
//                    )
//                    Text(
//                        text = " askedaihelp@gmail.com",
//                        style = MaterialTheme.typography.labelMedium,
//                        modifier = Modifier.padding(16.dp)
//                    )
//                }
            }
        }
    }
}

@Composable
fun SubscriptionStateLess(
    productMonthly: MutableState<ProductDetails?>,
    productAnnually: MutableState<ProductDetails?>,

    priceSubAnnually: MutableState<String?>,
    priceDiscountSubAnnually: MutableState<String?>,
    priceSubMonthly: MutableState<String?>,

    priceMicrosSubAnnually: MutableState<Long?>,

    priceMicrosSubMonthly: MutableState<Long?>,
    priceCurrencySubAnnually: MutableState<String?>,

    userOption: MutableState<Option>,

    onNavigateUp: () -> Unit,

    onSendEmail: () -> Unit,

    onLaunchPurchaseFlow: (ProductDetails) -> Unit,
) {
    val scrollState = rememberScrollState()
    //Define the target date here
    val targetDate =
        LocalDateTime.of(2024, 3, 31, 0, 0) // Example: 31 March

    val selectedPlan = remember { mutableStateOf(productAnnually) }

    when (userOption.value) {
        Option.OptionMonthly -> selectedPlan.value = productMonthly
        Option.OptionAnnually -> selectedPlan.value = productAnnually
    }

    Log.i(TAG, "userOption -> ${userOption.value} selectedPlan -> ${selectedPlan.value}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
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
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "Cancel"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Asked",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = " Premium",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "Cancel2",
                            tint = Color.Transparent
                        )
                    }
                }

                /**
                 * CountDown
                 */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Oferta de Lanzamiento",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        CountdownTimer(targetDate = targetDate)
                        Text(
                            text = stringResource(R.string.only_until_march_31st),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                }

                /**
                 * Benefits
                 */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        TitleBenefit(
                            painterResource = painterResource(id = R.drawable.token_fill0_wght400_grad0_opsz24),
                            text = stringResource(R.string.subscription_unlimited)
                        )
                        SubTitleBenefit(text = stringResource(R.string.subscription_unlimited_text))


                        Spacer(modifier = Modifier.height(8.dp))

                        TitleBenefit(
                            painterResource = painterResource(id = R.drawable.camera_svgrepo_filled),
                            text = stringResource(R.string.subscription_camera_title)
                        )
                        SubTitleBenefit(text = stringResource(R.string.subscription_camera_text))

                    }

                    Column {
                        TitleBenefit(
                            painterResource = painterResource(id = R.drawable.sofa_svgrepo_filled),
                            text = stringResource(R.string.subscription_assistant_title)
                        )
                        SubTitleBenefit(text = stringResource(R.string.subscription_assistant_text))

                        Spacer(modifier = Modifier.height(8.dp))

                        TitleBenefit(
                            painterResource = painterResource(id = R.drawable.book_bookmark_svgrepo_filled),
                            text = stringResource(R.string.subscription_guide_title)
                        )
                        SubTitleBenefit(text = stringResource(R.string.subscription_guide_text))

                        Spacer(modifier = Modifier.height(8.dp))

                        TitleBenefit(
                            painterResource = painterResource(id = R.drawable.subscription_star2),
                            text = stringResource(R.string.subscription_exclusive_functions)
                        )
                        SubTitleBenefit(text = stringResource(id = R.string.subscription_higher_word_limit))
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            /**
             * TRIAL
             */
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.subscription_try_7_free_days_of_asked_premium),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.subscription_private_tutor_comparation),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )


            /**
             * Products
             */
            // Product 1 - Weekly
            priceSubMonthly.value?.let { price ->
                SubscriptionCard(
                    subscriptionDuration = stringResource(R.string.subscription_monthly),
                    allPrice = price,
                    priceWithDiscount = null,
                    subscriptionOption = Option.OptionMonthly,
                    userOption = userOption.value
                ) { userOption.value = Option.OptionMonthly }
            }

            // Product 2 - LifeTime
            priceSubAnnually.value?.let { price ->
                SubscriptionCard(
                    subscriptionDuration = stringResource(R.string.subscription_annually),
                    allPrice = price,
                    priceWithDiscount = priceDiscountSubAnnually.value,
                    priceAnnualMonthly = priceMicrosSubAnnually.value?.let { micro ->
                        priceCurrencySubAnnually.value?.let { currency ->
                            formatPriceAnnualToMonthly(
                                micro,
                                currency
                            )
                        }
                    },
                    savingsPercentage = priceMicrosSubMonthly.value?.let { microsMonth ->
                        priceMicrosSubAnnually.value?.let { microsAnnual ->
                            calculateSavingsPercentage(
                                microsMonth, microsAnnual
                            )
                        }
                    },
                    subscriptionOption = Option.OptionAnnually,
                    userOption = userOption.value
                ) { userOption.value = Option.OptionAnnually }
            }
        }


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            if (userOption.value == Option.OptionAnnually && priceDiscountSubAnnually.value != null) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(R.string.subscription_first_year_at) + " " + priceDiscountSubAnnually.value + stringResource(
                        id = R.string.subscription_year
                    ) + ", " + stringResource(
                        R.string.then
                    ) + " " + priceSubAnnually.value + stringResource(id = R.string.subscription_year) + ". " + stringResource(
                        R.string.subscription_automatic_renewal_easy_cancellation
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center
                )
            }
            /**
             * Button
             */
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
                    shape = RoundedCornerShape(30.dp)
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
            val smallLetterPadding = PaddingValues(bottom = 8.dp, start = 20.dp, end = 20.dp)

            Text(
                modifier = Modifier.padding(smallLetterPadding),
                text = stringResource(R.string.subscription_info_policy),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )

            val features = listOf(
                Feature(stringResource(R.string.feature_unlimited_tokens),stringResource(R.string.feature_3_day), "✓"),
                Feature(
                    stringResource(R.string.feature_unlimited_camera_messages),
                    stringResource(R.string.feature_3_day), "✓"
                ),
                Feature(
                    stringResource(R.string.feature_unlimited_assistant_messages),
                    stringResource(
                        R.string.feature_1_day
                    ), "✓"
                ),
                Feature(stringResource(R.string.feature_access_full_guide), "-", "✓"),
                Feature(stringResource(R.string.feature_all_cameras_categories), "-", "✓"),
            )
            SubscriptionComparisonTable(features = features)
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.subscription_having_issues_with_your_subscription),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.subscription_contact_us_at_askedaihelp_gmail_com),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium.merge(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSendEmail() },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}