package com.example.agridirect20

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CenteredLogo() {
    Image(
        painter = painterResource(id = R.drawable.logo), // logo.png
        contentDescription = "App Logo",
        modifier = Modifier.size(150.dp), // adjust smaller/larger if needed
        alignment = Alignment.Center
    )
}
