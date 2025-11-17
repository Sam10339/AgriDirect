package com.example.agridirect20

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

/**
 * RegisterFarmScreen
 *
 * Allows users to register a new farm and add products before saving to Firebase.
 * Features:
 *  - Collect farm info (name, description, ZIP)
 *  - Add multiple products with validation
 *  - Save farm + product list to Firestore through FirebaseFarmRepository
 *
 * This screen is used when:
 *    navController.navigate("registerFarm")
 *
 * Layout:
 *  - Farm information form
 *  - Product entry form
 *  - Scrollable preview of added products
 *  - Save button to upload everything to Firestore
 */
@Composable
fun RegisterFarmScreen(navController: NavController) {

    // Farm Input Fields
    var farmName by remember { mutableStateOf("") }
    var farmDescription by remember { mutableStateOf("") }
    var farmZip by remember { mutableStateOf("") }

    // List of products being added in-memory before saving
    val products = remember { mutableStateListOf<Product>() }

    // Product Input Fields
    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPriceText by remember { mutableStateOf("") }
    var productAmountText by remember { mutableStateOf("") }

    // UI State
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { AgriTopBar(navController = navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {

            // Page Title
            Text(
                text = "Edit / Register Your Farm",
                style = MaterialTheme.typography.headlineSmall
            )

            // Farm Information Form
            OutlinedTextField(
                value = farmName,
                onValueChange = {
                    farmName = it; errorMessage = null; successMessage = null
                },
                label = { Text("Farm Name") },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            )

            OutlinedTextField(
                value = farmDescription,
                onValueChange = {
                    farmDescription = it; errorMessage = null; successMessage = null
                },
                label = { Text("Farm Description") },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            )

            OutlinedTextField(
                value = farmZip,
                onValueChange = {
                    farmZip = it; errorMessage = null; successMessage = null
                },
                label = { Text("Farm ZIP Code") },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            )


            Spacer(Modifier.height(24.dp))

            // Product Section
            Text(
                text = "Products for this farm",
                style = MaterialTheme.typography.titleMedium
            )

            // Product Name
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            )

            // Product Description
            OutlinedTextField(
                value = productDescription,
                onValueChange = { productDescription = it },
                label = { Text("Product Description") },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            )

            // Price + Amount Row
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = productPriceText,
                    onValueChange = { productPriceText = it },
                    label = { Text("Price (e.g. 2.50)") },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = productAmountText,
                    onValueChange = { productAmountText = it },
                    label = { Text("Amount in Stock") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Add Product Button
            AgriPrimaryButton(
                onClick = {
                    errorMessage = null; successMessage = null

                    // Validation
                    if (productName.isBlank()) {
                        errorMessage = "Product name is required."
                        return@AgriPrimaryButton
                    }

                    val price = productPriceText.toDoubleOrNull()
                    if (price == null || price < 0) {
                        errorMessage = "Enter a valid price."
                        return@AgriPrimaryButton
                    }

                    val amount = productAmountText.toIntOrNull()
                    if (amount == null || amount < 0) {
                        errorMessage = "Enter a valid amount."
                        return@AgriPrimaryButton
                    }

                    // Add product to list
                    products.add(
                        Product(
                            name = productName.trim(),
                            description = productDescription.trim(),
                            price = price,
                            amount = amount
                        )
                    )

                    // Reset fields
                    productName = ""
                    productDescription = ""
                    productPriceText = ""
                    productAmountText = ""

                    successMessage = "Product added (remember to Save Farm)."
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Add Product To This Farm")
            }

            // Preview Added Products
            if (products.isNotEmpty()) {
                Text(
                    text = "Current Products:",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(products) { product ->
                        Text(
                            text = "${product.name} – ${product.amount} @ $${"%.2f".format(product.price)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Display Errors
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            // Display Success
            successMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }


            Spacer(Modifier.height(16.dp))

            // SAVE FARM BUTTON
            AgriPrimaryButton(
                onClick = {
                    errorMessage = null; successMessage = null

                    // Basic validation
                    if (farmName.isBlank()) {
                        errorMessage = "Farm name is required."
                        return@AgriPrimaryButton
                    }
                    if (farmZip.isBlank()) {
                        errorMessage = "ZIP code is required."
                        return@AgriPrimaryButton
                    }

                    // Upload to Firebase
                    scope.launch {
                        try {
                            isSaving = true

                            FirebaseFarmRepository.createFarmWithProducts(
                                name = farmName,
                                description = farmDescription,
                                zip = farmZip,
                                products = products.toList()
                            )

                            // Clear form after success
                            farmName = ""
                            farmDescription = ""
                            farmZip = ""
                            products.clear()

                            successMessage = "Farm saved to Firebase!"
                        } catch (e: Exception) {
                            errorMessage = "Failed to save farm: ${e.message}"
                        } finally {
                            isSaving = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isSaving) "Saving..." else "Save Farm")
            }
        }
    }
}
