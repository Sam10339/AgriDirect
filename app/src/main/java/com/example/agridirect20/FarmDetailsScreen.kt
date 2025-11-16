package com.example.agridirect20

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

data class ProductUi(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val description: String = ""
)

@Composable
fun FarmDetailsScreen(
    navController: NavController,
    farmName: String,
    onAddToCart: (ProductUi) -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var products by remember { mutableStateOf<List<ProductUi>>(emptyList()) }
    var farmDescription by remember { mutableStateOf("Loading farm info…") }

    // ---------- FIREBASE LOAD ----------
    LaunchedEffect(farmName) {
        loading = true
        error = null

        // Load farm info
        db.collection("farms")
            .document(farmName)
            .get()
            .addOnSuccessListener { doc ->
                farmDescription = doc.getString("description") ?: "No description available."

                // Load products
                db.collection("farms")
                    .document(farmName)
                    .collection("products")
                    .get()
                    .addOnSuccessListener { productSnapshot ->
                        products = productSnapshot.documents.map { d ->
                            ProductUi(
                                id = d.id,
                                name = d.getString("name") ?: "Unnamed item",
                                price = d.getDouble("price") ?: 0.0,
                                description = d.getString("description") ?: ""
                            )
                        }
                        loading = false
                    }
                    .addOnFailureListener { e ->
                        error = "Failed to load products: ${e.message}"
                        loading = false
                    }

            }
            .addOnFailureListener { e ->
                error = "Failed to load farm: ${e.message}"
                loading = false
            }
    }

    // ---------- UI ----------
    Scaffold(
        topBar = { AgriTopBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Text(farmName, style = MaterialTheme.typography.headlineSmall)

            Spacer(Modifier.height(8.dp))

            when {
                loading -> Text("Loading…")
                error != null -> Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error
                )
                else -> {
                    Text(farmDescription)

                    Spacer(Modifier.height(24.dp))

                    Text("Products", style = MaterialTheme.typography.titleMedium)

                    Spacer(Modifier.height(8.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(products) { product ->
                            ProductCard(product = product, onAddToCart = onAddToCart)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: ProductUi, onAddToCart: (ProductUi) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text(product.description, style = MaterialTheme.typography.bodyMedium)
            Text("$${product.price}", style = MaterialTheme.typography.labelMedium)

            Spacer(Modifier.height(8.dp))

            Button(onClick = { onAddToCart(product) }) {
                Text("Add to Cart")
            }
        }
    }
}
