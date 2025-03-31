package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay

/**
 * ErrorMessageDialog is a Composable function that displays a dialog with an error message.
 *
 * @param message The error message to be displayed.
 * @param messageType The type of the message (text, success, error, warning, info).
 * @param onDismiss Callback function to be called when the dialog is dismissed.
 */

@Composable
fun ErrorMessageDialog(
    // Parameters:
    message: String?,
    messageType: MessageType,
    onDismiss: () -> Unit
) {
    // Determine the text color based on the message type
    val textColor = when (messageType) {
        MessageType.TEXT -> Black
        MessageType.SUCCESS -> Green
        MessageType.ERROR -> Red
        MessageType.WARNING -> Yellow
        MessageType.INFO -> White
    }
    // Display the dialog with the message
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        // Card to hold the message
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = cardColors(
                containerColor = White
            ),
            elevation = cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier
                .width(IntrinsicSize.Max)
        ) {
            // Box to center the message
            Box(
                contentAlignment = Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Text to display the message
                Text(
                    text = message ?: "",
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentWidth()
                )
            }
        }
    }
    // Automatically dismiss the dialog after 3 seconds if the message is not null or empty
    if (!message.isNullOrEmpty()
    ) {
        LaunchedEffect(message) {
            delay(3000)
            onDismiss()
        }
    }
}

// Enum class to define different message types
enum class MessageType {
    TEXT,
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}