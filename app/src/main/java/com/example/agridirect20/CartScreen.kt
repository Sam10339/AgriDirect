package com.example.agridirect20

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class CartItem(
    val farmId: String,
    val productId: String,
    val productName: String,
    val price: Double,
    val quantity: Int
)

@Composable
fun CartScreen(
    navController: NavController,
    cartItems: List<CartItem>,
    onCheckout: () -> Unit
) {
    val total = cartItems.sumOf { it.price * it.quantity }

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
                text = "Shopping Cart",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(16.dp))

            if (cartItems.isEmpty()) {
                Text(
                    text = "Your cart is empty.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemCard(item = item)
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = String.format("$%.2f", total),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(Modifier.height(12.dp))

                AgriPrimaryButton(
                    onClick = onCheckout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Proceed to Checkout")
                }
            }
        }
    }
}

@Composable
fun CartItemCard(item: CartItem) {
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
                text = String.format("Price: $%.2f", item.price),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = String.format("Line Total: $%.2f", item.price * item.quantity),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}