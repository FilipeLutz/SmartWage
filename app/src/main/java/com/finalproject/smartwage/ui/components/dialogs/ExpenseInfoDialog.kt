package com.finalproject.smartwage.ui.components.dialogs

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.ui.theme.DarkBlue
import java.util.Locale

/**
 * ExpenseInfoDialog is a Composable function that displays an AlertDialog with information about
 * a specific expense category. It includes a clickable link to the Revenue website for more details.
 *
 * @param onDismiss A lambda function to be called when the dialog is dismissed.
 * @param category The category of the expense for which information is being displayed.
 * @param onLinkClick A lambda function to be called when the link is clicked.
 */

@Composable
fun ExpenseInfoDialog(
    // Parameters for the dialog
    onDismiss: () -> Unit,
    category: String,
    onLinkClick: (Context) -> Unit
) {
    // Get the current context
    val context = LocalContext.current
    // AlertDialog with a title, text, and a button
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            // Row to center the title
            Row(
                verticalAlignment = CenterVertically,
                horizontalArrangement = Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Title text
                Text(
                    text = "Eligibility Information",
                    fontSize = 24.sp,
                    fontWeight = Bold
                )
            }
        },
        text = {
            // Build an annotated string for the text
            val text = buildAnnotatedString {
                // Append the main text
                append(
                    when (
                        category.uppercase(Locale.getDefault())
                    ) {
                        "RENT TAX CREDIT" -> "Check if you qualify for the Rent Tax Credit by reviewing the qualifying conditions on "
                        "TUITION FEE RELIEF" -> "Learn more about eligibility requirements for Tuition Fee Relief on "
                        else -> "Additional information about this expense category on "
                    }
                )
                // Append the link text
                pushStringAnnotation(
                    tag = "URL",
                    annotation = when (
                        category.uppercase(Locale.getDefault())
                    ) {
                        "RENT TAX CREDIT" -> "https://www.revenue.ie/rent-credit"
                        "TUITION FEE RELIEF" -> "https://www.revenue.ie/tuition-fees"
                        else -> "https://www.revenue.ie"
                    }
                )
                withStyle(
                    style = SpanStyle(
                        fontWeight = Bold,
                        color = DarkBlue,
                        textDecoration = Underline
                    )
                ) {
                    append("Revenue.ie")
                }
                pop()
            }
            // Display the text with a clickable link
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = SemiBold,
                style = TextStyle(
                    lineHeight = 25.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {// Handle link click
                        text.getStringAnnotations(0, text.length)
                            // Get the string annotations
                            .firstOrNull()?.let { annotation ->
                                onLinkClick(context)
                                onDismiss()
                            }
                    }
            )
        },
        // Button to confirm and dismiss the dialog
        confirmButton = {
            Button(
                colors = buttonColors(DarkBlue),
                onClick = onDismiss,
            ) {
                // Button text
                Text(
                    "OK",
                    fontSize = 18.sp
                )
            }
        }
    )
}