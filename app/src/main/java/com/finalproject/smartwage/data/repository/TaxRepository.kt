package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.model.Tax
import com.finalproject.smartwage.data.remote.FirestoreService

class TaxRepository(private val firestoreService: FirestoreService) {

    suspend fun saveTax(tax: Tax) {
        firestoreService.saveTax(tax)
    }

    suspend fun getUserTax(userId: String): List<Tax> {
        return firestoreService.getUserTax(userId)
    }

    suspend fun calculateTax(userId: String): Double {
        return firestoreService.calculateTax(userId)
    }
}