package com.example.agridirect20

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineSmall
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 16.dp))

        androidx.compose.foundation.layout.Row(
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

        androidx.compose.foundation.layout.Row(
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
    }
}
