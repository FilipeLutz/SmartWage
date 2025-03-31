package com.finalproject.smartwage.ui.income

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.cards.IncomeListCard
import com.finalproject.smartwage.ui.components.dialogs.PayslipFormDialog
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.viewModel.ExpenseViewModel
import com.finalproject.smartwage.viewModel.IncomeViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * IncomeTaxScreen is a Composable function that displays the income tax screen.
 * It shows a list of user incomes and allows the user to add or edit their income.
 *
 * @param navController The NavController for navigation.
 * @param viewModel The IncomeViewModel for managing income data.
 * @param expenseViewModel The ExpenseViewModel for managing expense data.
 */

@Composable
fun IncomeTaxScreen(
    // Parameters
    navController: NavController,
    viewModel: IncomeViewModel = hiltViewModel(),
    expenseViewModel: ExpenseViewModel = hiltViewModel()
) {
    // State variables
    var showPayslipForm by remember { mutableStateOf(false) }
    var editingIncome by remember { mutableStateOf<Income?>(null) }
    var showFab by remember { mutableStateOf(true) }
    val userIncomes by viewModel.userIncomes.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Reload incomes when the screen is displayed or userId changes
    LaunchedEffect(userId) {
        viewModel.loadIncomes()
    }
    // Scaffold to provide the main structure of the screen
    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) },
        floatingActionButton = {
            // Floating Action Button (FAB) to add a new income
            if (showFab) {
                // FAB to add a new income
                FloatingActionButton(
                    onClick = {
                        showPayslipForm = true
                        editingIncome = null
                        showFab = false
                    },
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(60.dp),
                    containerColor = DarkBlue,
                    contentColor = White,
                ) {
                    // Icon inside the FAB
                    Icon(
                        Default.Add,
                        contentDescription = "Add Income",
                    )
                }
            }
        }
    ) { paddingValues ->
        // Surface to provide a background color
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorScheme.background)
        ) {
            // column to hold the content
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                // Row to display the title
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Center
                ) {

                    // Title text
                    Text(
                        text = "Income Tax",
                        fontSize = 35.sp,
                        fontWeight = Bold,
                        color = colorScheme.primary,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (userIncomes.isEmpty()) {
                    // Column to show a message when there are no incomes
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Center,
                        horizontalAlignment = CenterHorizontally
                    ) {
                        // Message text
                        Text(
                            text = "Click on the \"+\" button to start adding your income.",
                            fontSize = 25.sp,
                            fontWeight = SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                } else {
                    // LazyColumn to display the list of incomes
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        items(userIncomes.size) { index ->
                            // Display each income using the IncomeListCard
                            val income = userIncomes[index]
                            IncomeListCard(
                                income = income,
                                viewModel = viewModel,
                                onEdit = { selectedIncome ->
                                    editingIncome = selectedIncome
                                    showPayslipForm = true
                                    showFab = false
                                },
                            )
                        }

                        items(1) {
                            Spacer(modifier = Modifier.height(90.dp))
                        }
                    }
                }
            }

            // Show PayslipFormCard inside AlertDialog
            if (showPayslipForm) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Black.copy(alpha = 0.7f)
                        )
                ) {
                    // Column to hold the PayslipFormDialog
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Center
                    ) {

                        // Show the PayslipFormDialog
                        PayslipFormDialog(
                            viewModel = viewModel,
                            incomeToEdit = editingIncome,
                            onDismiss = {
                                showPayslipForm = false
                                showFab = true
                            },
                            onCancel = {

                                showPayslipForm = false
                                showFab = true

                            },
                            expenseViewModel = expenseViewModel
                        )
                    }
                }
            }
        }
    }
}