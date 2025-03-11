package com.finalproject.smartwage.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.navigation.Destinations
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.cards.DashboardCards
import com.finalproject.smartwage.ui.components.dialogs.TaxResultDialog
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.utils.QuickTaxCalculator
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
    var calculatedTax by remember { mutableStateOf<Triple<Double, Double, Double>?>(null) }
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
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp)
                        .padding(horizontal = 10.dp)
                ) {

                    Text(
                        text = "Dashboard",
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Quick Tax Calculator
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "Quick Tax Calculator",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp)
                    ) {
                        OutlinedTextField(
                            value = incomeInput,
                            onValueChange = { newValue ->
                                if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                    incomeInput = newValue
                                }
                            },
                            label = { Text("Enter Income (€)", fontSize = 18.sp) },
                            textStyle = TextStyle(fontSize = 24.sp),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .width(180.dp)
                                .scrollable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberScrollState(),
                                    enabled = true
                                )
                        )

                        Button(
                            onClick = {
                                val annualIncome = incomeInput.toDoubleOrNull()
                                if (annualIncome != null) {
                                    val (calculatedPAYE, calculatedUSC, calculatedPRSI) = QuickTaxCalculator.calculateQuickTax(annualIncome)
                                    calculatedTax = Triple(calculatedPAYE, calculatedUSC, calculatedPRSI)
                                    showTaxDialog = true
                                } else {
                                    calculatedTax = null
                                    showTaxDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                            modifier = Modifier
                                .width(150.dp)
                                .height(60.dp)
                                .padding(top = 10.dp)
                        ) {
                            Text(
                                "Calculate",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
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
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DashboardCards(
                        label = "Income",
                        value = totalIncome,
                        iconRes = R.drawable.income,
                        navController = navController,
                        destination = Destinations.Income.route
                    )

                    DashboardCards(
                        label = "Expenses",
                        value = totalExpenses,
                        iconRes = R.drawable.expense,
                        navController = navController,
                        destination = Destinations.Expense.route
                    )

                    DashboardCards(
                        label = "Tax Paid",
                        value = taxPaid,
                        iconRes = R.drawable.taxes,
                        navController = navController,
                        destination = Destinations.TaxCredit.route
                    )

                    // Show "Underpayment Tax" when it's > 0, otherwise show "Overpayment Tax" when it's > 0
                    if (taxOwed > 0.0) {
                        DashboardCards(
                            label = "Tax Underpayment",
                            value = taxOwed,
                            iconRes = R.drawable.taxbag,
                            navController = navController,
                            destination = Destinations.TaxCredit.route
                        )
                    } else if (taxBack > 0.0) {
                        DashboardCards(
                            label = "Tax Overpayment",
                            value = taxBack,
                            iconRes = R.drawable.taxbag,
                            navController = navController,
                            destination = Destinations.TaxCredit.route
                        )
                    }

                    Spacer(modifier = Modifier.height(7.dp))
                }
            }
        }
    }

    // Call the TaxResultDialog composable if showTaxDialog is true
    if (showTaxDialog) {
        TaxResultDialog(
            calculatedTax = calculatedTax,
            onDismiss = { showTaxDialog = false }
        )
    }
}