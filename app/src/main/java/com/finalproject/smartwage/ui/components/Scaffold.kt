package com.finalproject.smartwage.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Center
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
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.layout.ContentScale.Companion.Crop
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
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.LightGrey
import com.finalproject.smartwage.viewModel.AuthViewModel
import com.finalproject.smartwage.viewModel.ProfileViewModel

/**
 * A custom Scaffold that includes a top bar and bottom bar for the dashboard screen.
 *
 * @param navController The NavController for navigation.
 * @param viewModel The AuthViewModel for handling authentication-related actions.
 * @param profileViewModel The ProfileViewModel for handling profile-related actions.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(
    // Parameters
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val menuExpanded = remember { mutableStateOf(false) }
    val imageUri by profileViewModel.imageUri.collectAsState()

    // Observe the imageUri state from the ProfileViewModel
    LaunchedEffect(imageUri) {
        println("TopBar Image URI: $imageUri")
    }

    // TopAppBar with a logo and user profile icon
    TopAppBar(
        title = {
            // Logo that navigates to the dashboard
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
            // User profile icon with dropdown menu
            Box {
                // IconButton for user profile
                IconButton(
                    onClick = {
                        menuExpanded.value = true
                    }
                ) {
                    // If user has a profile picture, show it
                    if (imageUri != null) {
                        // Load the image using AsyncImage
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape),
                            contentScale = Crop
                        )
                    } else {
                        // Default user icon
                        Icon(
                            imageVector = Default.AccountCircle,
                            contentDescription = "User Profile",
                            modifier = Modifier
                                .size(45.dp),
                            tint = White,
                        )
                    }
                }

                // Dropdown menu for profile options
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
                    // DropdownMenuItem for Profile with navigation and icon
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Profile",
                                fontSize = 22.sp,
                                color = DarkBlue
                            )
                        },
                        onClick = {
                            menuExpanded.value = false
                            navController.navigate(Destinations.Profile.route)
                        },
                        leadingIcon = {
                            // Icon for Profile
                            Icon(
                                imageVector = Default.Person,
                                contentDescription = "Profile Icon",
                                tint = DarkBlue
                            )
                        }
                    )

                    // Divider between menu items
                    HorizontalDivider(
                        color = Black,
                        thickness = 1.dp,
                        modifier = Modifier
                            .padding(
                                horizontal = 10.dp
                            )
                    )

                    // DropdownMenuItem for Log Out with navigation and icon
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Log Out",
                                color = Red,
                                fontSize = 22.sp
                            )
                        },
                        onClick = {
                            menuExpanded.value = false
                            // Log out the user
                            viewModel.logout()
                            // Navigate to the login screen
                            navController.navigate(
                                Destinations.Login.route
                            ) {
                                // Clear the back stack to prevent going back to the dashboard
                                popUpTo(Destinations.Login.route) { inclusive = true }
                            }
                        },
                        leadingIcon = {
                            // Icon for Log Out
                            Image(
                                painter = painterResource(id = R.drawable.logout),
                                contentDescription = "Log Out Icon",
                                colorFilter = tint(Red),
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    )
                }
            }
        },
        colors = topAppBarColors(
            containerColor = colorScheme.primary,
            titleContentColor = White,
        )
    )
}

/**
 * A Bottom Navigation Bar.
 *
 * @param navController The NavController for navigation.
 */
@Composable
fun DashboardBottomBar(navController: NavController) {
    // Get the current route from the NavController
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Row layout for the bottom navigation bar
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(colorScheme.primaryContainer)
    ) {
        // Iterate through each BottomNavItem
        BottomNavItem.entries.forEach { item ->
            // Check if the current item is selected
            val isSelected = currentRoute == item.route

            // Box layout for each item
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
                // Column layout for the Text and Icon
                Column(
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                ) {
                    // Icon for each item
                    Image(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(6.dp),
                        colorFilter = if (isSelected) {
                            // Apply a color filter for the selected icon
                            tint(colorScheme.primary)
                        } else {
                            // Apply a color filter for the unselected icon
                            tint(colorScheme.onSurfaceVariant)
                        }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Text label
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

/**
 * A sealed class representing the different bottom navigation items.
 *
 * @param route The route for the navigation item.
 * @param iconRes The resource ID for the icon.
 * @param label The label for the navigation item.
 */

enum class BottomNavItem(
    val route: String,
    val iconRes: Int,
    val label: String
) {
    Dashboard(
        route = "dashboard/{userId}",
        R.drawable.home,
        "Dashboard"
    ),
    Income(
        route = "income",
        R.drawable.income,
        "Income"
    ),
    Expense(
        route = "expense",
        R.drawable.expense,
        "Expenses"
    ),
    Settings(
        route = "settings",
        R.drawable.setting,
        "Settings"
    )
}