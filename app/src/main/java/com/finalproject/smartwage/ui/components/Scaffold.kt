package com.finalproject.smartwage.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.navigation.Destinations
import com.finalproject.smartwage.R
import com.finalproject.smartwage.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val menuExpanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Image(
                painter = painterResource(
                    id = R.drawable.logo
                ),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(200.dp)
            )
        },
        actions = {
            Box {
                IconButton(
                    onClick = { menuExpanded.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Profile",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 5.dp),
                        tint = Color.White,
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded.value,
                    onDismissRequest = { menuExpanded.value = false },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    // Account Button
                    DropdownMenuItem(
                        text = { Text("Account") },
                        onClick = {
                            menuExpanded.value = false
                            navController.navigate(Destinations.Profile.route)
                        }
                    )

                    // Logout Button
                    DropdownMenuItem(
                        text = { Text("Logout", color = Color.Red) },
                        onClick = {
                            menuExpanded.value = false
                            viewModel.logout()
                            navController.navigate(Destinations.Login.route) {
                                popUpTo(Destinations.Login.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
        )
    )
}

@Composable
fun DashboardBottomBar(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Image(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        modifier = Modifier.size(32.dp)
                    )
                },
                label = {
                    Text(text = item.label)
                }
            )
        }
    }
}

enum class BottomNavItem(val route: String, val iconRes: Int, val label: String) {
    Dashboard(route = "dashboard/{userId}", R.drawable.home, "Dashboard"),
    Income(Destinations.Income.route, R.drawable.income, "Income"),
    Expense(Destinations.Expense.route, R.drawable.expense, "Expense"),
    Settings(Destinations.Settings.route, R.drawable.setting, "Settings")
}