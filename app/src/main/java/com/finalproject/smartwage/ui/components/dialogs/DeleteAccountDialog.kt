package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.ui.theme.DarkBlue

/**
 * DeleteAccountDialog is a Composable function that displays a dialog for confirming account deletion.
 *
 * @param onDismiss Callback function to be called when the dialog is dismissed.
 * @param onConfirm Callback function to be called when the delete action is confirmed.
 */

@Composable
fun DeleteAccountDialog(
    // Parameters
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    // Delete Account Dialog
    AlertDialog(
        onDismissRequest = {},
        title = {
            // Row for the title
            Row(
                horizontalArrangement = Center,
                verticalAlignment = CenterVertically
            ) {
                Text(
                    "Delete Account",
                    fontSize = 30.sp,
                    fontWeight = Bold,
                    color = Red
                )
            }
        },
        text = {
            // Text for the dialog
            Text(
                "Are you sure you want to delete your account?",
                fontSize = 21.sp,
                fontWeight = SemiBold,
                style = TextStyle(
                    lineHeight = 22.sp
                )
            )
        },
        confirmButton = {
            // Button to confirm the delete
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                // Text for the confirm button
                Text(
                    "DELETE",
                    color = Red,
                    fontSize = 20.sp,
                    fontWeight = Bold
                )
            }
        },
        dismissButton = {
            // Button to dismiss the dialog
            TextButton(
                onClick = onDismiss
            ) {
                // Text for the dismiss button
                Text(
                    "CANCEL",
                    color = DarkBlue,
                    fontSize = 20.sp,
                    fontWeight = Bold
                )
            }
        }
    )
}