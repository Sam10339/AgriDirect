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

/**
 * MainActivity
 *
 * Entry point of the app. Sets the app theme and loads the navigation graph.
 *
 * Usage:
 *
 * - Called automatically when the app launches.
 * - Displays AppNav(), which manages all screens/routes.
 */
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

/**
 * AgriDirectHomeScreen
 *
 * The home dashboard shown after sign-in. Lets users:
 * - Search for farms
 * - Register or edit their own farm
 * - Search for farmers’ markets
 *
 * Navigation is handled by callbacks passed from AppNav().
 *
 * Usage Example (from AppNav):
 *
 * AgriDirectHomeScreen(
 *     navController,
 *     onOpenFarms = { navController.navigate("farms") },
 *     onOpenMarkets = { navController.navigate("markets") },
 *     onOpenRegisterFarm = { navController.navigate("myFarms") }
 * )
 */
@Composable
fun AgriDirectHomeScreen(
    navController: NavController,
    onOpenFarms: () -> Unit,
    onOpenMarkets: () -> Unit,
    onOpenRegisterFarm: () -> Unit,
) {
    Scaffold(
        topBar = {} // No top bar on home screen (clean landing page)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            HomeTile(
                title = "Find Local Farms",
                imageRes = R.drawable.home_farms,
                onClick = onOpenFarms
            )

            Spacer(modifier = Modifier.height(32.dp))

            HomeTile(
                title = "Edit/Register Your Farm",
                imageRes = R.drawable.home_register_farm,
                onClick = onOpenRegisterFarm
            )

            Spacer(modifier = Modifier.height(32.dp))

            HomeTile(
                title = "Find Farmer's Market",
                imageRes = R.drawable.home_markets,
                onClick = onOpenMarkets
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * HomeTile
 *
 * Reusable clickable card for the home screen.
 *
 * Parameters:
 *  - title: Text label shown on the card
 *  - imageRes: Illustration shown on the left
 *  - onClick: Action when the tile is tapped (usually navigates to another screen)
 *
 * Usage:
 *
 * HomeTile("Find Local Farms", R.drawable.home_farms) {
 *     navController.navigate("farms")
 * }
 */
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
