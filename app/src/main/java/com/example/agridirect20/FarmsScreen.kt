package com.example.agridirect20

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class FarmUi(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val availability: String = "",
    val price: String = "",
    val imageUrl: String = ""
)

@Composable
fun FarmsScreen(
    navController: NavController,
    db: FirebaseFirestore,
    onFarmClick: (String) -> Unit
) {
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val farms = remember { mutableStateListOf<FarmUi>() }

    // Load farms from Firebase
    LaunchedEffect(Unit) {
        try {
            loading = true
            error = null
            farms.clear()

            val snapshot = db.collection("farms").get().await()

            val list = snapshot.documents.map { doc ->
                FarmUi(
                    id = doc.id,
                    name = doc.getString("name") ?: "",
                    description = doc.getString("description") ?: "",
                    availability = doc.getString("availability") ?: "",
                    price = doc.getString("price") ?: "",
                    imageUrl = doc.getString("imageUrl") ?: ""
                )
            }

            farms.addAll(list)
        } catch (e: Exception) {
            error = "Failed to load farms: ${e.message}"
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = { AgriTopBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Local Farms",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                loading -> CircularProgressIndicator()

                error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error)

                farms.isEmpty() -> Text("No farms available.")

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(farms) { farm ->
                            FarmCard(
                                farm = farm,
                                onClick = { onFarmClick(farm.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FarmCard(
    farm: FarmUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            // Farm Image
            if (farm.imageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(farm.imageUrl),
                    contentDescription = farm.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = farm.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = farm.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            if (farm.price.isNotEmpty()) {
                Text(
                    text = "Price: ${farm.price}",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}
