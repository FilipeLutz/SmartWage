package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryEditable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.data.local.entities.Expenses
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.viewModel.ExpenseViewModel
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * AddExpenseDialog.kt
 * This file contains the implementation of the AddExpenseDialog Composable function.
 * It is used to display a dialog for adding or editing an expense.
 *
 * @param viewModel The ViewModel instance for managing expenses.
 * @param expenseToEdit The expense to be edited, if any.
 * @param onDismiss Callback function to be called when the dialog is dismissed.
 * @param isEditMode Boolean flag indicating whether the dialog is in edit mode or not.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    // Parameters:
    viewModel: ExpenseViewModel,
    expenseToEdit: Expenses?,
    onDismiss: () -> Unit,
    isEditMode: Boolean
) {
    // State variables to manage the dialog's state
    var expenseCategory by remember { mutableStateOf(expenseToEdit?.category ?: "") }
    var amount by remember { mutableStateOf(expenseToEdit?.amount?.toString() ?: "") }
    var description by remember { mutableStateOf(expenseToEdit?.description ?: "") }
    var showExpenseForm by remember { mutableStateOf(isEditMode) }
    var showExpenseErrorDialog by remember { mutableStateOf(false) }
    var missingFields by remember { mutableStateOf(listOf<String>()) }
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val isEditing = expenseToEdit != null
    // Date input field
    var expenseDate by remember {
        mutableStateOf(
            // Format the date to dd-MM-yyyy
            SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(
                // Convert the date to a string
                Date(expenseToEdit?.date ?: System.currentTimeMillis())
            )
        )
    }
    // Show the dialog
    AlertDialog(
        onDismissRequest = {},
        title = {
            // Title of the dialog
            Row(
                horizontalArrangement = Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Display the title based on whether it's editing or adding
                Text(
                    if (isEditing) "Edit Expense"
                    else "Add Expense",
                    fontSize = 28.sp,
                    fontWeight = SemiBold
                )
            }
        },
        text = {
            // Column to hold the form fields
            Column(
                verticalArrangement = spacedBy(10.dp)
            ) {
                // If not in edit mode, show the category selection dropdown
                if (!showExpenseForm) {
                    // Category Selection Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        // Category Input
                        OutlinedTextField(
                            value = expenseCategory.uppercase(Locale.getDefault()),
                            onValueChange = {},
                            textStyle = TextStyle(
                                fontSize = 20.sp
                            ),
                            readOnly = true,
                            trailingIcon = {
                                // Dropdown icon
                                IconButton(
                                    modifier = Modifier
                                        .clickable { expanded = true }
                                        .menuAnchor(
                                            PrimaryEditable,
                                            enabled = true
                                        ),
                                    onClick = { expanded = !expanded }
                                ) {
                                    // Icon for dropdown
                                    Icon(
                                        Default.ArrowDropDown,
                                        contentDescription = "Dropdown",
                                        tint = DarkBlue,
                                        modifier = Modifier
                                            .size(30.dp)
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(
                                    PrimaryEditable,
                                    enabled = true
                                )
                                .clickable { expanded = true },
                            label = {
                                Text(
                                    "Select Category",
                                    fontSize = 18.sp
                                )
                            }
                        )
                        // Dropdown menu items
                        DropdownMenu(
                            modifier = Modifier
                                .exposedDropdownSize(),
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            // Rent Tax Credit option
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "RENT TAX CREDIT",
                                        fontSize = 20.sp
                                    )
                                },
                                // On click, set the selected category and show the form
                                onClick = {
                                    expenseCategory = "RENT TAX CREDIT"
                                    showExpenseForm = true
                                    expanded = false
                                }
                            )

                            HorizontalDivider()

                            // Tuition Fee Relief option
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "TUITION FEE RELIEF",
                                        fontSize = 20.sp
                                    )
                                },
                                // On click, set the selected category and show the form
                                onClick = {
                                    expenseCategory = "TUITION FEE RELIEF"
                                    showExpenseForm = true
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                // If in edit mode, show the selected category
                else {
                    // Category Display Read-Only
                    OutlinedTextField(
                        value = expenseCategory
                            .uppercase(
                                Locale.getDefault()
                            ),
                        onValueChange = {},
                        textStyle = TextStyle(
                            fontSize = 22.sp
                        ),
                        readOnly = true,
                        label = {
                            Text(
                                "Category",
                                fontSize = 17.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    // Expense Date Input Opens CalendarDialog
                    OutlinedTextField(
                        value = expenseDate,
                        // On value change, update the date
                        onValueChange = { newValue ->
                            // Validate the date format
                            if (newValue.matches(Regex("^[0-9-]*$"))) {
                                expenseDate = newValue
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text
                        ),
                        label = {
                            Text(
                                "Payment Date (dd-MM-yyyy)",
                                fontSize = 15.sp
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 22.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { showDatePicker = true }
                            ),
                        trailingIcon = {
                            IconButton(
                                onClick = { showDatePicker = true }
                            ) {
                                // Icon for date selection
                                Icon(
                                    imageVector = Default.DateRange,
                                    contentDescription = "Select Date",
                                    tint = DarkBlue,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(end = 5.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() },
                                            onClick = { showDatePicker = true }
                                        )
                                )
                            }
                        }
                    )

                    // Expense Amount Input
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { newValue ->
                            // Validate the amount format
                            if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                amount = newValue
                            }
                        },
                        singleLine = true,
                        label = {
                            // Label for amount input
                            Text(
                                // Set label based on category
                                text = if (expenseCategory == "RENT TAX CREDIT")
                                    "Annual Rent Paid (€)"
                                else
                                    "Tuition Fee Paid (€)",
                                fontSize = 17.sp
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 22.sp
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
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
                        label = {
                            Text(
                                "Description (Optional)",
                                fontSize = 17.sp
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 22.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .scrollable(
                                orientation = Orientation.Horizontal,
                                state = rememberScrollState(),
                                enabled = true
                            )
                    )
                }
            }
        },
        // Confirm button actions
        confirmButton = {
            // If in edit mode, show the save and back buttons
            if (showExpenseForm) {
                // Row for buttons
                Row(
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier
                            .width(115.dp)
                            .height(45.dp),
                        onClick = {
                            // If in edit mode, hide the form
                            if (showExpenseForm) {
                                // Hide the form and show the category selection
                                showExpenseForm = false
                            } else {
                                // Dismiss the dialog
                                onDismiss()
                            }
                        },
                        colors = buttonColors(DarkBlue)
                    ) {
                        Text(
                            "BACK",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.width(22.dp))

                    // Save button
                    Button(
                        modifier = Modifier
                            .size(
                                115.dp,
                                45.dp
                            ),
                        colors = buttonColors(DarkBlue),
                        onClick = {
                            // Validate the input fields
                            missingFields = mutableListOf<String>().apply {
                                // Check for empty fields
                                if (expenseCategory.isBlank()) add("Category")
                                // Check for empty date
                                if (expenseDate.isBlank()) add("Date")
                                // Check for empty amount
                                if (amount.isBlank() || amount == "0") add("Amount (€)")
                            }

                            // If there are missing fields, show the error dialog
                            if (missingFields.isNotEmpty()) {
                                showExpenseErrorDialog = true
                            }
                            // If all fields are filled, proceed to save the expense
                            else {
                                // Parse the amount and date
                                val expenseAmount = amount.toDoubleOrNull() ?: 0.0
                                // Parse the date
                                val parsedDate = try {
                                    SimpleDateFormat(
                                        "dd-MM-yyyy",
                                        Locale.UK
                                    ).parse(expenseDate)?.time
                                    // Convert to milliseconds
                                } catch (e: Exception) {
                                    Timber.e(e, "Date parsing error. Using current time instead.")
                                    System.currentTimeMillis()
                                }

                                // If the amount is valid, create a new expense object
                                if (expenseAmount > 0) {
                                    // Create a new expense object
                                    val newExpense = Expenses(
                                        id = expenseToEdit?.id ?: UUID.randomUUID().toString(),
                                        category = expenseCategory,
                                        amount = expenseAmount,
                                        description = description,
                                        date = parsedDate ?: System.currentTimeMillis(),
                                        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                                    )
                                    viewModel.addOrUpdateExpense(newExpense)
                                    onDismiss()
                                }
                            }
                        }
                    ) {
                        Text(
                            "SAVE",
                            fontSize = 16.sp,
                            fontWeight = Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            // If not in edit mode, show the cancel button
            else {
                // Row for cancel button
                Row(
                    horizontalArrangement = Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier
                            .width(115.dp)
                            .height(45.dp),
                        onClick = {
                            // If the form is visible, hide it
                            if (showExpenseForm) {
                                // Hide the form and show the category selection
                                showExpenseForm = false
                            } else {
                                // Dismiss the dialog
                                onDismiss()
                            }
                        },
                        colors = buttonColors(Red)
                    ) {
                        Text(
                            "CANCEL",
                            fontSize = 15.sp,
                            fontWeight = Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
    )

    // Show CalendarDialog when needed
    CalendarDialog(
        showDialog = showDatePicker,
        onDismiss = { showDatePicker = false },
        onDateSelected = { selectedDate -> expenseDate = selectedDate }
    )

    // Show error dialog if there are missing fields
    if (showExpenseErrorDialog) {
        AddErrorDialog(
            missingFields = missingFields,
            onDismiss = { showExpenseErrorDialog = false }
        )
    }
}