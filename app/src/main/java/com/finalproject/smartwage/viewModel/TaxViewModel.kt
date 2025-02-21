package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.repository.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TaxViewModel @Inject constructor(
    private val IncomeRepo: IncomeRepository
) : ViewModel() {

    // State for tax owed
    private val _taxOwed = MutableStateFlow(0.0)
    val taxOwed: StateFlow<Double> = _taxOwed

    // State for loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // State for errors
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Fetch tax owed for a user
    fun fetchTaxOwed(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            Timber.d("TaxViewModel: Fetching tax owed for userId: $userId")

            try {
                IncomeRepo.getUserIncomes(userId).collect { incomes ->
                    Timber.d("TaxViewModel: Received incomes: ${incomes.size}")
                    val totalPAYE = incomes.sumOf { it.paye }
                    val totalUSC = incomes.sumOf { it.usc }
                    val totalPRSI = incomes.sumOf { it.prsi }
                    _taxOwed.value = totalPAYE + totalUSC + totalPRSI
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch tax data: ${e.message}"
                Timber.e(e, "TaxViewModel: Error fetching tax data: ${e.message}")
            } finally {
                _isLoading.value = false
                Timber.d("TaxViewModel: Finished fetching tax owed")
            }
        }
    }
}