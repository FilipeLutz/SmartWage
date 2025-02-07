package com.finalproject.smartwage.ui.income

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeTaxScreen(navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Income Tax Calculator") }) }) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Calculate your income tax liability.", style = MaterialTheme.typography.bodyLarge)
        }
    }
}