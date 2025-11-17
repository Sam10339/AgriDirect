package com.example.agridirect20

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Represents one item in the bottom navigation bar.
 *
 * @param route The navigation route to navigate to when selected.
 * @param label The text shown under the icon.
 * @param iconRes The drawable resource used as the nav icon.
 *
 * USAGE:
 * BottomNavItem("home", "Home", R.drawable.ic_nav_home)
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val iconRes: Int
)

/**
 * Bottom navigation bar for the main sections of the app.
 *
 * Appears on most screens (except Sign In). Handles switching between
 * Home, Farms, Blog, Cart, and Menu.
 *
 * The navController handles restoring state and preventing duplicate destinations.
 *
 * USAGE (AppNav):
 * Scaffold(bottomBar = { BottomNavBar(navController) }) { innerPadding -> ... }
 */
@Composable
fun BottomNavBar(navController: NavHostController) {

    // All available bottom nav tabs
    val items = listOf(
        BottomNavItem("home", "Home", R.drawable.ic_nav_home),
        BottomNavItem("farms", "Farms", R.drawable.ic_nav_farms),
        BottomNavItem("blog", "Blog", R.drawable.ic_nav_blog),
        BottomNavItem("cart", "Cart", R.drawable.ic_nav_cart),
        BottomNavItem("menu", "Menu", R.drawable.ic_nav_menu)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        // Detect which route is currently selected
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Build each tab item
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Prevent stacking multiple copies of the same screen
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}
