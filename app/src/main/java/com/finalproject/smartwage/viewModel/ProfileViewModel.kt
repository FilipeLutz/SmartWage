package com.finalproject.smartwage.viewModel

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for managing user profile data and operations.
 *
 * @param userRepo Repository for user data operations.
 * @param auth Firebase Authentication instance.
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    // injecting the UserRepository and FirebaseAuth instances
    private val userRepo: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    // StateFlow to hold the reset email sent status
    private val _resetEmailSent = MutableStateFlow(false)

    // state flow to hold the error message
    private val _errorMessage = MutableStateFlow<String?>(null)

    // StateFlow to hold the user profile data
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    // StateFlow to hold the image URI
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    // StateFlow to check if the profile picture exists
    private val _hasProfilePicture = MutableStateFlow(false)

    // initializing the ViewModel
    init {
        // Load the user profile data
        loadUserProfile()

        viewModelScope.launch {
            // Observe the user profile data and update the image URI accordingly
            user.collect { user ->
                // Check if the user has a profile picture
                user?.profilePicture?.let { uriString ->
                    // Convert the URI string to a Uri object
                    _imageUri.value = uriString.toUri()
                    // Check if the URI is not empty
                } ?: run { _imageUri.value = null }
            }
        }
    }

    // loading the user profile data
    private fun loadUserProfile() {
        viewModelScope.launch {
            // Fetch the current user from the repository
            val currentUser = userRepo.getCurrentUserWithSync()
            _user.value = currentUser

            // Properly handle the image URI
            currentUser?.profilePicture?.let { uriString ->
                runCatching {
                    // Convert the URI string to a Uri object
                    uriString.toUri().takeIf { it != Uri.EMPTY }
                }.getOrNull()?.let { uri ->
                    // Update the image URI state flow
                    _imageUri.value = uri
                }
            }
        }
    }

    // Update User Profile
    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                // Update the user profile in Firestore and Room
                userRepo.saveUserToFirestore(user)
                userRepo.saveUserToRoom(user)
                _user.value = user
            } catch (e: Exception) {
                // Timber the error
                Timber.e(e, "Error updating user")
            }
        }
    }

    // Delete User Account
    fun deleteUserAccount(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Check if the user is authenticated
        val userId = auth.currentUser?.uid ?: return

        // viewModelScope to launch a coroutine
        viewModelScope.launch {
            try {
                // Delete from Firestore
                userRepo.deleteUserFromFirestore(userId)

                // Delete from Room
                userRepo.deleteUserFromRoom(userId)

                // Delete from Authentication
                auth.currentUser?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _user.value = null
                        onSuccess()
                    } else {
                        // Handle failure to delete from Authentication
                        onFailure(
                            task.exception ?: Exception("Failed to delete from Authentication")
                        )
                    }
                }
            } catch (e: Exception) {
                // Handle any exceptions that occur during the deletion process
                onFailure(e)
            }
        }
    }

    // Update Profile Picture
    fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch {
            try {
                // Check if the user is authenticated
                val userId = auth.currentUser?.uid ?: return@launch
                // Upload the image to Firebase Storage
                userRepo.updateProfilePicture(userId, uri.toString())

                // Update the user profile in Firestore and Room
                _user.value?.let { currentUser ->
                    val updatedUser = currentUser.copy(profilePicture = uri.toString())
                    _user.value = updatedUser
                    _imageUri.value = uri
                }
            } catch (e: Exception) {
                // Handle any exceptions that occur during the update process
                Timber.e(e, "Error updating profile picture")
            }
        }
    }

    // Delete profile picture
    fun deleteProfilePicture() {
        viewModelScope.launch {
            try {
                _imageUri.value = null
                userRepo.deleteProfilePicture()
                _user.value?.let { currentUser ->
                    // Update the user profile in Firestore and Room
                    val updatedUser = currentUser.copy(profilePicture = null)
                    _user.value = updatedUser
                    _hasProfilePicture.value = false
                }
            } catch (e: Exception) {
                // Handle any exceptions that occur during the deletion process
                Timber.e(e, "Error deleting profile picture")
            }
        }
    }

    // Send Password Reset Email
    fun sendPasswordResetEmail(
        email: String
    ) {
        viewModelScope.launch {
            val result = userRepo.sendPasswordResetEmail(email)
            // Update the reset email sent status
            _resetEmailSent.value = result
            if (!result) {
                // Handle the error case
                _errorMessage.value = "Failed to send reset email. Please try again."
            }
        }
    }
}