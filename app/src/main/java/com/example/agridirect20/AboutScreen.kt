package com.example.agridirect20

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier.padding(16.dp)
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
