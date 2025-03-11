package com.finalproject.smartwage.ui.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.cards.ExpenseItem
import com.finalproject.smartwage.ui.components.dialogs.AddExpenseDialog
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.viewModel.ExpenseViewModel

@Composable
fun ExpenseScreen(navController: NavController) {
    val viewModel: ExpenseViewModel = hiltViewModel()

    var showFab by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = {
                        viewModel.showExpenseDialog.value = true
                        showFab = false
                    },
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(60.dp),
                    containerColor = DarkBlue,
                    contentColor = Color.White,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Expense")
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Expenses",
                    fontSize = 30.sp,
                    fontWeight = Bold
                )

                val expenses = viewModel.expenses.collectAsState().value
                if (expenses.isEmpty()) {
                    Text("No expenses added yet.", fontSize = 18.sp, color = Color.Gray)
                } else {
                    LazyColumn {
                        items(expenses.size) { index ->
                            ExpenseItem(
                                expense = expenses[index],
                                onDelete = { viewModel.deleteExpense(expenses[index]) },
                                onEdit = { viewModel.updateExpense(expenses[index]) }
                            )
                        }
                    }
                }

                if (viewModel.showExpenseDialog.value) {
                    AddExpenseDialog(
                        onDismiss = {
                            viewModel.showExpenseDialog.value = false
                            showFab = true
                        },
                        onAddExpense = { category, amount, description ->
                            viewModel.addExpense(category, amount, description)
                        }
                    )
                }
            }
        }
    }
}