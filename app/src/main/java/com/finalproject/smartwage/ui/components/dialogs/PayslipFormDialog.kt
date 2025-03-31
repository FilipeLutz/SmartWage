package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions.Companion.Default
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType.Companion.Number
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.ui.components.dropdownmenu.CompanyNameDropdownMenuField
import com.finalproject.smartwage.ui.components.dropdownmenu.FrequencyDropdownMenuField
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.utils.TaxCalculator
import com.finalproject.smartwage.viewModel.ExpenseViewModel
import com.finalproject.smartwage.viewModel.IncomeViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Composable function to show a dialog for adding or editing payslip details.
 *
 * @param viewModel The IncomeViewModel instance for managing income data.
 * @param expenseViewModel The ExpenseViewModel instance for managing expense data.
 * @param incomeToEdit The Income object to edit, or null if adding a new payslip.
 * @param onDismiss Callback function to be called when the dialog is dismissed.
 * @param onCancel Callback function to be called when the cancel button is clicked.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayslipFormDialog(
    // Parameters
    viewModel: IncomeViewModel,
    expenseViewModel: ExpenseViewModel,
    incomeToEdit: Income?,
    onDismiss: () -> Unit,
    onCancel: () -> Unit
) {
    // Variables to hold the state of the dialog
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    var showDatePicker by remember { mutableStateOf(false) }
    var company by remember {
        mutableStateOf(
            incomeToEdit?.source ?: viewModel.getLastCompanyName()
        )
    }
    var incomeAmount by remember { mutableStateOf(incomeToEdit?.amount?.toString() ?: "") }
    var taxPaid by remember { mutableStateOf(incomeToEdit?.paye?.toString() ?: "") }
    var usc by remember { mutableStateOf(incomeToEdit?.usc?.toString() ?: "") }
    var prsi by remember { mutableStateOf(incomeToEdit?.prsi?.toString() ?: "") }
    var frequency by remember { mutableStateOf(incomeToEdit?.frequency ?: "") }
    val companyNames = viewModel.getAllCompanyNames()
    // List of frequencies for the dropdown menu
    val frequencies = listOf("Weekly", "Fortnightly", "Monthly")
    var missingFields by remember { mutableStateOf<List<String>>(emptyList()) }
    // Date format for the income date
    var incomeDate by remember {
        mutableStateOf(
            SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(
                Date(incomeToEdit?.date ?: System.currentTimeMillis())
            )
        )
    }
    // Show the alert dialog
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth(),
        onDismissRequest = {},
        confirmButton = {},
        text = {
            // Column to arrange the text fields and buttons vertically
            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp
                    )
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // if editing, show Edit Payslip Details else Add Payslip Details
                Text(
                    text = if (incomeToEdit != null) "Edit Payslip Details"
                    else "Add Payslip Details",
                    fontSize = 28.sp,
                    fontWeight = Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Company Name Dropdown
                CompanyNameDropdownMenuField(
                    label = "Company Name",
                    selectedItem = company,
                    items = companyNames
                ) { selectedCompany ->
                    // Update the selected company name
                    company = selectedCompany
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Income Date Field
                OutlinedTextField(
                    value = incomeDate,
                    onValueChange = { newValue ->
                        // Validate the date format
                        if (newValue.matches(Regex("^[0-9-]*$"))) {
                            incomeDate = newValue
                        }
                    },
                    label = {
                        Text(
                            "Payment Date (dd-MM-yyyy)",
                            fontSize = 15.sp,
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 22.sp
                    ),
                    keyboardOptions = Default.copy(
                        keyboardType = Text
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { showDatePicker = true }
                        ),
                    trailingIcon = {
                        // Calendar icon to open date picker
                        IconButton(
                            onClick = {
                                showDatePicker = true
                            }
                        ) {
                            // Icon to select date
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select Date",
                                tint = DarkBlue,
                                modifier = Modifier
                                    .size(45.dp)
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

                Spacer(modifier = Modifier.height(5.dp))
                // Frequency Dropdown menu
                FrequencyDropdownMenuField(
                    label = "Frequency",
                    selectedItem = frequency,
                    items = frequencies
                ) {
                    // Update the selected frequency
                    frequency = it
                }

                Spacer(modifier = Modifier.height(5.dp))

                // Income Amount Field
                OutlinedTextField(
                    value = incomeAmount,
                    onValueChange = { newValue ->
                        // Validate the income amount format
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            incomeAmount = newValue
                        }
                    },
                    label = {
                        Text(
                            "Gross Pay (€)",
                            fontSize = 17.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 22.sp
                    ),
                    singleLine = true,
                    keyboardOptions = Default.copy(
                        keyboardType = Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(
                            orientation = Orientation.Horizontal,
                            state = rememberScrollState(),
                            enabled = true
                        )
                )

                Spacer(modifier = Modifier.height(5.dp))

                // Tax Paid (PAYE) Field
                OutlinedTextField(
                    value = taxPaid,
                    onValueChange = { newValue ->
                        // Validate the tax paid format
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            taxPaid = newValue
                        }
                    },
                    label = {
                        Text(
                            "Tax Paid (PAYE) (€)",
                            fontSize = 17.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 22.sp
                    ),
                    singleLine = true,
                    keyboardOptions = Default.copy(
                        keyboardType = Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(
                            orientation = Orientation.Horizontal,
                            state = rememberScrollState(),
                            enabled = true
                        )
                )

                Spacer(modifier = Modifier.height(5.dp))

                // PRSI Tax Field
                OutlinedTextField(
                    value = prsi,
                    onValueChange = { newValue ->
                        // Validate the PRSI format
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            prsi = newValue
                        }
                    },
                    label = {
                        Text(
                            "PRSI (€)",
                            fontSize = 17.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 22.sp
                    ),
                    singleLine = true,
                    keyboardOptions = Default.copy(
                        keyboardType = Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(
                            orientation = Orientation.Horizontal,
                            state = rememberScrollState(),
                            enabled = true
                        )
                )

                Spacer(modifier = Modifier.height(5.dp))

                // USC Tax Field
                OutlinedTextField(
                    value = usc,
                    onValueChange = { newValue ->
                        // Validate the USC format
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            usc = newValue
                        }
                    },
                    label = {
                        Text(
                            "USC (€)",
                            fontSize = 17.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 22.sp
                    ),
                    singleLine = true,
                    keyboardOptions = Default.copy(
                        keyboardType = Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(
                            orientation = Orientation.Horizontal,
                            state = rememberScrollState(),
                            enabled = true
                        )
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Row to arrange the buttons horizontally
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = SpaceEvenly
                ) {
                    // Cancel button
                    Button(
                        onClick = { onCancel() },
                        modifier = Modifier
                            .width(115.dp)
                            .height(45.dp),
                        colors = buttonColors(Red)
                    ) {
                        // Text for the cancel button
                        Text(
                            "CANCEL",
                            fontSize = 16.sp,
                            fontWeight = Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Save button
                    Button(
                        onClick = {
                            // Validate the input fields
                            val tempMissingFields = mutableListOf<String>()

                            if (company.isBlank()) tempMissingFields.add("Company Name")
                            if (frequency.isBlank()) tempMissingFields.add("Frequency")
                            if (incomeDate.isBlank() || incomeDate == "Date") tempMissingFields.add(
                                "Date"
                            )
                            if (incomeAmount.isBlank() || incomeAmount.toDoubleOrNull() == null) tempMissingFields.add(
                                "Income Amount"
                            )
                            if (taxPaid.isBlank() || taxPaid.toDoubleOrNull() == null) tempMissingFields.add(
                                "Tax Paid (PAYE)"
                            )
                            if (prsi.isBlank() || prsi.toDoubleOrNull() == null) tempMissingFields.add(
                                "PRSI"
                            )
                            if (usc.isBlank() || usc.toDoubleOrNull() == null) tempMissingFields.add(
                                "USC"
                            )

                            // Check if there are any missing fields
                            if (tempMissingFields.isNotEmpty()) {
                                missingFields = tempMissingFields
                                return@Button
                            }

                            // If all fields are filled, proceed with saving
                            val selectedFrequency = when (frequency) {
                                "Weekly" -> TaxCalculator.PaymentFrequency.WEEKLY
                                "Fortnightly" -> TaxCalculator.PaymentFrequency.FORTNIGHTLY
                                else -> TaxCalculator.PaymentFrequency.MONTHLY
                            }

                            // Convert income amount to double
                            val incomeAmountValue = incomeAmount.toDoubleOrNull() ?: 0.0
                            val actualTaxPaid =
                                (taxPaid.toDoubleOrNull() ?: 0.0) + (usc.toDoubleOrNull()
                                    ?: 0.0) + (prsi.toDoubleOrNull() ?: 0.0)

                            // Get tuition fee relief and rent tax credit from the expense view model
                            val tuitionFeeRelief = expenseViewModel.getTuitionFeeRelief()
                            val rentTaxCredit = expenseViewModel.getRentTaxCredit()

                            // Calculate expected PAYE, USC, and PRSI
                            val (expectedPAYE, expectedUSC, expectedPRSI) = TaxCalculator.calculateTax(
                                incomeAmountValue,
                                selectedFrequency,
                                tuitionFeeRelief,
                                rentTaxCredit
                            )
                            // Calculate total expected tax
                            val totalExpectedTax = expectedPAYE + expectedUSC + expectedPRSI

                            // Calculate overpaid and underpaid tax
                            val taxDifference = actualTaxPaid - totalExpectedTax
                            val overpaidTax = if (taxDifference > 0) taxDifference else 0.0
                            val underpaidTax = if (taxDifference < 0) -taxDifference else 0.0

                            // Create a new Income object
                            val newIncome = Income(
                                id = incomeToEdit?.id ?: UUID.randomUUID().toString(),
                                source = company,
                                amount = incomeAmountValue,
                                paye = taxPaid.toDoubleOrNull() ?: 0.0,
                                usc = usc.toDoubleOrNull() ?: 0.0,
                                prsi = prsi.toDoubleOrNull() ?: 0.0,
                                date = SimpleDateFormat(
                                    "dd-MM-yyyy",
                                    Locale.UK
                                ).parse(incomeDate)?.time
                                    ?: System.currentTimeMillis(),
                                frequency = frequency,
                                userId = currentUser?.uid ?: "",
                                overpaidTax = overpaidTax,
                                underpaidTax = underpaidTax
                            )

                            viewModel.updateIncome(newIncome)
                            onDismiss()
                        },
                        modifier = Modifier
                            .width(115.dp)
                            .height(45.dp),
                        colors = buttonColors(DarkBlue)
                    ) {
                        // Text for the save button
                        Text(
                            "SAVE",
                            fontSize = 16.sp,
                            fontWeight = Bold
                        )
                    }
                }
            }
        }
    )

    // Show CalendarDialog when needed
    CalendarDialog(
        showDialog = showDatePicker,
        onDismiss = { showDatePicker = false },
        onDateSelected = { selectedDate -> incomeDate = selectedDate }
    )

    // Show IncomeErrorDialog if fields are missing
    if (missingFields.isNotEmpty()) {
        AddErrorDialog(
            missingFields = missingFields,
            onDismiss = { missingFields = emptyList() }
        )
    }
}