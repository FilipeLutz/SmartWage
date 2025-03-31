package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.repository.AuthRepository
import com.finalproject.smartwage.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling authentication-related operations.
 *
 * @property authRepository The repository for authentication operations.
 * @property userRepo The repository for user-related operations.
 * @property auth The FirebaseAuth instance for Firebase authentication.
 */

@HiltViewModel
class AuthViewModel @Inject constructor(
    /// Injecting the AuthRepository and UserRepository dependencies using Hilt
    private val authRepository: AuthRepository,
    private val userRepo: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    // The MutableStateFlow for holding the current user
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    // The MutableStateFlow for holding the loading state
    private val _isLoading = MutableStateFlow(false)

    // The MutableStateFlow for holding the reset email sent state
    private val _resetEmailSent = MutableStateFlow<Boolean>(false)

    // The MutableStateFlow for holding the error message
    private val _errorMessage = MutableStateFlow<String?>(null)

    // The MutableStateFlow for holding the email existence state
    private val _emailExists = MutableStateFlow<Boolean?>(null)
    val emailExists: StateFlow<Boolean?> = _emailExists.asStateFlow()

    // Initializing the ViewModel by loading the current user
    init {
        loadUser()
    }

    // Function to load the current user from the repository
    private fun loadUser() {
        viewModelScope.launch {
            val currentUser = userRepo.getCurrentUserWithSync()
            _user.value = currentUser
        }
    }

    // Function to send a password reset email
    fun sendPasswordResetEmail(
        email: String
    ) {
        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(email)
            if (result) {
                _resetEmailSent.value = true
            } else {
                _errorMessage.value = "Failed to send reset email. Please try again."
            }
        }
    }

    // Function to log in the user
    fun login(
        email: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            // Set loading state to true
            val result = authRepository.login(email, password)
            // Check if the login was successful
            if (result is AuthRepository.AuthResult.Success) {
                // Set loading state to false
                loadUser()
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    // Function to sign up a new user
    fun signUp(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        onResult: (Boolean) -> Unit
    ) {
        // Check if the email is already registered
        viewModelScope.launch {
            // Set loading state to true
            _isLoading.value = true
            _errorMessage.value = null
            // Check if the email is already registered
            val isRegistered = authRepository.isEmailRegistered(email)
            _emailExists.value = isRegistered
            // If the email is already registered, set the error message and return
            if (isRegistered) {
                _errorMessage.value = "This email is already registered."
                _isLoading.value = false
                return@launch
            }
            // Attempt to sign up the user
            val result = authRepository.signUp(name, email, password, phoneNumber)
            if (result is AuthRepository.AuthResult.Success) {
                // Immediately save user to Room after signup
                auth.currentUser?.uid?.let { userId ->
                    val newUser = User(
                        id = userId,
                        name = name,
                        email = email,
                        phoneNumber = phoneNumber,
                        profilePicture = null
                    )
                    // Save the new user to Room
                    userRepo.saveUserToRoom(newUser)
                    _user.value = newUser
                }
                // Load the user from the repository
                onResult(true)
            } else {
                // If the sign-up failed, set the error message
                _errorMessage.value = (result as AuthRepository.AuthResult.Failure).errorMessage
                onResult(false)
            }
            // Set loading state to false
            _isLoading.value = false
        }
    }

    // Function to check if the email is already registered
    fun isEmailRegistered(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // Check if the email is already registered
            val isRegistered = authRepository.isEmailRegistered(email)
            _emailExists.value = isRegistered
            _isLoading.value = false
        }
    }

    // Function to log out the user
    fun logout() {
        auth.signOut()
    }
}