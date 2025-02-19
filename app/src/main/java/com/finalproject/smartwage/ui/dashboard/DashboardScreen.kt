package com.finalproject.smartwage.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardCard
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.TaxResultDialog
import com.finalproject.smartwage.utils.TaxCalculator
import com.finalproject.smartwage.viewModel.DashboardViewModel

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
                        .padding(top = 20.dp)
                        .padding(horizontal = 10.dp)
                ) {

                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        text = "Dashboard",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    // Tax Calculator Section
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "Quick Tax Calculator",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = incomeInput,
                            onValueChange = { incomeInput = it },
                            label = { Text("Enter your income", fontSize = 18.sp) },
                            textStyle = TextStyle(fontSize = 24.sp),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .width(200.dp)
                        )

                        Button(
                            onClick = {
                                val income = incomeInput.toDoubleOrNull()
                                if (income != null) {
                                    calculatedTax = TaxCalculator.calculateTax(income)
                                    showTaxDialog = true
                                } else {
                                    calculatedTax = null
                                    showTaxDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .width(180.dp)
                                .height(60.dp)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Calculate",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "Overview",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DashboardCard(
                        label = "Income",
                        value = totalIncome,
                        iconRes = R.drawable.income,
                        navController = navController,
                        destination = "income"
                    )

                    DashboardCard(
                        label = "Expenses",
                        value = totalExpenses,
                        iconRes = R.drawable.expense,
                        navController = navController,
                        destination = "expense"
                    )

                    DashboardCard(
                        label = "Tax Paid",
                        value = taxPaid,
                        iconRes = R.drawable.taxes,
                        navController = navController,
                        destination = "taxcredit"
                    )

                    // Show "Tax Owed" when it's > 0, otherwise show "Tax Back" when it's > 0
                    if (taxOwed > 0.0) {
                        DashboardCard(
                            label = "Tax Owed",
                            value = taxOwed,
                            iconRes = R.drawable.taxes,
                            navController = navController,
                            destination = "taxcredit"
                        )
                    } else if (taxBack > 0.0) {
                        DashboardCard(
                            label = "Tax Back",
                            value = taxBack,
                            iconRes = R.drawable.taxes,
                            navController = navController,
                            destination = "taxcredit"
                        )
                    }

                    Spacer(modifier = Modifier.height(7.dp))
                }
            }
        }
    }

    // Call the TaxResultDialog composable if showTaxDialog is true
    if (calculatedTax != null && showTaxDialog) {
        TaxResultDialog(calculatedTax = calculatedTax) {
            showTaxDialog = false
        }
    }
}