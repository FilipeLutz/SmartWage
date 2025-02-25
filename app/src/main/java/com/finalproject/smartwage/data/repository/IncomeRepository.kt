package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.IncomeDao
import com.finalproject.smartwage.data.remote.FirestoreService
import com.finalproject.smartwage.data.local.entities.Income
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class IncomeRepository @Inject constructor(
    private val incomeDao: IncomeDao,
    private val firestoreService: FirestoreService,
    private val auth: FirebaseAuth
) {
    suspend fun saveIncome(income: Income) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val incomeWithUser = income.copy(userId = currentUser.uid)
            withContext(Dispatchers.IO) {
                try {
                    firestoreService.saveIncome(incomeWithUser)
                    incomeDao.insertIncome(incomeWithUser)
                } catch (e: Exception) {
                    Timber.e(e, "Error saving income")
                }
            }
        } else {
            Timber.e("No logged-in user found. Cannot save income.")
        }
    }

    suspend fun getUserIncomes(): Flow<List<Income>> {
        val currentUser = auth.currentUser
        return if (currentUser != null) {
            firestoreService.getUserIncomes(currentUser.uid)
            incomeDao.getUserIncome(currentUser.uid)
        } else {
            flowOf(emptyList())
        }
    }

    suspend fun deleteIncome(incomeId: String) {
        withContext(Dispatchers.IO) {
            try {
                firestoreService.deleteIncome(incomeId)
                incomeDao.deleteIncome(incomeId)
            } catch (e: Exception) {
                Timber.e(e, "Error deleting income")
            }
        }
    }
}