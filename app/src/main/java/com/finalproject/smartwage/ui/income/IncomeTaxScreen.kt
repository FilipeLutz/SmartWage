package com.finalproject.smartwage.ui.income

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.cards.IncomeListCard
import com.finalproject.smartwage.ui.components.cards.PayslipFormCard
import com.finalproject.smartwage.viewModel.IncomeViewModel

@Composable
fun IncomeTaxScreen(navController: NavController, viewModel: IncomeViewModel = hiltViewModel()) {
    var showPayslipForm by remember { mutableStateOf(false) }
    var editingIncome by remember { mutableStateOf<Income?>(null) }
    val userIncomes by viewModel.userIncomes.collectAsState()

    // Reload incomes when the screen is displayed
    LaunchedEffect(Unit) {
        viewModel.loadIncomes("currentUserId") // Replace with actual user ID
    }

    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showPayslipForm = true
                    editingIncome = null // Reset for new entry
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Income")
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(userIncomes.size) { index ->
                    val income = userIncomes[index] // Access the income by index
                    IncomeListCard(
                        income = income,
                        viewModel = viewModel,
                        onEdit = { selectedIncome ->
                            editingIncome = selectedIncome // Set for editing
                            showPayslipForm = true
                        }
                    )
                }
            }
        }

        // Show PayslipFormCard inside AlertDialog
        if (showPayslipForm) {
            AlertDialog(
                onDismissRequest = { showPayslipForm = false },
                text = {
                    PayslipFormCard(
                        viewModel = viewModel,
                        incomeToEdit = editingIncome,  // Pass existing income for editing
                        onDismiss = { showPayslipForm = false }
                    )
                },
                confirmButton = {}
            )
        }
    }
}