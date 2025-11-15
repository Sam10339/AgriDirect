package com.example.agridirect20

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FavoritesScreen() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Here you could show saved farms, vendors, or products.",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
