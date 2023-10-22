@file:OptIn(ExperimentalMaterial3Api::class)

package com.sginnovations.asked.ui.ui_components.topbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sginnovations.asked.ui.ui_components.points.PointsDisplay
import com.sginnovations.asked.viewmodel.TokenViewModel

@Composable
fun ChatTopBar(
    vmTokens: TokenViewModel,
    currentScreenTitle: String,

    onNavigatePoints: () -> Unit,
    navigateUp: () -> Unit,
) {

    val tokens = vmTokens.tokens.collectAsState()

    TopAppBar(
        title = {
            currentScreenTitle
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "ArrowBack"
                )
            }
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                PointsDisplay(tokens = tokens,showPlus = true) { vmTokens.switchPointsVisibility() }
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
