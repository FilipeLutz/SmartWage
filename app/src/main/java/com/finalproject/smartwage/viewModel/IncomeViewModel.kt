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

@HiltViewModel
class IncomeViewModel @Inject constructor(
    private val incomeRepo: IncomeRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _userIncomes = MutableStateFlow<List<Income>>(emptyList())
    val userIncomes: StateFlow<List<Income>> = _userIncomes.asStateFlow()

    init {
        loadIncomes()
    }

    fun loadIncomes() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                incomeRepo.getUserIncomes().collect { incomes ->
                    _userIncomes.value = incomes.sortedByDescending { it.date }
                }
            }
        } else {
            Timber.e("No logged-in user found. Cannot load incomes.")
        }
    }

    fun updateIncome(income: Income) {
        viewModelScope.launch {
            incomeRepo.saveOrUpdateIncome(income)
            loadIncomes()
        }
    }

    fun deleteIncome(incomeId: String) {
        viewModelScope.launch {
            incomeRepo.deleteIncome(incomeId)
            loadIncomes()
        }
    }
}