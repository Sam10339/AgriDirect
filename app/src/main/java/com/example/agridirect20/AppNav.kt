package com.example.agridirect20

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AppNav() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Firebase instances
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

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

    fun clearCart() = cartItems.clear()

    Scaffold(
        bottomBar = {
            if (currentRoute != "signin") {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = if (auth.currentUser == null) "signin" else "home",
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
                    navController = navController,
                    onOpenFarms = { navController.navigate("farms") },
                    onOpenMarkets = { navController.navigate("markets") },
                    onOpenRegisterFarm = { navController.navigate("registerFarm") },
                    onOpenRegisterBooth = { navController.navigate("registerBooth") }
                )
            }

            composable("farms") {
                FarmsScreen(
                    navController = navController,
                    db = db,
                    onFarmClick = { farmId ->
                        navController.navigate("farmDetails/$farmId")
                    }
                )
            }

            composable(
                route = "farmDetails/{farmId}",
                arguments = listOf(navArgument("farmId") { type = NavType.StringType })
            ) { backStackEntry ->
                val farmId = backStackEntry.arguments?.getString("farmId") ?: "Farm"

                FarmDetailsScreen(
                    navController = navController,
                    farmName = farmId,
                    onAddToCart = { product -> addToCart(product) }
                )
            }

            composable("markets") {
                MarketsScreen(navController)
            }

            composable("registerFarm") {
                RegisterFarmScreen(navController)
            }

            composable("registerBooth") {
                RegisterBoothScreen(navController)
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
                arguments = listOf(navArgument("articleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getString("articleId") ?: "a1"
                ArticleScreen(navController = navController, articleId = articleId)
            }

            composable("cart") {
                CartScreen(
                    navController = navController,
                    cartItems = cartItems,
                    onCheckout = { navController.navigate("checkout") }
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
                FavoritesScreen(navController)
            }

            composable("settings") {
                SettingsScreen(navController)
            }

            composable("notifications") {
                NotificationsScreen(navController)
            }

            composable("about") {
                AboutScreen(navController)
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
        }
    }
}
