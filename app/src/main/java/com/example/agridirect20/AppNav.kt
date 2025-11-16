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

    // Shared cart state
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
                    },
                    onNavigateToSignUp = {
                        navController.navigate("signup")
                    }
                )
            }

            composable("signup") {
                SignUpScreen(
                    onSignUpSuccess = {
                        navController.navigate("home") {
                            popUpTo("signin") { inclusive = true }
                        }
                    },
                    onNavigateToSignIn = {
                        navController.popBackStack()
                    }
                )
            }

            composable("home") {
                AgriDirectHomeScreen(
                    navController = navController,
                    onOpenFarms = { navController.navigate("farms") },
                    onOpenMarkets = { navController.navigate("markets") },
                    onOpenRegisterFarm = { navController.navigate("myFarms") },
                                    )
            }

            composable("farms") {
                FarmsScreen(navController = navController)
            }

            composable(
                route = "farmDetails/{farmId}",
                arguments = listOf(
                    navArgument("farmId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val farmId = backStackEntry.arguments?.getString("farmId") ?: ""
                FarmDetailsScreen(
                    navController = navController,
                    farmId = farmId,
                    onAddToCart = { product -> addToCart(product) }
                )
            }

            composable("myFarms") {
                MyFarmsScreen(navController = navController)
            }

            composable(
                route = "manageFarm/{farmId}",
                arguments = listOf(
                    navArgument("farmId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val farmId = backStackEntry.arguments?.getString("farmId") ?: ""
                ManageFarmScreen(
                    navController = navController,
                    farmId = farmId
                )
            }

            composable("markets") {
                MarketsScreen(navController = navController)
            }

            composable("registerFarm") {
                RegisterFarmScreen(navController = navController)
            }

            composable("blog") {
                BlogScreen(
                    navController = navController,
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
                ArticleScreen(
                    navController = navController,
                    articleId = articleId
                )
            }

            composable("cart") {
                CartScreen(
                    navController = navController,
                    cartItems = cartItems,
                    onCheckout = {
                        navController.navigate("checkout")
                    }
                )
            }

            composable("checkout") {
                CheckoutScreen(
                    navController = navController,
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
                ProfileScreen(navController = navController)
            }

            composable("favorites") {
                FavoritesScreen(navController = navController)
            }

            composable("settings") {
                SettingsScreen(navController = navController)
            }

            composable("notifications") {
                NotificationsScreen(navController = navController)
            }

            composable("about") {
                AboutScreen(navController = navController)
            }

            composable("menu") {
                MainMenuScreen(
                    navController = navController,
                    onOpenProfile = { navController.navigate("profile") },
                    onOpenSettings = { navController.navigate("settings") },
                    onOpenFavorites = { navController.navigate("favorites") },
                    onOpenNotifications = { navController.navigate("notifications") }
                )
            }

            composable(
                route = "manageFarm/{farmId}",
                arguments = listOf(
                    navArgument("farmId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val farmId = backStackEntry.arguments?.getString("farmId") ?: ""
                ManageFarmScreen(
                    navController = navController,
                    farmId = farmId
                )
            }
        }
    }
}
