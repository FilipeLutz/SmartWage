package com.finalproject.smartwage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finalproject.smartwage.data.repository.ExpenseRepository
import com.finalproject.smartwage.data.repository.IncomeRepository
import com.finalproject.smartwage.utils.TaxCalculator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TaxViewModel @Inject constructor(
    private val incomeRepo: IncomeRepository,
    private val expenseRepo: ExpenseRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _taxPaid = MutableStateFlow(0.0)
    val taxPaid: StateFlow<Double> = _taxPaid.asStateFlow()

    private val _paye = MutableStateFlow(0.0)
    val paye: StateFlow<Double> = _paye.asStateFlow()

    private val _usc = MutableStateFlow(0.0)
    val usc: StateFlow<Double> = _usc.asStateFlow()

    private val _prsi = MutableStateFlow(0.0)
    val prsi: StateFlow<Double> = _prsi.asStateFlow()

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome.asStateFlow()

    private val _rentTaxCredit = MutableStateFlow(0.0)
    val rentTaxCredit: StateFlow<Double> = _rentTaxCredit.asStateFlow()

    private val _tuitionFeeRelief = MutableStateFlow(0.0)
    val tuitionFeeRelief: StateFlow<Double> = _tuitionFeeRelief.asStateFlow()

    private val _expectedTax = MutableStateFlow(0.0)
    val expectedTax: StateFlow<Double> = _expectedTax.asStateFlow()

    private val _expectedPAYE = MutableStateFlow(0.0)
    val expectedPAYE: StateFlow<Double> = _expectedPAYE.asStateFlow()

    private val _expectedUSC = MutableStateFlow(0.0)
    val expectedUSC: StateFlow<Double> = _expectedUSC.asStateFlow()

    private val _expectedPRSI = MutableStateFlow(0.0)
    val expectedPRSI: StateFlow<Double> = _expectedPRSI.asStateFlow()

    fun fetchTaxData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val userId = auth.currentUser?.uid ?: return@launch

            try {
                combine(
                    incomeRepo.getUserIncomes(userId),
                    expenseRepo.getUserExpenses(userId)
                ) { incomes, expenses ->

                    val rentCredit = expenses
                        .filter { it.category == "RENT TAX CREDIT" }
                        .sumOf { it.amount }

                    val tuitionRelief = expenses
                        .filter { it.category == "TUITION FEE RELIEF" }
                        .sumOf { it.amount }

                    val totalPAYE = incomes.sumOf { it.paye }
                    val totalUSC = incomes.sumOf { it.usc }
                    val totalPRSI = incomes.sumOf { it.prsi }
                    val totalIncome = incomes.sumOf { it.amount }

                    _paye.value = totalPAYE
                    _usc.value = totalUSC
                    _prsi.value = totalPRSI
                    _taxPaid.value = totalPAYE + totalUSC + totalPRSI
                    _totalIncome.value = totalIncome

                    // Update tax credits
                    _rentTaxCredit.value = rentCredit
                    _tuitionFeeRelief.value = tuitionRelief

                    // Expected Tax Calculation
                    var expectedTotalTax = 0.0
                    var expectedPAYE = 0.0
                    var expectedUSC = 0.0
                    var expectedPRSI = 0.0

                    incomes.forEach { income ->
                        val selectedFrequency = when (income.frequency) {
                            "Weekly" -> TaxCalculator.PaymentFrequency.WEEKLY
                            "Fortnightly" -> TaxCalculator.PaymentFrequency.FORTNIGHTLY
                            else -> TaxCalculator.PaymentFrequency.MONTHLY
                        }

                        val (paye, usc, prsi) = TaxCalculator.calculateTax(
                            income.amount,
                            selectedFrequency,
                            tuitionRelief,
                            rentCredit
                        )

                        expectedPAYE += paye
                        expectedUSC += usc
                        expectedPRSI += prsi
                        expectedTotalTax += paye + usc + prsi
                    }

                    _expectedTax.value = expectedTotalTax
                    _expectedPAYE.value = expectedPAYE
                    _expectedUSC.value = expectedUSC
                    _expectedPRSI.value = expectedPRSI

                    Timber.d("Expected Tax: $expectedTotalTax")
                }.collectLatest {
                    Timber.d("Successfully collected tax data, updating UI")
                    _isLoading.value = false
                }

            } catch (e: Exception) {
                Timber.e(e, "Error fetching tax data")
                _errorMessage.value = "Error fetching tax data: ${e.localizedMessage}"
                _isLoading.value = false
            }
        }
    }
}