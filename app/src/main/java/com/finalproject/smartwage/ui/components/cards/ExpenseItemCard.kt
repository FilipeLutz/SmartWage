package com.finalproject.smartwage.ui.components.cards

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.finalproject.smartwage.data.local.entities.Expenses
import com.finalproject.smartwage.ui.components.dialogs.ExpenseInfoDialog
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.PurpleGrey40
import com.finalproject.smartwage.viewModel.ExpenseViewModel
import java.text.NumberFormat.getNumberInstance
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// This composable function creates an expense item card that displays the details of an expense.
@Composable
fun ExpenseItem(
    // Parameters:
    expenses: Expenses,
    viewModel: ExpenseViewModel,
    onEdit: (Expenses) -> Unit
) {

    // Initialize a variable to control the visibility of the expense info dialog
    var showExpenseInfoDialog by remember { mutableStateOf(false) }
    // Get the current context
    val numberFormat = getNumberInstance(Locale.UK)
        .apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    // Format the amount to a string
    val formattedAmount = numberFormat.format(expenses.amount)

    // Expense Item Card with details
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = cardColors(PurpleGrey40),
        elevation = cardElevation(6.dp)
    ) {
        // Row layout for the card content
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            // Column for displaying expense details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = spacedBy(8.dp)
            ) {
                // Expense Category Label and Info Icon
                Row(
                    horizontalArrangement = spacedBy(10.dp),
                    verticalAlignment = CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // Expense Category
                    Text(
                        expenses.category.uppercase(Locale.getDefault()),
                        fontSize = 20.sp,
                        fontWeight = Bold
                    )

                    // Info Icon
                    Icon(
                        imageVector = Filled.Info,
                        contentDescription = "Check Info",
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .clickable(
                                onClick = { showExpenseInfoDialog = true },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                    )
                }
                // Expense Amount
                Text(
                    "Amount Paid: €$formattedAmount",
                    fontSize = 20.sp,
                    fontWeight = SemiBold
                )
                // Expense Date
                Text(
                    "Expense Date: ${
                        SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.UK
                        ).format(Date(expenses.date))
                    }",
                    fontSize = 20.sp,
                    fontWeight = SemiBold
                )
                // Expense Description Label
                Text(
                    "Description:",
                    fontSize = 20.sp,
                    fontWeight = SemiBold
                )
                // Expense Description
                Text(
                    expenses.description,
                    fontSize = 20.sp,
                    fontWeight = SemiBold
                )
            }

            // Column for displaying action Icon buttons
            Column(
                verticalArrangement = spacedBy(8.dp)
            ) {
                // Edit Icon
                IconButton(
                    onClick = { onEdit(expenses) },
                    modifier = Modifier
                        .size(80.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(35.dp),
                        imageVector = Filled.Edit,
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
                        modifier = Modifier
                            .size(35.dp),
                        imageVector = Filled.Delete,
                        contentDescription = "Delete Expense",
                        tint = Red
                    )
                }
            }
        }
    }

    // Expense Info Dialog
    if (showExpenseInfoDialog) {
        ExpenseInfoDialog(
            onDismiss = { showExpenseInfoDialog = false },
            category = expenses.category,
            onLinkClick = { context ->
                val url = when (expenses.category.uppercase(Locale.getDefault())) {
                    "RENT TAX CREDIT" -> "https://www.revenue.ie/en/personal-tax-credits-reliefs-and-exemptions/land-and-property/rent-credit/qualifying-conditions.aspx"
                    "TUITION FEE RELIEF" -> "https://www.revenue.ie/en/personal-tax-credits-reliefs-and-exemptions/education/tuition-fees-paid-for-third-level-education/index.aspx"
                    else -> null
                }
                url?.let {
                    val intent = Intent(Intent.ACTION_VIEW, it.toUri())
                    context.startActivity(intent)
                }
            }
        )
    }
}