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

    private val _totalExpenses = MutableStateFlow(0.0)
    val totalExpenses: StateFlow<Double> = _totalExpenses.asStateFlow()

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

    private val _totalTaxBack = MutableStateFlow(0.0)

    private val _totalTaxOwed = MutableStateFlow(0.0)

    fun fetchTaxData() {
        viewModelScope.launch {

            val userId = auth.currentUser?.uid ?: return@launch

            try {
                combine(
                    incomeRepo.getUserIncomes(userId),
                    expenseRepo.getUserExpenses(userId)
                ) { incomes, expenses ->

                    val userIncomes = incomes.filter { it.userId == userId }
                    val userExpenses = expenses.filter { it.userId == userId }

                    val totalIncome = userIncomes.sumOf { it.amount }
                    val totalPAYE = userIncomes.sumOf { it.paye }
                    val totalUSC = userIncomes.sumOf { it.usc }
                    val totalPRSI = userIncomes.sumOf { it.prsi }
                    val totalTaxLiability = totalPAYE + totalUSC + totalPRSI

                    val rentPaid = userExpenses
                        .filter { it.category == "RENT TAX CREDIT" }
                        .sumOf { it.amount }

                    val tuitionFees = userExpenses
                        .filter { it.category == "TUITION FEE RELIEF" }
                        .sumOf { it.amount }

                    val rentTaxCreditAmount = TaxCalculator.calculateRentTaxCredit(rentPaid, totalIncome)
                    val tuitionFeeReliefAmount = TaxCalculator.calculateTuitionFeeRelief(tuitionFees)

                    // Tax values
                    _paye.value = totalPAYE
                    _usc.value = totalUSC
                    _prsi.value = totalPRSI
                    _taxPaid.value = totalTaxLiability
                    _totalIncome.value = totalIncome

                    _rentTaxCredit.value = rentTaxCreditAmount
                    _tuitionFeeRelief.value = tuitionFeeReliefAmount

                    var expectedTotalTax = 0.0
                    var expectedPAYE = 0.0
                    var expectedUSC = 0.0
                    var expectedPRSI = 0.0

                    userIncomes.forEach { income ->
                        val selectedFrequency = when (income.frequency) {
                            "Weekly" -> TaxCalculator.PaymentFrequency.WEEKLY
                            "Fortnightly" -> TaxCalculator.PaymentFrequency.FORTNIGHTLY
                            else -> TaxCalculator.PaymentFrequency.MONTHLY
                        }

                        val (paye, usc, prsi) = TaxCalculator.calculateTax(
                            income.amount,
                            selectedFrequency,
                            tuitionFees,
                            rentPaid
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

                    // Calculate Overpaid Tax
                    val payeDifference = totalPAYE - expectedPAYE
                    val prsiDifference = totalPRSI - expectedPRSI
                    val uscDifference = totalUSC - expectedUSC

                    val payeBack = totalPAYE

                    val prsiBack = if (prsiDifference > 0) prsiDifference else 0.0

                    val uscBack = if (uscDifference > 0) uscDifference else 0.0

                    // Total Overpaid Tax
                    val totalTaxBack = payeBack + prsiBack + uscBack
                    _totalTaxBack.value = totalTaxBack

                    // Total Tax Owed
                    val payeOwed = if (payeDifference < 0) -payeDifference else 0.0
                    val uscOwed = if (uscDifference < 0) -uscDifference else 0.0
                    val prsiOwed = if (prsiDifference < 0) -prsiDifference else 0.0
                    val totalTaxOwed = payeOwed + uscOwed + prsiOwed
                    _totalTaxOwed.value = totalTaxOwed

                    Timber.d("Rent Tax Credit: $rentTaxCreditAmount, Tuition Fee Relief: $tuitionFeeReliefAmount")
                }.collectLatest {
                    Timber.d("Tax data fetched successfully")
                }

            } catch (e: Exception) {
                Timber.e(e, "Error fetching tax data")
            }
        }
    }
}