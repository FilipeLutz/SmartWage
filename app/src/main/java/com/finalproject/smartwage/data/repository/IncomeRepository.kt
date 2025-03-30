package com.finalproject.smartwage.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.finalproject.smartwage.data.local.dao.IncomeDao
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.data.remote.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

// Class to handle income data operations
class IncomeRepository @Inject constructor(
    // Dependency injection for IncomeDao and FirestoreService
    private val incomeDao: IncomeDao,
    private val firestoreService: FirestoreService,
    private val auth: FirebaseAuth,
    private val context: Context
) {
    // List to hold offline updates
    private val offlineUpdateQueue = mutableListOf<Income>()
    // Function to save or update income
    suspend fun saveOrUpdateIncome(income: Income) {
        // Get the current user
        val currentUser = auth.currentUser ?: return
        // Create a copy of the income object with the user ID
        val updatedIncome = income.copy(userId = currentUser.uid)
        // Check if the user is online
        withContext(Dispatchers.IO) {
            // If the user is online, save or update the income in Firestore
            try {
                if (isOnline()) {

                    val existingIncome = firestoreService.getUserIncomes(userId = currentUser.uid)
                        .find { it.id == updatedIncome.id }

                    if (existingIncome != null) {
                        firestoreService.updateIncome(updatedIncome)
                    } else {
                        firestoreService.saveIncome(updatedIncome)
                    }
                } else {
                    offlineUpdateQueue.add(updatedIncome)
                }

                incomeDao.insertIncome(updatedIncome)
            } catch (e: Exception) {
                Timber.e(e, "Error saving/updating income")
            }
        }
    }

    // Function to get user incomes
    fun getUserIncomes(): Flow<List<Income>> {
        // Get the current user
        val currentUser = auth.currentUser
        return if (currentUser != null) {
            flow {
                val firebaseIncomes = firestoreService.getUserIncomes(currentUser.uid)
                if (firebaseIncomes.isNotEmpty()) {
                    emit(firebaseIncomes)
                } else {
                    incomeDao.getUserIncome(currentUser.uid).collect { emit(it) }
                }
            }
        } else {
            flowOf(emptyList())
        }
    }
    // Function to get offline updates
    @SuppressLint("ServiceCast")
    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
    // Function to delete income
    suspend fun deleteIncome(incomeId: String) {
        withContext(Dispatchers.IO) {
            try {
                firestoreService.deleteIncome(incomeId, auth.currentUser?.uid ?: "")
                incomeDao.deleteIncome(incomeId)
            } catch (e: Exception) {
                Timber.e(e, "Error deleting income")
            }
        }
    }
}