package com.finalproject.smartwage.ui.components.cards

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.ui.theme.Black
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.LightPurple
import com.finalproject.smartwage.viewModel.IncomeViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun IncomeListCard(
    income: Income,
    viewModel: IncomeViewModel,
    onEdit: (Income) -> Unit
) {
    val totalTax = income.paye + income.usc + income.prsi
    val netPay = income.amount - totalTax

    val numberFormat = NumberFormat.getNumberInstance(Locale.UK).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    val formattedGrossPay = numberFormat.format(income.amount)
    val formattedTotalTax = numberFormat.format(totalTax)
    val formattedNetPay = numberFormat.format(netPay)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(LightPurple),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = income.source,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
                Text(
                    "Gross Pay: €$formattedGrossPay",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )
                Text(
                    "Total Tax: €$formattedTotalTax",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )
                Text(
                    "Net Pay: €$formattedNetPay",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )
                Text(
                    "Payment Date: ${SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(Date(income.date))}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )
            }

            // Icons
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Edit Icon
                IconButton(
                    onClick = { onEdit(income) },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(35.dp),
                        imageVector = Icons.Filled.Edit,
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
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Income",
                        tint = Red
                    )
                }
            }
        }
    }
}