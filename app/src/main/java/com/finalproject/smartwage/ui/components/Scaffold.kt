@file:Suppress("DEPRECATION")

package com.finalproject.smartwage.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.finalproject.smartwage.R
import com.finalproject.smartwage.navigation.Destinations
import com.finalproject.smartwage.ui.theme.Black
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.LightGrey
import com.finalproject.smartwage.ui.theme.White
import com.finalproject.smartwage.viewModel.AuthViewModel
import com.finalproject.smartwage.viewModel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val menuExpanded = remember { mutableStateOf(false) }
    val imageUri by profileViewModel.imageUri.collectAsState()

    LaunchedEffect(imageUri) {
        println("TopBar Image URI: $imageUri")
    }

    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(200.dp)
                    .clickable(
                        onClick = {
                            navController.navigate(route = "dashboard/{userId}")
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )
        },
        actions = {
            Box {
                IconButton(
                    onClick = ({ menuExpanded.value = true })
                ) {
                    if (imageUri != null) {
                        // If user has a profile picture, show it
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Default user icon
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User Profile",
                            modifier = Modifier
                                .size(45.dp),
                            tint = White,
                        )
                    }
                }

                DropdownMenu(
                    expanded = menuExpanded.value,
                    onDismissRequest = { menuExpanded.value = false },
                    modifier = Modifier
                        .background(LightGrey)
                        .border(
                            width = 2.dp,
                            color = colorScheme.primary
                        )
                ) {
                    // Profile Button
                    DropdownMenuItem(
                        text = { Text("Profile", fontSize = 22.sp, color = DarkBlue) },
                        onClick = {
                            menuExpanded.value = false
                            navController.navigate(Destinations.Profile.route)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Icon",
                                tint = DarkBlue
                            )
                        }
                    )

                    HorizontalDivider(
                        color = Black,
                        thickness = 1.dp,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )

                    // Logout Button
                    DropdownMenuItem(
                        text = { Text("Log Out", color = Color.Red, fontSize = 22.sp) },
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
            containerColor = colorScheme.primary,
            titleContentColor = White,
        )
    )
}

@Composable
fun DashboardBottomBar(navController: NavController) {
    // Get the current route from the NavController
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(colorScheme.primaryContainer)
    ) {
        BottomNavItem.entries.forEach { item ->
            // Check if the current item is selected
            val isSelected = currentRoute == item.route

            // Custom clickable wrapper for the icon and text
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        // Navigate to the selected item
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    .background(
                        if (isSelected) {
                            // Background color for the selected item
                            colorScheme.primary.copy(alpha = 0.2f)
                        } else {
                            // Transparent background for unselected items
                            Color.Transparent
                        },
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                ) {
                    // Icon
                    Image(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(6.dp),
                        colorFilter = if (isSelected) {
                            // Apply a color filter for the selected icon
                            ColorFilter.tint(colorScheme.primary)
                        } else {
                            // Apply a color filter for the unselected icon
                            ColorFilter.tint(colorScheme.onSurfaceVariant)
                        }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Text
                    Text(
                        text = item.label,
                        fontSize = 13.sp,
                        fontWeight = SemiBold,
                        color = if (isSelected) {
                            // Change text color for the selected item
                            colorScheme.primary
                        } else {
                            // Default text color for unselected items
                            colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}

enum class BottomNavItem(val route: String, val iconRes: Int, val label: String) {
    Dashboard(route = "dashboard/{userId}", R.drawable.home, "Dashboard"),
    Income(route = "income", R.drawable.income, "Income"),
    Expense(route = "expense", R.drawable.expense, "Expenses"),
    Settings(route = "settings", R.drawable.setting, "Settings")
}