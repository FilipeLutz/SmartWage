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

    private val _tuitionFeeRelief = MutableStateFlow(0.0)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val userId: String
        get() = auth.currentUser?.uid ?: ""

    /**
     * Load user data (Income, Expenses, and Tax calculations)
     */
    fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val incomes = incomeRepo.getUserIncomes(userId).first()
                val expenses = expenseRepo.getUserExpenses(userId).first()

                var totalIncomeValue = 0.0
                var totalTaxPaidValue = 0.0
                var totalExpectedTaxValue = 0.0

                // Get rent and tuition fee from expenses
                val rentPaid = expenses
                    .filter { it.category == "RENT TAX CREDIT" }
                    .sumOf { it.amount }

                val tuitionFees = expenses
                    .filter { it.category == "TUITION FEE RELIEF" }
                    .sumOf { it.amount }

                // Calculate tax credit and relief amounts
                val rentTaxCreditAmount = TaxCalculator.calculateRentTaxCredit(rentPaid, totalTaxPaidValue)
                val tuitionFeeReliefAmount = TaxCalculator.calculateTuitionFeeRelief(tuitionFees)

                // Update state with tax credits
                _rentTaxCredit.value = rentTaxCreditAmount
                _tuitionFeeRelief.value = tuitionFeeReliefAmount

                for (income in incomes) {
                    totalIncomeValue += income.amount
                    totalTaxPaidValue += income.paye + income.usc + income.prsi

                    // Determine payment frequency
                    val selectedFrequency = when (income.frequency) {
                        "Weekly" -> TaxCalculator.PaymentFrequency.WEEKLY
                        "Fortnightly" -> TaxCalculator.PaymentFrequency.FORTNIGHTLY
                        else -> TaxCalculator.PaymentFrequency.MONTHLY
                    }

                    // Calculate expected tax based on user expenses
                    val (expectedPAYE, expectedUSC, expectedPRSI) =
                        TaxCalculator.calculateTax(
                            income.amount,
                            selectedFrequency,
                            tuitionFees,
                            rentPaid
                        )
                    totalExpectedTaxValue += expectedPAYE + expectedUSC + expectedPRSI
                }

                // Apply tax credits before comparison
                totalExpectedTaxValue -= (rentTaxCreditAmount + tuitionFeeReliefAmount)
                totalExpectedTaxValue = maxOf(totalExpectedTaxValue, 0.0)

                _totalIncome.value = totalIncomeValue
                _taxPaid.value = totalTaxPaidValue

                // Compare actual tax vs expected tax
                val taxDifference = totalTaxPaidValue - totalExpectedTaxValue
                _taxOwed.value = if (taxDifference < 0) -taxDifference else 0.0
                _taxBack.value = if (taxDifference > 0) taxDifference else 0.0

                // Store total expenses
                _totalExpenses.value = expenses.sumOf { it.amount }

            } catch (e: Exception) {
                _errorMessage.value = "Failed to load data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}