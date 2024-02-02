package com.sginnovations.asked.ui.subscription

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.billingclient.api.ProductDetails
import com.sginnovations.asked.R
import com.sginnovations.asked.ui.subscription.components.Feature
import com.sginnovations.asked.ui.subscription.components.SubTitleBenefit
import com.sginnovations.asked.ui.subscription.components.SubscriptionComparisonTable
import com.sginnovations.asked.ui.subscription.components.TitleBenefit
import com.sginnovations.asked.ui.ui_components.subscription.SubscriptionCard
import com.sginnovations.asked.ui.ui_components.tokens.TokenIcon
import com.sginnovations.asked.viewmodel.BillingViewModel
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

    onNavigateUp: () -> Unit,
) {
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
    priceDiscountSubAnnually: MutableState<String?>,
    priceSubMonthly: MutableState<String?>,

    priceMicrosSubAnnually: MutableState<Long?>,

    priceMicrosSubMonthly: MutableState<Long?>,
    priceCurrencySubAnnually: MutableState<String?>,

    userOption: MutableState<Option>,

    onNavigateUp: () -> Unit,

    onLaunchPurchaseFlow: (ProductDetails) -> Unit,
) {
    val scrollState = rememberScrollState()
    //Define the target date here
    val targetDate =
        LocalDateTime.of(2024, 5, 30, 0, 0) // Example: May 30, 2024 at midnight

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
//                /**
//                 * CountDown
//                 */
//                CountdownTimer(targetDate = targetDate)
//                Column(
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                ) {
//                    Text(
//                        text = "Choose Your Plan",
//                        style = MaterialTheme.typography.titleMedium,
//                    )
//                    Text(
//                        text = "Con nuestra suscripción premium, abres un mundo de posibilidades para ti y tu hijo. Experimenta una educación más fácil, interactiva y efectiva. ¡Todo en la palma de tu mano!",
//                        style = MaterialTheme.typography.bodyMedium,
//                    )
//                }
                /**
                 * Benefits
                 */
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            TitleBenefit(
                                painterResource = painterResource(id = R.drawable.token_fill0_wght400_grad0_opsz24),
                                text = stringResource(R.string.subscription_unlimited)
                            )
                            SubTitleBenefit(text = "Libertad total, tokens ilimitados")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            TitleBenefit(
                                painterResource = painterResource(id = R.drawable.camera_svgrepo_filled),
                                text = "Soluciones Instantáneas"
                            )
                            SubTitleBenefit(text = "Fotos ilimitadas, mensajes ilimitados y todas las cameras disponibles.")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            TitleBenefit(
                                painterResource = painterResource(id = R.drawable.sofa_svgrepo_filled),
                                text = "Respuestas sin limites"
                            )
                            SubTitleBenefit(text = "Mensajes ilimitados para el Asistente, todas sus dudas resueltas.")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            TitleBenefit(
                                painterResource = painterResource(id = R.drawable.book_bookmark_svgrepo_filled),
                                text = "Sabiduría a Su Alcance"
                            )
                            SubTitleBenefit(text = "Acceda a consejos extraídos directamente de libros de expertos, reunidos para usted.")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            TitleBenefit(
                                painterResource = painterResource(id = R.drawable.subscription_star2),
                                text = stringResource(R.string.subscription_exclusive_functions)
                            )
                            SubTitleBenefit(text = stringResource(id = R.string.subscription_higher_word_limit))
                        }
                    }
                }
            }
        }

        /**
         * Products
         */
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.subscription_try_7_free_days_of_asked_premium),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            // Product 1 - Weekly
            priceSubMonthly.value?.let { price ->
                SubscriptionCard(
                    durationTime = stringResource(R.string.subscription_monthly),
                    allPrice = price,
                    priceWithDiscount = null,
                    subscriptionOption = Option.OptionMonthly,
                    userOption = userOption.value
                ) { userOption.value = Option.OptionMonthly }
            }

            // Product 2 - LifeTime
            priceSubAnnually.value?.let { price ->
                SubscriptionCard(
                    durationTime = stringResource(R.string.subscription_annually),
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
                    text = stringResource(R.string.subscription_first_year_at) + " " + priceDiscountSubAnnually.value + stringResource(id = R.string.subscription_year) + ", " + stringResource(
                        R.string.then
                    ) + " " + priceSubAnnually.value + stringResource(id = R.string.subscription_year) + ". " + stringResource(
                        R.string.subscription_automatic_renewal_easy_cancellation),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall,
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
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )

            val features = listOf(
                Feature(stringResource(R.string.feature_unlimited_tokens), "-", "✓"),
                Feature(stringResource(R.string.feature_all_cameras_categories), "-", "✓"),
                Feature(stringResource(R.string.feature_unlimited_camera_messages),
                    stringResource(R.string.feature_3_day), "✓"),
                Feature(stringResource(R.string.feature_unlimited_assistant_messages),
                    stringResource(
                        R.string.feature_1_day
                    ), "✓"),
                Feature(stringResource(R.string.feature_access_full_guide), "-", "✓")
            )
            SubscriptionComparisonTable(features = features)
        }
    }
}