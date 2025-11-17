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

@Composable
fun CheckoutScreen(
    navController: NavController,
    cartItems: List<CartItem>,
    onOrderPlaced: () -> Unit
) {
    val total = cartItems.sumOf { it.price * it.quantity }
    val scope = rememberCoroutineScope()
    var isPlacingOrder by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                                Text(
                                    text = item.productName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Quantity: ${item.quantity}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = String.format(
                                        "Line Total: $%.2f",
                                        item.price * item.quantity
                                    ),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = String.format("Order Total: $%.2f", total),
                    style = MaterialTheme.typography.titleMedium
                )

                if (errorMessage != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(Modifier.height(12.dp))

                AgriPrimaryButton(
                    onClick = {
                        scope.launch {
                            isPlacingOrder = true
                            errorMessage = null
                            try {
                                // Update stock in Firebase for each item
                                cartItems.forEach { item ->
                                    FirebaseFarmRepository.decrementProductStock(
                                        farmId = item.farmId,
                                        productId = item.productId,
                                        quantityPurchased = item.quantity
                                    )
                                }

                                // After successful stock updates
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
