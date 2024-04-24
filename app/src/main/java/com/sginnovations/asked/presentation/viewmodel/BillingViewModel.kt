package com.sginnovations.asked.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.google.common.collect.ImmutableList
import com.sginnovations.asked.Constants.Companion.MAX_RECONNECTION_ATTEMPTS
import com.sginnovations.asked.Constants.Companion.RECONNECTION_DELAY_MILLIS
import com.sginnovations.asked.data.AskedNotificationService
import com.sginnovations.asked.domain.repository.AppsFlyerRepository
import com.sginnovations.asked.domain.usecase.firebase.setters.SetPremiumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "BillingViewModel"
private const val FIVE_DAYS = 5 * 24 * 60 * 60 * 1000L
private const val MINUTE = 1 * 60 * 1000L

@HiltViewModel
class BillingViewModel @Inject constructor(
    @ApplicationContext appContext: Context,

    private val setPremiumUseCase: SetPremiumUseCase,
    private val askedNotificationService: AskedNotificationService,
    private val appsFlyerRepository: AppsFlyerRepository,
) : ViewModel() {

    val isPremium = mutableStateOf(false)

    private var currentProductSKU: String? = null
    private var currentProductPrice: Double? = null

    val productMonthly = mutableStateOf<ProductDetails?>(null)
    val productAnnually = mutableStateOf<ProductDetails?>(null)
    val productAnnuallyRR = mutableStateOf<ProductDetails?>(null)
    val productAnnuallyRR2 = mutableStateOf<ProductDetails?>(null)

    val subsLoaded = mutableStateOf<Boolean>(false)

    val billingResponseCode = MutableLiveData<Int>()

    private var reconnectionAttempts = 0 // Counter for tracking reconnection attempts

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        viewModelScope.launch {
            // This code block is executed when a purchase is completed
            Log.d(TAG, "purchasesUpdatedListener ")
            onPurchasesUpdated(billingResult, purchases, appContext)
        }
    }

    private val billingClient = BillingClient.newBuilder(appContext)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    suspend fun connectToGooglePlay(): CompletableDeferred<Boolean> {
        val connectionDeferred = CompletableDeferred<Boolean>()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.i(TAG, "onBillingSetupFinished: The BillingClient is ready")

                    connectionDeferred.complete(true)

                    Log.i(TAG, "onBillingSetupFinished: following billing")

                    viewModelScope.launch { loadQueryProductsDetails() }
                    billingResponseCode.value = BillingClient.BillingResponseCode.OK
                } else {
                    connectionDeferred.complete(false)
                }
            }

            override fun onBillingServiceDisconnected() {
                if (reconnectionAttempts < MAX_RECONNECTION_ATTEMPTS) {
                    // Try to restart the connection on the next request to Google Play by calling the startConnection() method.
                    billingResponseCode.value =
                        BillingClient.BillingResponseCode.SERVICE_DISCONNECTED
                    Log.i(TAG, "onBillingServiceDisconnected: Trying to reconnect")
                    viewModelScope.launch {
                        delay(RECONNECTION_DELAY_MILLIS)
                        reconnectionAttempts++
                        connectToGooglePlay()
                    }
                } else {
                    // Handle the case where the maximum number of reconnection attempts is reached.
                    //TODO CRASH COME
                    connectionDeferred.complete(false)
                }
            }
        })
        return connectionDeferred
    }

    private suspend fun loadQueryProductsDetails() {
        Log.i(TAG, "loadQueryProductsDetails")
        // Query subscriptions
        val subscriptionList = ImmutableList.of(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("asked_subscription_monthly")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("asked_subscription_annually")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("asked_subscription_annually_discount")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("asked_subscription_annually_discount2")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
        )

        val subscriptionQueryParams = QueryProductDetailsParams.newBuilder()
            .setProductList(subscriptionList)
            .build()

        createProductList(subscriptionQueryParams)
    }

    private suspend fun createProductList(
        queryProductDetailsParams: QueryProductDetailsParams,
    ) {
        Log.i(TAG, "createProductList")
        billingClient.queryProductDetailsAsync(queryProductDetailsParams) {
                billingResult,
                productDetailsList,
            ->
            // check billingResult
            // process returned productDetailsList
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                // Clear or reset previous values if necessary
                productAnnually.value = null
                productMonthly.value = null
                productAnnuallyRR.value = null
                productAnnuallyRR2.value = null

                // Assign products based on their SKUs
                for (productDetails in productDetailsList) {
                    when (productDetails.productId) {
                        "asked_subscription_annually" -> productAnnually.value = productDetails
                        "asked_subscription_monthly" -> productMonthly.value = productDetails
                        "asked_subscription_annually_discount" -> productAnnuallyRR.value =
                            productDetails

                        "asked_subscription_annually_discount2" -> productAnnuallyRR2.value =
                            productDetails
                    }
                }
                subsLoaded.value = true
                // Log to verify the assignments (optional)
                Log.i(
                    TAG,
                    "Annual: ${productAnnually.value?.productId}, Monthly: ${productMonthly.value?.productId}, Annual RR: ${productAnnuallyRR.value?.productId}"
                )
            }
        }
    }

    init {
        viewModelScope.launch {
            Log.d(TAG, "Init")
            val isConnected = connectToGooglePlay().await()
            Log.d(TAG, isConnected.toString())
            if (isConnected) {
                Log.d(TAG, "Starting while")
                while (true) {
                    try {
                        checkSubscriptions()
                    } catch (e: Exception) {
                        // handle error
                        Log.e(TAG, "Error checking subscriptions", e)
                    }
                    delay(20000 * 4) // 5 min
                }
            } else {
                //is not connected to google play
                Log.d(TAG, "Failed to connect to Google Play")
            }
        }

    }

    private suspend fun checkSubscriptions() = suspendCoroutine { continuation ->
        viewModelScope.launch {
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()

            billingClient.queryPurchasesAsync(params) { _, purchases ->
                if (purchases.isEmpty()) {
                    viewModelScope.launch {
                        Log.d(TAG, "Purchases empty")
                        isPremium.value  = setPremiumUseCase(false)
                        Log.d("PremiumRun 2", "${isPremium.value} ")
                    }
                } else {
                    viewModelScope.launch {
                        Log.d(TAG, "Have purchases $purchases")
                        isPremium.value = setPremiumUseCase(true)
                        Log.d("PremiumRun 3", "${isPremium.value} ")
                    }
                }
            }
            continuation.resume(Unit)
        }
    }


    private suspend fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: List<Purchase>?,
        appContext: Context,
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            Log.d(TAG, "onPurchasesUpdated: BillingClient.BillingResponseCode.OK")
            val fiveDaysInMillis = FIVE_DAYS
            askedNotificationService.scheduleNotification(fiveDaysInMillis)

            for (purchase in purchases) {
                verifySubPurchase(purchase, appContext)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.d(TAG, "onPurchasesUpdated: user cancelling the purchase flow")
        } else {
            // Handle any other error codes.
            Log.d(TAG, "onPurchasesUpdated: Other errors ${billingResult.responseCode}")
        }
    }

    private suspend fun verifySubPurchase(purchase: Purchase, applicationContext: Context) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user, then acknowledge the purchase
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        // Purchase acknowledged successfully, grant entitlement to the user
                        // Log the purchase event with AppsFlyer
                        viewModelScope.launch {
                            Log.d(TAG, "verifySubPurchase: Purchase acknowledged successfully")
                            isPremium.value = true
                            setPremiumUseCase(true)

                            // AppsFlyer and Facebook Log
                            appsFlyerRepository.logSubscriptionEvent(
                                currentProductSKU = currentProductSKU,
                                currentProductPrice = currentProductPrice,
                                applicationContext = applicationContext,
                            )
                        }
                    } else {
                        // Handle other response codes
                        Log.d(TAG, "verifySubPurchase: Handle other response codes")
                    }
                }
            }
        } else {
            Log.d(TAG, "verifySubPurchase: Not purchased")
        }
    }

    suspend fun launchBillingFlowSubs(
        activity: Activity,
        productDetails: ProductDetails,
    ) {
        val subscriptionOfferDetails = productDetails.subscriptionOfferDetails
        if (!subscriptionOfferDetails.isNullOrEmpty()) {

            val selectedOfferToken = subscriptionOfferDetails[0].offerToken

            // Store SKU and price for later use
            currentProductSKU = productDetails.productId // This is your SKU
            val priceAmountMicros =
                subscriptionOfferDetails[0].pricingPhases.pricingPhaseList[0].priceAmountMicros
            val priceInUnits = priceAmountMicros / 1_000_000.0
            currentProductPrice = priceInUnits

            Log.d(
                TAG, "launchBillingFlowSubs: currentProductSKU -> $currentProductSKU \n" +
                        "currentProductPrice -> $currentProductPrice"
            )

            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(selectedOfferToken)
                    .build()
            )
            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()

            // Launch the billing flow
            val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)

            // Check the result
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.i(TAG, "Billing flow launched successfully")
            } else {
                Log.e(TAG, "Failed to launch billing flow: ${billingResult.debugMessage}")
            }

        }
    }
}
