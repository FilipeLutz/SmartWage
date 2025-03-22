package com.finalproject.smartwage.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
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

            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                // PAYE
                Text(
                    text = "1. **PAYE (Pay As You Earn)**",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = Bold)
                )
                Text(
                    text = "   - Applied at 20% on the first €44,000 of income, and 40% on any income above that threshold.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // USC
                Text(
                    text = "2. **USC (Universal Social Charge)**",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = Bold)
                )
                Text(
                    text = "   - Applies to gross income with the following rates:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "     - 0.5% on the first €12,012",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "     - 2% on the next €3,358 (from €12,013 to €15,370)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "     - 3% on the next €27,292 (from €15,371 to €42,662)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "     - 8% on income above €42,662",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // PRSI
                Text(
                    text = "3. **PRSI (Pay Related Social Insurance)**",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = Bold)
                )
                Text(
                    text = "   - Charged at 4.1% on incomes above €18,304 annually.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Standard Tax Credit
                Text(
                    text = "4. **Standard Tax Credit (€4,000)**",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = Bold)
                )
                Text(
                    text = "   - Reduces your total income tax liability by €4,000.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Rent Tax Credit
                Text(
                    text = "5. **Rent Tax Credit**",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = Bold)
                )
                Text(
                    text = "   - The Rent Tax Credit allows renters to claim 20% of their annual rent payments as a tax credit. For 2024 and 2025, the maximum claim amounts are:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "     - **€1,000** for single individuals.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "     - **€2,000** for a jointly assessed couple.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "   - This credit applies to rent paid on a primary residence, a second home for work or education, or a property rented by your child. However, rent paid under certain housing support schemes (such as HAP or RAS) does not qualify.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "   - The Rent Tax Credit can only be used to offset Income Tax liability and cannot reduce USC or PRSI. If the credit exceeds your Income Tax liability, the unused portion is not refunded.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Tuition Fee Relief
                Text(
                    text = "6. **Tuition Fee Relief**",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = Bold)
                )
                Text(
                    text = "   - Tuition Fee Relief provides tax relief for qualifying tuition fees paid for approved courses at approved institutions. The relief is granted at the standard tax rate of 20% and applies to the portion of fees paid above certain disregard amounts.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "   - For full-time courses, the first €3,000 of tuition fees is disregarded, and for part-time courses, the first €1,500 is disregarded. For example, if you pay €6,000 in tuition fees for a full-time course, you can claim relief on €3,000, resulting in a tax credit of €600 (20% of €3,000).",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "   - The maximum claim per course is **€7,000**. This means you can claim relief for fees up to €7,000 per course, and you can apply for relief on multiple courses if applicable.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "   - Please note that additional fees such as student levies, sports fees, or the USI levy are not eligible for relief.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Additional Note
                Text(
                    text = "*Your final tax liability is influenced by your total income, applicable deductions, and credits. Overpayments may be refundable upon assessment.*",
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = Italic),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
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