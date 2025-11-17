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
 * Displays the static About page for the app.
 * Shows a brief description of AgriDirect and the current app version.
 *
 * Usage Example:
 * ```
 * navController.navigate("about")
 * ```
 */
@Composable
fun AboutScreen(navController: NavController) {

    Scaffold(
        topBar = { AgriTopBar(navController = navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Text(
                text = "About AgriDirect",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "AgriDirect connects consumers with local farmers and markets, " +
                        "providing easy access to fresh, sustainable, and community-grown produce.",
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Version 1.0.0",
                modifier = Modifier.padding(top = 24.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
