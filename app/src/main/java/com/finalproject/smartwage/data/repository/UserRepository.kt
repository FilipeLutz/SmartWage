package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.UserDao
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.remote.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
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
                Timber.e(e, "Error saving user: ${e.message}")
            }
        }
    }

    // Get User from Local DB or Fetch from Firestore
    fun getUser(userId: String): Flow<User?> {
        return userDao.getUserById(userId)
    }

    // Logout (Clear local user data)
    suspend fun logout(userId: String) {
        withContext(Dispatchers.IO) {
            try {
                userDao.deleteUser(userId)
                firestoreService.deleteUserData(userId)
                Timber.d("UserRepository: User logged out and data deleted: $userId")
            } catch (e: Exception) {
                Timber.e(e, "Error logging out: ${e.message}")
            }
        }
    }
}