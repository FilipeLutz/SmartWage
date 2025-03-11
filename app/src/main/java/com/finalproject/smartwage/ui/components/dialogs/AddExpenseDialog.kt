package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onAddExpense: (String, Double, String, String) -> Unit
) {
    var expenseCategory by remember { mutableStateOf("") }
    var expenseDate by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showExpenseForm by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Expense") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (!showExpenseForm) {
                    // Category Selection Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = expenseCategory,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                IconButton(
                                    modifier = Modifier
                                        .clickable { expanded = true }
                                        .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true),
                                    onClick = { expanded = !expanded }) {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Dropdown"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                                .clickable { expanded = true },
                            label = { Text("Select Category") }
                        )
                        DropdownMenu(
                            modifier = Modifier
                                .exposedDropdownSize(),
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Rent Tax Credit", fontSize = 20.sp) },
                                onClick = {
                                    expenseCategory = "Rent Tax Credit"
                                    showExpenseForm = true
                                    expanded = false
                                }
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            DropdownMenuItem(
                                text = { Text("Tuition Fees Relief", fontSize = 20.sp) },
                                onClick = {
                                    expenseCategory = "Tuition Fees Relief"
                                    showExpenseForm = true
                                    expanded = false
                                }
                            )
                        }
                    }
                } else {
                    // Category Display Read-Only
                    OutlinedTextField(
                        value = expenseCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Expense Date Input Opens CalendarDialog
                    OutlinedTextField(
                        value = expenseDate,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Date (dd/MM/yyyy)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                            }
                        }
                    )

                    // Amount Input
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { newValue ->
                            if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                amount = newValue
                            }
                        },
                        singleLine = true,
                        label = { Text("Amount (€)") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .scrollable(
                                orientation = Orientation.Horizontal,
                                state = rememberScrollState(),
                                enabled = true
                            )
                    )

                    // Description Input
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        singleLine = true,
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .scrollable(
                                orientation = Orientation.Horizontal,
                                state = rememberScrollState(),
                                enabled = true
                            )
                    )
                }
            }
        },
        confirmButton = {
            if (showExpenseForm) {
                Button(
                    onClick = {
                        val expenseAmount = amount.toDoubleOrNull() ?: 0.0
                        if (expenseAmount > 0 && expenseDate.isNotBlank()) {
                            onAddExpense(expenseCategory, expenseAmount, description, expenseDate)
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

    // Show CalendarDialog when needed
    CalendarDialog(
        showDialog = showDatePicker,
        onDismiss = { showDatePicker = false },
        onDateSelected = { selectedDate -> expenseDate = selectedDate }
    )
}