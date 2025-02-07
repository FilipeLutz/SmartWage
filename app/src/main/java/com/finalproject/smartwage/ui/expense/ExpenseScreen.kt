package com.finalproject.smartwage.ui.expense

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ExpenseScreen(navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Expenses") }) }) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Log and track your expenses here.", style = MaterialTheme.typography.bodyLarge)
        }
    }
}