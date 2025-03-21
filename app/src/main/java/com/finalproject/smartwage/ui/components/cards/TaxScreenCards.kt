package com.finalproject.smartwage.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.Red

@Composable
fun TaxSummaryCard(
    totalIncome: Double,
    paye: Double,
    usc: Double,
    prsi: Double,
    taxPaid: Double,
    expectedPAYE: Double,
    expectedUSC: Double,
    expectedPRSI: Double,
    expectedTax: Double,
    rentTaxCredit: Double,
    tuitionFeeRelief: Double,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Income Tax Breakdown",
                    fontSize = 26.sp,
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Income & Taxes
            Text("Total Income: ${formatCurrency(totalIncome)}",
                fontSize = 20.sp,
                fontWeight = Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Taxes Paid
                Column (
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row (
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text("Tax Paid",
                            fontSize = 18.sp,
                            fontWeight = SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("PAYE : ${formatCurrency(paye)}", fontSize = 17.sp)
                    Text("USC : ${formatCurrency(usc)}", fontSize = 17.sp)
                    Text("PRSI : ${formatCurrency(prsi)}", fontSize = 17.sp)
                }

                // Expected Tax
                Column (
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row (
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text("Expected",
                            fontSize = 18.sp,
                            fontWeight = SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(formatCurrency(expectedPAYE), fontSize = 17.sp)
                    Text(formatCurrency(expectedUSC), fontSize = 17.sp)
                    Text(formatCurrency(expectedPRSI), fontSize = 17.sp)
                }

                // Difference between Tax Paid and Expected Tax
                Column (
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row (
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text("Difference",
                            fontSize = 18.sp,
                            fontWeight = SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(formatCurrency(paye - expectedPAYE), fontSize = 17.sp)
                    Text(formatCurrency(usc - expectedUSC), fontSize = 17.sp)
                    Text(formatCurrency(prsi - expectedPRSI), fontSize = 17.sp)
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Total Paid : ${formatCurrency(taxPaid)}",
                    fontSize = 18.sp,
                    fontWeight = SemiBold
                )
                Text(
                    text = "Expected Total : ${formatCurrency(expectedTax)}",
                    fontSize = 18.sp,
                    fontWeight = SemiBold
                )

                // Calculate the difference and check if it's positive or negative
                val totalDifference = taxPaid - expectedTax
                val differenceText = if (totalDifference > 0) {
                    "Overpaid"
                } else if (totalDifference < 0) {
                    "Underpaid"
                } else {
                    "No difference"
                }

                // total overpaid tax or underpaid tax
                Text(
                    text = "Total Difference: ${formatCurrency(totalDifference)} ($differenceText)",
                    fontSize = 18.sp,
                    fontWeight = SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "OBS: NO TAX CREDITS INCLUDED SO FAR",
                    fontSize = 16.sp,
                    color = DarkBlue
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Tax Credits Breakdown section
            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Tax Credits Breakdown",
                    fontSize = 26.sp,
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Tax Credits
            Text("Personal Tax Credit: ${formatCurrency(4000.0)}",
                fontSize = 17.sp
            )

            if (rentTaxCredit > 0.0) {
                Text("Rent Tax Credit: ${formatCurrency(rentTaxCredit)}",
                    fontSize = 17.sp
                )
            }

            if (tuitionFeeRelief > 0.0) {
                Text("Tuition Fee Relief: ${formatCurrency(tuitionFeeRelief)}",
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Total Tax Credits: ${
                    formatCurrency(
                        4000.0 +
                                (if (rentTaxCredit > 0.0) rentTaxCredit else 0.0) +
                                (if (tuitionFeeRelief > 0.0) tuitionFeeRelief else 0.0)
                    )
                }",
                fontSize = 18.sp,
                fontWeight = Bold
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Tax Result",
                    fontSize = 26.sp,
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Calculate Overpaid/Underpaid Tax
            val totalTaxCredits = 4000.0 + rentTaxCredit + tuitionFeeRelief
            val adjustedExpectedTax = maxOf(0.0, expectedTax - totalTaxCredits)

            val prsiOverpaid = prsi - expectedPRSI
            val uscOverpaid = usc - expectedUSC

            // Overpaid Tax Calculation
            val overpaidTax = paye + maxOf(0.0, prsiOverpaid) + maxOf(0.0, uscOverpaid)

            // Overpaid or Underpaid Tax Message
            val taxMessage = when {
                overpaidTax > 0 -> "You have overpaid tax : ${formatCurrency(overpaidTax)}. \nYou may be eligible for a tax refund."
                adjustedExpectedTax > taxPaid -> "You have underpaid tax : ${formatCurrency(adjustedExpectedTax - taxPaid)}. \nYou may owe additional tax."
                else -> "Your tax payments align with expected tax calculations."
            }

            Text(
                text = taxMessage,
                color = if (overpaidTax > 0) DarkBlue else Red,
                fontSize = 20.sp,
                fontWeight = SemiBold,
                style = TextStyle(lineHeight = 30.sp)
            )
        }
    }
}

@Composable
fun TaxExplanationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Understanding Your Taxes",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "1. **PAYE (Pay As You Earn)** - Deducted at source based on income tax bands. PAYE is fully refundable if overpaid.\n" +
                        "2. **USC (Universal Social Charge)** - Applies to income over €13,000, with progressive rates. USC is not refundable.\n" +
                        "3. **PRSI (Pay Related Social Insurance)** - Required for social benefits and pensions, calculated on a weekly basis. PRSI is not refundable.\n" +
                        "4. **Standard Tax Credit (€4000)** - Applied to reduce your tax liability.\n" +
                        "5. **Rent Tax Credit** - If you pay rent, a portion is deductible from your tax.\n" +
                        "6. **Tuition Fee Relief** - Some education expenses reduce your taxable amount.\n\n" +
                        "💡 *Your final tax liability is influenced by income, deductions, and credits. If you have paid more than your expected taxes, you may be eligible for a refund.*",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Suppress("DEPRECATION")
@Composable
fun TaxDataMessage(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No income or expenses found.",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ClickableText(
                text = buildAnnotatedString {
                    append("Please add your ")

                    pushStyle(
                        SpanStyle(
                            color = DarkBlue,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                    append("income")
                    pop()

                    append(" and ")

                    pushStyle(
                        SpanStyle(
                            color = DarkBlue,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                    append("expenses")
                    pop()

                    append(" to see your tax breakdown.")
                },
                style = MaterialTheme.typography.bodyLarge,
                onClick = { offset ->
                    if (offset in 15..21) {
                        navController.navigate("income")
                    } else if (offset in 26..34) {
                        navController.navigate("expense")
                    }
                }
            )
        }
    }
}

fun formatCurrency(amount: Double): String {
    return "€${"%.2f".format(amount)}"
}