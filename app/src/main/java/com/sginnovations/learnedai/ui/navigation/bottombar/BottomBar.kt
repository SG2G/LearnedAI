package com.sginnovations.learnedai.ui.navigation.bottombar

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.sginnovations.learnedai.R

@Composable
fun LearnedBottomBar(
    navController: NavController,
    canNavigateBack: Boolean,
    backStackEntry: NavBackStackEntry?,
) {
    val currentRoute = remember { mutableStateOf(Camera.route) }

    if (!canNavigateBack) {
        val items = listOf(Camera, Chats, Profile)
        NavigationBar(
            modifier = Modifier.height(64.dp),
        ) {
            items.forEach { item ->
                val isSelected = item.route == backStackEntry?.destination?.route
                NavigationBarItem(
                    icon = {
                        AnimatedIconWithLine(item = item, isSelected = isSelected)
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

                            currentRoute.value = item.route
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        indicatorColor = Color(0xFF191c22)
                    )
                )
            }
        }
    }
}

@Composable
fun AnimatedIconWithLine(item: BottomBarDestinations, isSelected: Boolean) {
    val transition = updateTransition(targetState = isSelected, label = "transition")
    val lineWidth by transition.animateDp(
        transitionSpec = { tween(durationMillis = 500) },
        label = "lineWidth"
    ) { selected -> if (selected) 24.dp else 0.dp }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedIcon(item = item, isSelected = isSelected)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(lineWidth)
                .height(2.dp)
                .background(Color.White, RoundedCornerShape(percent = 50))
        )
    }
}
@Composable
fun AnimatedIcon(item: BottomBarDestinations,isSelected: Boolean) {
    Crossfade(targetState = isSelected, animationSpec = tween(durationMillis = 750),
        label = ""
    ) { selected ->
        if (selected) {
            Icon(
                painter = item.selectedIcon(),
                contentDescription = null
            )
        } else {
            Icon(
                painter = item.icon(),
                contentDescription = null
            )
        }
    }
}