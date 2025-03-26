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
import com.finalproject.smartwage.data.local.entities.Expenses
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.PurpleGrey40
import com.finalproject.smartwage.viewModel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.text.NumberFormat

@SuppressLint("DefaultLocale")
@Composable
fun ExpenseItem(
    expenses: Expenses,
    viewModel: ExpenseViewModel,
    onEdit: (Expenses) -> Unit
) {

    val numberFormat = NumberFormat.getNumberInstance(Locale.UK).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    val formattedAmount = numberFormat.format(expenses.amount)

    // Expense Item Card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(PurpleGrey40),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Expense Category
                Text(
                    expenses.category.uppercase(Locale.getDefault()),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                // Expense Amount
                Text(
                    "Amount Paid: €$formattedAmount",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                // Expense Date
                Text(
                    "Date: ${SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(Date(expenses.date))}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                // Expense Description Label
                Text(
                    "Description:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                // Expense Description
                Text(
                    expenses.description,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Icons
            Column (
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                // Edit Icon
                IconButton(
                    onClick = { onEdit(expenses) },
                    modifier = Modifier
                        .size(80.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Expense",
                        tint = DarkBlue
                    )
                }
                // Delete Icon
                IconButton(
                    onClick = { viewModel.deleteExpense(expenses.id) },
                    modifier = Modifier
                        .size(80.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Expense",
                        tint = Red
                    )
                }
            }
        }
    }
}