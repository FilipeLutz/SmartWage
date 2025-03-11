package com.finalproject.smartwage.viewModel

import androidx.compose.runtime.mutableStateOf
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
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    var showExpenseDialog = mutableStateOf(false)

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            auth.currentUser?.let { user ->
                repository.getExpenses(user.uid).collect { expenses ->
                    _expenses.value = expenses
                }
            }
        }
    }

    fun addExpense(category: String, amount: Double, description: String, date: String) {
        auth.currentUser?.let { user ->
            val newExpense = Expense(
                category = category,
                amount = amount,
                description = description,
                date = System.currentTimeMillis(),
                userId = user.uid
            )
            viewModelScope.launch {
                repository.addExpense(newExpense)
                loadExpenses()
            }
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.addExpense(expense)
            loadExpenses()
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense.id)
            loadExpenses()
        }
    }
}