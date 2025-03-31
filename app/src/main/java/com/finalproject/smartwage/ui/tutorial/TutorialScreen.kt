package com.finalproject.smartwage.ui.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.cards.TutorialCard

/**
 * TutorialScreen is a Composable function that displays a list of tutorial videos.
 * It includes a search bar to filter the videos based on user input.
 *
 * @param navController The NavController used for navigation between screens.
 */

@Composable
fun TutorialScreen(
    // NavController for navigation
    navController: NavController
) {
    // List of tutorial videos
    val videoList = listOf(
        TutorialVideo("editprofile.mp4", "Edit Profile"),
        TutorialVideo("quicktax.mp4", "Quick Tax Calculator"),
        TutorialVideo("addincome.mp4", "Add/Edit Income"),
        TutorialVideo("deleteincome.mp4", "Delete Income"),
        TutorialVideo("addexpenses.mp4", "Add/Edit Expenses"),
        TutorialVideo("deleteexpenses.mp4", "Delete Expenses"),
        TutorialVideo("taxsummary.mp4", "Tax Summary"),
        TutorialVideo("notifications.mp4", "App Notifications"),
        TutorialVideo("gotorevenue.mp4", "Go to Revenue"),
        TutorialVideo("aboutapp.mp4", "About App"),
        TutorialVideo("logout.mp4", "Log Out"),
        TutorialVideo("deleteaccount.mp4", "Delete Account"),
    )
    // State variable to hold the search query
    var searchQuery by remember { mutableStateOf("") }

    // Filter videos based on search
    val filteredVideos = videoList.filter { video ->
        // Check if the video title or file name contains the search query
        video.title.contains(searchQuery, ignoreCase = true) ||
                video.fileName.contains(searchQuery, ignoreCase = true)
    }
    // Scaffold to provide a structure for the screen
    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) }
    ) { paddingValues ->
        // Surface to provide a background for the screen
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorScheme.background)
        ) {
            // Column to arrange the UI elements vertically
            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                // Row to hold the back button and title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = CenterVertically
                ) {
                    // Box to hold the back button
                    Box(
                        modifier = Modifier
                            .padding(start = 22.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { navController.popBackStack() }
                            )
                    ) {
                        // Back button icon
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            tint = colorScheme.primary,
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }

                    // App Tutorial Title
                    Text(
                        text = "App Tutorial",
                        fontSize = 35.sp,
                        fontWeight = Bold,
                        color = colorScheme.primary,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 50.dp)
                            .wrapContentWidth(CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Tutorial Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 22.dp,
                            vertical = 10.dp
                        ),
                    label = {
                        Text(
                            "Search Tutorial",
                            fontSize = 18.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 22.sp
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Default.Search,
                            contentDescription = "Search",
                            Modifier.size(24.dp)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                HorizontalDivider()

                // Display video cards in a grid
                LazyVerticalGrid(
                    // Grid layout for video cards
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = spacedBy(10.dp),
                    horizontalArrangement = spacedBy(2.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(filteredVideos.size) { index ->
                        // TutorialCard for each video
                        val video = filteredVideos[index]
                        // Card to display video information
                        TutorialCard(
                            // Video thumbnail
                            video = video,
                            onCardClick = {
                                // Navigate to the video player screen
                                navController.navigate("fullscreen/${video.fileName}")
                            }
                        )
                    }
                }
            }
        }
    }
}

// Data class for Tutorial Video
data class TutorialVideo(
    // File name of the video
    val fileName: String,
    // Title of the video
    val title: String
)