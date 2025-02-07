package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.repository.IncomeRepository
import com.finalproject.smartwage.data.repository.ExpenseRepository
import com.finalproject.smartwage.data.repository.TaxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val taxRepository: TaxRepository
) : ViewModel() {

    fun fetchDashboardData(userId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val income = incomeRepository.getUserIncomes(userId)
            val expenses = expenseRepository.getUserExpenses(userId)
            val tax = taxRepository.calculateTax(userId)
            onResult(true)
        }
    }
}