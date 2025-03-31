package com.finalproject.smartwage.data.repository

import com.finalproject.smartwage.data.local.dao.TaxDao
import com.finalproject.smartwage.data.remote.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

/**
 * TaxRepository is responsible for managing tax-related data.
 * It interacts with both local (Room) and remote (Firestore) data sources.
 *
 * @param taxDao The DAO for accessing tax data in the local database.
 * @param firestoreService The service for accessing tax data in Firestore.
 */

class TaxRepository @Inject constructor(
    // Dependency injection for TaxDao and FirestoreService
    private val taxDao: TaxDao,
    private val firestoreService: FirestoreService
) {

    // Function to get the tax credit for a user
    fun getRentTaxCredit(userId: String): Flow<Double> = flow {
        try {
            val firestoreData = firestoreService.getRentTaxCredit(userId)
            emit(firestoreData)
        } catch (firestoreException: Exception) {
            Timber.e(firestoreException, "Error fetching rent tax credit from Firestore")
            try {
                taxDao.getRentTaxCredit(userId).collect { daoData ->
                    emit(daoData)
                }
            } catch (roomException: Exception) {
                Timber.e(roomException, "Error fetching rent tax credit from Room")
                emit(0.0)
            }
        }
    }

    // Function to get the tuition fee relief for a user
    fun getTuitionFeeRelief(userId: String): Flow<Double> = flow {
        try {
            val firestoreData = firestoreService.getTuitionFeeRelief(userId)
            emit(firestoreData)
        } catch (firestoreException: Exception) {
            Timber.e(firestoreException, "Error fetching tuition fee relief from Firestore")
            try {
                taxDao.getTuitionFeeRelief(userId).collect { daoData ->
                    emit(daoData)
                }
            } catch (roomException: Exception) {
                Timber.e(roomException, "Error fetching tuition fee relief from Room")
                emit(0.0)
            }
        }
    }
}