package com.sginnovations.asked.viewmodel

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
import com.google.common.collect.ImmutableList
import com.sginnovations.asked.Constants.Companion.MAX_RECONNECTION_ATTEMPTS
import com.sginnovations.asked.Constants.Companion.RECONNECTION_DELAY_MILLIS
import com.sginnovations.asked.domain.firebase.setters.SetPremiumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "BillingViewModel"

@HiltViewModel
class BillingViewModel @Inject constructor(
    @ApplicationContext appContext: Context,

    private val setPremiumUseCase: SetPremiumUseCase,
) : ViewModel() {

    val productMonthly = mutableStateOf<ProductDetails?>(null)
    val productAnnually = mutableStateOf<ProductDetails?>(null)

    val billingResponseCode = MutableLiveData<Int>()

    private var reconnectionAttempts = 0 // Counter for tracking reconnection attempts

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        viewModelScope.launch {
            Log.d(TAG, "purchasesUpdatedListener ")
            onPurchasesUpdated(billingResult, purchases)
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
                    // Set the PurchasesUpdatedListener
                    PurchasesUpdatedListener { billingResult, purchases ->
                        viewModelScope.launch {
                            onPurchasesUpdated(billingResult, purchases)
                        }
                    }
                } else {
                    connectionDeferred.complete(false)
                }
            }

            override fun onBillingServiceDisconnected() {
                if (reconnectionAttempts < MAX_RECONNECTION_ATTEMPTS) {
                    // Try to restart the connection on the next request to Google Play by calling the startConnection() method.
                    billingResponseCode.value = BillingClient.BillingResponseCode.SERVICE_DISCONNECTED
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
                .build()
        )

        val subscriptionQueryParams = QueryProductDetailsParams.newBuilder()
            .setProductList(subscriptionList)
            .build()

        // Query in-app products
//        val inAppProductList = ImmutableList.of(
//            QueryProductDetailsParams.Product.newBuilder()
//                .setProductId("asked_product_lifetime")
//                .setProductType(BillingClient.ProductType.INAPP)
//                .build()
//        )
//
//        val inAppProductQueryParams = QueryProductDetailsParams.newBuilder()
//            .setProductList(inAppProductList)
//            .build()

        createProductList(subscriptionQueryParams)
    }

    private suspend fun createProductList(
        queryProductDetailsParams: QueryProductDetailsParams,
    ) {
        Log.i(TAG, "createProductList 1")
        billingClient.queryProductDetailsAsync(queryProductDetailsParams) {
                billingResult,
                productDetailsList,
            ->
            // check billingResult
            // process returned productDetailsList
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                productMonthly.value = productDetailsList[0]
                productAnnually.value = productDetailsList[1]
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
                    delay(20000) // 5 min
                }
            } else {
                //is not connected to google play
                Log.d(TAG, "Failed to connect to Google Play")
            }
        }

    }

    private suspend fun checkSubscriptions()= suspendCoroutine { continuation ->
        viewModelScope.launch {
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
//                .setProductType(BillingClient.ProductType.INAPP)
                .build()

            billingClient.queryPurchasesAsync(params) { _, purchases ->
                if (purchases.isEmpty()) {
                    Log.d(TAG, "Purchases empty")
                    viewModelScope.launch {
                        setPremiumUseCase(false)

                    }
                } else {
                    Log.d(TAG, "Have purchases $purchases")
                    viewModelScope.launch {
                        setPremiumUseCase(true)
                    }
                }
            }
            continuation.resume(Unit)
        }
    }


    private suspend fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: List<Purchase>?,
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            Log.d(TAG, "onPurchasesUpdated: BillingClient.BillingResponseCode.OK")
            for (purchase in purchases) {
                verifySubPurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.d(TAG, "onPurchasesUpdated: user cancelling the purchase flow")
        } else {
            // Handle any other error codes.
            Log.d(TAG, "onPurchasesUpdated: Other errors ${billingResult.responseCode}")
        }
    }

    private suspend fun verifySubPurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user, then acknowledge the purchase
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.d(TAG, "verifySubPurchase: Purchase acknowledged successfully")
                        // Purchase acknowledged successfully, grant entitlement to the user
                        viewModelScope.launch {
                            setPremiumUseCase(true)
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

    suspend fun launchBillingFlowSubs(activity: Activity, productDetails: ProductDetails) {
        val subscriptionOfferDetails = productDetails.subscriptionOfferDetails
        if (!subscriptionOfferDetails.isNullOrEmpty()) {

            val selectedOfferToken = subscriptionOfferDetails[0].offerToken

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

//    suspend fun launchBillingFlowInApp(activity: Activity, productDetails: ProductDetails) {
//        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
//            .setProductDetails(productDetails)
//            .build()
//
//        val billingFlowParams = BillingFlowParams.newBuilder()
//            .setProductDetailsParamsList(listOf(productDetailsParams))
//            .build()
//
//        // Launch the billing flow
//        val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)
//
//        // Check the result
//        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//            Log.i(TAG, "Billing flow launched successfully")
//        } else {
//            Log.e(TAG, "Failed to launch billing flow: ${billingResult.debugMessage}")
//        }
//    }

}
