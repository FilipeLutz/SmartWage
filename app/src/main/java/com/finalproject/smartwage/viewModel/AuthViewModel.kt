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

@Suppress("DEPRECATION")
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepo: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    private val _resetEmailSent = MutableStateFlow<Boolean>(false)

    private val _errorMessage = MutableStateFlow<String?>(null)

    private val _emailExists = MutableStateFlow<Boolean?>(null)
    val emailExists: StateFlow<Boolean?> = _emailExists.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            val currentUser = userRepo.getCurrentUserWithSync()
            _user.value = currentUser
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(email)
            if (result) {
                _resetEmailSent.value = true
            } else {
                _errorMessage.value = "Failed to send reset email. Please try again."
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            if (result is AuthRepository.AuthResult.Success) {
                loadUser()
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun signUp(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val isRegistered = authRepository.isEmailRegistered(email)
            _emailExists.value = isRegistered

            if (isRegistered) {
                _errorMessage.value = "This email is already registered."
                _isLoading.value = false
                return@launch
            }

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
                    userRepo.saveUserToRoom(newUser)
                    _user.value = newUser
                }
                onResult(true)
            } else {
                _errorMessage.value = (result as AuthRepository.AuthResult.Failure).errorMessage
                onResult(false)
            }
            _isLoading.value = false
        }
    }

    fun isEmailRegistered(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val isRegistered = authRepository.isEmailRegistered(email)
            _emailExists.value = isRegistered
            _isLoading.value = false
        }
    }

    fun logout() {
        auth.signOut()
    }
}