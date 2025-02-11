package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.model.Income
import com.finalproject.smartwage.data.repository.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(
    private val incomeRepo: IncomeRepository
) : ViewModel() {

    private val _userIncomes = MutableStateFlow<List<Income>>(emptyList())
    val userIncomes: StateFlow<List<Income>> = _userIncomes.asStateFlow()

    fun loadIncomes(userId: String) {
        viewModelScope.launch {
            val incomes = incomeRepo.getUserIncomes(userId)
            _userIncomes.value = incomes
        }
    }

    fun addIncome(income: com.finalproject.smartwage.data.local.entities.Income) {
        viewModelScope.launch {
            incomeRepo.saveIncome(income)
            loadIncomes(income.userId)
        }
    }

    fun deleteIncome(incomeId: String, userId: String) {
        viewModelScope.launch {
            incomeRepo.deleteIncome(incomeId)
            loadIncomes(userId)
        }
    }
}