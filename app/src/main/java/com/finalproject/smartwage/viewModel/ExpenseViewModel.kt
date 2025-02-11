package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.model.Expense
import com.finalproject.smartwage.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepo: ExpenseRepository
) : ViewModel() {

    private val _userExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val userExpenses: StateFlow<List<Expense>> = _userExpenses.asStateFlow()

    fun loadExpenses(userId: String) {
        viewModelScope.launch {
            val expenses = expenseRepo.getUserExpenses(userId).map { entity ->
                Expense(
                    id = entity.id,
                    userId = entity.userId,
                    category = entity.category,
                    amount = entity.amount,
                    date = entity.date
                )
            }
            _userExpenses.value = expenses
        }
    }

    fun addExpense(expense: com.finalproject.smartwage.data.local.entities.Expense) {
        viewModelScope.launch {
            expenseRepo.saveExpense(expense)
            loadExpenses(expense.userId)
        }
    }

    fun deleteExpense(expenseId: String, userId: String) {
        viewModelScope.launch {
            expenseRepo.deleteExpense(expenseId)
            loadExpenses(userId)
        }
    }
}