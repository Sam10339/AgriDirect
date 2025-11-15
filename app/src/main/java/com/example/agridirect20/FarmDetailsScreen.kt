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

// Simple product model for this screen + cart
data class ProductUi(
    val id: String,
    val name: String,
    val price: Double,
    val description: String
)

@Composable
fun FarmDetailsScreen(
    farmName: String,
    onAddToCart: (ProductUi) -> Unit
) {
    // You can customize this per farm; for now it's a demo list
    val products = listOf(
        ProductUi(
            id = "carrots",
            name = "Carrots",
            price = 1.19,
            description = "Fresh carrots from $farmName."
        ),
        ProductUi(
            id = "lettuce",
            name = "Lettuce",
            price = 1.60,
            description = "Crisp head lettuce grown locally."
        ),
        ProductUi(
            id = "peppers",
            name = "Bell Peppers",
            price = 3.00,
            description = "Colorful peppers, great for cooking."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Banner placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Text(
                text = "Farm Banner Placeholder",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = farmName,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "This is where farm details go (location, hours, description, etc.).",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Products",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(products) { product ->
                ProductCard(product = product, onAddToCart = onAddToCart)
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ProductUi,
    onAddToCart: (ProductUi) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = String.format("$%.2f", product.price),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { onAddToCart(product) }
            ) {
                Text("Add to Cart")
            }
        }
    }
}

