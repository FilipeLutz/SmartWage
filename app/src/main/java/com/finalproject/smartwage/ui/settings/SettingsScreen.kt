package com.finalproject.smartwage.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.navigation.Destinations
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.viewModel.SettingsViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    // Observe the settings from ViewModel
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val darkModeEnabled by viewModel.darkModeEnabled.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    var showAboutDialog by remember { mutableStateOf(false) }
    val languages = listOf("English", "Portuguese", "Spanish")

    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Notifications setting
            SettingItem(title = "Notifications", icon = Icons.Default.Notifications) {
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { viewModel.notificationsEnabled }
                )
            }

            HorizontalDivider()

            // Change Language setting
            SettingItem(title = "Change Language", icon = Icons.Default.Edit) {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    Text(selectedLanguage, modifier = Modifier.clickable { expanded = true })
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        languages.forEach { language ->
                            DropdownMenuItem(
                                text = { Text(language) },
                                onClick = {
                                    viewModel.selectedLanguage
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            // FAQs navigation
            SettingItem(title = "FAQs", icon = Icons.Default.Info) {
                IconButton(onClick = { navController.navigate(Destinations.Help.route) }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Go to FAQs")
                }
            }

            HorizontalDivider()

            // Tutorial navigation
            SettingItem(title = "Tutorial", icon = Icons.Default.PlayArrow) {
                IconButton(onClick = { navController.navigate(Destinations.Tutorial.route) }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Go to Tutorial")
                }
            }

            HorizontalDivider()

            // Dark Mode setting
            SettingItem(title = "Dark Mode", icon = Icons.Default.Edit) {
                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = { viewModel.darkModeEnabled }
                )
            }

            HorizontalDivider()

            // About the App setting
            SettingItem(title = "About the App", icon = Icons.Default.Info) {
                IconButton(onClick = { showAboutDialog = true }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "About the App")
                }
            }

            // Show About dialog
            if (showAboutDialog) {
                AlertDialog(
                    onDismissRequest = { showAboutDialog = false },
                    title = { Text("About the App") },
                    text = {
                        Column {
                            Text("Version: 1.0.0")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("This app provides information about tax and revenue, along with tutorials to guide users.")
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showAboutDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SettingItem(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = title, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontSize = 18.sp)
        }
        content()
    }
}