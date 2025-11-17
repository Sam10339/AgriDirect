package com.example.agridirect20

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun MarketDetailsScreen(
    navController: NavController,
    venueId: String
) {
    var venue by remember { mutableStateOf<Venue?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(venueId) {
        scope.launch {
            try {
                venue = FirebaseVenueRepository.getVenueById(venueId)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to load market details"
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
        ) {
            when {
                errorMessage != null -> {
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                venue == null -> {
                    Text("Loading market details...")
                }

                else -> {
                    Text(
                        text = venue!!.name,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Latitude: ${"%.4f".format(venue!!.address.latitude)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Longitude: ${"%.4f".format(venue!!.address.longitude)}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "In the future, this screen could show:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text("• Vendors at this market")
                    Text("• Products available for pre-order")
                    Text("• Market schedule and hours")
                }
            }
        }
    }
}