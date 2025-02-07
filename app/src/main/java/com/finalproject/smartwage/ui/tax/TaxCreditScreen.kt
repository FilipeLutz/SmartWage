package com.finalproject.smartwage.ui.tax

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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaxCreditScreen(navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Tax Credits") }) }) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Track your tax credits and deductions.", style = MaterialTheme.typography.bodyLarge)
        }
    }
}