package com.example.agridirect20

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

// Data class for farms
data class FarmUi(
    val name: String,
    val description: String,
    val availability: String,
    val price: String,
    val imageRes: Int
)

@Composable
fun FarmsScreen(
    onFarmClick: (String) -> Unit
) {
    val farms = listOf(
        FarmUi(
            name = "Nearby Farm",
            description = "This is a description of this item as a vegetable, fruit, or animal product.",
            availability = "Availability",
            price = "Price",
            imageRes = R.drawable.farm_1
        ),
        FarmUi(
            name = "Nearby Farm 2",
            description = "This is a description of this item as a vegetable, fruit, or animal product.",
            availability = "Availability",
            price = "Price",
            imageRes = R.drawable.farm_2
        ),
        FarmUi(
            name = "Nearby Farm 3",
            description = "This is a description of this item as a vegetable, fruit, or animal product.",
            availability = "Availability",
            price = "Price",
            imageRes = R.drawable.farm_3
        ),
        FarmUi(
            name = "Nearby Farm 4",
            description = "This is a description of this item as a vegetable, fruit, or animal product.",
            availability = "Availability",
            price = "Price",
            imageRes = R.drawable.farm_4
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Local Farms",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(farms) { farm ->
                FarmCard(
                    farm = farm,
                    onClick = { onFarmClick(farm.name) }
                )
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
            .height(150.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Left image block with real farm/product image
            Image(
                painter = painterResource(id = farm.imageRes),
                contentDescription = farm.name,
                modifier = Modifier
                    .width(150.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(10.dp)
            ) {
                Text(
                    text = farm.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = farm.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .weight(1f),
                    maxLines = 2
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = farm.availability,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = farm.price,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}