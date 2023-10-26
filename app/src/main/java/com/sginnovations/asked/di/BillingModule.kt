package com.sginnovations.asked.di

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingModule {
    @Provides
    @Singleton
    fun provideBillingClient(
        @ApplicationContext appContext: Context,
        purchasesUpdatedListener: PurchasesUpdatedListener
    ): BillingClient {
        return BillingClient.newBuilder(appContext)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    @Provides
    fun providePurchasesUpdatedListener(): PurchasesUpdatedListener {
        return PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
        }
    }
}
