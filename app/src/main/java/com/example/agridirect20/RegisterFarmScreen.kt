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

@Composable
fun RegisterFarmScreen(navController: NavController) {
    var farmName by remember { mutableStateOf("") }
    var farmDescription by remember { mutableStateOf("") }
    var farmZip by remember { mutableStateOf("") }

    val products = remember { mutableStateListOf<Product>() }

    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPriceText by remember { mutableStateOf("") }
    var productAmountText by remember { mutableStateOf("") }

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
            Text(
                text = "Edit / Register Your Farm",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = farmName,
                onValueChange = {
                    farmName = it
                    errorMessage = null
                    successMessage = null
                },
                label = { Text("Farm Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            OutlinedTextField(
                value = farmDescription,
                onValueChange = {
                    farmDescription = it
                    errorMessage = null
                    successMessage = null
                },
                label = { Text("Farm Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            OutlinedTextField(
                value = farmZip,
                onValueChange = {
                    farmZip = it
                    errorMessage = null
                    successMessage = null
                },
                label = { Text("Farm ZIP Code") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Products for this farm",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            OutlinedTextField(
                value = productDescription,
                onValueChange = { productDescription = it },
                label = { Text("Product Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
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

            AgriPrimaryButton(
                onClick = {
                    errorMessage = null
                    successMessage = null

                    if (productName.isBlank()) {
                        errorMessage = "Product name is required."
                        return@AgriPrimaryButton
                    }

                    val price = productPriceText.toDoubleOrNull()
                    if (price == null || price < 0.0) {
                        errorMessage = "Enter a valid price."
                        return@AgriPrimaryButton
                    }

                    val amount = productAmountText.toIntOrNull()
                    if (amount == null || amount < 0) {
                        errorMessage = "Enter a valid amount."
                        return@AgriPrimaryButton
                    }

                    products.add(
                        Product(
                            name = productName.trim(),
                            description = productDescription.trim(),
                            price = price,
                            amount = amount
                        )
                    )

                    productName = ""
                    productDescription = ""
                    productPriceText = ""
                    productAmountText = ""

                    successMessage = "Product added (remember to Save Farm)."
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("Add Product To This Farm")
            }

            if (products.isNotEmpty()) {
                Text(
                    text = "Current Products:",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp),
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

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            if (successMessage != null) {
                Text(
                    text = successMessage!!,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            AgriPrimaryButton(
                onClick = {
                    errorMessage = null
                    successMessage = null

                    if (farmName.isBlank()) {
                        errorMessage = "Farm name is required."
                        return@AgriPrimaryButton
                    }
                    if (farmZip.isBlank()) {
                        errorMessage = "ZIP code is required."
                        return@AgriPrimaryButton
                    }

                    scope.launch {
                        try {
                            isSaving = true
                            FirebaseFarmRepository.createFarmWithProducts(
                                name = farmName,
                                description = farmDescription,
                                zip = farmZip,
                                products = products.toList()
                            )

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
