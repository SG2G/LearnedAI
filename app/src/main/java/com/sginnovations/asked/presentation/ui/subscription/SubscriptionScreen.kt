package com.sginnovations.asked.presentation.ui.subscription

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.GppGood
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.android.billingclient.api.ProductDetails
import com.sginnovations.asked.R
import com.sginnovations.asked.domain.usecase.checkPermissionAndRequest
import com.sginnovations.asked.presentation.ui.subscription.components.AskedSubscriptionTitle
import com.sginnovations.asked.presentation.ui.subscription.components.Feature
import com.sginnovations.asked.presentation.ui.subscription.components.SubscriptionBenefits
import com.sginnovations.asked.presentation.ui.subscription.components.SubscriptionButton
import com.sginnovations.asked.presentation.ui.subscription.components.SubscriptionComparisonTable
import com.sginnovations.asked.presentation.ui.subscription.components.SubscriptionFlow
import com.sginnovations.asked.presentation.ui.ui_components.subscription.SubscriptionCard
import com.sginnovations.asked.presentation.viewmodel.AuthViewModel
import com.sginnovations.asked.presentation.viewmodel.BillingViewModel
import com.sginnovations.asked.presentation.viewmodel.IntentViewModel
import com.sginnovations.asked.presentation.viewmodel.PreferencesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
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

@Composable
fun SubscriptionStateFull(
    vmBilling: BillingViewModel,
    vmIntent: IntentViewModel,
    vmAuth: AuthViewModel,
    vmPreferences: PreferencesViewModel,

//    onNavigateUp: () -> Unit,
    onNavigateUpAndOffer: () -> Unit,
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
            vmPreferences,

            showOffer,
            showSubscriptionGift,

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

            onNavigateUpAndOffer,

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
                    /**
                     * Title
                     */
                    AskedSubscriptionTitle(onDismissRequest = {})
                }
            }
        }
    }
}

@Composable
fun SubscriptionStateLess(
    vmPreferences: PreferencesViewModel,

    showOffer: MutableState<Boolean>,
    showSubscriptionGift: MutableState<Boolean>,

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

    onNavigateUpAndOffer: () -> Unit,

    onSendEmail: () -> Unit,

    onLaunchPurchaseFlow: (ProductDetails) -> Unit,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val firstTimeLaunch = vmPreferences.firstTimeLaunch.value

    val selectedPlan = remember { mutableStateOf(productAnnually) }

    when (userOption.value) {
        Option.OptionMonthly -> selectedPlan.value = productMonthly
        Option.OptionAnnually -> selectedPlan.value = productAnnually
        Option.OptionAnnuallyRR -> selectedPlan.value = productAnnuallyRR
    }


    fun Context.getActivity(): Activity? {
        return when (this) {
            is Activity -> this
            is ContextWrapper -> baseContext.getActivity()
            else -> null
        }
    }

    val activity = context.getActivity()

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
                /**
                 * Title
                 */
                AskedSubscriptionTitle(onNavigateUpAndOffer)

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
//                    Text(
//                        text = stringResource(R.string.benefits),
//                        color = MaterialTheme.colorScheme.onBackground,
//                        style = MaterialTheme.typography.titleSmall,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))

                    /**
                     * Benefits and Subscription Flow
                     */
                    if (firstTimeLaunch) SubscriptionFlow() else SubscriptionBenefits()

                    /**
                     * Products
                     */
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
                    priceSubAnnually.value?.let { discountPrice ->
                        priceSubAnnually.value?.let { allPrice ->
                            SubscriptionCard(
                                subscriptionDuration = stringResource(R.string.subscription_annually),
                                allPrice = allPrice,
                                priceWithDiscount = discountPrice,
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


                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Bottom
                        ) {
//                            if (userOption.value != Option.OptionMonthly && !priceSubAnnuallyDiscount.value.isNullOrEmpty()) {
//                                Text(
//                                    modifier = Modifier.padding(horizontal = 16.dp),
//                                    text = stringResource(R.string.subscription_first_year_at) + " " + priceSubAnnuallyDiscount.value + stringResource(
//                                        id = R.string.subscription_year
//                                    ) + ", " + stringResource(
//                                        R.string.then
//                                    ) + " " + priceSubAnnually.value + stringResource(id = R.string.subscription_year) + ". " + stringResource(
//                                        R.string.subscription_automatic_renewal_easy_cancellation
//                                    ),
//                                    color = MaterialTheme.colorScheme.onBackground,
//                                    style = MaterialTheme.typography.labelMedium,
//                                    textAlign = TextAlign.Center
//                                )
//                            }
                            /**
                             * Button
                             */
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                SubscriptionButton(
                                    textButton = stringResource(R.string.subscription_unlock_asked_ai_pro),
                                    onLaunchPurchaseFlow = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            if (activity != null) {
                                                Log.d(
                                                    TAG,
                                                    "SubscriptionStateLess: checkPermissionAndRequest"
                                                )
                                                checkPermissionAndRequest(
                                                    activity = activity,
                                                    context = context,
                                                    permission = Manifest.permission.POST_NOTIFICATIONS,
                                                    permName = context.getString(R.string.notifications),
                                                    onPermissionGranted = {
                                                        selectedPlan.value.value?.let {
                                                            Log.d(
                                                                TAG,
                                                                "SubscriptionStateLess: 1 onLaunchPurchaseFlow"
                                                            )
                                                            onLaunchPurchaseFlow(it)
                                                        }
                                                    }
                                                )
                                            }
                                        } else {
                                            Log.d(
                                                TAG,
                                                "SubscriptionStateLess: 2 onLaunchPurchaseFlow"
                                            )
                                            selectedPlan.value.value?.let {
                                                onLaunchPurchaseFlow(it)
                                            }
                                        }

                                    }
                                )

                                TextButton(
                                    onClick = { onNavigateUpAndOffer() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(R.string.reject_offer),
                                        color = MaterialTheme.colorScheme.secondary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    /**
                     * Secure by
                     */
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedCard {
                            Row(
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Sharp.GppGood,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.secured_with_play_store),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    /**
                     * Small Letter
                     */
                    val smallLetterPadding =
                        PaddingValues(bottom = 8.dp, start = 20.dp, end = 20.dp)

                    Text(
                        modifier = Modifier.padding(smallLetterPadding),
                        text = stringResource(R.string.subscription_info_policy),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )


//                    Column(
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        /**
//                         * TRIAL
//                         */
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 32.dp),
//                            text = stringResource(R.string.subscription_pvu),
//                            color = MaterialTheme.colorScheme.onBackground,
//                            style = MaterialTheme.typography.titleMedium,
//                            textAlign = TextAlign.Center
//                        )
//                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
//                        Text(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp),
//                            text = stringResource(R.string.subscription_private_tutor_comparation),
//                            color = MaterialTheme.colorScheme.onSurface,
//                            style = MaterialTheme.typography.bodySmall,
//                            textAlign = TextAlign.Center
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                    }

                    val features = listOf(
                        Feature(
                            "No Ads",
                            "",
                            "✓"
                        ),
                        Feature(
                            stringResource(R.string.feature_unlimited_tokens),
                            stringResource(R.string.feature_2_day),
                            "✓"
                        ),
                        Feature(
                            stringResource(R.string.feature_unlimited_camera_messages),
                            stringResource(R.string.feature_2_day), "✓"
                        ),
                        Feature(
                            stringResource(R.string.feature_unlimited_assistant_messages),
                            stringResource(
                                R.string.feature_2_day
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
    }
}