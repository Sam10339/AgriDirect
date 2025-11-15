package com.example.agridirect20

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        "home",
        "farms",
        "blog",
        "cart",
        "menu"   // <-- last item is the menu icon
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { route ->
            val (label, icon) = when (route) {
                "home" -> "Home" to Icons.Filled.Home
                "farms" -> "Farms" to Icons.Filled.Person   // you can later swap this for a Figma farm icon
                "blog" -> "Blog" to Icons.Filled.Person    // swap later for a blog icon if you want
                "cart" -> "Cart" to Icons.Filled.ShoppingCart
                "menu" -> "Menu" to Icons.Filled.Menu      // this matches your designed menu slot
                else -> route to Icons.Filled.Home
            }

            NavigationBarItem(
                selected = currentRoute == route,
                onClick = { navController.navigate(route) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label
                    )
                },
                label = { Text(label) }
            )
        }
    }
}

