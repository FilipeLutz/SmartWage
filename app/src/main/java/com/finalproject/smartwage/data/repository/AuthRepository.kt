package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.UserDao
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.remote.AuthService
import com.finalproject.smartwage.data.remote.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val userDao: UserDao
) {

    // Sign Up with Firebase + Save User Locally
    suspend fun signUp(name: String, email: String, phoneNumber: String): Boolean {
        return withContext(Dispatchers.IO) {
            val firebaseUser = authService.signUp(name, email)
            if (firebaseUser != null) {
                val user = User(
                    id = firebaseUser.uid,
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
        }
    }

    // Log In with Firebase + Fetch User Profile
    suspend fun login(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val firebaseUser = authService.login(email, password)
            if (firebaseUser != null) {
                val userId = firebaseUser.uid
                val user = firestoreService.getUser(userId)
                true
            } else {
                false
            }
        }
    }

    // Get Currently Logged-In User (Offline First)
    suspend fun getCurrentUser(): User? {
        return withContext(Dispatchers.IO) {
            val firebaseUser = authService.getCurrentUser()
            if (firebaseUser != null) {
                userDao.getUserById(firebaseUser.uid)
            } else {
                null
            }
        }
    }

    // Logout from Firebase + Clear Local Data
    suspend fun logout() {
        withContext(Dispatchers.IO) {
            authService.logout()
            userDao.deleteUser(getCurrentUser()?.id ?: "")
        }
    }
}