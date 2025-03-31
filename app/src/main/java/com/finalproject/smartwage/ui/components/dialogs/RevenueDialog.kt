package com.finalproject.smartwage.ui.components.dialogs

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.finalproject.smartwage.ui.theme.DarkBlue

/**
 * RevenueDialog is a Composable function that displays an AlertDialog
 * This dialog is used to inform the user that they are about to leave the app
 * and visit the Revenue.ie website.
 *
 * @param onDismiss A lambda function to be called when the dialog is dismissed
 */

@Composable
fun RevenueDialog(
    // Parameter to handle the dismissal of the dialog
    onDismiss: () -> Unit
) {
    // Get the current context
    val context = LocalContext.current
    // AlertDialog to show the information
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            // Row to arrange the title and icon
            Row(
                horizontalArrangement = Center,
                verticalAlignment = CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Text to show the title
                Text(
                    text = "Open Revenue Website",
                    fontSize = 22.sp,
                    fontWeight = Bold
                )
            }
        },
        text = {
            // Row to show the message
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                // Message text
                Text(
                    text = "You are about to leave the app and visit the Revenue.ie",
                    fontSize = 19.sp,
                    fontWeight = SemiBold,
                    style = TextStyle(
                        lineHeight = 30.sp
                    )
                )
            }
        },
        // Buttons to confirm or cancel the action
        confirmButton = {
            // Row to arrange the buttons
            Row(
                horizontalArrangement = Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                // Button to cancel the action
                TextButton(
                    onClick = { onDismiss() }
                ) {
                    // Text to show the cancel action
                    Text(
                        text = "CANCEL",
                        fontSize = 18.sp,
                        fontWeight = SemiBold,
                        color = Red,
                        textDecoration = Underline
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                // Button to confirm the action
                TextButton(
                    onClick = {
                        // Create an intent to open the Revenue website
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = "https://www.revenue.ie/en/home.aspx".toUri()
                        }
                        context.startActivity(intent)
                        onDismiss()
                    }
                ) {
                    // Text to show the confirm action
                    Text(
                        text = "CONTINUE",
                        fontSize = 18.sp,
                        fontWeight = SemiBold,
                        color = DarkBlue,
                        textDecoration = Underline
                    )
                }
            }
        }
    )
}