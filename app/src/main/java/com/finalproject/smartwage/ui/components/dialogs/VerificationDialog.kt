package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.ui.theme.DarkBlue

/**
 * A dialog that prompts the user to verify their email address.
 *
 * @param email The email address to be verified.
 * @param onConfirm Callback function to be invoked when the user confirms the dialog.
 */

@Composable
fun VerificationDialog(
    // Parameter to display the email address
    email: String,
    onConfirm: () -> Unit
) {
    // AlertDialog to show the verification message
    AlertDialog(
        onDismissRequest = {},
        title = {
            // Title of the dialog
            Text(
                "Verify Your Email",
                fontWeight = Bold,
                fontSize = 25.sp,
                color = DarkBlue
            )
        },
        text = {
            // Column to display the verification message
            Column(
                verticalArrangement = spacedBy(10.dp)
            ) {
                // Text messages to inform the user about the verification process
                Text(
                    "A verification email has been sent to",
                    fontSize = 20.sp,
                    fontWeight = SemiBold,
                    color = Black
                )
                // Display the user email address
                Text(
                    email,
                    fontSize = 20.sp,
                    fontWeight = SemiBold
                )
                // Additional message to inform the user about the confirmation process
                Text(
                    "Please check your email and confirm before logging in.",
                    fontSize = 20.sp,
                    fontWeight = SemiBold,
                    color = Black
                )
            }
        },
        confirmButton = {
            // Button to confirm the verification process
            Button(
                colors = buttonColors(DarkBlue),
                onClick = onConfirm
            ) {
                // Text on the button
                Text(
                    "OK",
                    fontSize = 18.sp
                )
            }
        }
    )
}