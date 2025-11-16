package com.example.agridirect20

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { AgriTopBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Row(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Enable notifications",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it },
                    colors = SwitchDefaults.colors()
                )
            }

            Row(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Dark mode (visual only)",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = { darkModeEnabled = it },
                    colors = SwitchDefaults.colors()
                )
            }
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
                    text = "AgriDirect connects consumers with local farmers and markets for fresh, fair, and sustainable produce.",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Version 1.0.0",
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

