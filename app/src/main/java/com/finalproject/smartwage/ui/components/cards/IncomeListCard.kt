package com.finalproject.smartwage.ui.components.cards

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.ui.theme.Black
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.LightPurple
import com.finalproject.smartwage.viewModel.IncomeViewModel
import java.text.NumberFormat.getNumberInstance
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// This composable function creates an income item card that displays the details of an income.
@Composable
fun IncomeListCard(
    // Parameters:
    income: Income,
    viewModel: IncomeViewModel,
    onEdit: (Income) -> Unit
) {
    // Variable to control the visibility of the income info dialog
    val totalTax = income.paye + income.usc + income.prsi
    // Calculate the net pay by subtracting the total tax from the gross income
    val netPay = income.amount - totalTax

    // Format the numbers to 2 decimal places
    val numberFormat = getNumberInstance(Locale.UK)
        .apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    // Format the amounts to strings
    val formattedGrossPay = numberFormat.format(income.amount)
    val formattedTotalTax = numberFormat.format(totalTax)
    val formattedNetPay = numberFormat.format(netPay)

    // Card to display the income details
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = cardColors(LightPurple),
        elevation = cardElevation(6.dp)
    ) {
        // Row to arrange the content inside the card
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            // Column for displaying income details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = spacedBy(8.dp)
            ) {
                // Company Name as Income Source
                Text(
                    text = income.source,
                    fontSize = 22.sp,
                    fontWeight = Bold,
                    color = Black
                )
                // Income Amount
                Text(
                    "Gross Pay: €$formattedGrossPay",
                    fontSize = 20.sp,
                    fontWeight = SemiBold,
                    color = Black
                )
                // Total Tax
                Text(
                    "Total Tax: €$formattedTotalTax",
                    fontSize = 20.sp,
                    fontWeight = SemiBold,
                    color = Black
                )
                // Net Pay
                Text(
                    "Net Pay: €$formattedNetPay",
                    fontSize = 20.sp,
                    fontWeight = SemiBold,
                    color = Black
                )
                // Payment Date
                Text(
                    "Payment Date: ${
                        SimpleDateFormat(
                            "dd-MM-yyyy", Locale.UK
                        ).format(
                            Date(income.date)
                        )
                    }",
                    fontSize = 20.sp,
                    fontWeight = SemiBold,
                    color = Black
                )
            }

            // Column for displaying action buttons (Edit and Delete)
            Column(
                verticalArrangement = spacedBy(8.dp)
            ) {
                // Edit Icon
                IconButton(
                    onClick = { onEdit(income) },
                    modifier = Modifier
                        .size(80.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(35.dp),
                        imageVector = Filled.Edit,
                        contentDescription = "Edit Income",
                        tint = DarkBlue
                    )
                }
                // Delete Icon
                IconButton(
                    onClick = { viewModel.deleteIncome(income.id) },
                    modifier = Modifier
                        .size(80.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(35.dp),
                        imageVector = Filled.Delete,
                        contentDescription = "Delete Income",
                        tint = Red
                    )
                }
            }
        }
    }
}