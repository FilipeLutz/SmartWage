package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Arrangement.End
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.finalproject.smartwage.ui.theme.DarkBlue
import java.text.NumberFormat
import java.util.Locale

/**
 * A dialog that displays the calculated tax result.
 *
 * @param navController The NavController for navigation.
 * @param calculatedTax A Triple containing the calculated PAYE, USC, and PRSI values.
 * @param onDismiss A callback function to be called when the dialog is dismissed.
 */

@Composable
fun TaxResultDialog(
    // Parameters
    navController: NavController,
    calculatedTax: Triple<Double, Double, Double>?,
    onDismiss: () -> Unit
) {

    // Number format for displaying currency
    val numberFormat = NumberFormat.getNumberInstance(Locale.UK)
        .apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }

    // Calculate total tax from PAYE, USC, and PRSI
    val calculatedPAYE = calculatedTax?.first ?: 0.0
    val calculatedUSC = calculatedTax?.second ?: 0.0
    val calculatedPRSI = calculatedTax?.third ?: 0.0
    val totalTax = calculatedPAYE + calculatedUSC + calculatedPRSI

    // Display the dialog
    Dialog(
        onDismissRequest = onDismiss
    ) {
        // Card to display the tax result
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = cardColors(containerColor = White),
            elevation = cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Column to arrange the content vertically
            Column(
                verticalArrangement = spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Tax Result Text
                Text(
                    text = "Tax Liability:",
                    fontSize = 28.sp,
                    fontWeight = Bold,
                    color = Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                // Tax Result Value
                Text(
                    text = "€${numberFormat.format(totalTax)}",
                    fontSize = 26.sp,
                    color = Black,
                    fontWeight = SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(5.dp))

                // Column to display additional information
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                ) {
                    // Clickable text to navigate to tax rate information
                    Text(
                        text = "Tax Rate Information",
                        fontSize = 22.sp,
                        color = DarkBlue,
                        fontWeight = SemiBold,
                        textDecoration = Underline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    onDismiss()
                                    navController.navigate("taxcredit")
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    // Note text
                    Text(
                        text = "Please note, this is a simple tax estimate. For a more precise calculation, enter your actual income and expenses in the app.",
                        fontSize = 18.sp,
                        color = Blue,
                        fontWeight = SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Row to arrange the button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            end = 10.dp,
                            bottom = 5.dp
                        ),
                    horizontalArrangement = End
                ) {
                    // Button to dismiss the dialog
                    Button(
                        onClick = onDismiss,
                        colors = buttonColors(DarkBlue),
                        modifier = Modifier
                            .width(80.dp)
                            .height(50.dp)
                    ) {
                        // Text inside the button
                        Text(
                            "OK",
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}