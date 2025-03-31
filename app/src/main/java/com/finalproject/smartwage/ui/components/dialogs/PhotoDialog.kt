package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.ui.theme.DarkBlue

/**
 * A dialog that allows the user to choose between taking a new photo or selecting one from the gallery.
 *
 * @param onDismiss Callback function to be invoked when the dialog is dismissed.
 * @param onCameraClick Callback function to be invoked when the camera option is clicked.
 * @param onGalleryClick Callback function to be invoked when the gallery option is clicked.
 */

@Composable
fun PhotoDialog(
    // Parameters
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    // AlertDialog to show the options
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            // Row to display the title and close button
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                // Title text
                Text(
                    "Choose an option",
                    fontSize = 24.sp,
                    fontWeight = Bold
                )

                // Close button
                IconButton(
                    onClick = onDismiss
                ) {
                    // Close icon
                    Icon(
                        imageVector = Default.Close,
                        contentDescription = "Close",
                        tint = DarkBlue,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        },
        text = {
            // Description text
            Text(
                "Would you like to take a new picture or choose from the gallery?",
                fontSize = 15.sp,
                fontWeight = SemiBold
            )
        },
        confirmButton = {
            // Column to display the buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = spacedBy(8.dp),
                horizontalAlignment = CenterHorizontally
            ) {
                // Button to take a photo
                Button(
                    onClick = {
                        onDismiss()
                        onCameraClick()
                    },
                    colors = buttonColors(DarkBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // Text for the button
                    Text(
                        "Take Photo",
                        fontSize = 18.sp,
                        fontWeight = Bold
                    )
                }

                // Button to choose from the gallery
                Button(
                    onClick = {
                        onDismiss()
                        onGalleryClick()
                    },
                    colors = buttonColors(DarkBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // Text for the button
                    Text(
                        "Choose from Gallery",
                        fontSize = 18.sp,
                        fontWeight = Bold
                    )
                }
            }
        },
        // Dismiss button
        dismissButton = {}
    )
}