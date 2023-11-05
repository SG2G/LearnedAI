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

//@Module
//@InstallIn(SingletonComponent::class)
//object BillingModule {
//    @Provides
//    @Singleton
//    fun provideBillingClient(
//        @ApplicationContext appContext: Context
//    ): BillingClient {
//        return BillingClient.newBuilder(appContext)
//            .enablePendingPurchases()
//            .build()
//    }
//}
