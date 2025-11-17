package com.example.agridirect20

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * Displays the AgriDirect logo centered in a fixed-size image.
 *
 * Used throughout the app on authentication screens and sections
 * where a simple centered brand element is needed.
 *
 * USAGE EXAMPLE:
 *
 * Column(horizontalAlignment = Alignment.CenterHorizontally) {
 *     CenteredLogo()
 * }
 *
 */
@Composable
fun CenteredLogo() {
    Image(
        painter = painterResource(id = R.drawable.logo),    // The app logo from drawable resources
        contentDescription = "App Logo",
        modifier = Modifier.size(150.dp),                    // Logo size (can be adjusted)
        alignment = Alignment.Center                         // Ensures centering inside its container
    )
}
