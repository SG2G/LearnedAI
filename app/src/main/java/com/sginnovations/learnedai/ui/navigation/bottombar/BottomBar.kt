package com.sginnovations.learnedai.ui.navigation.bottombar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

@Composable
fun LearnedBottomBar(
    navController: NavController,
    canNavigateBack: Boolean,
    backStackEntry: NavBackStackEntry?,
) {
    if (!canNavigateBack) {
        val items = listOf(Home, Search, Profile)
        NavigationBar {
            items.forEach { screen ->
                NavigationBarItem(
                    icon = { Icon(screen.icon, contentDescription = null) },
                    label = { Text(screen.getLabel(LocalContext.current)) },
                    selected = false,
                    onClick = {
                        // This is where you handle navigation
                        navController.navigate(screen.route) {
                            // This ensures that the previous screen is removed from the backstack
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                                saveState = true
                            }
                            // Prevents multiple instances of the same screen in the backstack
                            launchSingleTop = true
                            // Restores the state when navigating back to the screen
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}