package com.finalproject.smartwage.ui.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finalproject.smartwage.viewModel.DashboardViewModel

@Composable
fun DashboardScreen(
    userId: String,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    // Collect state from ViewModel
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val taxOwed by viewModel.taxOwed.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Load data when the screen is first displayed
    LaunchedEffect(userId) {
        viewModel.loadUserData(userId)
    }

    // UI
    Surface(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Total Income
                DashboardItem(
                    label = "Total Income",
                    value = totalIncome
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Total Expenses
                DashboardItem(
                    label = "Total Expenses",
                    value = totalExpenses
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tax Owed
                DashboardItem(
                    label = "Tax Owed",
                    value = taxOwed
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun DashboardItem(
    label: String,
    value: Double
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$${String.format("%.2f", value)}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}