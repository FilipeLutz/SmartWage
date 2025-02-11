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

    // Live Data or StateFlow to observe user data
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        getCurrentUser()
    }

    // Fetch current user inside a coroutine
    private fun getCurrentUser() {
        viewModelScope.launch {
            _user.value = authRepository.getCurrentUser()
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = authRepository.login(email, password)
            if (success) getCurrentUser() // Update user state on successful login
            onResult(success)
        }
    }

    fun signUp(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val success = authRepository.signUp(name, email, password)
            if (success) getCurrentUser() // Update user state on successful signup
            onResult(success)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _user.value = null // Reset user on logout
        }
    }
}