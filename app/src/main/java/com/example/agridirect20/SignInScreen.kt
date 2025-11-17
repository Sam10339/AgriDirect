package com.example.agridirect20

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * SignInScreen
 *
 * This composable displays the sign-in UI for the AgriDirect app.
 * Users enter their email and password and authenticate using Firebase
 * through the AuthManager. If authentication succeeds, the parent
 * composable navigates to the home screen. Otherwise, an error message
 * is displayed.
 *
 * @param onSignInSuccess Callback invoked after a successful login.
 * @param onNavigateToSignUp Callback to navigate to the sign-up screen.
 */
@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    // State variables for user input
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Used to display Firebase authentication errors
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Coroutine scope for asynchronous Firebase login
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App logo at the top of the screen
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "AgriDirect Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Title text
        Text("Sign In", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Email input field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        // Password input field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        // Sign-in button triggers Firebase authentication
        Button(
            onClick = {
                scope.launch {
                    try {
                        // Attempt to authenticate
                        AuthManager.signIn(email.trim(), password.trim())
                        onSignInSuccess()      // Navigate on success
                    } catch (e: Exception) {
                        // Display error to user
                        errorMessage = e.message
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Sign In")
        }

        // Navigation to the sign-up screen
        TextButton(
            onClick = onNavigateToSignUp,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Don't have an account? Create one")
        }

        // Display authentication error if one exists
        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}



