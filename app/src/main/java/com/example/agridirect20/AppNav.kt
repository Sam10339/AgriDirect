package com.example.agridirect20

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Shared cart state for the whole app
    val cartItems = remember { mutableStateListOf<CartItem>() }

    fun addToCart(product: ProductUi) {
        val existingIndex = cartItems.indexOfFirst { it.productName == product.name }
        if (existingIndex >= 0) {
            val existing = cartItems[existingIndex]
            cartItems[existingIndex] = existing.copy(quantity = existing.quantity + 1)
        } else {
            cartItems.add(
                CartItem(
                    productName = product.name,
                    price = product.price,
                    quantity = 1
                )
            )
        }
    }

    fun clearCart() {
        cartItems.clear()
    }

    Scaffold(
        bottomBar = {
            if (currentRoute != "signin") {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "signin",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("signin") {
                SignInScreen(
                    onSignInSuccess = {
                        navController.navigate("home") {
                            popUpTo("signin") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") {
                AgriDirectHomeScreen(
                    onOpenFarms = { navController.navigate("farms") },
                    onOpenMarkets = { navController.navigate("markets") },
                    onOpenRegisterFarm = { navController.navigate("registerFarm") },
                    onOpenRegisterBooth = { navController.navigate("registerBooth") }
                )
            }

            composable("farms") {
                FarmsScreen(
                    onFarmClick = { farmName ->
                        navController.navigate("farmDetails/$farmName")
                    }
                )
            }

            composable(
                route = "farmDetails/{farmName}",
                arguments = listOf(
                    navArgument("farmName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val farmName = backStackEntry.arguments?.getString("farmName") ?: "Farm"
                FarmDetailsScreen(
                    farmName = farmName,
                    onAddToCart = { product -> addToCart(product) }
                )
            }

            composable("markets") {
                MarketsScreen()
            }

            composable("registerFarm") {
                RegisterFarmScreen()
            }

            composable("registerBooth") {
                RegisterBoothScreen()
            }

            composable("blog") {
                BlogScreen(
                    onArticleClick = { articleId ->
                        navController.navigate("article/$articleId")
                    }
                )
            }

            composable(
                route = "article/{articleId}",
                arguments = listOf(
                    navArgument("articleId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getString("articleId") ?: "a1"
                ArticleScreen(articleId = articleId)
            }

            composable("cart") {
                CartScreen(
                    cartItems = cartItems,
                    onCheckout = {
                        navController.navigate("checkout")
                    }
                )
            }

            // NEW: Checkout route
            composable("checkout") {
                CheckoutScreen(
                    cartItems = cartItems,
                    onOrderPlaced = {
                        clearCart()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                        }
                    }
                )
            }

            composable("profile") {
                ProfileScreen()
            }

            composable("favorites") {
                FavoritesScreen()
            }

            composable("settings") {
                SettingsScreen()
            }

            composable("notifications") {
                NotificationsScreen()
            }

            composable("about") {
                AboutScreen()
            }

            composable("menu") {
                MainMenuScreen(
                    onOpenProfile = { navController.navigate("profile") },
                    onOpenSettings = { navController.navigate("settings") },
                    onOpenFavorites = { navController.navigate("favorites") },
                    onOpenNotifications = { navController.navigate("notifications") }
                )
            }
        }
    }
}
