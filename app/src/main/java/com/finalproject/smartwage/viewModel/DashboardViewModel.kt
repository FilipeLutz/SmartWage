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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val incomeRepo: IncomeRepository,
    private val expenseRepo: ExpenseRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome

    private val _totalExpenses = MutableStateFlow(0.0)
    val totalExpenses: StateFlow<Double> = _totalExpenses

    private val _taxPaid = MutableStateFlow(0.0)
    val taxPaid: StateFlow<Double> = _taxPaid

    private val _taxOwed = MutableStateFlow(0.0)
    val taxOwed: StateFlow<Double> = _taxOwed

    private val _taxBack = MutableStateFlow(0.0)
    val taxBack: StateFlow<Double> = _taxBack

    private val _rentTaxCredit = MutableStateFlow(0.0)
    val rentTaxCredit: StateFlow<Double> = _rentTaxCredit

    private val _tuitionFeeRelief = MutableStateFlow(0.0)
    val tuitionFeeRelief: StateFlow<Double> = _tuitionFeeRelief

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val userId: String
        get() = auth.currentUser?.uid ?: ""

    fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val incomes = incomeRepo.getUserIncomes(userId).first()
                val expenses = expenseRepo.getUserExpenses().first()

                val userIncomes = incomes.filter { it.userId == userId }
                val userExpenses = expenses.filter { it.userId == userId }

                // Calculate tax paid
                val totalPAYE = userIncomes.sumOf { it.paye }
                val totalUSC = userIncomes.sumOf { it.usc }
                val totalPRSI = userIncomes.sumOf { it.prsi }
                val totalTaxPaid = totalPAYE + totalUSC + totalPRSI
                val totalIncome = userIncomes.sumOf { it.amount }

                // Calculate expenses
                val rentPaid = userExpenses
                    .filter { it.category == "RENT TAX CREDIT" }
                    .sumOf { it.amount }

                val tuitionFees = userExpenses
                    .filter { it.category == "TUITION FEE RELIEF" }
                    .sumOf { it.amount }

                // Calculate tax credits
                val rentTaxCredit = TaxCalculator.calculateRentTaxCredit(rentPaid, totalIncome)
                val tuitionRelief = TaxCalculator.calculateTuitionFeeRelief(tuitionFees)
                val personalTaxCredit = 4000.0
                val totalCredits = personalTaxCredit + rentTaxCredit + tuitionRelief

                // Calculate tax
                var expectedPAYE = 0.0
                var expectedUSC = 0.0
                var expectedPRSI = 0.0

                userIncomes.forEach { income ->
                    val frequency = when (income.frequency) {
                        "Weekly" -> TaxCalculator.PaymentFrequency.WEEKLY
                        "Fortnightly" -> TaxCalculator.PaymentFrequency.FORTNIGHTLY
                        else -> TaxCalculator.PaymentFrequency.MONTHLY
                    }

                    val (paye, usc, prsi) = TaxCalculator.calculateTax(
                        income.amount,
                        frequency,
                        tuitionFees,
                        rentPaid
                    )
                    expectedPAYE += paye
                    expectedUSC += usc
                    expectedPRSI += prsi
                }

                // Get tax credits
                val creditPerPayment = totalCredits / 52
                val adjustedExpectedPAYE = maxOf(0.0, expectedPAYE - creditPerPayment)
                val adjustedExpectedTax = adjustedExpectedPAYE + expectedUSC + expectedPRSI

                // Calculate final tax position
                val netTaxPosition = totalTaxPaid - adjustedExpectedTax

                // Update values
                _totalIncome.value = totalIncome
                _totalExpenses.value = userExpenses.sumOf { it.amount }
                _taxPaid.value = totalTaxPaid
                _rentTaxCredit.value = rentTaxCredit
                _tuitionFeeRelief.value = tuitionRelief

                if (netTaxPosition > 0) {
                    _taxBack.value = netTaxPosition
                    _taxOwed.value = 0.0
                } else {
                    _taxBack.value = 0.0
                    _taxOwed.value = -netTaxPosition
                }

            } catch (e: Exception) {
                _errorMessage.value = "Failed to load data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}