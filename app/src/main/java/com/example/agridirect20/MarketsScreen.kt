package com.example.agridirect20

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * MarketsScreen
 *
 * Displays a map and simple information about local farmers' markets.
 * Uses MapScreen() to render the actual Google Map.
 *
 * Usage Example (from AppNav):
 * composable("markets") { MarketsScreen(navController) }
 */
@Composable
fun MarketsScreen(navController: NavController) {

    Scaffold(
        topBar = { AgriTopBar(navController = navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            // Google Map showing market locations
            MapScreen()

            Text(
                text = "Find Farmer's Market",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "This screen will show markets / venues where local booths are located.",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
