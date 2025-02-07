package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.model.Income
import com.finalproject.smartwage.data.remote.FirestoreService

class IncomeRepository(private val firestoreService: FirestoreService) {

    suspend fun saveIncome(income: Income) {
        firestoreService.saveIncome(income)
    }

    suspend fun getUserIncomes(userId: String): List<Income> {
        return firestoreService.getUserIncomes(userId)
    }
}