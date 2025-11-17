package com.example.agridirect20

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * AuthManager handles all Firebase Authentication actions for the app.
 *
 * This includes signing users in, creating accounts, checking the
 * currently logged-in user, and signing out.
 *
 * The methods inside this object are `suspend` so they can be used
 * inside coroutines from Compose screens (e.g., using rememberCoroutineScope()).
 *
 * USAGE EXAMPLES:
 *
 *  // Sign in a user:
 *  scope.launch {
 *      try {
 *          AuthManager.signIn(email, password)
 *          navController.navigate("home")
 *      } catch (e: Exception) { ... }
 *  }
 *
 *  // Create a new user:
 *  scope.launch {
 *      AuthManager.signUp(email, password)
 *  }
 *
 *  // Get the current user:
 *  val user = AuthManager.currentUser
 *
 *  // Sign out:
 *  AuthManager.signOut()
 */
object AuthManager {

    /** Firebase authentication instance used by all auth operations. */
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * The currently logged-in user, or null if nobody is signed in.
     *
     * Example:
     * val userEmail = AuthManager.currentUser?.email
     */
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /**
     * Creates a new Firebase user with the given email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return The newly created FirebaseUser.
     * @throws IllegalStateException if Firebase returns a null user.
     */
    suspend fun signUp(email: String, password: String): FirebaseUser {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user ?: throw IllegalStateException("User is null after sign up")
    }

    /**
     * Signs in a user using Firebase Authentication.
     *
     * @param email The user's registered email.
     * @param password The user's password.
     * @return The signed-in FirebaseUser.
     * @throws IllegalStateException if Firebase returns a null user.
     */
    suspend fun signIn(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user ?: throw IllegalStateException("User is null after sign in")
    }

    /**
     * Signs out the currently logged-in user.
     *
     * Example:
     * AuthManager.signOut()
     * navController.navigate("signin")
     */
    fun signOut() {
        auth.signOut()
    }
}
