package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.repository.IncomeRepository
import com.finalproject.smartwage.data.repository.ExpenseRepository
import com.finalproject.smartwage.data.repository.TaxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val incomeRepo: IncomeRepository,
    private val expenseRepo: ExpenseRepository,
    private val taxRepo: TaxRepository
) : ViewModel() {

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome

    private val _totalExpenses = MutableStateFlow(0.0)
    val totalExpenses: StateFlow<Double> = _totalExpenses

    private val _taxOwed = MutableStateFlow(0.0)
    val taxOwed: StateFlow<Double> = _taxOwed

    fun loadUserData(userId: String) {
        viewModelScope.launch {
            _totalIncome.value = incomeRepo.getUserIncomes(userId = userId).sumOf { it.amount }
            _totalExpenses.value = expenseRepo.getUserExpenses(userId = userId).sumOf { it.amount }
            _taxOwed.value = taxRepo.getUserTax(userId = userId)?.taxOwed ?: 0.0
        }
    }
}