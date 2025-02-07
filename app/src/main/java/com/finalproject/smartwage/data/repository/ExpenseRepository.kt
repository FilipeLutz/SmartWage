package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.model.Expense
import com.finalproject.smartwage.data.remote.FirestoreService

class ExpenseRepository(private val firestoreService: FirestoreService) {

    suspend fun saveExpense(expense: Expense) {
        firestoreService.saveExpense(expense)
    }

    suspend fun getUserExpenses(userId: String): List<Expense> {
        return firestoreService.getUserExpenses(userId)
    }
}