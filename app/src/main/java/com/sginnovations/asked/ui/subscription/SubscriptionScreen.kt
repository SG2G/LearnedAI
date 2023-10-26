package com.sginnovations.asked.ui.subscription

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.ProductDetails
import com.sginnovations.asked.ui.ui_components.subscription.SubscriptionCard
import com.sginnovations.asked.viewmodel.BillingViewModel
import kotlin.collections.firstOrNull


enum class Option {
    Option1Months,
    Option6Months,
    Option12Months
}

@Composable
fun SubscriptionStateFull(
    vmBilling: BillingViewModel,
) {
    val userOption = remember { mutableStateOf(Option.Option1Months) }
    val productDetails = vmBilling.productDetails.value

    if (productDetails != null) {
        SubscriptionStateLess(
            productDetails,

            userOption
        )
    }
}

@Composable
fun SubscriptionStateLess(
    productDetails: ProductDetails,

    userOption: MutableState<Option>,
) {

    Column(
        Modifier.padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "AskedAI Pro",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
        }
        val subscriptionOfferDetailsList = productDetails.subscriptionOfferDetails
        val price = subscriptionOfferDetailsList
            ?.firstOrNull()?.pricingPhases?.pricingPhaseList
            ?.firstOrNull { it.priceAmountMicros > 0 }?.formattedPrice



        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            SubscriptionCard(
                numMonths = productDetails.zza().toString(),
                priceEachMonths = productDetails.name,
                allPrice = price.toString(),
                subscriptionOption = Option.Option12Months,
                userOption = userOption.value
            ) { userOption.value = Option.Option12Months }

            SubscriptionCard(
                numMonths = "1 Months",
                priceEachMonths = "6,98€ each month",
                allPrice = "6,98€",
                subscriptionOption = Option.Option12Months,
                userOption = userOption.value
            ) { userOption.value = Option.Option12Months }

            SubscriptionCard(
                numMonths = "12 Months",
                priceEachMonths = "5,6€ each month",
                allPrice = "67,2€",
                subscriptionOption = Option.Option1Months,
                userOption = userOption.value
            ) { userOption.value = Option.Option1Months }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Buy AskedAI Pro", color = MaterialTheme.colorScheme.onBackground,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

    }
}

