package com.finalproject.smartwage.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.End
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.navigation.Destinations
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.dialogs.RevenueDialog
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.viewModel.AuthViewModel
import com.finalproject.smartwage.viewModel.SettingsViewModel

/**
 * SettingsScreen is a Composable function that displays the settings screen of the app.
 * It includes options for language selection, dark mode, profile settings, notifications,
 * app tutorial, revenue information, and logout functionality.
 *
 * @param navController The NavController used for navigation between screens.
 * @param authViewModel The AuthViewModel used for authentication-related operations.
 * @param viewModel The SettingsViewModel used for managing settings-related operations.
 */

@Composable
fun SettingsScreen(
    // Parameters
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: SettingsViewModel = hiltViewModel()
) {

    // State variables
    var showRevenueDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Collecting the dark mode state from the ViewModel
    val darkModeEnabled by viewModel.darkModeEnabled.collectAsState()

    // Scaffold to provide the top bar and bottom bar
    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) }
    ) { paddingValues ->
        // Surface to provide a background color and padding
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorScheme.background)
        ) {
            // Column to arrange the settings items vertically
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                // Row to display the title "Settings"
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Center
                ) {

                    // Title text
                    Text(
                        text = "Settings",
                        fontSize = 35.sp,
                        fontWeight = Bold,
                        color = colorScheme.primary,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Column to display the settings items
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    HorizontalDivider()

                    // Language selection
                    SettingItem(
                        title = "Language",
                        icon = painterResource(id = R.drawable.language),
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        // Row to display the selected language
                        Row(
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = End
                        ) {
                            // Language Text
                            Text(
                                text = "EN",
                                fontSize = 20.sp,
                                fontWeight = SemiBold,
                                color = DarkBlue
                            )

                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }

                    HorizontalDivider()

                    // Dark Mode setting
                    SettingItem(
                        title = "Dark Mode",
                        icon = painterResource(id = R.drawable.mode),
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        // Switch to toggle dark mode
                        Switch(
                            colors = colors(
                                checkedThumbColor = White,
                                checkedTrackColor = DarkBlue,
                                uncheckedThumbColor = DarkBlue,
                            ),
                            checked = darkModeEnabled,
                            onCheckedChange = { enabled ->
                                // Update the dark mode state in the ViewModel
                                viewModel.updateDarkModeEnabled(enabled)
                            }
                        )
                    }

                    HorizontalDivider()

                    // Profile setting
                    SettingItem(
                        title = "Profile",
                        icon = painterResource(id = R.drawable.profile),
                        onClick = {
                            // Navigate to the profile settings screen
                            navController.navigate(
                                Destinations.Profile.route
                            )
                        },
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        // Icon to navigate to profile settings
                        Icon(
                            painter = painterResource(id = R.drawable.arrowforward),
                            contentDescription = "Profile Settings",
                            tint = DarkBlue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }

                    HorizontalDivider()

                    // Notifications setting
                    SettingItem(
                        title = "Notifications",
                        icon = painterResource(id = R.drawable.notifications),
                        onClick = {
                            // Open notification settings
                            openNotificationSettings(context)
                        },
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        // Icon to navigate to notification settings
                        Icon(
                            painter = painterResource(id = R.drawable.arrowforward),
                            contentDescription = "Notification Settings",
                            tint = DarkBlue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }

                    HorizontalDivider()

                    // Tutorial navigation
                    SettingItem(
                        title = "App Tutorial",
                        icon = painterResource(id = R.drawable.tutorial),
                        onClick = {
                            // Navigate to the tutorial screen
                            navController.navigate(
                                Destinations.Tutorial.route
                            )
                        },
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowforward),
                            contentDescription = "Go to Tutorial",
                            tint = DarkBlue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }

                    HorizontalDivider()

                    // Go to Revenue.ie
                    SettingItem(
                        title = "Revenue IE",
                        icon = painterResource(id = R.drawable.faq),
                        onClick = {
                            // Show the revenue dialog
                            showRevenueDialog = true
                        },
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        // Icon to navigate to Revenue.ie
                        Icon(
                            painter = painterResource(id = R.drawable.arrowforward),
                            contentDescription = "Revenue.ie",
                            tint = DarkBlue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }

                    HorizontalDivider()

                    // About the app
                    SettingItem(
                        title = "About App",
                        icon = painterResource(id = R.drawable.info),
                        onClick = {
                            // Navigate to the about app screen
                            navController.navigate(
                                Destinations.AboutApp.route
                            )
                        },
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        // Icon to navigate to about app
                        Icon(
                            painter = painterResource(id = R.drawable.arrowforward),
                            contentDescription = "About the App",
                            tint = DarkBlue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }

                    HorizontalDivider()

                    // Log out option
                    SettingItem(
                        title = "Log Out",
                        icon = painterResource(id = R.drawable.logout),
                        onClick = {
                            // Log out the user
                            authViewModel.logout()
                            // Navigate to the login screen
                            navController.navigate(Destinations.Login.route) {
                                // Clear the back stack
                                popUpTo(Destinations.Login.route) { inclusive = true }
                            }
                        },
                        titleColor = Red,
                        iconColor = Red
                    ) {
                        // Icon to log out
                        Icon(
                            painter = painterResource(id = R.drawable.arrowforward),
                            contentDescription = "Logout",
                            tint = Red,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    HorizontalDivider()
                }
                // Show the revenue dialog if the state is true
                if (showRevenueDialog) {
                    RevenueDialog(
                        onDismiss = {
                            // Dismiss the dialog
                            showRevenueDialog = false
                        },
                    )
                }
            }
        }
    }
}

/**
 * SettingItem is a Composable function that represents a single item in the settings screen.
 * It includes an icon, title, and an optional content area for additional actions.
 *
 * @param title The title of the setting item.
 * @param icon The icon to be displayed next to the title.
 * @param titleColor The color of the title text.
 * @param iconColor The color of the icon.
 * @param onClick The action to be performed when the item is clicked.
 * @param content The additional content to be displayed in the item.
 */

@Composable
fun SettingItem(
    // Parameters
    title: String,
    icon: Painter,
    titleColor: Color,
    iconColor: Color,
    onClick: () -> Unit = {},
    content: @Composable (() -> Unit)
) {
    // Row to display the setting item
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(20.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                // Perform the click action
                onClick()
            },
        verticalAlignment = CenterVertically,
        horizontalArrangement = SpaceBetween,
    ) {
        // Row to display the icon and title
        Row(
            verticalAlignment = CenterVertically
        ) {
            // Icon
            Icon(
                icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier
                    .size(26.dp),
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Title text
            Text(
                title,
                fontSize = 20.sp,
                fontWeight = SemiBold,
                color = titleColor
            )
        }
        // Additional content area
        content()
    }
}

/**
 * openNotificationSettings is a function that opens the notification settings for the app.
 *
 * @param context The context used to start the activity.
 */

private fun openNotificationSettings(
    // Parameters
    context: Context
) {
    // Create an intent to open the notification settings
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // For Android O and above, use the app notification settings
        Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            // Set the package name of the app
            putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    } else {
        // For Android versions below O, use the general settings
        Intent(android.provider.Settings.ACTION_SETTINGS).apply {
            // Set the action to open the settings
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }
    // Start the activity to open the notification settings
    context.startActivity(intent)
}