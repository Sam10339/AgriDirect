package com.example.agridirect20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.agridirect20.ui.theme.AgriDirectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgriDirectTheme {
                AppNav()
            }
        }
    }
}

@Composable
fun AgriDirectHomeScreen(
    navController: NavController,
    onOpenFarms: () -> Unit,
    onOpenMarkets: () -> Unit,
    onOpenRegisterFarm: () -> Unit,
) {
    Scaffold(
        topBar = {}
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeTile(
                title = "Find Local Farms",
                imageRes = R.drawable.home_farms,
                onClick = onOpenFarms
            )
            HomeTile(
                title = "Edit/Register Your Farm",
                imageRes = R.drawable.home_register_farm,
                onClick = onOpenRegisterFarm
            )
            HomeTile(
                title = "Find Farmer's Market",
                imageRes = R.drawable.home_markets,
                onClick = onOpenMarkets
            )
        }
    }
}

@Composable
fun HomeTile(
    title: String,
    imageRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        onClick = onClick
    )
    {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .width(145.dp)
                    .height(114.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}