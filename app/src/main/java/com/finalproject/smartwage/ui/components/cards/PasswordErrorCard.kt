package com.finalproject.smartwage.ui.components.cards

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Arrangement.Start
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.utils.PasswordValidationError

// This composable function creates a card that displays password validation errors.
@Composable
fun PasswordErrorCard(
    // Parameters:
    errors: List<PasswordValidationError>, onDismiss: () -> Unit
) {
    // Card to display the password validation errors
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = cardColors(
            containerColor = Color(0xFFFFEBEE)
        ),
        elevation = cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Column to arrange the elements vertically
        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = spacedBy(10.dp),
            modifier = Modifier
                .padding(16.dp),
        ) {
            // Title of the card
            Row(
                horizontalArrangement = Start,
                verticalAlignment = CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Password Requirements:",
                    color = Red,
                    fontSize = 22.sp,
                    fontWeight = Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // List of password validation errors
            Column(
                verticalArrangement = spacedBy(6.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Iterate through the errors and display them
                errors.forEach { error ->
                    // Display each error message
                    Text(
                        text = when (error) {
                            PasswordValidationError
                                .TOO_SHORT -> "• At least 8 characters long"

                            PasswordValidationError
                                .NO_UPPERCASE -> "• At least one uppercase letter"

                            PasswordValidationError
                                .NO_DIGIT -> "• At least one digit"

                            PasswordValidationError
                                .NO_SPECIAL_CHARACTER -> "• At least one special character"
                        },
                        color = DarkGray,
                        fontSize = 18.sp,
                        fontWeight = SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Button to dismiss the card
            Row(
                verticalAlignment = CenterVertically,
                horizontalArrangement = Center
            ) {
                Button(
                    onClick = onDismiss,
                    colors = buttonColors(
                        containerColor = Red
                    ),
                    modifier = Modifier
                        .width(85.dp)
                        .height(45.dp)
                ) {
                    Text(
                        "OK",
                        fontSize = 22.sp,
                        fontWeight = Bold,
                        color = White
                    )
                }
            }
        }
    }
}