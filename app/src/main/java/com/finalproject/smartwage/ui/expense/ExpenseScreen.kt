package com.finalproject.smartwage.ui.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.data.local.entities.Expense
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.cards.ExpenseItem
import com.finalproject.smartwage.ui.components.dialogs.AddExpenseDialog
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.viewModel.ExpenseViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

@Composable
fun ExpenseScreen(navController: NavController) {
    val viewModel: ExpenseViewModel = hiltViewModel()
    var editingExpenses by remember { mutableStateOf<Expense?>(null) }
    val userExpenses = viewModel.userExpenses.collectAsState()
    var showFab by remember { mutableStateOf(true) }
    var showExpenseDialog by remember { mutableStateOf(false) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Load expenses when the screen opens
    LaunchedEffect(userId) {
        viewModel.loadExpenses()
    }

    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = {
                        showExpenseDialog = true
                        editingExpenses = null
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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Expenses",
                        fontSize = 35.sp,
                        fontWeight = Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (userExpenses.value.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Click on the \"+\" button to start adding your expenses.",
                            fontSize = 25.sp,
                            fontWeight = SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    // Display the list of expenses
                    LazyColumn (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ){
                        items(userExpenses.value.size) { index ->
                            ExpenseItem(
                                expense = userExpenses.value[index],
                                onDelete = {
                                    viewModel.deleteExpense(userExpenses.value[index].id)
                                },
                                onEdit = {
                                    editingExpenses = userExpenses.value[index]
                                    showExpenseDialog = true
                                    showFab = false
                                }
                            )
                        }
                    }
                }

                if (showExpenseDialog) {
                    AddExpenseDialog(
                        onDismiss = {
                            showExpenseDialog = false
                            showFab = true
                            editingExpenses = null // Reset after dismissing
                        },
                        onAddExpense = { category, amount, description, date ->
                            if (editingExpenses == null) {
                                viewModel.updateExpense(
                                    Expense(
                                        id = UUID.randomUUID().toString(),
                                        category = category,
                                        amount = amount,
                                        description = description,
                                        date = System.currentTimeMillis(),
                                        userId = userId
                                    )
                                )
                            } else {
                                val updatedExpense = editingExpenses!!.copy(
                                    category = category,
                                    amount = amount,
                                    description = description,
                                    date = System.currentTimeMillis()
                                )
                                viewModel.updateExpense(updatedExpense)
                            }
                        }
                    )
                }
            }
        }
    }
}