package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.ExpenseDao
import com.finalproject.smartwage.data.local.entities.Expense
import com.finalproject.smartwage.data.remote.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val firestoreService: FirestoreService
) {

    // Save Expense (Sync to Firestore and Room)
    suspend fun saveExpense(expense: Expense) {
        withContext(Dispatchers.IO) {
            try {
                val expenseId = if (expense.id.isEmpty()) firestoreService.generateExpenseId() else expense.id
                val updatedExpense = expense.copy(id = expenseId)

                firestoreService.saveExpense(updatedExpense)  // Cloud Firestore
                expenseDao.insertExpense(updatedExpense)  // Local Room DB
            } catch (e: Exception) {
                // Handle error (e.g., log or show a message)
                println("Error saving expense: ${e.message}")
            }
        }
    }

    // Get User Expenses (Flow for real-time updates from Room)
    fun getUserExpenses(userId: String): Flow<List<Expense>> {
        return expenseDao.getUserExpenses(userId)
    }

    // Delete Expense (Sync Firebase + Room)
    suspend fun deleteExpense(expenseId: String) {
        withContext(Dispatchers.IO) {
            try {
                firestoreService.deleteExpense(expenseId)  // Firestore
                expenseDao.deleteExpense(expenseId)  // Room
            } catch (e: Exception) {
                // Handle error (e.g., log or show a message)
                println("Error deleting expense: ${e.message}")
            }
        }
    }
}