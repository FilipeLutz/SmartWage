package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PhotoDialog(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose an option") },
        text = { Text("Would you like to take a new picture, choose from the gallery, or delete the current picture?") },
        confirmButton = {
            Column {
                Button(onClick = {
                    onDismiss()
                    onCameraClick()
                }) { Text("Take Photo") }

                Spacer(modifier = Modifier.height(4.dp))

                Button(onClick = {
                    onDismiss()
                    onGalleryClick()
                }) { Text("Choose from Gallery") }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
