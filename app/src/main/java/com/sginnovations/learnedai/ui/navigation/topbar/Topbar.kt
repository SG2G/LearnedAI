@file:OptIn(ExperimentalMaterial3Api::class)

package com.sginnovations.learnedai.ui.navigation.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LearnedTopBar(
    currentScreenTitle: String?,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (canNavigateBack) {
        TopAppBar(
            title = {
                if (!currentScreenTitle.isNullOrEmpty()) {
                    Text(text = currentScreenTitle)
                }
            },
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "ArrowBack"
                    )
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
}