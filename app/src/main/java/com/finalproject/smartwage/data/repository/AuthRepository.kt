package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.UserDao
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.remote.AuthService
import com.finalproject.smartwage.data.remote.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@Suppress("DEPRECATION")
// Class to handle authentication and user management
class AuthRepository @Inject constructor(
    // Injecting the required dependencies
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val userDao: UserDao,
    private val auth: FirebaseAuth
) {
    // Sealed class to represent the result of authentication operations
    sealed class AuthResult {
        // Success and failure states
        data class Success(val user: FirebaseUser) : AuthResult()
        data class Failure(val errorMessage: String) : AuthResult()
    }

    // Function to send a password reset email
    suspend fun sendPasswordResetEmail(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to send reset email")
            false
        }
    }

    // Function to login a user
    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val result = authService.login(email, password)
            if (result is AuthService.AuthResult.Success) {
                val user = result.user.toUser()
                userDao.insertUser(user)
                AuthResult.Success(result.user)
            } else {
                AuthResult.Failure((result as AuthService.AuthResult.Failure).errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Login error")
            AuthResult.Failure("Login failed. Please try again.")
        }
    }

    // Function to signup a new user
    suspend fun signUp(name: String, email: String, password: String, phoneNumber: String): AuthResult {
        return try {
            if (isEmailRegistered(email)) {
                return AuthResult.Failure("This email is already registered.")
            }

            val result = authService.signUp(name, email, password)
            if (result is AuthService.AuthResult.Success) {
                val user = result.user.toUser(name, phoneNumber)
                userDao.insertUser(user)
                firestoreService.saveUser(user)
                AuthResult.Success(result.user)
            } else {
                AuthResult.Failure((result as AuthService.AuthResult.Failure).errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Sign-up error")
            AuthResult.Failure("Sign-up failed. Please try again.")
        }
    }

    // Function to check if email is already registered
    suspend fun isEmailRegistered(email: String): Boolean {
        return try {
            val result = auth.fetchSignInMethodsForEmail(email.lowercase()).await()
            Timber.tag("AuthRepository").d("Sign-in methods for $email: ${result.signInMethods}")
            result.signInMethods?.isNotEmpty() == true
        } catch (e: Exception) {
            Timber.e(e, "Error checking email registration")
            false
        }
    }

    // Private function to get the current user
    private fun FirebaseUser.toUser(name: String = "", phoneNumber: String = ""): User {
        return User(
            id = uid,
            name = name.ifEmpty { displayName ?: "" },
            email = email ?: "",
            phoneNumber = phoneNumber,
            taxCredit = 4000.0,
            profilePicture = null
        )
    }
}