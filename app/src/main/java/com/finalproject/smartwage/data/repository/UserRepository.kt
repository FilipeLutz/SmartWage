package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.model.User
import com.finalproject.smartwage.data.remote.FirestoreService

class UserRepository(private val firestoreService: FirestoreService) {

    suspend fun saveUser(user: User) {
        firestoreService.saveUser(user)
    }

    suspend fun getUser(userId: String): User? {
        return firestoreService.getUser(userId)
    }
}