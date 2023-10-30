package com.sginnovations.asked.ui.earn_points

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sginnovations.asked.ui.top_bottom_bar.RefCode
import com.sginnovations.asked.ui.top_bottom_bar.Subscription
import com.sginnovations.asked.viewmodel.AdsViewModel
import com.sginnovations.asked.viewmodel.TokenViewModel

@Composable
fun EarnPoints(
    vmToken: TokenViewModel,
    vmAds: AdsViewModel,

    navController: NavHostController,
) {
    val pointsScreenVisible = vmToken.pointsScreenVisible.collectAsStateWithLifecycle()

    if (pointsScreenVisible.value) {
        EarnPointsStateFul(
            vmToken = vmToken,
            vmAds = vmAds,
            onNavigateSubscriptions = { navController.navigate(route = Subscription.route) },
            onNavigateRefCode = { navController.navigate(route = RefCode.route) }
        )
    }
}