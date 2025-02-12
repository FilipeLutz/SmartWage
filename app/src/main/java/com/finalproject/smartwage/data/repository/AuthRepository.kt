package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.UserDao
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.remote.AuthService
import com.finalproject.smartwage.data.remote.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val userDao: UserDao
) {

    // Sign Up with Firebase + Save User Locally
    suspend fun signUp(name: String, email: String, phoneNumber: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val firebaseUser = authService.signUp(email, name)
                if (true) {
                    val user = User(
                        id = firebaseUser.toString(),
                        name = name,
                        email = email,
                        phoneNumber = phoneNumber,
                        taxCredit = 4000.00,
                        profilePicture = null
                    )
                    firestoreService.saveUser(user)  // Save to Firestore
                    userDao.insertUser(user)  // Save locally
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                // Handle error (e.g., log or show a message)
                println("Error signing up: ${e.message}")
                false
            }
        }
    }

    // Log In with Firebase + Fetch User Profile
    suspend fun login(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val firebaseUser = authService.login(email, password)
                if (true) {
                    val userId = firebaseUser.toString()
                    val user = firestoreService.getUser(userId)
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                // Handle error (e.g., log or show a message)
                println("Error logging in: ${e.message}")
                false
            }
        }
    }

    // Get Currently Logged-In User (Offline First)
    fun getCurrentUser(): Flow<User?> {
        return userDao.getUserById(authService.getCurrentUser()?.uid ?: "")
    }

    // Logout from Firebase + Clear Local Data
    suspend fun logout() {
        withContext(Dispatchers.IO) {
            try {
                authService.logout()
                userDao.deleteUser(getCurrentUser().toString())
            } catch (e: Exception) {
                // Handle error (e.g., log or show a message)
                println("Error logging out: ${e.message}")
            }
        }
    }
}