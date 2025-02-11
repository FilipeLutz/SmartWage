package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.IncomeDao
import com.finalproject.smartwage.data.remote.FirestoreService
import com.finalproject.smartwage.data.local.entities.Income
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IncomeRepository(
    private val incomeDao: IncomeDao,
    private val firestoreService: FirestoreService
) {

    // Save Income (Sync to Firestore & Room)
    suspend fun saveIncome(income: Income) {
        withContext(Dispatchers.IO) {
            val incomeId = if (income.id.isEmpty()) firestoreService.generateIncomeId() else income.id
            val updatedIncome = income.copy(id = incomeId)

            firestoreService.saveIncome(updatedIncome)  // Firestore
            incomeDao.insertIncome(updatedIncome)  // Room
        }
    }

    // Get User's Incomes (First from Room, then Firestore)
    suspend fun getUserIncomes(userId: String): List<com.finalproject.smartwage.data.model.Income> {
        return withContext(Dispatchers.IO) {
            incomeDao.getUserIncome(userId)
            firestoreService.getUserIncomes(userId)
            }
        }


    // Delete Income (Firestore & Room)
    suspend fun deleteIncome(incomeId: String) {
        withContext(Dispatchers.IO) {
            firestoreService.deleteIncome(incomeId)  // Firestore
            incomeDao.deleteIncome(incomeId)  // Room
        }
    }
}