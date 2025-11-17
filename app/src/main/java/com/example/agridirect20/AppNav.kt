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

/**
 * Central navigation graph for the entire app.
 * Manages:
 * - NavController
 * - Screen routes
 * - Shared cart state across screens
 *
 * Usage:
 * ```
 * setContent { AppNav() }
 * ```
 */
@Composable
fun AppNav() {

    /* Main navigation controller */
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    /* Shared cart state accessible by FarmDetails, Cart, Checkout */
    val cartItems = remember { mutableStateListOf<CartItem>() }

    /** Add an item or increment quantity if it already exists */
    fun addToCart(product: ProductUi) {
        val index = cartItems.indexOfFirst { it.productId == product.productId }
        if (index >= 0) {
            val existing = cartItems[index]
            cartItems[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            cartItems.add(
                CartItem(
                    farmId = product.farmId,
                    productId = product.productId,
                    productName = product.name,
                    price = product.price,
                    quantity = 1
                )
            )
        }
    }

    /** Empty the cart after a successful order */
    fun clearCart() = cartItems.clear()

    Scaffold(
        /* Hide bottom nav on login screen */
        bottomBar = {
            if (currentRoute != "signin") {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->

        /* Root of all app navigation */
        NavHost(
            navController = navController,
            startDestination = "signin",
            modifier = Modifier.padding(innerPadding)
        ) {


            // AUTH FLOW


            composable("signin") {
                SignInScreen(
                    onSignInSuccess = {
                        navController.navigate("home") {
                            popUpTo("signin") { inclusive = true } // remove login from backstack
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
                    onNavigateToSignIn = { navController.popBackStack() }
                )
            }


            // MAIN HOME + SECTIONS


            composable("home") {
                AgriDirectHomeScreen(
                    navController = navController,
                    onOpenFarms  = { navController.navigate("farms") },
                    onOpenMarkets = { navController.navigate("markets") },
                    onOpenRegisterFarm = { navController.navigate("myFarms") }
                )
            }

            composable("farms") { FarmsScreen(navController) }

            // Farm details page with products
            composable(
                route = "farmDetails/{farmId}",
                arguments = listOf(navArgument("farmId") { type = NavType.StringType })
            ) { backStackEntry ->
                val farmId = backStackEntry.arguments?.getString("farmId") ?: ""
                FarmDetailsScreen(
                    navController = navController,
                    farmId = farmId,
                    onAddToCart = { addToCart(it) }
                )
            }

            // My farms (farmer section)
            composable("myFarms") { MyFarmsScreen(navController) }

            composable(
                route = "manageFarm/{farmId}",
                arguments = listOf(navArgument("farmId") { type = NavType.StringType })
            ) { backStackEntry ->
                val farmId = backStackEntry.arguments?.getString("farmId") ?: ""
                ManageFarmScreen(navController, farmId)
            }

            composable("markets") { MarketsScreen(navController) }
            composable("registerFarm") { RegisterFarmScreen(navController) }


            // BLOG + ARTICLES


            composable("blog") {
                BlogScreen(
                    navController = navController,
                    onArticleClick = { id -> navController.navigate("article/$id") }
                )
            }

            composable(
                route = "article/{articleId}",
                arguments = listOf(navArgument("articleId") { type = NavType.StringType })
            ) { entry ->
                ArticleScreen(
                    navController = navController,
                    articleId = entry.arguments?.getString("articleId") ?: "a1"
                )
            }


            // CART + CHECKOUT


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


            // PROFILE + INFO


            composable("profile") { ProfileScreen(navController) }
            composable("about")   { AboutScreen(navController) }

            composable("menu") {
                MainMenuScreen(
                    navController = navController,
                    onOpenProfile = { navController.navigate("profile") },
                    onOpenAbout = { navController.navigate("about") }
                )
            }
        }
    }
}
