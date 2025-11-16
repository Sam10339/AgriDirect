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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgriTopBar(
    navController: NavController
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back"
                )
            }
        },
        title = {},
        actions = {
            Box(
                modifier = Modifier
                    .height(48.dp)        // gives enough vertical space
                    .padding(end = 8.dp)  // spacing on right edge
                    .clickable {
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
                        .fillMaxHeight()     // use full height available
                        .aspectRatio(1f),    // keeps it perfectly square
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