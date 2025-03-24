package com.finalproject.smartwage.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.repository.AuthRepository
import com.finalproject.smartwage.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val authRepo: AuthRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _resetEmailSent = MutableStateFlow<Boolean>(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        getCurrentUser()
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            val result = userRepo.sendPasswordResetEmail(email)
            if (result) {
                _resetEmailSent.value = true
            } else {
                _errorMessage.value = "Failed to send reset email. Please try again."
            }
        }
    }

    // Fetch current user data
    private fun getCurrentUser() {
        auth.currentUser?.uid?.let { userId ->
            viewModelScope.launch {
                userRepo.getUser(userId, viewModelScope).collect { localUser ->
                    if (localUser != null) {
                        _user.value = localUser
                    }
                }
            }
        }
    }

    // Update User Profile
    fun updateUser(name: String, phoneNumber: String, imageUri: Uri?) {
        viewModelScope.launch {
            val currentUser = _user.value
            currentUser?.let {
                val updatedUser = it.copy(
                    name = name,
                    phoneNumber = phoneNumber,
                    profilePicture = imageUri?.toString() ?: it.profilePicture
                )
                userRepo.saveUser(updatedUser)
                _user.value = updatedUser
            }
        }
    }

    // Delete User Account
    fun deleteUserAccount() {
        auth.currentUser?.uid?.let { userId ->
            viewModelScope.launch {
                userRepo.deleteUserAccount(userId)
                authRepo.deleteUser()
                _user.value = null
            }
        }
    }
}