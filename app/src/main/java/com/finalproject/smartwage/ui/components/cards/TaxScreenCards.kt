package com.finalproject.smartwage.ui.components.cards

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.Green
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
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Income & Tax Breakdown",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Income & Taxes
            Text("Total Income: ${formatCurrency(totalIncome)}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("PAYE: ${formatCurrency(paye)} (Expected: ${formatCurrency(expectedPAYE)})")
            Text("USC: ${formatCurrency(usc)} (Expected: ${formatCurrency(expectedUSC)})")
            Text("PRSI: ${formatCurrency(prsi)} (Expected: ${formatCurrency(expectedPRSI)})")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Total Taxes Paid: ${formatCurrency(taxPaid)} (Expected: ${formatCurrency(expectedTax)})",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tax Credits
            Text("Tax Credit: ${formatCurrency(4000.0)}")

            if (rentTaxCredit > 0.0) {
                Text("Rent Tax Credit: ${formatCurrency(rentTaxCredit)}")
            }

            if (tuitionFeeRelief > 0.0) {
                Text("Tuition Fee Relief: ${formatCurrency(tuitionFeeRelief)}")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Total Tax Credits: ${
                    formatCurrency(4000.0 +
                            (if (rentTaxCredit > 0.0) rentTaxCredit else 0.0) +
                            (if (tuitionFeeRelief > 0.0) tuitionFeeRelief else 0.0))
                }",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Calculate Overpaid/Underpaid Tax
            val totalTaxCredits = 4000.0 + rentTaxCredit + tuitionFeeRelief
            val adjustedExpectedTax = maxOf(0.0, expectedTax - totalTaxCredits)

            val prsiOverpaid = prsi - expectedPRSI
            val uscOverpaid = usc - expectedUSC

            // Overpaid Tax Calculation
            val overpaidTax = paye + maxOf(0.0, prsiOverpaid) + maxOf(0.0, uscOverpaid)

            // Overpaid or Underpaid Tax Message
            val taxMessage = when {
                overpaidTax > 0 -> "You have overpaid your taxes by ${formatCurrency(overpaidTax)}. You may be eligible for a tax refund."
                adjustedExpectedTax > taxPaid -> "You have underpaid your taxes. You may owe additional tax."
                else -> "Your tax payments align with expected tax calculations."
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = taxMessage,
                color = if (overpaidTax > 0) Green else Red,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = Bold)
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

                    pushStyle(SpanStyle(color = DarkBlue, textDecoration = TextDecoration.Underline))
                    append("income")
                    pop()

                    append(" and ")

                    pushStyle(SpanStyle(color = DarkBlue, textDecoration = TextDecoration.Underline))
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