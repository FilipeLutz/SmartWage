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

/**
 * ViewModel for managing tax-related data and calculations.
 *
 * @property incomeRepo Repository for income data.
 * @property expenseRepo Repository for expense data.
 * @property auth Firebase authentication instance.
 */

@HiltViewModel
class TaxViewModel @Inject constructor(
    // Inject repositories and FirebaseAuth instance
    private val incomeRepo: IncomeRepository,
    private val expenseRepo: ExpenseRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    // Function to get the total tax paid
    private val _taxPaid = MutableStateFlow(0.0)
    val taxPaid: StateFlow<Double> = _taxPaid.asStateFlow()

    // Function to get the pay-as-you-earn (PAYE) paid
    private val _paye = MutableStateFlow(0.0)
    val paye: StateFlow<Double> = _paye.asStateFlow()

    // Function to get the USC paid
    private val _usc = MutableStateFlow(0.0)
    val usc: StateFlow<Double> = _usc.asStateFlow()

    // Function to get the PRSI paid
    private val _prsi = MutableStateFlow(0.0)
    val prsi: StateFlow<Double> = _prsi.asStateFlow()

    // Function to get the total income
    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome.asStateFlow()

    // Function to get the total expenses
    private val _totalExpenses = MutableStateFlow(0.0)
    val totalExpenses: StateFlow<Double> = _totalExpenses.asStateFlow()

    // Function to get the rent tax credit
    private val _rentTaxCredit = MutableStateFlow(0.0)
    val rentTaxCredit: StateFlow<Double> = _rentTaxCredit.asStateFlow()

    // Function to get the tuition fee relief
    private val _tuitionFeeRelief = MutableStateFlow(0.0)
    val tuitionFeeRelief: StateFlow<Double> = _tuitionFeeRelief.asStateFlow()

    // Function to get the expected tax
    private val _expectedTax = MutableStateFlow(0.0)
    val expectedTax: StateFlow<Double> = _expectedTax.asStateFlow()

    // Function to get the expected PAYE
    private val _expectedPAYE = MutableStateFlow(0.0)
    val expectedPAYE: StateFlow<Double> = _expectedPAYE.asStateFlow()

    // Function to get the expected USC
    private val _expectedUSC = MutableStateFlow(0.0)
    val expectedUSC: StateFlow<Double> = _expectedUSC.asStateFlow()

    // Function to get the expected PRSI
    private val _expectedPRSI = MutableStateFlow(0.0)
    val expectedPRSI: StateFlow<Double> = _expectedPRSI.asStateFlow()

    // Function to get the total tax back
    private val _totalTaxBack = MutableStateFlow(0.0)
    val totalTaxBack: StateFlow<Double> = _totalTaxBack.asStateFlow()

    // Function to get total tax owed
    private val _totalTaxOwed = MutableStateFlow(0.0)

    // Function to get tax data
    fun fetchTaxData() {
        viewModelScope.launch {
            // Get the current user ID from FirebaseAuth
            val userId = auth.currentUser?.uid ?: return@launch

            try {
                // Combine income and expense data from repositories
                combine(
                    incomeRepo.getUserIncomes(),
                    expenseRepo.getUserExpenses()
                ) { incomes, expenses ->
                    // Filter incomes and expenses for the current user
                    val userIncomes = incomes.filter { it.userId == userId }
                    val userExpenses = expenses.filter { it.userId == userId }

                    // Calculate total income and expenses
                    val totalIncome = userIncomes.sumOf { it.amount }
                    val totalPAYE = userIncomes.sumOf { it.paye }
                    val totalUSC = userIncomes.sumOf { it.usc }
                    val totalPRSI = userIncomes.sumOf { it.prsi }
                    val totalTaxLiability = totalPAYE + totalUSC + totalPRSI

                    // Calculate rent tax credit
                    val rentPaid = userExpenses
                        .filter { it.category == "RENT TAX CREDIT" }
                        .sumOf { it.amount }

                    // Calculate tuition fee relief
                    val tuitionFees = userExpenses
                        .filter { it.category == "TUITION FEE RELIEF" }
                        .sumOf { it.amount }

                    // Calculate total expenses
                    val rentTaxCreditAmount =
                        TaxCalculator.calculateRentTaxCredit(rentPaid, totalIncome)
                    val tuitionFeeReliefAmount =
                        TaxCalculator.calculateTuitionFeeRelief(tuitionFees)

                    // Tax values
                    _paye.value = totalPAYE
                    _usc.value = totalUSC
                    _prsi.value = totalPRSI
                    _taxPaid.value = totalTaxLiability
                    _totalIncome.value = totalIncome

                    // Expenses
                    _rentTaxCredit.value = rentTaxCreditAmount
                    _tuitionFeeRelief.value = tuitionFeeReliefAmount

                    // Calculate expected tax
                    var expectedTotalTax = 0.0
                    var expectedPAYE = 0.0
                    var expectedUSC = 0.0
                    var expectedPRSI = 0.0

                    // Calculate expected tax based on user incomes
                    userIncomes.forEach { income ->
                        // Get the frequency of payment
                        val selectedFrequency = when (income.frequency) {
                            "Weekly" -> TaxCalculator.PaymentFrequency.WEEKLY
                            "Fortnightly" -> TaxCalculator.PaymentFrequency.FORTNIGHTLY
                            else -> TaxCalculator.PaymentFrequency.MONTHLY
                        }

                        // Calculate expected tax for each income
                        val (paye, usc, prsi) = TaxCalculator.calculateTax(
                            income.amount,
                            selectedFrequency,
                            tuitionFees,
                            rentPaid
                        )

                        // Add to expected tax values
                        expectedPAYE += paye
                        expectedUSC += usc
                        expectedPRSI += prsi
                        expectedTotalTax += paye + usc + prsi
                    }

                    // Adjust expected tax values based on tax credits
                    _expectedTax.value = expectedTotalTax
                    _expectedPAYE.value = expectedPAYE
                    _expectedUSC.value = expectedUSC
                    _expectedPRSI.value = expectedPRSI

                    // Calculate Overpaid Tax
                    val payeDifference = totalPAYE - expectedPAYE
                    val prsiDifference = totalPRSI - expectedPRSI
                    val uscDifference = totalUSC - expectedUSC

                    // Calculate prsi overpaid tax
                    val prsiBack = if (prsiDifference > 0) prsiDifference else 0.0
                    // Calculate usc overpaid tax
                    val uscBack = if (uscDifference > 0) uscDifference else 0.0
                    // Calculate prsi underpaid tax
                    val prsiOwed = if (prsiDifference < 0) -prsiDifference else 0.0
                    // Calculate usc underpaid tax
                    val uscOwed = if (uscDifference < 0) -uscDifference else 0.0

                    // Set tax back
                    _totalTaxBack.value = when {
                        prsiBack > 0 || uscBack > 0 -> totalPAYE + prsiBack + uscBack
                        else -> totalPAYE - prsiOwed - uscOwed
                    }

                    // Total Tax Owed
                    val payeOwed = if (payeDifference < 0) -payeDifference else 0.0
                    val totalTaxOwed = payeOwed + uscOwed + prsiOwed
                    _totalTaxOwed.value = totalTaxOwed

                    Timber.d("Rent Tax Credit: $rentTaxCreditAmount, Tuition Fee Relief: $tuitionFeeReliefAmount")
                }.collectLatest {
                    // Collect the combined flow
                    Timber.d("Tax data fetched successfully")
                }

            } catch (e: Exception) {
                // Handle any exceptions that occur during data fetching
                Timber.e(e, "Error fetching tax data")
            }
        }
    }
}

