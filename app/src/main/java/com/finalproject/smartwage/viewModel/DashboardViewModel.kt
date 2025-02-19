package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.repository.IncomeRepository
import com.finalproject.smartwage.data.repository.ExpenseRepository
import com.finalproject.smartwage.data.repository.TaxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val incomeRepo: IncomeRepository,
    private val expenseRepo: ExpenseRepository,
    private val taxRepo: TaxRepository
) : ViewModel() {

    // State for total income
    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome

    // State for total expenses
    private val _totalExpenses = MutableStateFlow(0.0)
    val totalExpenses: StateFlow<Double> = _totalExpenses

    // State for tax paid
    private val _taxPaid = MutableStateFlow(0.0)
    val taxPaid: StateFlow<Double> = _taxPaid

    // State for tax owed
    private val _taxOwed = MutableStateFlow(0.0)
    val taxOwed: StateFlow<Double> = _taxOwed

    // State for tax back
    private val _taxBack = MutableStateFlow(0.0)
    val taxBack: StateFlow<Double> = _taxBack

    // State for loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // State for errors
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Load user data
    fun loadUserData(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Fetch incomes
                val incomes = incomeRepo.getUserIncomes(userId).first()
                var totalIncomeValue = 0.0
                for (income in incomes) {
                    totalIncomeValue += income.amount
                }
                _totalIncome.value = totalIncomeValue

                // Fetch expenses
                val expenses = expenseRepo.getUserExpenses(userId).first()
                var totalExpensesValue = 0.0
                for (expense in expenses) {
                    totalExpensesValue += expense.amount
                }
                _totalExpenses.value = totalExpensesValue

                // Fetch tax
                val tax = taxRepo.getUserTax(userId).first()
                _taxOwed.value = tax.firstOrNull()?.taxOwed ?: 0.0
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}