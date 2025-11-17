package com.example.agridirect20

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val email = currentUser?.email ?: "AgriDirect user"

    var statusMessage by remember { mutableStateOf<String?>(null) }

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
                text = "Hello, $email",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Spacer(modifier = Modifier.padding(top = 20.dp))

            // Change Password (Send reset email)
            AgriPrimaryButton(
                onClick = {
                    if (currentUser?.email != null) {
                        auth.sendPasswordResetEmail(currentUser.email!!)
                            .addOnCompleteListener { task ->
                                statusMessage = if (task.isSuccessful) {
                                    "Password reset email sent to ${currentUser.email} check spam folder"
                                } else {
                                    "Failed to send reset email: ${task.exception?.message ?: "Unknown error"}"
                                }
                            }
                    } else {
                        statusMessage = "No email associated with this account."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change Password")
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))

            // Delete Account
            AgriPrimaryButton(
                onClick = {
                    if (currentUser != null) {
                        currentUser.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    statusMessage = "Account deleted."
                                    // After delete, navigate to Sign In and clear back stack
                                    navController.navigate("signin") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                } else {
                                    statusMessage = "Failed to delete account: ${task.exception?.message ?: "Unknown error"}"
                                }
                            }
                    } else {
                        statusMessage = "No logged-in user to delete."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Account")
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            // Sign Out Button
            AgriPrimaryButton(
                onClick = {
                    AuthManager.signOut()
                    navController.navigate("signin") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Out")
            }

            // Status / error text
            statusMessage?.let {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}



