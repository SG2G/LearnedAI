package com.sginnovations.asked.presentation.ui.top_bottom_bar.bottombar

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.sginnovations.asked.Auth
import com.sginnovations.asked.Camera
import com.sginnovations.asked.ChatsHistory
import com.sginnovations.asked.Constants.Companion.DARK_NAVIGATION_BAR_COLOR
import com.sginnovations.asked.Constants.Companion.LIGHT_NAVIGATION_BAR_COLOR
import com.sginnovations.asked.OnBoarding
import com.sginnovations.asked.ParentalAssist
import com.sginnovations.asked.ParentalGuidance
import com.sginnovations.asked.Profile
import com.sginnovations.asked.ScreensDestinations
import com.sginnovations.asked.presentation.viewmodel.PreferencesViewModel

private const val TAG = "LearnedBottomBar"

@Composable
fun LearnedBottomBar(
    vmPreferences: PreferencesViewModel,

    navController: NavController,
    currentScreen: ScreensDestinations?,
    canNavigateBack: Boolean,
    backStackEntry: NavBackStackEntry?,
) {
    val context = LocalContext.current

    val theme = vmPreferences.theme

    val indicatorColor = if (!theme.value) {
        LIGHT_NAVIGATION_BAR_COLOR
    } else {
        DARK_NAVIGATION_BAR_COLOR
    }
    val containerColor = if (!theme.value) {
        LIGHT_NAVIGATION_BAR_COLOR
    } else {
        DARK_NAVIGATION_BAR_COLOR
    }
    val unselectedColor = if (!theme.value) {
        Color(0xFF46464F)
    } else {
        Color(0xFF665B61)
    }

    val selectedIconColor = if (!theme.value) {
        Color(0xFF554D51)
    } else {
        Color(0xFFB6A9AF)
    }

    Log.i(
        TAG,
        "canNavigateBack: $canNavigateBack backStackEntry: ${backStackEntry.toString()} navController: $navController"
    )
    if (!canNavigateBack) {
        Log.i(TAG, "currentScreenTitle: $currentScreen")
        if (currentScreen?.route != Auth.route) {
            if (currentScreen?.route != OnBoarding.route) {
                val items = listOf(Camera, ChatsHistory, ParentalAssist, ParentalGuidance, Profile)
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.6f)
                            .background(MaterialTheme.colorScheme.onSurfaceVariant)
                            .height(2.dp)
                    ) {
                    }
                    NavigationBar(
                        containerColor = containerColor,
                    ) {
                        items.forEach { item ->
                            val isSelected = item.route == backStackEntry?.destination?.route
                            NavigationBarItem(
                                icon = {
                                    AnimatedIconWithLine(
                                        item = item,
                                        isSelected = isSelected
                                    )
                                },
                                selected = isSelected,
                                onClick = {
                                    // This is where you handle navigation
                                    navController.navigate(item.route) {
                                        // This ensures that the previous screen is removed from the backstack
                                        popUpTo(navController.currentDestination?.route ?: "") {
                                            inclusive = true
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = selectedIconColor,
                                    indicatorColor = indicatorColor,
                                    unselectedIconColor = unselectedColor,
                                    unselectedTextColor = unselectedColor,
                                ),
                                label = {
                                    Text(
                                        text = item.getBottomName(context),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedIconWithLine(item: ScreensDestinations, isSelected: Boolean) {
    val transition = updateTransition(targetState = isSelected, label = "transition")
    val lineWidth by transition.animateDp(
        transitionSpec = { tween(durationMillis = 500) },
        label = "lineWidth"
    ) { selected -> if (selected) 24.dp else 0.dp }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedIcon(item = item, isSelected = isSelected)
//        Spacer(modifier = Modifier.height(4.dp))
//        Box(
//            modifier = Modifier
//                .width(lineWidth)
//                .height(2.dp)
//                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(percent = 50))
//        )
    }
}

@Composable
fun AnimatedIcon(item: ScreensDestinations, isSelected: Boolean) {
    val scale = remember { androidx.compose.animation.core.Animatable(1f) }
    LaunchedEffect(isSelected) {
        scale.animateTo(
            targetValue = if (isSelected) 0.7f else 1f,
            animationSpec = tween(100, easing = LinearEasing)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Crossfade(
        targetState = isSelected, animationSpec = tween(durationMillis = 750),
        label = ""
    ) { selected ->
        (if (selected) item.selectedIcon else item.icon)?.let { iconRedId ->
            val icon = painterResource(id = iconRedId)
            Icon(
                modifier = Modifier
                    .scale(scale.value)
                    .size(28.dp),
                painter = icon,
                contentDescription = null
            )
        }
    }
}



