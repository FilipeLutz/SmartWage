package com.finalproject.smartwage.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun DashboardScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val user by authViewModel.user.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        user?.let {
            Text("Welcome, ${it.email}", style = MaterialTheme.typography.bodyLarge)
        } ?: Text("Loading...")

        Button(onClick = {
            authViewModel.signOut()
            navController.navigate("login") { popUpTo("dashboard") { inclusive = true } }
        }) {
            Text("Logout")
        }
    }
}