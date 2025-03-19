package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.ExpenseDao
import com.finalproject.smartwage.data.local.entities.Expenses
import com.finalproject.smartwage.data.remote.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val incomeDao: ExpenseDao,
    private val firestoreService: FirestoreService,
    private val auth: FirebaseAuth
) {

    suspend fun saveOrUpdateExpenses(expenses: Expenses) {
        val currentUser = auth.currentUser ?: return
        val updatedExpenses = expenses.copy(userId = currentUser.uid)

        withContext(Dispatchers.IO) {
            try {
                firestoreService.saveExpenses(updatedExpenses)
                incomeDao.insertExpenses(updatedExpenses)
            } catch (e: Exception) {
                Timber.e(e, "Error saving/updating expenses")
            }
        }
    }

    fun getUserExpenses(userId: String): Flow<List<Expenses>> {
        val currentUser = auth.currentUser
        return if (currentUser != null) {
            flow {
                firestoreService.getUserExpenses(currentUser.uid).let { firebaseExpenses ->
                    if (firebaseExpenses.isNotEmpty()) {
                        emit(firebaseExpenses)
                    } else {
                        emit(incomeDao.getUserExpenses(currentUser.uid).firstOrNull() ?: emptyList())
                    }
                }
            }.flowOn(Dispatchers.IO)
        } else {
            flowOf(emptyList())
        }
    }

    suspend fun deleteExpense(expenseId: String) {
        withContext(Dispatchers.IO) {
            try {
                firestoreService.deleteExpenses(expenseId, auth.currentUser?.uid ?: "")
                incomeDao.deleteExpenses(expenseId)
            } catch (e: Exception) {
                Timber.e(e, "Error deleting expense")
            }
        }
    }
}