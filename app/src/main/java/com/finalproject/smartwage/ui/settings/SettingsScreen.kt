package com.finalproject.smartwage.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.navigation.Destinations
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.Red
import com.finalproject.smartwage.ui.theme.White
import com.finalproject.smartwage.viewModel.AuthViewModel
import com.finalproject.smartwage.viewModel.SettingsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: SettingsViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    // Observe settings
    val darkModeEnabled by viewModel.darkModeEnabled.collectAsState()

    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Settings",
                        fontSize = 35.sp,
                        fontWeight = Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(modifier = Modifier.fillMaxSize()) {

                    HorizontalDivider()

                    // Language selection
                    SettingItem(
                        title = "Language",
                        icon = painterResource(id = R.drawable.language),
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.english),
                                contentDescription = "Language Flag",
                                modifier = Modifier.size(35.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))
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
                        Switch(
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = White,
                                checkedTrackColor = DarkBlue,
                                uncheckedThumbColor = DarkBlue,
                            ),
                            checked = darkModeEnabled,
                            onCheckedChange = { enabled ->
                                viewModel.updateDarkModeEnabled(enabled)
                            }
                        )
                    }

                    HorizontalDivider()

                    // Profile setting
                    SettingItem(
                        title = "Profile",
                        icon = painterResource(id = R.drawable.user),
                        onClick = { navController.navigate(Destinations.Profile.route) },
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowforward),
                            contentDescription = "Profile Settings",
                            tint = DarkBlue,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    HorizontalDivider()

                    // Notifications setting
                    SettingItem(
                        title = "Notifications",
                        icon = painterResource(id = R.drawable.notifications),
                        onClick = { openNotificationSettings(context) },
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowforward),
                            contentDescription = "Notification Settings",
                            tint = DarkBlue,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    HorizontalDivider()

                    // Tutorial navigation
                    SettingItem(
                        title = "App Tutorial",
                        icon = painterResource(id = R.drawable.tutorial),
                        onClick = { navController.navigate(Destinations.Tutorial.route) },
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowforward),
                            contentDescription = "Go to Tutorial",
                            tint = DarkBlue,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    HorizontalDivider()

                    SettingItem(
                        title = "Revenue IE",
                        icon = painterResource(id = R.drawable.faq),
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = "https://www.revenue.ie/en/home.aspx".toUri()
                            }
                            context.startActivity(intent)
                        },
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowforward),
                            contentDescription = "Revenue.ie",
                            tint = DarkBlue,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    HorizontalDivider()

                    // About the app
                    SettingItem(
                        title = "About App",
                        icon = painterResource(id = R.drawable.info),
                        onClick = { navController.navigate(Destinations.AboutApp.route) },
                        titleColor = DarkBlue,
                        iconColor = DarkBlue
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowforward),
                            contentDescription = "About the App",
                            tint = DarkBlue,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    HorizontalDivider()

                    // logout
                    SettingItem(
                        title = "Log Out",
                        icon = painterResource(id = R.drawable.logout),
                        onClick = {
                            authViewModel.logout()
                            navController.navigate(Destinations.Login.route) {
                                popUpTo(Destinations.Login.route) { inclusive = true }
                            }
                        },
                        titleColor = Red,
                        iconColor = Red
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowforward),
                            contentDescription = "Logout",
                            tint = Red,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    icon: Painter,
    titleColor: Color,
    iconColor: Color,
    onClick: () -> Unit = {},
    content: @Composable (() -> Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(20.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = title, modifier = Modifier.size(26.dp), tint = iconColor)
            Spacer(modifier = Modifier.width(10.dp))
            Text(title, fontSize = 20.sp, color = titleColor)
        }
        content()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun openNotificationSettings(context: Context) {
    val intent = Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}
