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

/**
 * ViewModel for the Dashboard screen.
 *
 * @param incomeRepo Repository for income data.
 * @param expenseRepo Repository for expense data.
 * @param auth Firebase authentication instance.
 */

@HiltViewModel
class DashboardViewModel @Inject constructor(
    // Injected dependencies
    private val incomeRepo: IncomeRepository,
    private val expenseRepo: ExpenseRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    // Total income state flow
    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome

    // Total expenses state flow
    private val _totalExpenses = MutableStateFlow(0.0)
    val totalExpenses: StateFlow<Double> = _totalExpenses

    // Tax paid state flow
    private val _taxPaid = MutableStateFlow(0.0)
    val taxPaid: StateFlow<Double> = _taxPaid

    // Tax owed state flow
    private val _taxOwed = MutableStateFlow(0.0)
    val taxOwed: StateFlow<Double> = _taxOwed

    // Tax back state flow
    private val _taxBack = MutableStateFlow(0.0)
    val taxBack: StateFlow<Double> = _taxBack

    // Rent tax credit state flow
    private val _rentTaxCredit = MutableStateFlow(0.0)
    val rentTaxCredit: StateFlow<Double> = _rentTaxCredit

    // Tuition fee relief state flow
    private val _tuitionFeeRelief = MutableStateFlow(0.0)
    val tuitionFeeRelief: StateFlow<Double> = _tuitionFeeRelief

    // Loading state flow
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Error message state flow
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Get the current user's ID from Firebase Authentication
    private val userId: String
        get() = auth.currentUser?.uid ?: ""

    // Load user data when the ViewModel is created
    fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Get incomes and expenses from repositories
                val incomes = incomeRepo.getUserIncomes().first()
                val expenses = expenseRepo.getUserExpenses().first()

                // Filter incomes and expenses for the current user
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

                // Calculate tuition fees
                val tuitionFees = userExpenses
                    .filter { it.category == "TUITION FEE RELIEF" }
                    .sumOf { it.amount }

                // Calculate tax credits
                val rentTaxCredit = TaxCalculator.calculateRentTaxCredit(rentPaid, totalIncome)
                val tuitionRelief = TaxCalculator.calculateTuitionFeeRelief(tuitionFees)

                // Calculate expected taxes
                var expectedPAYE = 0.0
                var expectedUSC = 0.0
                var expectedPRSI = 0.0

                // Calculate expected taxes based on user incomes
                userIncomes.forEach { income ->
                    // Get the frequency of payment
                    val frequency = when (income.frequency) {
                        "Weekly" -> TaxCalculator.PaymentFrequency.WEEKLY
                        "Fortnightly" -> TaxCalculator.PaymentFrequency.FORTNIGHTLY
                        else -> TaxCalculator.PaymentFrequency.MONTHLY
                    }

                    // Calculate expected taxes for each income
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

                // Calculate final tax position
                val prsiDifference = totalPRSI - expectedPRSI
                val uscDifference = totalUSC - expectedUSC

                // Calculate overpaid tax
                val payeBack = totalPAYE
                val prsiBack = if (prsiDifference > 0) prsiDifference else 0.0
                val uscBack = if (uscDifference > 0) uscDifference else 0.0
                val totalTaxBack = payeBack + prsiBack + uscBack

                // Adjust totalTaxBack for underpaid USC and PRSI
                val prsiOwed = if (prsiDifference < 0) -prsiDifference else 0.0
                val uscOwed = if (uscDifference < 0) -uscDifference else 0.0
                val adjustedTotalTaxBack = totalTaxBack - prsiOwed - uscOwed

                // Update values
                _totalIncome.value = totalIncome
                _totalExpenses.value = userExpenses.sumOf { it.amount }
                _taxPaid.value = totalTaxPaid
                _rentTaxCredit.value = rentTaxCredit
                _tuitionFeeRelief.value = tuitionRelief

                // Set tax owed or tax back
                if (adjustedTotalTaxBack > 0) {
                    _taxBack.value = adjustedTotalTaxBack
                    _taxOwed.value = 0.0
                } else {
                    _taxBack.value = 0.0
                    _taxOwed.value = -adjustedTotalTaxBack
                }
                // Handle any exceptions that occur during data loading
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load data: ${e.message}"
            } finally {
                // Set loading state to false
                _isLoading.value = false
            }
        }
    }
}
