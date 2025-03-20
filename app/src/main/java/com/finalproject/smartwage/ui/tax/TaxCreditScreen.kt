package com.finalproject.smartwage.ui.tax

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.Green
import com.finalproject.smartwage.ui.theme.Red
import com.finalproject.smartwage.viewModel.TaxViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TaxCreditScreen(
    navController: NavController
) {
    val viewModel: TaxViewModel = hiltViewModel()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val paye by viewModel.paye.collectAsState()
    val usc by viewModel.usc.collectAsState()
    val prsi by viewModel.prsi.collectAsState()
    val taxPaid by viewModel.taxPaid.collectAsState()
    val rentTaxCredit by viewModel.rentTaxCredit.collectAsState()
    val tuitionFeeRelief by viewModel.tuitionFeeRelief.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val expectedTax by viewModel.expectedTax.collectAsState()
    val expectedPAYE by viewModel.expectedPAYE.collectAsState()
    val expectedUSC by viewModel.expectedUSC.collectAsState()
    val expectedPRSI by viewModel.expectedPRSI.collectAsState()

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            viewModel.fetchTaxData()
        }
    }

    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                ) {
                    item {
                        if (totalIncome == 0.0 && totalExpenses == 0.0) {
                            // No income or expenses saved yet
                            NoDataMessage(navController)
                        } else {
                            // Show tax breakdown when data is available
                            TaxSummaryCard(
                                totalIncome, paye, usc, prsi, taxPaid,
                                expectedPAYE, expectedUSC, expectedPRSI, expectedTax,
                                rentTaxCredit, tuitionFeeRelief
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        TaxCalculationExplanationCard()
                    }

                    item {
                        Spacer(modifier = Modifier.height(25.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TaxSummaryCard(
    totalIncome: Double,
    paye: Double,
    usc: Double,
    prsi: Double,
    taxPaid: Double,
    expectedPAYE: Double,
    expectedUSC: Double,
    expectedPRSI: Double,
    expectedTax: Double,
    rentTaxCredit: Double,
    tuitionFeeRelief: Double,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Income & Tax Breakdown",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Income & Taxes
            Text("Total Income: ${formatCurrency(totalIncome)}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("PAYE: ${formatCurrency(paye)} (Expected: ${formatCurrency(expectedPAYE)})")
            Text("USC: ${formatCurrency(usc)} (Expected: ${formatCurrency(expectedUSC)})")
            Text("PRSI: ${formatCurrency(prsi)} (Expected: ${formatCurrency(expectedPRSI)})")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Total Taxes Paid: ${formatCurrency(taxPaid)} (Expected: ${formatCurrency(expectedTax)})",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tax Credits
            Text("Tax Credit: ${formatCurrency(4000.0)}")

            if (rentTaxCredit > 0.0) {
                Text("Rent Tax Credit: ${formatCurrency(rentTaxCredit)}")
            }

            if (tuitionFeeRelief > 0.0) {
                Text("Tuition Fee Relief: ${formatCurrency(tuitionFeeRelief)}")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Total Tax Credits: ${
                    formatCurrency(4000.0 +
                            (if (rentTaxCredit > 0.0) rentTaxCredit else 0.0) +
                            (if (tuitionFeeRelief > 0.0) tuitionFeeRelief else 0.0))
                }",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val totalTaxCredits = 4000.0 + rentTaxCredit + tuitionFeeRelief
            val adjustedExpectedTax = maxOf(0.0, expectedTax - totalTaxCredits)
            val taxDifference = taxPaid - adjustedExpectedTax
            val taxBack = minOf(taxPaid, maxOf(0.0, taxDifference))
            val taxOwed = maxOf(0.0, -taxDifference)

            // Overpayment or Underpayment Tax Message
            val taxMessage = when {
                taxOwed > 0 -> "You have underpaid your taxes by ${formatCurrency(taxOwed)}. This means you might owe additional tax to Revenue."
                taxBack > 0 -> "You have overpaid your taxes by ${formatCurrency(taxBack)}. You may be eligible for a tax refund."
                else -> "Your tax payments align with expected tax calculations."
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = taxMessage,
                color = if (taxOwed > 0) Red else Green,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = Bold)
            )
        }
    }
}

@Composable
fun TaxCalculationExplanationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Understanding Your Taxes",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "1. **PAYE (Pay As You Earn)** - Deducted at source based on income tax bands.\n" +
                        "2. **USC (Universal Social Charge)** - Applies to income over €13,000, with progressive rates.\n" +
                        "3. **PRSI (Pay Related Social Insurance)** - Required for social benefits and pensions, calculated on a weekly basis.\n" +
                        "4. **Standard Tax Credit (€4000)** - Applied to reduce your tax liability.\n" +
                        "5. **Rent Tax Credit** - If you pay rent, a portion is deductible from your tax.\n" +
                        "6. **Tuition Fee Relief** - Some education expenses reduce your taxable amount.\n\n" +
                        "💡 *Your final tax liability is influenced by income, deductions, and credits.*",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Suppress("DEPRECATION")
@Composable
fun NoDataMessage(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No income or expenses found.",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ClickableText(
                text = buildAnnotatedString {
                    append("Please add your ")

                    pushStyle(SpanStyle(color = DarkBlue, textDecoration = TextDecoration.Underline))
                    append("income")
                    pop()

                    append(" and ")

                    pushStyle(SpanStyle(color = DarkBlue, textDecoration = TextDecoration.Underline))
                    append("expenses")
                    pop()

                    append(" to see your tax breakdown.")
                },
                style = MaterialTheme.typography.bodyLarge,
                onClick = { offset ->
                    if (offset in 15..21) {
                        navController.navigate("income")
                    } else if (offset in 26..34) {
                        navController.navigate("expense")
                    }
                }
            )
        }
    }
}

fun formatCurrency(amount: Double): String {
    return "€${"%.2f".format(amount)}"
}