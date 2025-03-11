@file:Suppress("DEPRECATION")

package com.finalproject.smartwage.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
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
            Box(
                modifier = Modifier
                    .offset(x = (-8).dp)
            ) {
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
    // Get the current route from the NavController
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        BottomNavItem.entries.forEach { item ->
            // Check if the current item is selected
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    // Navigate to the selected item
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    // Wrap both the icon and text in a Box
                    Box(
                        modifier = Modifier
                            .size(100.dp, 80.dp)
                            .background(
                                if (isSelected) {
                                    // Background color for the selected item
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                } else {
                                    // Transparent background for unselected items
                                    Color.Transparent
                                },
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Icon
                            Image(
                                painter = painterResource(id = item.iconRes),
                                contentDescription = item.label,
                                modifier = Modifier
                                    .size(30.dp), // Adjust the size of the icon
                                colorFilter = if (isSelected) {
                                    // Apply a color filter for the selected icon
                                    ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                } else {
                                    // Apply a color filter for the unselected icon
                                    ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Text (Label)
                            Text(
                                text = item.label,
                                fontSize = 14.sp,
                                fontWeight = SemiBold,
                                color = if (isSelected) {
                                    // Change text color for the selected item
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    // Default text color for unselected items
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}

enum class BottomNavItem(val route: String, val iconRes: Int, val label: String) {
    Dashboard(route = "dashboard/{userId}", R.drawable.home, "Dashboard"),
    Income(route = "income", R.drawable.income, "Income"),
    Expense(route = "expense", R.drawable.expense, "Expenses"),
    Settings(route = "settings", R.drawable.setting, "Settings")
}