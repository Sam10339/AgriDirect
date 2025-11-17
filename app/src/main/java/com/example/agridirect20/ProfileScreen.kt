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

/**
 * ProfileScreen
 *
 * Displays basic account information for the currently signed-in user.
 * Includes actions for:
 *  - Sending a password reset email
 *  - Deleting the account
 *  - Signing out
 *
 * Uses FirebaseAuth to read and modify user authentication state.
 *
 * Example Usage:
 * navController.navigate("profile")
 *
 * Visual Structure:
 *  ┌──────────── Top App Bar ────────────┐
 *  |   Back Button     Logo/Home Button  |
 *  └──────────────────────────────────────┘
 *  Profile header → account actions → status message
 */
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

            // --- Page Title ---
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            // --- User Greeting ---
            Text(
                text = "Hello, $email",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            /**
             *
             *  CHANGE PASSWORD
             *
             * Sends a Firebase password reset email
             * to the email attached to the user account.
             */
            AgriPrimaryButton(
                onClick = {
                    if (currentUser?.email != null) {
                        auth.sendPasswordResetEmail(currentUser.email!!)
                            .addOnCompleteListener { task ->
                                statusMessage = if (task.isSuccessful) {
                                    "Password reset email sent to ${currentUser.email}. Check your spam folder."
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

            /**
             *
             *  DELETE ACCOUNT
             *
             * Permanently deletes the user's Firebase account.
             * After deletion, navigates back to Sign-In.
             */
            AgriPrimaryButton(
                onClick = {
                    if (currentUser != null) {
                        currentUser.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    statusMessage = "Account deleted."
                                    navController.navigate("signin") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                } else {
                                    statusMessage =
                                        "Failed to delete account: ${task.exception?.message ?: "Unknown error"}"
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

            /**
             *
             *  SIGN OUT
             *
             * Signs out via AuthManager and returns to login.
             */
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

            // Status/Error messages
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



