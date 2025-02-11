package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.UserDao
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.remote.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val firestoreService: FirestoreService
) {

    // Save User (Syncs to Firestore and Room)
    suspend fun saveUser(user: User) {
        withContext(Dispatchers.IO) {
            firestoreService.saveUser(user)  // Save to Firestore
            userDao.insertUser(user)  // Save to Local Room DB
        }
    }

    // Get User from Local DB or Fetch from Firestore
    suspend fun getUser(userId: String): User? {
        return withContext(Dispatchers.IO) {
            (userDao.getUserById(userId) ?: firestoreService.getUser(userId)) as User?
        }
    }

    // Logout (Clear local user data)
    suspend fun logout() {
        withContext(Dispatchers.IO) {
            userDao.deleteUser(userDao.getUserById(userId = String())?.id ?: "")
        }
    }
}