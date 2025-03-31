package com.finalproject.smartwage.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar

/**
 * AboutAppScreen is a Composable function that displays information about the app,
 * including the developers, project specifications, and what the app does.
 *
 * @param navController The NavController used for navigation within the app.
 */

@Composable
fun AboutAppScreen(
    navController: NavController
) {
    // Scaffold to provide a top bar and bottom bar
    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) }
    ) { paddingValues ->
        // Surface to hold the content with a background color
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorScheme.background)
        ) {
            // Column to arrange the content vertically
            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                // Row with Back Button and Screen Title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = CenterVertically,
                ) {
                    // Back Button
                    Box(
                        modifier = Modifier
                            .padding(start = 22.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { navController.popBackStack() }
                            )
                    ) {
                        // Back Icon
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            tint = colorScheme.primary,
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }

                    // Screen Title
                    Text(
                        text = "App Information",
                        fontSize = 35.sp,
                        fontWeight = Bold,
                        color = colorScheme.primary,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 50.dp)
                            .wrapContentWidth(CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                // LazyColumn to display the content in a scrollable list
                LazyColumn(
                    verticalArrangement = spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                ) {
                    item {
                        // Row to display the title and subtitle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Center
                        ) {
                            // App Title
                            Text(
                                text = "Dorset College Dublin",
                                fontSize = 28.sp,
                                fontWeight = Bold
                            )

                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Row to display the subtitle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Center
                        ) {
                            // App Subtitle
                            Text(
                                text = "Final Project - BSC30924",
                                fontSize = 22.sp,
                                fontWeight = SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        // Developers Section
                        Text(
                            text = "Developed by:",
                            fontSize = 20.sp,
                            fontWeight = SemiBold
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        // Developers List
                        Text(
                            text = "• Filipe Lutz Mariano (25956)\n• Vinicius de Morais Miranda (70973)",
                            fontSize = 18.sp,
                            style = TextStyle(
                                lineHeight = 25.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Lecturer Section
                        Text(
                            text = "Supervised by:",
                            fontSize = 20.sp,
                            fontWeight = SemiBold
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        // Lecturer Name
                        Text(
                            text = "• Wenhao Fu",
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(25.dp))

                        HorizontalDivider()

                        Spacer(modifier = Modifier.height(25.dp))

                        // Row to display the Project Specifications title
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Center
                        ) {
                            // Project Specifications
                            Text(
                                text = "Project Specifications",
                                fontSize = 24.sp,
                                fontWeight = Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Project Specifications List
                        Text(
                            text = "• Developed using Kotlin\n" +
                                    "• Uses Firebase Authentication\n" +
                                    "• Stores user data in Firestore\n   (Cloud Database)\n" +
                                    "• Uses Room as the local database for\n   offline storage\n" +
                                    "• Built with Jetpack Compose for UI",
                            fontSize = 20.sp,
                            style = TextStyle(
                                lineHeight = 28.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(25.dp))

                        HorizontalDivider()

                        Spacer(modifier = Modifier.height(25.dp))

                        // Row to display the App Specifications title
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Center
                        ) {
                            // App Specifications
                            Text(
                                text = "App Specifications",
                                fontSize = 24.sp,
                                fontWeight = Bold,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // App Specifications List
                        Text(
                            text = "The app calculates the user's tax based on their income and expenses.\n" +
                                    "It determines tax deductions based on the official revenue rules set by the Irish government, " +
                                    "including PAYE, USC, and PRSI. Users can input their financial details " +
                                    "to get an accurate overview of their tax obligations.",
                            fontSize = 20.sp,
                            style = TextStyle(
                                lineHeight = 28.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(25.dp))
                    }
                }
            }
        }
    }
}