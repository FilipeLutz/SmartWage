@file:Suppress("DEPRECATION")

package com.finalproject.smartwage.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.navigation.Destinations
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
            Box (
                modifier = Modifier
                    .offset(x = (-8).dp)
            ){
                IconButton(
                    onClick = { menuExpanded.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Profile",
                        modifier = Modifier
                            .size(40.dp),
                        tint = Color.White,
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded.value,
                    onDismissRequest = { menuExpanded.value = false },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    // Profile Button
                    DropdownMenuItem(
                        text = { Text("Profile", fontSize = 22.sp) },
                        onClick = {
                            menuExpanded.value = false
                            navController.navigate(Destinations.Profile.route)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Icon"
                            )
                        }
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    // Logout Button
                    DropdownMenuItem(
                        text = { Text("Logout", color = Color.Red, fontSize = 22.sp) },
                        onClick = {
                            menuExpanded.value = false
                            viewModel.logout()
                            navController.navigate(Destinations.Login.route) {
                                popUpTo(Destinations.Login.route) { inclusive = true }
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Logout Icon",
                                tint = Color.Red
                            )
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
                        modifier = Modifier
                            .size(45.dp)
                            .padding(horizontal = 5.dp)
                            .padding(top = 5.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 14.sp)
                }
            )
        }
    }
}

enum class BottomNavItem(val route: String, val iconRes: Int, val label: String) {
    Dashboard(route = "dashboard/{userId}", R.drawable.home, "Dashboard"),
    Income(route = Destinations.Income.route, R.drawable.income, "Income"),
    Expense(route = Destinations.Expense.route, R.drawable.expense, "Expenses"),
    Settings(route = Destinations.Settings.route, R.drawable.setting, "Settings")
}