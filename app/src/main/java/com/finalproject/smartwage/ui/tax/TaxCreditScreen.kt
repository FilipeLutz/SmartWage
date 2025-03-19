package com.finalproject.smartwage.ui.tax

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.viewModel.TaxViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TaxCreditScreen(
    navController: NavController
) {
    val viewModel: TaxViewModel = hiltViewModel()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val paye by viewModel.paye.collectAsState()
    val usc by viewModel.usc.collectAsState()
    val prsi by viewModel.prsi.collectAsState()
    val taxPaid by viewModel.taxPaid.collectAsState()
    val rentTaxCredit by viewModel.rentTaxCredit.collectAsState()
    val tuitionFeeRelief by viewModel.tuitionFeeRelief.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
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
                    .padding(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    TaxBreakdownSection(totalIncome, paye, usc, prsi, taxPaid, expectedPAYE, expectedUSC, expectedPRSI, expectedTax)
                    Spacer(modifier = Modifier.height(24.dp))
                    TaxCreditsSection(rentTaxCredit , tuitionFeeRelief)
                    Spacer(modifier = Modifier.height(24.dp))
                    TaxCalculationExplanation()
                }
            }
        }
    }
}

@Composable
fun TaxBreakdownSection(
    totalIncome: Double,
    paye: Double,
    usc: Double,
    prsi: Double,
    taxPaid: Double,
    expectedPAYE: Double,
    expectedUSC: Double,
    expectedPRSI: Double,
    expectedTax: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Income & Tax Breakdown",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
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
        }
    }
}

@Composable
fun TaxCreditsSection(rentTaxCredit: Double, tuitionFeeRelief: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tax Credits",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text("Standard Tax Credit: ${formatCurrency(4000.0)}")
            Text("Rent Tax Credit: ${formatCurrency(rentTaxCredit)}")
            Text("Tuition Fee Relief: ${formatCurrency(tuitionFeeRelief)}")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Total Tax Credits: ${formatCurrency(4000.0 + rentTaxCredit + tuitionFeeRelief)}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = Bold)
            )
        }
    }
}

@Composable
fun TaxCalculationExplanation() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "How Your Tax is Calculated",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "1. PAYE is deducted from your income based on tax thresholds.\n" +
                        "2. USC is calculated using progressive rates on your income.\n" +
                        "3. PRSI is deducted based on your weekly income.\n" +
                        "4. Tax credits (e.g., standard credit, rent credit, tuition relief) are applied to reduce your tax liability.\n\n" +
                        "Note: Your final tax liability may change depending on your income and expenses for the rest of the year.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun formatCurrency(amount: Double): String {
    return "€${"%.2f".format(amount)}"
}