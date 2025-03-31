package com.finalproject.smartwage.data.repository

import android.net.Uri
import androidx.core.net.toUri
import com.finalproject.smartwage.data.local.dao.UserDao
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.remote.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * UserRepository is a singleton class that handles user data operations.
 * It interacts with Firestore and Room to manage user information.
 *
 * @param userDao The DAO for accessing user data in the local database.
 * @param firestoreService The service for accessing Firestore.
 * @param auth The FirebaseAuth instance for authentication.
 */

@Singleton
class UserRepository @Inject constructor(
    // Dependency injection for UserDao, FirestoreService and FirebaseAuth
    private val userDao: UserDao,
    private val firestoreService: FirestoreService,
    private val auth: FirebaseAuth
) {

    // Get current user from Firestore or Room
    suspend fun getCurrentUserWithSync(): User? {
        val firebaseUser = auth.currentUser ?: return null.also {
            Timber.d("No current user")
        }

        Timber.d("Fetching user from Firestore...")
        val remoteUser = firestoreService.getUser(firebaseUser.uid).also {
            Timber.d("Firestore user: ${it?.profilePicture}")
        }

        remoteUser?.let { user ->
            Timber.d("Saving user to Room: ${user.profilePicture}")
            userDao.insertUser(user)
            return user
        }

        return userDao.getUserById(firebaseUser.uid).firstOrNull().also {
            Timber.d("Room user: ${it?.profilePicture}")
        }
    }

    // Save user to Firestore
    suspend fun saveUserToFirestore(user: User) {
        try {
            firestoreService.saveUser(user)
        } catch (e: Exception) {
            Timber.e(e, "Error saving user to Firestore")
        }
    }

    // Save user to Room
    suspend fun saveUserToRoom(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }

    // Update profile picture
    suspend fun updateProfilePicture(userId: String, imageUri: String) {
        if (!isUriValid(imageUri)) {
            Timber.w("Invalid image URI provided: $imageUri")
            return
        }

        try {
            firestoreService.saveProfilePicture(userId, imageUri)
            userDao.getUserById(userId).firstOrNull()?.let { user ->
                userDao.insertUser(user.copy(profilePicture = imageUri))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error updating profile picture")
            throw e
        }
    }

    // Check if the URI is valid
    fun isUriValid(uriString: String): Boolean {
        return try {
            val uri = uriString.toUri()
            uri != Uri.EMPTY && uri.toString().isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }

    // Delete profile picture
    suspend fun deleteProfilePicture() {
        val userId = auth.currentUser?.uid ?: return
        try {
            firestoreService.deleteProfilePicture(userId)
            userDao.getUserById(userId).firstOrNull()?.let { user ->
                val updatedUser = user.copy(profilePicture = null)
                userDao.insertUser(updatedUser)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error deleting profile picture")
            throw e
        }
    }


    // Delete user from Firestore
    suspend fun deleteUserFromFirestore(userId: String) {
        try {
            firestoreService.deleteUser(userId)
        } catch (e: Exception) {
            Timber.e(e, "Error deleting user from Firestore")
        }
    }

    // Delete user from Room
    suspend fun deleteUserFromRoom(userId: String) {
        withContext(Dispatchers.IO) {
            userDao.deleteUser(userId)
        }
    }

    // Send password reset email
    suspend fun sendPasswordResetEmail(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to send reset email")
            false
        }
    }
}