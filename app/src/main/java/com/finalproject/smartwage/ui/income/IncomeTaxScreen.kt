package com.finalproject.smartwage.ui.income

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.cards.IncomeListCard
import com.finalproject.smartwage.ui.components.cards.PayslipFormCard
import com.finalproject.smartwage.viewModel.IncomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun IncomeTaxScreen(navController: NavController, viewModel: IncomeViewModel = hiltViewModel()) {
    var showPayslipForm by remember { mutableStateOf(false) }
    var editingIncome by remember { mutableStateOf<Income?>(null) }
    var showFab by remember { mutableStateOf(true) }
    val userIncomes by viewModel.userIncomes.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Reload incomes when the screen is displayed or userId changes
    LaunchedEffect(userId) {
        viewModel.loadIncomes()
    }

    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = {
                        showPayslipForm = true
                        editingIncome = null
                        showFab = false
                    },
                    modifier = Modifier
                        .size(70.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Income",
                    )
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
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Spacer(modifier = Modifier.height(20.dp))

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){

                    Text(
                        text = "Income Tax",
                        fontSize = 35.sp,
                        fontWeight = Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp)
                ) {
                    items(userIncomes.size) { index ->
                        val income = userIncomes[index]
                        IncomeListCard(
                            income = income,
                            viewModel = viewModel,
                            onEdit = { selectedIncome ->
                                editingIncome = selectedIncome
                                showPayslipForm = true
                                showFab = false
                            }
                        )
                    }

                    items(1) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            // Show PayslipFormCard inside AlertDialog
            if (showPayslipForm) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        PayslipFormCard(
                            viewModel = viewModel,
                            incomeToEdit = editingIncome,
                            onDismiss = {
                                showPayslipForm = false
                                showFab = true
                            },
                            onCancel = {
                                showPayslipForm = false
                                showFab = true
                            }
                        )
                    }
                }
            }
        }
    }
}