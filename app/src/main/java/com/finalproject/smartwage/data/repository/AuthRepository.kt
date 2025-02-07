package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.remote.AuthService
import com.google.firebase.auth.FirebaseUser

class AuthRepository(private val authService: AuthService) {

    suspend fun signUp(email: String, password: String): FirebaseUser? {
        return authService.signUp(email, password)
    }

    suspend fun login(email: String, password: String): FirebaseUser? {
        return authService.login(email, password)
    }

    fun getCurrentUser(): FirebaseUser? {
        return authService.getCurrentUser()
    }

    fun logout() {
        authService.logout()
    }
}