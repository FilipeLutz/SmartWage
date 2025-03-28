package com.finalproject.smartwage.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
        viewModel.loadUserData()
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp)
                ) {

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = "Dashboard",
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Quick Tax Calculator
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "Quick Tax Calculator",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

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
                            label = { Text("Enter Income €", fontSize = 18.sp) },
                            textStyle = TextStyle(fontSize = 24.sp),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .width(195.dp)
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

                    Spacer(modifier = Modifier.height(10.dp))

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

                    Spacer(modifier = Modifier.height(10.dp))

                    DashboardCards(
                        label = "Total Income",
                        value = totalIncome,
                        iconRes = R.drawable.income,
                        navController = navController,
                        destination = Destinations.Income.route
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DashboardCards(
                        label = "Total Expenses",
                        value = totalExpenses,
                        iconRes = R.drawable.expense,
                        navController = navController,
                        destination = Destinations.Expense.route,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DashboardCards(
                        label = "Total Tax Paid",
                        value = taxPaid,
                        iconRes = R.drawable.taxes,
                        navController = navController,
                        destination = Destinations.TaxCredit.route
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Show Tax Overpayment or Underpayment if applicable, else show a message
                    if (taxOwed > 0) {
                        DashboardCards(
                            label = "Tax Underpayment",
                            value = taxOwed,
                            iconRes = R.drawable.taxbag,
                            navController = navController,
                            destination = Destinations.TaxCredit.route
                        )
                    } else if (taxBack > 0) {
                        DashboardCards(
                            label = "Tax Overpayment",
                            value = taxBack,
                            iconRes = R.drawable.taxbag,
                            navController = navController,
                            destination = Destinations.TaxCredit.route
                        )
                    } else {
                        Text("Any overpayment or underpayment tax will appear here after incomes and expenses have been added.",
                            fontSize = 20.sp,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 5.dp)
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
            onDismiss = { showTaxDialog = false },
            navController = navController
        )
    }
}