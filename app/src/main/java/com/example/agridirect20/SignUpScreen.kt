package com.example.agridirect20

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * SignUpScreen
 *
 * Displays the account creation UI for new AgriDirect users.
 * Handles email + password input, Firebase authentication via AuthManager,
 * and basic loading/error states. On successful registration, the parent
 * composable navigates forward to the app's main content.
 *
 * @param onSignUpSuccess Callback triggered after successful account creation.
 * @param onNavigateToSignIn Callback to navigate back to the sign-in screen.
 */
@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    // State values for user input
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Error and loading indicators
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Coroutine scope for async Firebase operations
    val scope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Application logo (centered)
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "AgriDirect Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Header text
            Text("Create Account", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            // Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            // Password input field with hidden characters
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            // Error message displayed under inputs (if any)
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Sign-up button triggers Firebase registration
            Button(
                onClick = {
                    errorMessage = null
                    scope.launch {
                        try {
                            isLoading = true
                            // Attempt account creation
                            AuthManager.signUp(email.trim(), password.trim())
                            onSignUpSuccess()
                        } catch (e: Exception) {
                            // Display readable error message
                            errorMessage = e.message ?: "Failed to sign up"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Creating account..." else "Sign Up")
            }

            // Navigation back to the Sign In screen
            TextButton(
                onClick = onNavigateToSignIn,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Already have an account? Sign in")
            }
        }
    }
}
