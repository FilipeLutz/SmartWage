package com.finalproject.smartwage.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import androidx.core.net.toUri
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _resetEmailSent = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    private val _hasProfilePicture = MutableStateFlow(false)

    init {
        loadUserProfile()
        viewModelScope.launch {
            userRepo.cleanupInvalidProfilePictures()
            delay(300)
            loadUserProfile()
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val currentUser = userRepo.getCurrentUserWithSync()
            _user.value = currentUser

            // Properly handle the image URI
            currentUser?.profilePicture?.let { uriString ->
                runCatching {
                    uriString.toUri().takeIf { it != Uri.EMPTY }
                }.getOrNull()?.let { uri ->
                    _imageUri.value = uri
                }
            }
        }
    }

    // Update User Profile
    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                userRepo.saveUserToFirestore(user)
                userRepo.saveUserToRoom(user)
                _user.value = user
            } catch (e: Exception) {
                Timber.e(e, "Error updating user")
            }
        }
    }

    // Delete User Account
    fun deleteUserAccount(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

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
                        onFailure(task.exception ?: Exception("Failed to delete from Authentication"))
                    }
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch
                userRepo.updateProfilePicture(userId, uri.toString())

                _user.value?.let { currentUser ->
                    val updatedUser = currentUser.copy(profilePicture = uri.toString())
                    _user.value = updatedUser
                    _imageUri.value = uri
                }
            } catch (e: Exception) {
                Timber.e(e, "Error updating profile picture")
            }
        }
    }

    // Delete profile picture
    fun deleteProfilePicture() {
        viewModelScope.launch {
            try {
                userRepo.deleteProfilePicture()
                _user.value?.let { currentUser ->
                    val updatedUser = currentUser.copy(profilePicture = null)
                    _user.value = updatedUser
                    _hasProfilePicture.value = false
                }
            } catch (e: Exception) {
                Timber.e(e, "Error deleting profile picture")
            }
        }
    }

    // Send Password Reset Email
    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            val result = userRepo.sendPasswordResetEmail(email)
            _resetEmailSent.value = result
            if (!result) {
                _errorMessage.value = "Failed to send reset email. Please try again."
            }
        }
    }
}