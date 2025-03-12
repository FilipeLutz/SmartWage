package com.finalproject.smartwage.ui.components.cards

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.finalproject.smartwage.data.local.entities.Expense
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.PurpleGrey40
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun ExpenseItem(expense: Expense, onDelete: () -> Unit, onEdit: () -> Unit) {
    // Expense Item Card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(PurpleGrey40)
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Expense Category
                Text(
                    text = "Category: ${expense.category}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                // Expense Amount
                Text(
                    "Amount Paid: ${String.format("%.2f", expense.amount)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                // Expense Date
                Text(
                    "Date: ${SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(Date(expense.date))}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                // Expense Description
                Text(
                    "Description: ${expense.description}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Icons
            Column {
                // Edit Icon
                IconButton(
                    onClick = { onEdit() },
                    modifier = Modifier.size(80.dp)
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
                    onClick = { onDelete() },
                    modifier = Modifier.size(80.dp)
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