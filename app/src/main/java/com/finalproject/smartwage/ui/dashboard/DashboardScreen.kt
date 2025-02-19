package com.finalproject.smartwage.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardCard
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.viewModel.DashboardViewModel
import com.finalproject.smartwage.R
import com.finalproject.smartwage.ui.components.TaxResultDialog
import com.finalproject.smartwage.utils.TaxCalculator

@Composable
fun DashboardScreen(
    userId: String,
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    // Collect state from ViewModel
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val taxPaid by viewModel.taxPaid.collectAsState()
    val taxOwed by viewModel.taxOwed.collectAsState()
    val taxBack by viewModel.taxBack.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var incomeInput by remember { mutableStateOf("") }
    var calculatedTax by remember { mutableStateOf<Double?>(null) }
    var showTaxDialog by remember { mutableStateOf(false) }

    // Load user data when screen is first displayed
    LaunchedEffect(userId) {
        viewModel.loadUserData(userId)
    }

    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp) // Added top & bottom padding
                ) {

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Dashboard",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp)
                    ) {

                        item {
                            Spacer(modifier = Modifier.height(5.dp))
                        }

                        item {
                            DashboardCard(
                                label = "Income",
                                value = totalIncome,
                                iconRes = R.drawable.income,
                                navController = navController,
                                destination = "income"
                            )
                        }
                        item {
                            DashboardCard(
                                label = "Expenses",
                                value = totalExpenses,
                                iconRes = R.drawable.expense,
                                navController = navController,
                                destination = "expense"
                            )
                        }
                        item {
                            DashboardCard(
                                label = "Tax Paid",
                                value = taxPaid,
                                iconRes = R.drawable.taxes,
                                navController = navController,
                                destination = "taxcredit"
                            )
                        }

                        // Show "Tax Owed" when it's > 0, otherwise show "Tax Back" when it's > 0
                        if (taxOwed > 0.0) {
                            item {
                                DashboardCard(
                                    label = "Tax Owed",
                                    value = taxOwed,
                                    iconRes = R.drawable.taxes,
                                    navController = navController,
                                    destination = "taxcredit"
                                )
                            }
                        } else if (taxBack > 0.0) {
                            item {
                                DashboardCard(
                                    label = "Tax Back",
                                    value = taxBack,
                                    iconRes = R.drawable.taxes,
                                    navController = navController,
                                    destination = "taxcredit"
                                )
                            }
                        }

                        // Tax Calculator Section
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "Quick Tax Calculator",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = incomeInput,
                                onValueChange = { incomeInput = it },
                                label = { Text("Enter your income (€)") },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    val income = incomeInput.toDoubleOrNull() ?: 0.0
                                    calculatedTax = TaxCalculator.calculateTax(income)
                                    showTaxDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Calculate Tax",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(7.dp))
                        }
                    }
                }
            }
        }
    }

    // Call the TaxResultDialog composable if showTaxDialog is true
    if (showTaxDialog) {
        TaxResultDialog(calculatedTax = calculatedTax) {
            showTaxDialog = false
        }
    }
}