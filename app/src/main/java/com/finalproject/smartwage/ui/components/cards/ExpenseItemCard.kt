package com.finalproject.smartwage.ui.components.cards

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.data.local.entities.Expense
import com.finalproject.smartwage.ui.theme.DarkBlue

@Composable
fun ExpenseItem(expense: Expense, onDelete: () -> Unit, onEdit: () -> Unit) {
    // Expense Item Card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Expense Details
            Column(modifier = Modifier.weight(1f)) {
                // Expense Category
                Text(text = "Category: ${expense.category}", fontSize = 18.sp, fontWeight = Bold)
                // Expense Amount
                Text(text = "Amount: €${expense.amount}", fontSize = 18.sp)
                // Expense Date
                Text(text = "Date: ${expense.date}", fontSize = 18.sp)
                // Expense Description if not empty
                if (expense.description.isNotEmpty()) {
                    Text(text = "Description: ${expense.description}", fontSize = 18.sp)
                }
            }

            // Icons
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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