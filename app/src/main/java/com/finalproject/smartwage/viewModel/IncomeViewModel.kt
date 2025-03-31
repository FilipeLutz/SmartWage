package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.data.repository.IncomeRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for managing income-related data and operations.
 *
 * @property incomeRepo Repository for income data operations.
 * @property auth Firebase authentication instance.
 */

@HiltViewModel
class IncomeViewModel @Inject constructor(
    // Injected dependencies
    private val incomeRepo: IncomeRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    // StateFlow to hold the list of incomes
    private val _userIncomes = MutableStateFlow<List<Income>>(emptyList())
    val userIncomes: StateFlow<List<Income>> = _userIncomes.asStateFlow()

    // Initialize the ViewModel and load incomes
    init {
        loadIncomes()
    }

    // Load the list of incomes
    fun loadIncomes() {
        // Check if the user is logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // If user is logged in, fetch incomes from the repository
            viewModelScope.launch {
                // Observe the incomes from the repository
                incomeRepo.getUserIncomes().collect { incomes ->
                    // Sort the incomes by date in descending order
                    _userIncomes.value = incomes.sortedByDescending { it.date }
                }
            }
        } else {
            // If no user is logged in, log a message
            Timber.e("No logged-in user found. Cannot load incomes.")
        }
    }

    // Get all company names from the list of incomes
    fun getAllCompanyNames(): List<String> {
        return userIncomes.value.map { it.source }.distinct()
    }

    // Get the most recently saved company name
    fun getLastCompanyName(): String {
        // Check if there are any incomes
        return userIncomes.value.firstOrNull()?.source ?: ""
    }

    // Get the last income amount from the list of incomes
    fun updateIncome(
        income: Income
    ) {
        viewModelScope.launch {
            // update the income in the repository
            incomeRepo.saveOrUpdateIncome(income)
            // Load the updated list of incomes
            loadIncomes()
        }
    }

    // Delete an income from the list of incomes
    fun deleteIncome(incomeId: String) {
        viewModelScope.launch {
            // Delete the income from the repository
            incomeRepo.deleteIncome(incomeId)
            // Load the updated list of incomes
            loadIncomes()
        }
    }
}