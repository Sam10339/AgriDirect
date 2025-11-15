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
fun RegisterBoothScreen() {
    var boothName by remember { mutableStateOf("") }
    var marketName by remember { mutableStateOf("") }
    var boothDescription by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Edit / Register Your Booth At a Market",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = boothName,
            onValueChange = {
                boothName = it
                errorMessage = null
                successMessage = null
            },
            label = { Text("Booth Name") },
            modifier = Modifier.padding(top = 16.dp)
        )

        OutlinedTextField(
            value = marketName,
            onValueChange = {
                marketName = it
                errorMessage = null
                successMessage = null
            },
            label = { Text("Market / Venue Name") },
            modifier = Modifier.padding(top = 16.dp)
        )

        OutlinedTextField(
            value = boothDescription,
            onValueChange = {
                boothDescription = it
                errorMessage = null
                successMessage = null
            },
            label = { Text("Booth Description") },
            modifier = Modifier.padding(top = 16.dp)
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
                when {
                    boothName.isBlank() ->
                        errorMessage = "Booth name is required."
                    marketName.isBlank() ->
                        errorMessage = "Market / venue name is required."
                    else -> {
                        errorMessage = null
                        successMessage = "Booth saved (demo only, not persisted yet)."
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save Booth")
        }
    }
}
