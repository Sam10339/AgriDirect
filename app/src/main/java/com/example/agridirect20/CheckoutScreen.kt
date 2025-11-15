package com.example.agridirect20

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CheckoutScreen(
    cartItems: List<CartItem>,
    onOrderPlaced: () -> Unit
) {
    val total = cartItems.sumOf { it.price * it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                                text = String.format("Line Total: $%.2f", item.price * item.quantity),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Simple "summary" section – could represent pickup info, etc.
            Text(
                text = String.format("Order Total: $%.2f", total),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onOrderPlaced,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Place Order")
            }
        }
    }
}
