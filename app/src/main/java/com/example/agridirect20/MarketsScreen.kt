package com.example.agridirect20

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MarketsScreen() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
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
