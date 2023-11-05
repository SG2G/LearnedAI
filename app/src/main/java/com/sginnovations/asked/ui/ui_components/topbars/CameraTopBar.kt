@file:OptIn(ExperimentalMaterial3Api::class)

package com.sginnovations.asked.ui.ui_components.topbars

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sginnovations.asked.ui.ui_components.tokens.TokenDisplay
import com.sginnovations.asked.viewmodel.TokenViewModel

@Composable
fun CameraTopBar(
    vmTokens: TokenViewModel,
    currentScreenTitle: String,
) {
    val tokens = vmTokens.tokens.collectAsState()

    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Take a picture", color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = TextStyle(
                        fontSize = 22.sp
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        navigationIcon = {},
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TokenDisplay(tokens = tokens, showPlus = true) { vmTokens.switchPointsVisibility() }
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