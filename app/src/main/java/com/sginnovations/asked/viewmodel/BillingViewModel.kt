package com.sginnovations.asked.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.google.common.collect.ImmutableList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "BillingViewModel"
@HiltViewModel
class BillingViewModel @Inject constructor(
    private val billingClient: BillingClient
) : ViewModel() {

    val productDetails: MutableState<ProductDetails?> = mutableStateOf(null)

    val billingResponseCode = MutableLiveData<Int>()

    init {
        Log.i(TAG, "init")
        connectToGooglePlay()
    }

    private fun connectToGooglePlay() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.

                    loadQueryProductsDetails()
                    Log.i(TAG, "onBillingSetupFinished: The BillingClient is ready")
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
        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    ImmutableList.of(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId("asked_test")
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()))
                .build()

        createProductList(queryProductDetailsParams)
    }

    private fun createProductList(queryProductDetailsParams: QueryProductDetailsParams) {
        Log.i(TAG, "createProductList")
        billingClient.queryProductDetailsAsync(queryProductDetailsParams) {
                billingResult,
                productDetailsList ->
            // check billingResult
            // process returned productDetailsList
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                productDetails.value = productDetailsList[0]
            }
        }
    }
}
