package com.finalproject.smartwage.ui.components.dialogs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.finalproject.smartwage.ui.theme.DarkBlue

@SuppressLint("DefaultLocale")
@Composable
fun TaxResultDialog(
    calculatedTax: Triple<Double, Double, Double>?,
    onDismiss: () -> Unit
) {
    val calculatedPAYE = calculatedTax?.first ?: 0.0
    val calculatedUSC = calculatedTax?.second ?: 0.0
    val calculatedPRSI = calculatedTax?.third ?: 0.0
    val totalTax = calculatedPAYE + calculatedUSC + calculatedPRSI

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
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
                Spacer(modifier = Modifier.height(12.dp))

                // Show Total Tax
                Text(
                    text = "Tax Due: €${String.format("%.2f", totalTax)}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Updated Tax Explanation
                Text(
                    text = "Tax Rate Information",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.fillMaxWidth()
                )

                Column(modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "• PAYE (Pay As You Earn)",
                        fontSize = 18.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Applied at 20% on the first €44,000 of income, and 40% on any income above it.",
                        fontSize = 18.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.SemiBold,
                        style = TextStyle(lineHeight = 23.sp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• USC (Universal Social Charge)",
                        fontSize = 18.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "   - 0.5% on the first €12,012",
                        fontSize = 17.sp, color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "   - 2% on the next €3,358",
                        fontSize = 17.sp, color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "   - 3% on the next €42,662",
                        fontSize = 17.sp, color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "   - 8% on anything above €58,032",
                        fontSize = 17.sp, color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• PRSI \n(Pay Related Social Insurance)",
                        fontSize = 18.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.SemiBold,
                        style = TextStyle(lineHeight = 23.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Charged at 4.1% on incomes above €18,304 annually.",
                        fontSize = 18.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.SemiBold,
                        style = TextStyle(lineHeight = 23.sp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• STANDARD TAX CREDIT",
                        fontSize = 18.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Reduces your total income tax liability by €4,000.",
                        fontSize = 18.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.SemiBold,
                        style = TextStyle(lineHeight = 25.sp)
                    )
                }

                Text(
                    text = "• Please note, this is a basic tax estimate. For a more precise calculation, enter your actual income and expenses in the app.",
                    fontSize = 18.sp,
                    color = Color.Blue,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
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