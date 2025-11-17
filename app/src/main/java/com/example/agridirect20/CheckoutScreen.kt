package com.example.agridirect20

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

/**
 * Displays the checkout screen where the user reviews their cart items
 * and places an order. Once confirmed, product stock is updated in Firebase.
 *
 * @param navController Navigation controller for routing.
 * @param cartItems List of CartItem objects representing the current cart.
 * @param onOrderPlaced Callback triggered after a successful checkout.
 *
 * USAGE EXAMPLE:
 * CheckoutScreen(
 *     navController = navController,
 *     cartItems = cartItems,
 *     onOrderPlaced = { clearCart() }
 * )
 *
 */
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartItems: List<CartItem>,
    onOrderPlaced: () -> Unit
) {
    val total = cartItems.sumOf { it.price * it.quantity }         // Cart total
    val scope = rememberCoroutineScope()

    var isPlacingOrder by remember { mutableStateOf(false) }       // UI loading state
    var errorMessage by remember { mutableStateOf<String?>(null) } // Firebase errors

    Scaffold(
        topBar = { AgriTopBar(navController = navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Text(
                text = "Checkout",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(16.dp))

            // If no items exist, show empty state
            if (cartItems.isEmpty()) {

                Text(
                    text = "Your cart is empty. Add items before checking out.",
                    style = MaterialTheme.typography.bodyMedium
                )

            } else {

                Text(
                    text = "Review your items:",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                // List of cart items
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartItems) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(item.productName, style = MaterialTheme.typography.titleMedium)
                                Text("Quantity: ${item.quantity}", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    String.format("Line Total: $%.2f", item.price * item.quantity),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Display order total
                Text(
                    text = String.format("Order Total: $%.2f", total),
                    style = MaterialTheme.typography.titleMedium
                )

                // Display Firebase error (if any)
                errorMessage?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Checkout button
                AgriPrimaryButton(
                    onClick = {
                        scope.launch {
                            isPlacingOrder = true
                            errorMessage = null

                            try {
                                // Reduce stock in Firebase for each purchased item
                                cartItems.forEach { item ->
                                    FirebaseFarmRepository.decrementProductStock(
                                        farmId = item.farmId,
                                        productId = item.productId,
                                        quantityPurchased = item.quantity
                                    )
                                }

                                // After success → trigger callback
                                onOrderPlaced()

                            } catch (e: Exception) {
                                errorMessage = "Failed to place order: ${e.message}"
                            } finally {
                                isPlacingOrder = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isPlacingOrder && cartItems.isNotEmpty()
                ) {
                    Text(if (isPlacingOrder) "Placing Order..." else "Place Order")
                }
            }
        }
    }
}
