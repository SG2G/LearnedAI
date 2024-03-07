@file:OptIn(ExperimentalMaterial3Api::class)

package com.sginnovations.asked.presentation.ui.ui_components.topbars

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sginnovations.asked.FirstOfferScreen
import com.sginnovations.asked.R
import com.sginnovations.asked.ScreensDestinations
import com.sginnovations.asked.Subscription
import com.sginnovations.asked.presentation.ui.subscription.components.GiftOffer

@Composable
fun ChatsHistoryTopBar(
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
        navigationIcon = {},
        actions = {
            GiftOffer(modifier = Modifier, onNavigateFirstOffer = { onNavigateFirstOffer(FirstOfferScreen) })
//            Box(
//                modifier = Modifier
//                    .padding(end = 16.dp)
//                    .background(
//                        MaterialTheme.colorScheme.primary,
//                        RoundedCornerShape(5.dp)
//                    )
//                    .pointerInput(Unit) {
//                        detectTapGestures(
//                            onTap = { onNavigateSubscriptions(Subscription) }
//                        )
//                    }
//            ) {
//                Text(
//                    text = stringResource(R.string.premium), color = MaterialTheme.colorScheme.onPrimary,
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
//                )
//            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
        )
    )
}