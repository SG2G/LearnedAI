@file:OptIn(ExperimentalMaterial3Api::class)

package com.sginnovations.asked.presentation.ui.ui_components.topbars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.FirstOfferScreen
import com.sginnovations.asked.ScreensDestinations
import com.sginnovations.asked.presentation.ui.subscription.components.GiftOffer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentalGuidanceTopBar(
    currentScreenTitle: String,

    onNavigateFirstOffer: (ScreensDestinations) -> Unit,
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = currentScreenTitle, color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        },
        actions = {
            GiftOffer(modifier = Modifier, onNavigateFirstOffer = { onNavigateFirstOffer(
                FirstOfferScreen
            ) })
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
        )
    )
}