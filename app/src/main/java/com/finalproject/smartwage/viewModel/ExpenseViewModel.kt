package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.local.entities.Expenses
import com.finalproject.smartwage.data.repository.ExpenseRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for managing expenses.
 *
 * @property expenseRepo Repository for accessing expense data.
 * @property auth Firebase authentication instance.
 */

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    // Injecting the ExpenseRepository and FirebaseAuth instances
    private val expenseRepo: ExpenseRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    // MutableStateFlow to hold the list of user expenses
    private val _userExpenses = MutableStateFlow<List<Expenses>>(emptyList())
    val userExpenses: StateFlow<List<Expenses>> = _userExpenses.asStateFlow()

    // Initializing the ViewModel and loading expenses
    init {
        loadExpenses()
    }

    // Function to load expenses from the repository
    fun loadExpenses() {
        // Check if the user is logged in
        val currentUser = auth.currentUser
        // If the user is logged in, collect expenses from the repository
        if (currentUser != null) {
            viewModelScope.launch {
                // Collecting expenses from the repository
                expenseRepo.getUserExpenses().collect { expenses ->
                    // Sort the expenses by date in descending order
                    _userExpenses.value = expenses.sortedByDescending { it.date }
                }
            }
        } else {
            // If no user is logged in, log a message
            Timber.e("No logged-in user found. Cannot load expenses.")
        }
    }

    // Function to add or update an expense
    fun addOrUpdateExpense(
        expenses: Expenses
    ) {
        viewModelScope.launch {
            // save or update the expense in the repository
            expenseRepo.saveOrUpdateExpenses(expenses)
            // Reload the expenses after saving
            loadExpenses()
        }
    }

    // Function to delete an expense
    fun deleteExpense(
        expenses: String
    ) {
        viewModelScope.launch {
            // Delete the expense from the repository
            expenseRepo.deleteExpense(expenses)
            // Reload the expenses after deletion
            loadExpenses()
        }
    }

    // Function to get rent tax credit
    fun getRentTaxCredit(): Double {
        return _userExpenses.value.filter { it.category == "Rent Tax Credit" }.sumOf { it.amount }
    }

    // Function to get tuition fee relief
    fun getTuitionFeeRelief(): Double {
        return _userExpenses.value.filter { it.category == "Tuition Fee Relief" }
            .sumOf { it.amount }
    }
}