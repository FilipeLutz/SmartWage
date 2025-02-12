package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.UserDao
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.remote.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val firestoreService: FirestoreService
) {

    // Save User (Syncs to Firestore and Room)
    suspend fun saveUser(user: User) {
        withContext(Dispatchers.IO) {
            try {
                firestoreService.saveUser(user)  // Save to Firestore
                userDao.insertUser(user)  // Save to Local Room DB
            } catch (e: Exception) {
                // Handle error (e.g., log or show a message)
                println("Error saving user: ${e.message}")
            }
        }
    }

    // Get User from Local DB or Fetch from Firestore
    fun getUser(userId: String): Flow<User?> {
        return userDao.getUserById(userId)
    }

    // Logout (Clear local user data)
    suspend fun logout() {
        withContext(Dispatchers.IO) {
            try {
                userDao.deleteUser(userDao.getUserById(userId = String()).first().toString())
            } catch (e: Exception) {
                // Handle error (e.g., log or show a message)
                println("Error logging out: ${e.message}")
            }
        }
    }
}