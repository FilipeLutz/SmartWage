package com.finalproject.smartwage.ui.settings

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    Button(onClick = {
        userViewModel.clearUsers() // Clear stored user data
        navController.navigate("login") // Redirect to login
    }) {
        Text("Logout")
    }
}