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

@Composable
fun MyFarmsScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    var farms by remember { mutableStateOf<List<Farm>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                isLoading = true
                farms = FirebaseFarmRepository.getFarmsForCurrentUser()
            } catch (e: Exception) {
                errorMessage = "Failed to load your farms: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

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
                text = "Your Farms",
                style = MaterialTheme.typography.headlineSmall
            )

            Button(
                onClick = { navController.navigate("registerFarm") },
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            ) {
                Text("Register a New Farm")
            }

            if (isLoading) {
                Text(
                    text = "Loading...",
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else if (farms.isEmpty()) {
                Text(
                    text = "You haven't registered any farms yet.",
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(farms) { farm ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("manageFarm/${farm.id}")
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