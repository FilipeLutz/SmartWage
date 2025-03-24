package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.UserDao
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.remote.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val firestoreService: FirestoreService,
    private val auth: FirebaseAuth
) {

    suspend fun sendPasswordResetEmail(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to send reset email")
            false
        }
    }

    // Save User
    suspend fun saveUser(user: User) {
        withContext(Dispatchers.IO) {
            try {
                firestoreService.saveUser(user)
                userDao.insertUser(user)
                Timber.d("UserRepository: User saved successfully: ${user.id}")
            } catch (e: Exception) {
                Timber.e(e, "Error saving user: ${e.message}")
            }
        }
    }

    // Get User from Local DB or Firestore if not available
    fun getUser(userId: String, scope: CoroutineScope): Flow<User?> {
        return userDao.getUserById(userId).onEach { localUser ->
            if (localUser == null) {
                scope.launch {
                    val fetchedUser = firestoreService.getUser(userId)
                    fetchedUser?.let {
                        userDao.insertUser(it)
                    }
                }
            }
        }
    }

    // Delete User Account
    suspend fun deleteUserAccount(userId: String) {
        withContext(Dispatchers.IO) {
            try {
                firestoreService.deleteUser(userId)
                userDao.deleteUser(userId)
                Timber.d("UserRepository: User deleted successfully: $userId")
            } catch (e: Exception) {
                Timber.e(e, "Error deleting user account: ${e.message}")
            }
        }
    }
}