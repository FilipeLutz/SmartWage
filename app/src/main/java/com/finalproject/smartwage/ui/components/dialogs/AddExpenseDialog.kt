package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onAddExpense: (String, Double, String) -> Unit
) {

    var expenseCategory by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showExpenseForm by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Expense") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (!showExpenseForm) {
                    // Category Selection Dropdown
                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = {}
                    ) {
                        TextField(
                            value = expenseCategory,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown"
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Select Category") }
                        )
                        DropdownMenu(
                            expanded = false,
                            onDismissRequest = {}
                        ) {
                            DropdownMenuItem(
                                text = { Text("Rent Tax Credit") },
                                onClick = {
                                    expenseCategory = "Rent Tax Credit"
                                    showExpenseForm = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Tuition Fees Relief") },
                                onClick = {
                                    expenseCategory = "Tuition Fees Relief"
                                    showExpenseForm = true
                                }
                            )
                        }
                    }
                } else {
                    // Expense Form
                    // Category Display read-only
                    OutlinedTextField(
                        value = expenseCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Amount Input
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Description Input
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            if (showExpenseForm) {
                Button(
                    onClick = {
                        val expenseAmount = amount.toDoubleOrNull() ?: 0.0
                        if (expenseAmount > 0) {
                            onAddExpense(expenseCategory, expenseAmount, description)
                            onDismiss()
                        }
                    }
                ) {
                    Text("Add")
                }
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(if (showExpenseForm) "Cancel" else "Back")
            }
        }
    )
}