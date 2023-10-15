@file:OptIn(ExperimentalMaterial3Api::class)

package com.sginnovations.learnedai.ui.navigation_bars.topbar

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.sginnovations.learnedai.ui.ui_components.topbars.ChatTopBar
import com.sginnovations.learnedai.ui.ui_components.topbars.PointsTopBar
import com.sginnovations.learnedai.ui.navigation_bars.Chat
import com.sginnovations.learnedai.ui.navigation_bars.Points
import com.sginnovations.learnedai.ui.ui_components.topbars.DefaultTopBar
import com.sginnovations.learnedai.viewmodel.TokenViewModel

private const val TAG = "LearnedTopBar"
@Composable
fun LearnedTopBar(
    vmTokens: TokenViewModel,

    currentScreenTitle: String?,
    canNavigateBack: Boolean,

    onNavigatePoints: () -> Unit,
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    if (canNavigateBack) {
        Log.i(TAG, "LearnedTopBar: ${currentScreenTitle.toString()}")
        when (currentScreenTitle) {
            Chat.getName(context) -> ChatTopBar(
                vmTokens,
                currentScreenTitle,
                onNavigatePoints,
                navigateUp
            )

            Points.getName(context) -> PointsTopBar(
                currentScreenTitle,
                navigateUp
            )
            else -> DefaultTopBar(
                currentScreenTitle,
                navigateUp
            )
        }
    }
}
