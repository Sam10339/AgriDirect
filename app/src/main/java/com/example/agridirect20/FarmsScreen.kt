package com.example.agridirect20

import androidx.compose.foundation.clickable
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
 * Screen for searching farms by ZIP code.
 *
 * Users can type a ZIP, submit the search, and view matching farms returned
 * from Firebase. Selecting a farm navigates to its FarmDetailsScreen.
 *
 * Main behaviors:
 * - Accept ZIP input
 * - Load farms from FirebaseFarmRepository
 * - Display errors (invalid ZIP, no results, Firestore failure)
 * - Navigate to farm details when a farm card is tapped
 *
 * USAGE IN NAV GRAPH:
 *
 * composable("farms") {
 *     FarmsScreen(navController)
 * }
 *
 */
@Composable
fun FarmsScreen(navController: NavController) {
    var zipInput by remember { mutableStateOf("") }
    var farms by remember { mutableStateOf<List<Farm>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { AgriTopBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {


            //  ZIP Input + Search Button

            Text(
                text = "Find Local Farms",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = zipInput,
                onValueChange = { zipInput = it },
                label = { Text("Enter ZIP Code") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            AgriPrimaryButton(
                onClick = {
                    errorMessage = null
                    if (zipInput.isBlank()) {
                        errorMessage = "Please enter a ZIP code."
                        return@AgriPrimaryButton
                    }

                    scope.launch {
                        try {
                            isLoading = true
                            farms = FirebaseFarmRepository.getFarmsByZip(zipInput)

                            if (farms.isEmpty()) {
                                errorMessage = "No farms found for that ZIP."
                            }
                        } catch (e: Exception) {
                            errorMessage = "Failed to load farms: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            ) {
                Text(if (isLoading) "Searching..." else "Search Farms")
            }


            //  Error Message

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(Modifier.height(16.dp))


            //  Farm Results List

            if (farms.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(farms) { farm ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("farmDetails/${farm.id}")
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = farm.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = farm.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "ZIP: ${farm.zip}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
