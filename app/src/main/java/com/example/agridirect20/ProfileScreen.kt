package com.example.agridirect20

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {

    Scaffold(
        topBar = { AgriTopBar(navController = navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Text(
                text = "This is where user profile information will go (name, email, role, favorite farms, etc.).",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            // Sign out button
            AgriPrimaryButton(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        AuthManager.signOut()

                        // Navigate back to SignIn and clear navigation history
                        navController.navigate("signin") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            ) {
                Text("Sign Out")
            }
        }
    }
}


