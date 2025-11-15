package com.example.agridirect20

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegisterFarmScreen() {
    var farmName by remember { mutableStateOf("") }
    var farmDescription by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.padding(16.dp)
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
                .padding(top = 16.dp)
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (successMessage != null) {
            Text(
                text = successMessage!!,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.padding(top = 8.dp))

        Button(
            onClick = {
                if (farmName.isBlank()) {
                    errorMessage = "Farm name is required."
                    successMessage = null
                } else {
                    errorMessage = null
                    // Future: save to database / repository
                    successMessage = "Farm saved (demo only, not persisted yet)."
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save Farm")
        }
    }
}
