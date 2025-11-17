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

/**
 * FarmDetailsScreen
 *
 * Displays information about a selected farm and lists all products
 * associated with that farm. The screen fetches data from Firebase via
 * FirebaseFarmRepository and shows loading, error, or content states.
 *
 * @param navController Used for navigation and passed to AgriTopBar.
 * @param farmId The ID of the farm whose details should be displayed.
 * @param onAddToCart Callback triggered when the user adds a product to the cart.
 */
@Composable
fun FarmDetailsScreen(
    navController: NavController,
    farmId: String,
    onAddToCart: (ProductUi) -> Unit
) {
    // Farm object loaded from Firebase
    var farm by remember { mutableStateOf<Farm?>(null) }

    // Error or loading state
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    // Fetch farm details when the screen is opened or when farmId changes
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
            AgriTopBar(navController = navController)   // App top bar
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // Determine which UI state to show
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
                    // If data is successfully loaded, show the farm details
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

                    // Display product list
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
                                        // Convert Product → ProductUi for cart
                                        onAddToCart(
                                            ProductUi(
                                                farmId = farmId,
                                                productId = p.id,
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

/**
 * ProductCardForDetails
 *
 * Displays an individual product inside the farm details screen.
 * Shows name, description, price, stock amount, and an "Add to Cart" button.
 *
 * @param product A product belonging to the farm.
 * @param onAddToCart Callback when the product is added to the cart.
 */
@Composable
fun ProductCardForDetails(
    product: Product,
    onAddToCart: (Product) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {

        Column(modifier = Modifier.padding(12.dp)) {

            // Product name
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )

            // Optional description
            if (product.description.isNotBlank()) {
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Price
            Text(
                text = "Price: $${product.price}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Stock amount
            Text(
                text = "In stock: ${product.amount}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp)
            )

            // Add to cart button
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

