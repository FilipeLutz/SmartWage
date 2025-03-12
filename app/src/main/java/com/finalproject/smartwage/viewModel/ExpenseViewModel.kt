package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.local.entities.Expense
import com.finalproject.smartwage.data.repository.ExpenseRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepo: ExpenseRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _userExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val userExpenses: StateFlow<List<Expense>> = _userExpenses.asStateFlow()

    init {
        loadExpenses()
    }

    fun loadExpenses() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                expenseRepo.getUserExpenses().collect { expenses ->
                    Timber.d("Expenses updated in ViewModel: $expenses")
                    _userExpenses.value = expenses
                }
            }
        } else {
            Timber.e("No logged-in user found. Cannot load expenses.")
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepo.saveOrUpdateExpenses(expense)
            loadExpenses()
        }
    }

    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            expenseRepo.deleteExpense(expenseId)
            loadExpenses()
        }
    }
}