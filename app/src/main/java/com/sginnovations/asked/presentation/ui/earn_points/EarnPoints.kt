package com.sginnovations.asked.presentation.ui.earn_points

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sginnovations.asked.RefCode
import com.sginnovations.asked.Subscription
import com.sginnovations.asked.presentation.viewmodel.TokenViewModel

@Composable
fun EarnPoints(
    vmToken: TokenViewModel,

    navController: NavHostController,
) {
    val pointsScreenVisible = vmToken.pointsScreenVisible.collectAsStateWithLifecycle()

    if (pointsScreenVisible.value) {
        EarnPointsStateFul(
            vmToken = vmToken,
            onNavigateSubscriptions = { navController.navigate(route = Subscription.route) },
            onNavigateRefCode = { navController.navigate(route = RefCode.route) }
        )
    }
}