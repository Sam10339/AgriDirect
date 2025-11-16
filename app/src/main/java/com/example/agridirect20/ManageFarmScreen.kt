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

// ----------------------------
// MAIN SCREEN: LOAD + EDIT
// ----------------------------

@Composable
fun ManageFarmScreen(
    navController: NavController,
    farmId: String
) {
    var farm by remember { mutableStateOf<Farm?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Load farm + products from Firestore
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
            scope.launch {
                FirebaseFarmRepository.updateFarm(farmId, name, desc, zip)
            }
        },
        onSaveProduct = { product ->
            scope.launch {
                FirebaseFarmRepository.updateProduct(farmId, product)
            }
        },
        onDeleteProduct = { productId ->
            scope.launch {
                FirebaseFarmRepository.deleteProduct(farmId, productId)
            }
        },
        onAddProduct = { product ->
            scope.launch {
                FirebaseFarmRepository.addProductToFarm(farmId, product)
            }
        }
    )
}

// --------------------------------------------------
// EDIT FARM + LIST/EDIT PRODUCTS
// --------------------------------------------------

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

        farm.products.forEach { product ->
            ProductEditor(
                product = product,
                onSave = onSaveProduct,
                onDelete = onDeleteProduct
            )
        }

        Spacer(Modifier.height(16.dp))

        AddNewProductSection(onAdd = onAddProduct)
    }
}

// --------------------------------------------------
// PRODUCT EDITOR COMPONENT
// --------------------------------------------------

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

// --------------------------------------------------
// ADD NEW PRODUCT SECTION
// --------------------------------------------------

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

                    // Reset fields
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
