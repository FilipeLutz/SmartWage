package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.ExpenseDao
import com.finalproject.smartwage.data.local.entities.Expense
import com.finalproject.smartwage.data.remote.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val incomeDao: ExpenseDao,
    private val firestoreService: FirestoreService,
    private val auth: FirebaseAuth,
) {

    suspend fun saveOrUpdateExpenses(expense: Expense) {
        val currentUser = auth.currentUser ?: return
        val updatedExpense = expense.copy(userId = currentUser.uid)

        withContext(Dispatchers.IO) {
            try {
                firestoreService.saveExpense(updatedExpense)
                incomeDao.insertExpenses(updatedExpense)
            } catch (e: Exception) {
                Timber.e(e, "Error saving/updating expense")
            }
        }
    }

    fun getUserExpenses(): Flow<List<Expense>> = flow {
        val currentUser = auth.currentUser ?: return@flow

        while (true) {
            val firebaseExpenses = firestoreService.getUserExpenses(currentUser.uid)
            val roomExpenses = incomeDao.getUserExpenses(currentUser.uid).firstOrNull() ?: emptyList()

            val mergedExpenses = (firebaseExpenses + roomExpenses).distinctBy { it.id }
            emit(mergedExpenses)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun deleteExpense(expenseId: String) {
        withContext(Dispatchers.IO) {
            try {
                firestoreService.deleteExpense(expenseId, auth.currentUser?.uid ?: "")
                incomeDao.deleteExpenses(expenseId)
            } catch (e: Exception) {
                Timber.e(e, "Error deleting expense")
            }
        }
    }
}