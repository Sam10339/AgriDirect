package com.example.agridirect20

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun FarmDetailsScreen(
    navController: NavController,
    farmId: String,
    onAddToCart: (ProductUi) -> Unit
) {
    var farm by remember { mutableStateOf<Farm?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(farmId) {
        scope.launch {
            try {
                isLoading = true
                farm = FirebaseFarmRepository.getFarmWithProducts(farmId)
            } catch (e: Exception) {
                errorMessage = "Failed to load farm details: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            AgriTopBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when {
                isLoading -> {
                    Text("Loading...", style = MaterialTheme.typography.bodyMedium)
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                farm == null -> {
                    Text(
                        text = "Farm not found.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    val f = farm!!

                    Text(
                        text = f.name,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = f.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "ZIP: ${f.zip}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Available Products",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (f.products.isEmpty()) {
                        Text(
                            text = "No products listed yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(f.products) { product ->
                                ProductCardForDetails(
                                    product = product,
                                    onAddToCart = { p ->
                                        // Convert Firebase Product -> ProductUi for your cart
                                        onAddToCart(
                                            ProductUi(
                                                name = p.name,
                                                price = p.price
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCardForDetails(
    product: Product,
    onAddToCart: (Product) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )
            if (product.description.isNotBlank()) {
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(
                text = "Price: $${product.price}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "In stock: ${product.amount}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp)
            )

            Button(
                onClick = { onAddToCart(product) },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Text("Add to Cart")
            }
        }
    }
}

