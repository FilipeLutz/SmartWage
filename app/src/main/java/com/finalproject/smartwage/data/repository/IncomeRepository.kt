package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.IncomeDao
import com.finalproject.smartwage.data.remote.FirestoreService
import com.finalproject.smartwage.data.local.entities.Income
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class IncomeRepository(
    private val incomeDao: IncomeDao,
    private val firestoreService: FirestoreService
) {

    // Save Income (Sync to Firestore and Room)
    suspend fun saveIncome(income: Income) {
        withContext(Dispatchers.IO) {
            try {
                val incomeId = if (income.id.isEmpty()) firestoreService.generateIncomeId() else income.id
                val updatedIncome = income.copy(id = incomeId)

                firestoreService.saveIncome(updatedIncome)  // Firestore
                incomeDao.insertIncome(updatedIncome)  // Room
            } catch (e: Exception) {
                // Handle error (e.g., log or show a message)
                println("Error saving income: ${e.message}")
            }
        }
    }

    // Get User Incomes (Flow for real-time updates from Room)
    fun getUserIncomes(userId: String): Flow<List<Income>> {
        return incomeDao.getUserIncome(userId)
    }

    // Delete Income (Sync Firebase + Room)
    suspend fun deleteIncome(incomeId: String) {
        withContext(Dispatchers.IO) {
            try {
                firestoreService.deleteIncome(incomeId)  // Firestore
                incomeDao.deleteIncome(incomeId)  // Room
            } catch (e: Exception) {
                // Handle error (e.g., log or show a message)
                println("Error deleting income: ${e.message}")
            }
        }
    }
}