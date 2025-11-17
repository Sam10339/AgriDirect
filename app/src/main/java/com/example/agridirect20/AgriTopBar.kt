package com.example.agridirect20

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Reusable top app bar shown across multiple screens.
 * Includes:
 * - Back button (left)
 * - App logo that navigates to Home (right)
 *
 * Usage:
 * ```
 * Scaffold(topBar = { AgriTopBar(navController) }) { ... }
 * ```
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgriTopBar(
    navController: NavController
) {
    CenterAlignedTopAppBar(

        /* Back button */
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back"
                )
            }
        },

        /* No title — we rely on the logo for branding */
        title = {},

        /* Tappable logo that returns to Home */
        actions = {
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .padding(end = 8.dp)
                    .clickable {
                        // Always return to Home without stacking duplicate screens
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "AgriDirect Logo",
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f), // keeps the logo square
                    contentScale = ContentScale.Fit
                )
            }
        },

        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
