package com.finalproject.smartwage.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.Start
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
import androidx.compose.foundation.text.KeyboardOptions.Companion.Default
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.KeyboardType.Companion.Number
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.navigation.Destinations.Expense
import com.finalproject.smartwage.navigation.Destinations.Income
import com.finalproject.smartwage.navigation.Destinations.TaxCredit
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.cards.DashboardCards
import com.finalproject.smartwage.ui.components.dialogs.TaxResultDialog
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.utils.QuickTaxCalculator
import com.finalproject.smartwage.viewModel.DashboardViewModel

/**
 * DashboardScreen is a Composable function that represents the dashboard screen of the application.
 * It displays the user's financial overview, including total income, expenses, and tax information.
 *
 * @param userId The ID of the user whose data is being displayed.
 * @param navController The NavController used for navigation between screens.
 * @param viewModel The ViewModel instance for managing UI-related data.
 */

@Composable
fun DashboardScreen(
    // Parameters
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

    // Local state variables
    var incomeInput by remember { mutableStateOf("") }
    var calculatedTax by remember { mutableStateOf<Triple<Double, Double, Double>?>(null) }
    var showTaxDialog by remember { mutableStateOf(false) }

    // Load user data when screen is first displayed
    LaunchedEffect(userId) {
        viewModel.loadUserData()
    }

    // Scaffold layout for the dashboard screen
    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) }
    ) { paddingValues ->
        // Surface to provide a background color
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorScheme.background)
        ) {
            // Box to display loading indicator or error message
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                // Box to display error message
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Text to show error message
                    Text(
                        text = errorMessage!!,
                        color = colorScheme.error,
                        fontSize = 16.sp
                    )
                }
            }
            // If no loading or error, display the dashboard content
            else {
                // Column to hold the dashboard content
                Column(
                    horizontalAlignment = CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp)
                ) {

                    Spacer(modifier = Modifier.height(25.dp))

                    // Dashboard title
                    Text(
                        text = "Dashboard",
                        fontSize = 35.sp,
                        fontWeight = Bold,
                        color = colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Row for the quick tax calculator
                    Row(
                        horizontalArrangement = Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        // Text for the quick tax calculator
                        Text(
                            "Quick Tax Calculator",
                            fontSize = 26.sp,
                            fontWeight = Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    // Row for the income input field and calculate button
                    Row(
                        verticalAlignment = CenterVertically,
                        horizontalArrangement = SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp)
                    ) {
                        // Text field for user to input income
                        OutlinedTextField(
                            value = incomeInput,
                            onValueChange = { newValue ->
                                // Only allow numeric input
                                if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                    incomeInput = newValue
                                }
                            },
                            label = {
                                Text(
                                    "Enter Income €",
                                    fontSize = 19.sp
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = 24.sp
                            ),
                            keyboardOptions = Default.copy(
                                keyboardType = Number
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .width(195.dp)
                                .scrollable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberScrollState(),
                                    enabled = true
                                )
                        )

                        // Button to calculate tax
                        Button(
                            onClick = {
                                // Validate and calculate tax
                                val annualIncome = incomeInput.toDoubleOrNull()
                                // If valid, calculate tax
                                if (annualIncome != null) {
                                    // Calculate PAYE, USC, and PRSI
                                    val (calculatedPAYE, calculatedUSC, calculatedPRSI) = QuickTaxCalculator.calculateQuickTax(
                                        annualIncome
                                    )
                                    calculatedTax =
                                        Triple(calculatedPAYE, calculatedUSC, calculatedPRSI)
                                    showTaxDialog = true
                                } else {
                                    // Show error message if input is invalid
                                    calculatedTax = null
                                    showTaxDialog = false
                                }
                            },
                            colors = buttonColors(containerColor = DarkBlue),
                            elevation = buttonElevation(defaultElevation = 8.dp),
                            modifier = Modifier
                                .width(150.dp)
                                .height(60.dp)
                                .padding(top = 10.dp)
                        ) {
                            // Text for the calculate button
                            Text(
                                "Calculate",
                                fontSize = 20.sp,
                                fontWeight = Bold,
                                color = White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Row for the overview title
                    Row(
                        horizontalArrangement = Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        // Text for the overview title
                        Text(
                            "Overview",
                            fontSize = 28.sp,
                            fontWeight = Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Dashboard cards for displaying financial information
                    // Show total income card
                    DashboardCards(
                        label = "Total Income",
                        value = totalIncome,
                        iconRes = R.drawable.income,
                        navController = navController,
                        destination = Income.route
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Show total expenses card
                    DashboardCards(
                        label = "Total Expenses",
                        value = totalExpenses,
                        iconRes = R.drawable.expense,
                        navController = navController,
                        destination = Expense.route,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Show total tax paid card
                    DashboardCards(
                        label = "Total Tax Paid",
                        value = taxPaid,
                        iconRes = R.drawable.taxes,
                        navController = navController,
                        destination = TaxCredit.route
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Show Tax Overpayment or Underpayment if applicable, else show a message
                    if (taxOwed > 0) {
                        // Show tax underpayment card
                        DashboardCards(
                            label = "Tax Underpayment",
                            value = taxOwed,
                            iconRes = R.drawable.taxbag,
                            navController = navController,
                            destination = TaxCredit.route
                        )
                    } else if (taxBack > 0) {
                        // Show tax overpayment card
                        DashboardCards(
                            label = "Tax Overpayment",
                            value = taxBack,
                            iconRes = R.drawable.taxbag,
                            navController = navController,
                            destination = TaxCredit.route
                        )
                    } else {
                        // Show message if no tax overpayment or underpayment
                        Text(
                            "Any overpayment or underpayment tax will appear here after incomes and expenses have been added.",
                            fontSize = 20.sp,
                            color = DarkGray,
                            fontWeight = SemiBold,
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