package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.TaxDao
import com.finalproject.smartwage.data.local.entities.Tax
import com.finalproject.smartwage.data.remote.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaxRepository(
    private val taxDao: TaxDao,
    private val firestoreService: FirestoreService
) {

    // Save Tax Calculation Result
    suspend fun saveTax(tax: Tax) {
        withContext(Dispatchers.IO) {
            firestoreService.saveTax(tax)  // Cloud Firestore
            taxDao.insertTax(tax)  // Local Room DB
        }
    }

    // Get Latest Tax Calculation (First from Room, then Firestore if empty)
    suspend fun getUserTax(userId: String): Tax? {
        return withContext(Dispatchers.IO) {
            taxDao.getUserTax(userId).firstOrNull() ?: firestoreService.getUserTax(userId) as Tax?
        }
    }

    // Clear Tax Calculation for User (Used when logging out)
    suspend fun deleteTax(userId: String) {
        withContext(Dispatchers.IO) {
            taxDao.deleteTax(userId)
        }
    }
}