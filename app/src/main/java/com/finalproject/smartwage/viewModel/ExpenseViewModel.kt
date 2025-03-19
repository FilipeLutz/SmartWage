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
import kotlin.collections.filter

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepo: ExpenseRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _userExpenses = MutableStateFlow<List<Expenses>>(emptyList())
    val userExpenses: StateFlow<List<Expenses>> = _userExpenses.asStateFlow()

    private val userId: String
        get() = auth.currentUser?.uid ?: ""

    init {
        loadExpenses()
    }

    fun loadExpenses() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                expenseRepo.getUserExpenses(userId).collect { expenses ->
                    _userExpenses.value = expenses.sortedByDescending { it.date }
                }
            }
        } else {
            Timber.e("No logged-in user found. Cannot load expenses.")
        }
    }

    fun addOrUpdateExpense(expenses: Expenses) {
        viewModelScope.launch {
            expenseRepo.saveOrUpdateExpenses(expenses)
            loadExpenses()
        }
    }

    fun deleteExpense(expenses: String) {
        viewModelScope.launch {
            expenseRepo.deleteExpense(expenses)
            loadExpenses()
        }
    }

    fun getRentTaxCredit(): Double {
        return _userExpenses.value.filter { it.category == "Rent Tax Credit" }.sumOf { it.amount }
    }

    fun getTuitionFeeRelief(): Double {
        return _userExpenses.value.filter { it.category == "Tuition Fee Relief" }.sumOf { it.amount }
    }
}