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

/**
 * ExpenseRepository is a class that handles the data operations related to expenses.
 * It interacts with the local database and Firestore to save, update, delete, and retrieve expenses.
 *
 * @param incomeDao The DAO for accessing expense data in the local database.
 * @param firestoreService The service for accessing Firestore.
 * @param auth The FirebaseAuth instance for user authentication.
 */

class ExpenseRepository @Inject constructor(
    // Injecting the required dependencies
    private val incomeDao: ExpenseDao,
    private val firestoreService: FirestoreService,
    private val auth: FirebaseAuth
) {
    // Function to save or update expenses
    suspend fun saveOrUpdateExpenses(expenses: Expenses) {
        val currentUser = auth.currentUser ?: return
        val updatedExpenses = expenses.copy(userId = currentUser.uid)
        // Save or update the expenses in Firestore and local database
        withContext(Dispatchers.IO) {
            // Check if the expense already exists in the local database
            try {
                firestoreService.saveExpenses(updatedExpenses)
                incomeDao.insertExpenses(updatedExpenses)
                // If the expense already exists, update it
            } catch (e: Exception) {
                Timber.e(e, "Error saving/updating expenses")
            }
        }
    }

    // Function to get user expenses
    fun getUserExpenses(): Flow<List<Expenses>> {
        val currentUser = auth.currentUser
        // Check if the user is logged in
        return if (currentUser != null) {
            // Get the user expenses from Firestore and local database
            flow {
                // Get expenses from Firestore
                firestoreService.getUserExpenses(currentUser.uid).let { firebaseExpenses ->
                    // Check if the expenses are not empty
                    if (firebaseExpenses.isNotEmpty()) {
                        emit(firebaseExpenses)
                    }
                    // If the expenses are empty, get them from the local database
                    else {
                        emit(incomeDao.getUserExpenses(currentUser.uid).firstOrNull() ?: emptyList())
                    }
                }
            }.flowOn(Dispatchers.IO)
            // If the user is not logged in, return an empty list
        } else {
            flowOf(emptyList())
        }
    }

    // Function to delete an expense
    suspend fun deleteExpense(expenseId: String) {
        withContext(Dispatchers.IO) {
            try {
                // Delete the expense from Firestore and local database
                firestoreService.deleteExpenses(expenseId)
                incomeDao.deleteExpenses(expenseId)
            } catch (e: Exception) {
                Timber.e(e, "Error deleting expense")
            }
        }
    }
}