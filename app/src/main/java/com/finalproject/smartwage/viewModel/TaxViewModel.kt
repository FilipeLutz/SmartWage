package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.repository.TaxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TaxViewModel @Inject constructor(
    private val taxRepo: TaxRepository
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

            try {
                taxRepo.getUserTax(userId).collect { taxes ->
                    // Assuming you want the sum of all taxOwed values
                    val totalTaxOwed = taxes.sumOf { it.taxOwed }
                    _taxOwed.value = totalTaxOwed
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch tax data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}