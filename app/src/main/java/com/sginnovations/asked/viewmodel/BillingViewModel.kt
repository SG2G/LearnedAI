package com.sginnovations.asked.viewmodel

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.google.common.collect.ImmutableList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BillingViewModel"

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val billingClient: BillingClient,
) : ViewModel() {

    val productDetails = mutableStateOf<List<ProductDetails>>(emptyList())

    val billingResponseCode = MutableLiveData<Int>()

    init {
        viewModelScope.launch {
            Log.i(TAG, "init")
            connectToGooglePlay()
        }
    }

    private fun connectToGooglePlay() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.

                    Log.i(TAG, "onBillingSetupFinished: The BillingClient is ready")
                    loadQueryProductsDetails()
                    billingResponseCode.value = BillingClient.BillingResponseCode.OK

                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                billingResponseCode.value = BillingClient.BillingResponseCode.SERVICE_DISCONNECTED
                Log.i(TAG, "onBillingServiceDisconnected: Trying to reconnect")
                connectToGooglePlay()
            }
        })
    }

    private fun loadQueryProductsDetails() {
        Log.i(TAG, "loadQueryProductsDetails")
        // Query subscriptions
        val subscriptionList = ImmutableList.of(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("asked_subscription_weekly")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        val subscriptionQueryParams = QueryProductDetailsParams.newBuilder()
            .setProductList(subscriptionList)
            .build()

        // Query in-app products
        val inAppProductList = ImmutableList.of(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("asked_product_lifetime")
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val inAppProductQueryParams = QueryProductDetailsParams.newBuilder()
            .setProductList(inAppProductList)
            .build()

        createProductList(subscriptionQueryParams, inAppProductQueryParams)
    }

    private fun createProductList(
        queryProductDetailsParams: QueryProductDetailsParams,
        inAppProductQueryParams: QueryProductDetailsParams,
    ) {
        Log.i(TAG, "createProductList 1")
        billingClient.queryProductDetailsAsync(queryProductDetailsParams) {
                billingResult,
                productDetailsList,
            ->
            // check billingResult
            // process returned productDetailsList
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                productDetails.value += productDetailsList
            }
        }
        Log.i(TAG, "createProductList 2")
        billingClient.queryProductDetailsAsync(inAppProductQueryParams) {
                billingResult,
                productDetailsList,
            ->
            // check billingResult
            // process returned productDetailsList
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                productDetails.value += productDetailsList
            }
        }
    }

    fun launchBillingFlowSubs(activity: Activity, productDetails: ProductDetails) {
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

    fun launchBillingFlowInApp(activity: Activity, productDetails: ProductDetails) {
        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .build()

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
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
