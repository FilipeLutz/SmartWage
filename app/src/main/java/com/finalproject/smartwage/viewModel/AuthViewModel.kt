package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // StateFlow to observe user data
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    // StateFlow for loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _resetEmailSent = MutableStateFlow<Boolean>(false)
    val resetEmailSent: StateFlow<Boolean> = _resetEmailSent.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

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

    // Fetch current user inside a coroutine
    private fun getCurrentUser() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                _user.value = user
            }
        }
    }

    // Login with email and password
    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = authRepository.login(email, password)
            if (result is AuthRepository.AuthResult.Success) {
                onResult(true)
            } else {
                _errorMessage.value = (result as AuthRepository.AuthResult.Failure).errorMessage
                onResult(false)
            }

            _isLoading.value = false
        }
    }

    // Sign up with name, email, password, and phoneNumber
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

            val result = authRepository.signUp(name, email, password, phoneNumber)
            if (result is AuthRepository.AuthResult.Success) {
                onResult(true)
            } else {
                _errorMessage.value = (result as AuthRepository.AuthResult.Failure).errorMessage
                onResult(false)
            }

            _isLoading.value = false
        }
    }

    // Logout the user
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _user.value = null
        }
    }
}