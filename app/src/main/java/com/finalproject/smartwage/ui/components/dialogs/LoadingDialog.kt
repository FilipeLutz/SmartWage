package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * LoadingDialog is a Composable function that displays a loading dialog with a circular progress indicator.
 * It is used to indicate that a background task is in progress.
 *
 * @param isLoading A Boolean value that controls the visibility of the dialog.
 */

@Composable
fun LoadingDialog(
    // Parameter to control the visibility of the dialog
    isLoading: Boolean
) {
    // If isLoading is true, show the dialog
    if (isLoading) {
        // Create a dialog
        Dialog(
            onDismissRequest = {}
        ) {
            // Box to hold the circular progress indicator
            Box(
                contentAlignment = Center,
                modifier = Modifier
                    .background(
                        Transparent
                    )
            ) {
                Box(
                    contentAlignment = Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Circular progress indicator
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(100.dp),
                        color = White
                    )
                }
            }
        }
    }
}