@file:OptIn(ExperimentalMaterial3Api::class)

package com.sginnovations.asked.presentation.ui.ui_components.topbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sginnovations.asked.presentation.ui.subscription.components.GiftOffer
import com.sginnovations.asked.presentation.ui.ui_components.tokens.TokenDisplay
import com.sginnovations.asked.presentation.viewmodel.TokenViewModel

@Composable
fun CameraTopBar(
    vmTokens: TokenViewModel,

    onNavigateFirstOffer: () -> Unit,
) {
//    val tokens = vmTokens.tokens.collectAsState()

    TopAppBar(
        title = {},
        navigationIcon = {},
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                GiftOffer(modifier = Modifier, onNavigateFirstOffer = { onNavigateFirstOffer() })
//                TokenDisplay(tokens = tokens, showPlus = false) { vmTokens.switchPointsVisibility() }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
        )
    )
}