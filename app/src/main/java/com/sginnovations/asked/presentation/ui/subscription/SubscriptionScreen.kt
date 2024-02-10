package com.sginnovations.asked.presentation.ui.subscription

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.android.billingclient.api.ProductDetails
import com.sginnovations.asked.R
import com.sginnovations.asked.presentation.ui.subscription.components.CountdownTimer
import com.sginnovations.asked.presentation.ui.subscription.components.Feature
import com.sginnovations.asked.presentation.ui.subscription.components.SubscriptionBenefits
import com.sginnovations.asked.presentation.ui.subscription.components.SubscriptionComparisonTable
import com.sginnovations.asked.presentation.ui.subscription.components.SubscriptionGift
import com.sginnovations.asked.presentation.ui.ui_components.subscription.SubscriptionCard
import com.sginnovations.asked.presentation.viewmodel.AuthViewModel
import com.sginnovations.asked.presentation.viewmodel.BillingViewModel
import com.sginnovations.asked.presentation.viewmodel.IntentViewModel
import com.sginnovations.asked.presentation.viewmodel.PreferencesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.Currency
import kotlin.math.roundToInt

private const val TAG = "SubscriptionStateFull"

enum class Option { OptionMonthly, OptionAnnually, OptionAnnuallyRR }

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

fun onLaunchPurchaseFlow(
    vmBilling: BillingViewModel,

    scope: CoroutineScope,
    activity: Activity,

    productDetails: ProductDetails,
) {
    if (activity != null) {
        scope.launch {
            Log.i(
                TAG,
                "SubscriptionStateFull: Activity not null, launching billing flow"
            )
            vmBilling.launchBillingFlowSubs(
                activity,
                productDetails,
            )
        }
    }
}

@Composable
fun SubscriptionStateFull(
    vmBilling: BillingViewModel,
    vmIntent: IntentViewModel,
    vmAuth: AuthViewModel,
    vmPreferences: PreferencesViewModel,

    onNavigateUp: () -> Unit,
) {
    val userAuth = vmAuth.userAuth.collectAsState()

    val userOption = remember { mutableStateOf(Option.OptionMonthly) }
    val showComposable = remember { mutableStateOf(false) }


    val productMonthly = vmBilling.productMonthly
    val productAnnually = vmBilling.productAnnually
    val productAnnuallyRR = vmBilling.productAnnuallyRR

    val priceSubAnnually = remember { mutableStateOf<String?>(null) }
    val priceSubAnnuallyDiscount = remember { mutableStateOf<String?>(null) }
    val priceSubAnnuallyRR = remember { mutableStateOf<String?>(null) }
    val priceSubMonthly = remember { mutableStateOf<String?>(null) }

    val priceMicrosSubAnnually = remember { mutableStateOf<Long?>(null) }
    val priceMicrosSubAnnuallyRR = remember { mutableStateOf<Long?>(null) }
    val priceMicrosSubMonthly = remember { mutableStateOf<Long?>(null) }

    val priceCurrencySubAnnually = remember { mutableStateOf<String?>(null) }

    val showSubscriptionGift = remember { mutableStateOf(false) }
    val showOffer = vmPreferences.showSubOffer

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

    LaunchedEffect(Unit) {
        if (vmPreferences.showSubOffer.value) {
            delay(7000)
            showSubscriptionGift.value = true
        }
    }
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

    if (showComposable.value) { //TODO CHANGE
        SubscriptionStateLess(
            showOffer,

            productMonthly,
            productAnnually,
            productAnnuallyRR,

            priceSubAnnually,
            priceSubAnnuallyDiscount,
            priceSubAnnuallyRR,
            priceSubMonthly,

            priceMicrosSubAnnually,
            priceMicrosSubMonthly,
            priceMicrosSubAnnuallyRR,

            priceCurrencySubAnnually,

            userOption,

            onNavigateUp,

            onSendEmail = { vmIntent.sendEmail(context, userAuth) },

            onLaunchPurchaseFlow = { productDetails ->
                if (activity != null) {
                    onLaunchPurchaseFlow(
                        vmBilling = vmBilling,

                        scope = scope,
                        activity = activity,

                        productDetails = productDetails,
                    )
                }
            }
        )
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
            }
        }
    }
    if (showOffer.value) {
        if (showSubscriptionGift.value) {
            SubscriptionGift(
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

            ) { scope.launch { vmPreferences.setShowSubOffer() } }
        }
    }
}

@Composable
fun SubscriptionStateLess(
    showOffer: MutableState<Boolean>,

    productMonthly: MutableState<ProductDetails?>,
    productAnnually: MutableState<ProductDetails?>,
    productAnnuallyRR: MutableState<ProductDetails?>,

    priceSubAnnually: MutableState<String?>,
    priceSubAnnuallyDiscount: MutableState<String?>,
    priceSubAnnuallyRR: MutableState<String?>,
    priceSubMonthly: MutableState<String?>,

    priceMicrosSubAnnually: MutableState<Long?>,
    priceMicrosSubMonthly: MutableState<Long?>,
    priceMicrosSubAnnuallyRR: MutableState<Long?>,

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
        Option.OptionAnnuallyRR -> selectedPlan.value = productAnnuallyRR
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
                if (!showOffer.value) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.oferta_de_lanzamiento),
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringResource(R.string.only_until_march_31st),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            CountdownTimer(
                                targetDate = targetDate,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }

                /**
                 * Benefits
                 */
                Card(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    colors = CardDefaults.cardColors(
                        MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    SubscriptionBenefits()
                    Spacer(modifier = Modifier.height(8.dp))
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
                text = stringResource(R.string.subscription_pvu),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.subscription_try_7_free_days_of_asked_premium),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.subscription_private_tutor_comparation),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        /**
         * Products
         */
        Card(
            modifier = Modifier.padding(horizontal = 8.dp),
            colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(25.dp)
        ) {
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
            if (showOffer.value) {
                priceSubAnnuallyRR.value?.let { discountPrice ->
                    priceSubAnnually.value?.let { allPrice ->
                        SubscriptionCard(
                            subscriptionDuration = stringResource(R.string.subscription_annually),
                            allPrice = allPrice,
                            priceWithDiscount = discountPrice,
                            priceAnnualMonthly = priceMicrosSubAnnuallyRR.value?.let { micro ->
                                priceCurrencySubAnnually.value?.let { currency ->
                                    formatPriceAnnualToMonthly(
                                        micro,
                                        currency
                                    )
                                }
                            },
                            savingsPercentage = priceMicrosSubMonthly.value?.let { microsMonth ->
                                priceMicrosSubAnnuallyRR.value?.let { microsAnnual ->
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
            } else {
                priceSubAnnually.value?.let { price ->
                    SubscriptionCard(
                        subscriptionDuration = stringResource(R.string.subscription_annually),
                        allPrice = price,
                        priceWithDiscount = priceSubAnnuallyDiscount.value,
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
                if (userOption.value == Option.OptionAnnually && priceSubAnnuallyDiscount.value != null) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(R.string.subscription_first_year_at) + " " + priceSubAnnuallyDiscount.value + stringResource(
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
                        onClick = { selectedPlan.value.value?.let { onLaunchPurchaseFlow(it) } },
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
                    }
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
            Feature(
                stringResource(R.string.feature_unlimited_tokens),
                stringResource(R.string.feature_3_day),
                "✓"
            ),
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