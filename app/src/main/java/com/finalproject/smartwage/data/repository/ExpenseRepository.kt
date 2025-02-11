package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.ExpenseDao
import com.finalproject.smartwage.data.local.entities.Expense
import com.finalproject.smartwage.data.remote.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val firestoreService: FirestoreService
) {

    // Save Expense (Sync to Firestore and Room)
    suspend fun saveExpense(expense: Expense) {
        withContext(Dispatchers.IO) {
            val expenseId = if (expense.id.isEmpty()) firestoreService.generateExpenseId() else expense.id
            val updatedExpense = expense.copy(id = expenseId)

            firestoreService.saveExpense(updatedExpense)  // Cloud Firestore
            expenseDao.insertExpense(updatedExpense)  // Local Room DB
        }
    }

    // Get User Expenses (First from Room, then Firestore if empty)
    suspend fun getUserExpenses(userId: String): List<com.finalproject.smartwage.data.model.Expense> {
        return withContext(Dispatchers.IO) {
            expenseDao.getUserExpenses(userId)
            firestoreService.getUserExpenses(userId)
            }
        }

    // Delete Expense (Sync Firebase + Room)
    suspend fun deleteExpense(expenseId: String) {
        withContext(Dispatchers.IO) {
            firestoreService.deleteExpense(expenseId)  // Firestore
            expenseDao.deleteExpense(expenseId)  // Room
        }
    }
}