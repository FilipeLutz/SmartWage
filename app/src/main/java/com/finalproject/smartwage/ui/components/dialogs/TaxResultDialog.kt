package com.finalproject.smartwage.ui.components.dialogs

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.finalproject.smartwage.ui.theme.Black
import com.finalproject.smartwage.ui.theme.Blue
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.White
import java.text.NumberFormat
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun TaxResultDialog(
    navController: NavController,
    calculatedTax: Triple<Double, Double, Double>?,
    onDismiss: () -> Unit
) {

    val numberFormat = NumberFormat.getNumberInstance(Locale.UK).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    val calculatedPAYE = calculatedTax?.first ?: 0.0
    val calculatedUSC = calculatedTax?.second ?: 0.0
    val calculatedPRSI = calculatedTax?.third ?: 0.0
    val totalTax = calculatedPAYE + calculatedUSC + calculatedPRSI

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
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
                    modifier = Modifier.fillMaxWidth()
                )

                // Tax Result Value
                Text(
                    text = "€${numberFormat.format(totalTax)}",
                    fontSize = 26.sp,
                    color = Black,
                    fontWeight = SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(5.dp))

                Column (
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                ){

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

                    Text(
                        text = "Please note, this is a simple tax estimate. For a more precise calculation, enter your actual income and expenses in the app.",
                        fontSize = 18.sp,
                        color = Blue,
                        fontWeight = SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(DarkBlue),
                        modifier = Modifier
                            .width(80.dp)
                            .height(50.dp)
                    ) {
                        Text("OK", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}