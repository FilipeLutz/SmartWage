package com.finalproject.smartwage.ui.expense

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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.data.local.entities.Expenses
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.cards.ExpenseItem
import com.finalproject.smartwage.ui.components.dialogs.AddExpenseDialog
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.viewModel.ExpenseViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * ExpenseScreen is a Composable function that displays the user's expenses.
 * It includes a floating action button to add new expenses and a dialog for editing existing ones.
 *
 * @param navController The NavController used for navigation.
 */

@Composable
fun ExpenseScreen(
    // NavController for navigation
    navController: NavController
) {
    // Variables to manage the state of the screen
    val viewModel: ExpenseViewModel = hiltViewModel()
    var editingExpense by remember { mutableStateOf<Expenses?>(null) }
    var isEditMode by remember { mutableStateOf(false) }
    val userExpenses = viewModel.userExpenses.collectAsState()
    var showFab by remember { mutableStateOf(true) }
    var showExpenseDialog by remember { mutableStateOf(false) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Load expenses when the screen opens
    LaunchedEffect(userId) {
        viewModel.loadExpenses()
    }

    // Scaffold to provide the basic structure of the screen
    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) },
        floatingActionButton = {
            // Floating action button (FAB) to add a new expense
            if (showFab) {
                // Show the FAB when there are no expenses
                FloatingActionButton(
                    onClick = {
                        showExpenseDialog = true
                        editingExpense = null
                        showFab = false
                    },
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(60.dp),
                    containerColor = DarkBlue,
                    contentColor = White,
                ) {
                    // Icon for the FAB
                    Icon(
                        Default.Add,
                        contentDescription = "Add Expense"
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
            // Column to arrange the content vertically
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

                    // Title of the screen
                    Text(
                        text = "Expenses",
                        fontSize = 35.sp,
                        fontWeight = Bold,
                        color = colorScheme.primary,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (userExpenses.value.isEmpty()) {
                    // Column to show when there are no expenses
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Center,
                        horizontalAlignment = CenterHorizontally
                    ) {
                        // Text to inform the user about adding expenses
                        Text(
                            text = "Click on the \"+\" button to start adding your expenses.",
                            fontSize = 25.sp,
                            fontWeight = SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                        )

                        // Text to inform the user about tax relief
                        Text(
                            text = "Some expenses are eligible to tax relief.",
                            fontSize = 20.sp,
                            fontWeight = SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                } else {
                    // LazyColumn to display the list of expenses
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        // Item to show the total expenses
                        items(userExpenses.value.size) { index ->
                            val expenses = userExpenses.value[index]
                            // Text to show the total expenses
                            ExpenseItem(
                                expenses = expenses,
                                viewModel = viewModel,
                                onEdit = {
                                    editingExpense = expenses
                                    isEditMode = true
                                    showExpenseDialog = true
                                    showFab = false
                                }
                            )
                        }
                        items(1) {
                            Spacer(modifier = Modifier.height(90.dp))
                        }
                    }
                }

                // Show the dialog for adding/editing expenses
                if (showExpenseDialog) {
                    // Box to overlay the dialog on top of the content
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        // Column to arrange the dialog content
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Center
                        ) {
                            // Dialog for adding/editing expenses
                            AddExpenseDialog(
                                expenseToEdit = editingExpense,
                                isEditMode = isEditMode,
                                onDismiss = {
                                    showExpenseDialog = false
                                    showFab = true
                                    editingExpense = null
                                    isEditMode = false
                                },
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}