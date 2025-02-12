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

    private val _taxOwed = MutableStateFlow(0.0)
    val taxOwed: StateFlow<Double> = _taxOwed

    fun calculateTax(userId: String) {
        viewModelScope.launch {
            taxRepo.getUserTax(userId).collect { taxes ->
                _taxOwed.value = taxes.firstOrNull()?.taxOwed ?: 0.0
            }
        }
    }
}