package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.ui.theme.DarkBlue

@Composable
fun PhotoDialog(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Choose an option",
                    fontSize = 24.sp,
                    fontWeight = Bold
                )

                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = DarkBlue,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        },
        text = {
            Text(
                "Would you like to take a new picture or choose from the gallery?",
                fontSize = 15.sp,
                fontWeight = SemiBold
            )
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        onDismiss()
                        onCameraClick()
                    },
                    colors = ButtonDefaults.buttonColors(DarkBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Take Photo",
                        fontSize = 18.sp,
                        fontWeight = Bold
                    )
                }

                Button(
                    onClick = {
                        onDismiss()
                        onGalleryClick()
                    },
                    colors = ButtonDefaults.buttonColors(DarkBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Choose from Gallery",
                        fontSize = 18.sp,
                        fontWeight = Bold
                    )
                }
            }
        },
        dismissButton = {}
    )
}