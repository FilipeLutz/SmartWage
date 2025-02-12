package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.TaxDao
import com.finalproject.smartwage.data.local.entities.Tax
import com.finalproject.smartwage.data.remote.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TaxRepository(
    private val taxDao: TaxDao,
    private val firestoreService: FirestoreService
) {

    // Save Tax Calculation Result
    suspend fun saveTax(tax: Tax) {
        withContext(Dispatchers.IO) {
            try {
                firestoreService.saveTax(tax)  // Firestore
                taxDao.insertTax(tax)  // Room
            } catch (e: Exception) {
                // Handle error (e.g., log or show a message)
                println("Error saving tax: ${e.message}")
            }
        }
    }

    // Get Latest Tax Calculation (Flow for real-time updates from Room)
    fun getUserTax(userId: String): Flow<List<Tax>> {
        return taxDao.getUserTax(userId)
    }

    // Clear Tax Calculation for User (Used when logging out)
    suspend fun deleteTax(userId: String) {
        withContext(Dispatchers.IO) {
            try {
                taxDao.deleteTax(userId)  // Room
            } catch (e: Exception) {
                // Handle error (e.g., log or show a message)
                println("Error deleting tax: ${e.message}")
            }
        }
    }
}