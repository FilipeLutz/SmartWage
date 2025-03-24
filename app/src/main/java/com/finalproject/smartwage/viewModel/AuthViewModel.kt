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

@Suppress("DEPRECATION")
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    private val _resetEmailSent = MutableStateFlow<Boolean>(false)

    private val _errorMessage = MutableStateFlow<String?>(null)

    private val _emailExists = MutableStateFlow<Boolean?>(null)
    val emailExists: StateFlow<Boolean?> = _emailExists.asStateFlow()

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

    private fun getCurrentUser() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                _user.value = user
            }
        }
    }

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
        viewModelScope.launch {
            authRepository.logout()
            _user.value = null
        }
    }
}