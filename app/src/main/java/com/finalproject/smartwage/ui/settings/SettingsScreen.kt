package com.finalproject.smartwage.ui.settings

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.viewModel.SettingsViewModel

@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: SettingsViewModel = viewModel()
    Button(onClick = { viewModel.logout() }) {
        Text("Logout")
    }
}