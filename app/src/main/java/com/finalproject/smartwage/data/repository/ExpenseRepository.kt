package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.ExpenseDao
import com.finalproject.smartwage.data.local.entities.Expense
import com.finalproject.smartwage.data.remote.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val firestoreService: FirestoreService,
    private val auth: FirebaseAuth
) {
    fun getExpenses(userId: String): Flow<List<Expense>> = firestoreService.getUserExpenses(userId)

    suspend fun addExpense(expense: Expense) {
        val currentUser = auth.currentUser ?: return
        val newExpense = expense.copy(userId = currentUser.uid)

        withContext(Dispatchers.IO) {
            try {
                firestoreService.saveExpense(newExpense)
                expenseDao.insertExpenses(newExpense)
            } catch (e: Exception) {
                Timber.e(e, "Error adding expense")
            }
        }
    }

    suspend fun deleteExpense(expenseId: String) {
        withContext(Dispatchers.IO) {
            try {
                firestoreService.deleteExpense(expenseId)
                expenseDao.deleteExpenses(expenseId)
            } catch (e: Exception) {
                Timber.e(e, "Error deleting expense")
            }
        }
    }
}