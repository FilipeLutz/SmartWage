package com.finalproject.smartwage.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

//  Class for handling user authentication
@Singleton
class AuthService @Inject constructor(
    //  Firebase authentication instance
    private val firebaseAuth: FirebaseAuth
) {
    //  Sealed class for handling authentication results
    sealed class AuthResult {
        // Success result with user information
        data class Success(val user: FirebaseUser) : AuthResult()
        // Failure result with error message
        data class Failure(val errorMessage: String) : AuthResult()
    }
    //  Function for signing up a user
    suspend fun signUp(name: String, email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user!!
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(profileUpdates).await()
            user.sendEmailVerification().await()
            Timber.d("User signed up successfully: ${user.uid}")
            AuthResult.Success(user)
        } catch (e: Exception) {
            Timber.e(e, "Sign-up failed: ${e.message}")
            AuthResult.Failure(e.message ?: "Sign-up failed. Please try again.")
        }
    }
    //  Function for logging in a user
    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user!!
            if (!user.isEmailVerified) {
                Timber.w("User email not verified: ${user.uid}")
                return AuthResult.Failure("Please verify your email before logging in.")
            }
            Timber.d("User logged in successfully: ${user.uid}")
            AuthResult.Success(user)
        } catch (e: Exception) {
            Timber.e(e, "Login failed: ${e.message}")
            AuthResult.Failure(e.message ?: "Login failed. Please check your credentials.")
        }
    }
}