package com.example.agridirect20

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

//
// ManageFarmScreen
//
// Loads a farm (and its products) from Firebase, then displays
// an editor so the user can update farm info and product list.
//
// Usage Example (from AppNav):
//
// composable("manageFarm/{farmId}") {
//     ManageFarmScreen(navController, farmId = it.arguments!!.getString("farmId")!!)
// }
//
@Composable
fun ManageFarmScreen(
    navController: NavController,
    farmId: String
) {
    var farm by remember { mutableStateOf<Farm?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Load farm + products from Firebase when farmId changes
    LaunchedEffect(farmId) {
        try {
            farm = FirebaseFarmRepository.getFarmWithProducts(farmId)
        } catch (e: Exception) {
            error = "Failed to load farm: ${e.message}"
        }
    }

    if (error != null) {
        Text("Error: $error", modifier = Modifier.padding(20.dp))
        return
    }

    if (farm == null) {
        Text("Loading...", modifier = Modifier.padding(20.dp))
        return
    }

    ManageFarmEditor(
        farm = farm!!,
        onSaveFarm = { name, desc, zip ->
            scope.launch { FirebaseFarmRepository.updateFarm(farmId, name, desc, zip) }
        },
        onSaveProduct = { product ->
            scope.launch { FirebaseFarmRepository.updateProduct(farmId, product) }
        },
        onDeleteProduct = { productId ->
            scope.launch { FirebaseFarmRepository.deleteProduct(farmId, productId) }
        },
        onAddProduct = { product ->
            scope.launch { FirebaseFarmRepository.addProductToFarm(farmId, product) }
        }
    )
}

//
// ManageFarmEditor
//
// Displays editable farm info + a scrollable list of products.
// Also includes a section to add new products.
//
// Parameters:
//  • farm — the farm loaded from Firebase
//  • onSaveFarm — save farm-level changes
//  • onSaveProduct — save a specific product
//  • onDeleteProduct — delete a specific product
//  • onAddProduct — add a new product to the farm
//
@Composable
fun ManageFarmEditor(
    farm: Farm,
    onSaveFarm: (String, String, String) -> Unit,
    onSaveProduct: (Product) -> Unit,
    onDeleteProduct: (String) -> Unit,
    onAddProduct: (Product) -> Unit
) {
    var name by remember { mutableStateOf(farm.name) }
    var desc by remember { mutableStateOf(farm.description) }
    var zip by remember { mutableStateOf(farm.zip) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("Edit Farm Information", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Farm Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = zip,
            onValueChange = { zip = it },
            label = { Text("ZIP Code") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onSaveFarm(name, desc, zip) },
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        ) {
            Text("Save Farm Info")
        }

        Spacer(Modifier.height(24.dp))

        Text("Products", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        // Add new product
        AddNewProductSection(onAdd = onAddProduct)

        Spacer(Modifier.height(16.dp))

        // Existing products
        farm.products.forEach { product ->
            ProductEditor(
                product = product,
                onSave = onSaveProduct,
                onDelete = onDeleteProduct
            )
        }
    }
}

//
// ProductEditor
//
// Editor UI for an individual product, with fields for
// name, description, price, and stock amount.
//
// Includes "Save" and "Delete" actions.
//
@Composable
fun ProductEditor(
    product: Product,
    onSave: (Product) -> Unit,
    onDelete: (String) -> Unit
) {
    var name by remember { mutableStateOf(product.name) }
    var desc by remember { mutableStateOf(product.description) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var amount by remember { mutableStateOf(product.amount.toString()) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Stock Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(Modifier.padding(top = 8.dp)) {

            Button(
                onClick = {
                    onSave(
                        product.copy(
                            name = name,
                            description = desc,
                            price = price.toDoubleOrNull() ?: 0.0,
                            amount = amount.toIntOrNull() ?: 0
                        )
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }

            Spacer(Modifier.width(8.dp))

            OutlinedButton(
                onClick = { onDelete(product.id) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Delete")
            }
        }
    }
}

//
// AddNewProductSection
//
// Provides text fields for entering a completely new product
// and a button to add it to the farm.
//
// Validation: only requires a product name.
//
@Composable
fun AddNewProductSection(onAdd: (Product) -> Unit) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Column(Modifier.padding(vertical = 8.dp)) {

        Text("Add New Product", style = MaterialTheme.typography.titleSmall)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (name.isNotEmpty()) {
                    onAdd(
                        Product(
                            name = name,
                            description = desc,
                            price = price.toDoubleOrNull() ?: 0.0,
                            amount = amount.toIntOrNull() ?: 0
                        )
                    )

                    // Clear fields after adding
                    name = ""
                    desc = ""
                    price = ""
                    amount = ""
                }
            },
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        ) {
            Text("Add Product")
        }
    }
}
