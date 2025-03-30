package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Arrangement.End
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.finalproject.smartwage.ui.theme.DarkBlue

// This dialog is used to show an error message when the user tries to save a form with missing fields.
@Composable
fun AddErrorDialog(
    // Parameter to check if there are any missing fields
    missingFields: List<String>,
    onDismiss: () -> Unit
) {
    // If there are no missing fields, do not show the dialog
    if (missingFields.isEmpty()) return
    // Show the dialog
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        // Card to display the error message
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = cardColors(containerColor = White),
            elevation = cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .width(IntrinsicSize.Max)
        ) {
            // Column to arrange the text and button vertically
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .padding(
                        horizontal = 20.dp,
                        vertical = 15.dp
                    ),
                horizontalAlignment = CenterHorizontally
            ) {
                // Title of the dialog
                Text(
                    text = "All fields should be completed before saving.",
                    color = Black,
                    fontSize = 20.sp,
                    fontWeight = SemiBold,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(20.dp))
                // Subtitle of the dialog
                Text(
                    text = "* Missing Fields *",
                    color = Red,
                    fontSize = 24.sp,
                    fontWeight = Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))
                // List of missing fields
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Start,
                    verticalArrangement = spacedBy(6.dp)
                ) {
                    // Iterate through the list of missing fields and display each one
                    missingFields.forEach { field ->
                        // Display each missing field with a bullet point
                        Text(
                            text = "• $field",
                            color = Red,
                            fontSize = 20.sp,
                            fontWeight = Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Button to dismiss the dialog
                Row(
                    horizontalArrangement = End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // OK button
                    Button(
                        onClick = { onDismiss() },
                        colors = buttonColors(DarkBlue),
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        Text(
                            "OK",
                            fontSize = 20.sp,
                            fontWeight = Bold
                        )
                    }
                }
            }
        }
    }
}