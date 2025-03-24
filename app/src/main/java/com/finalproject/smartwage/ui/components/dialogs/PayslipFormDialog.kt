package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.ui.components.dropdown.CompanyNameDropdownMenuField
import com.finalproject.smartwage.ui.components.dropdown.FrequencyDropdownMenuField
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.utils.TaxCalculator
import com.finalproject.smartwage.viewModel.ExpenseViewModel
import com.finalproject.smartwage.viewModel.IncomeViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayslipFormDialog(
    viewModel: IncomeViewModel,
    expenseViewModel: ExpenseViewModel,
    incomeToEdit: Income?,
    onDismiss: () -> Unit,
    onCancel: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    var showDatePicker by remember { mutableStateOf(false) }
    var company by remember { mutableStateOf(incomeToEdit?.source ?: viewModel.getLastCompanyName()) }
    var incomeAmount by remember { mutableStateOf(incomeToEdit?.amount?.toString() ?: "") }
    var taxPaid by remember { mutableStateOf(incomeToEdit?.paye?.toString() ?: "") }
    var usc by remember { mutableStateOf(incomeToEdit?.usc?.toString() ?: "") }
    var prsi by remember { mutableStateOf(incomeToEdit?.prsi?.toString() ?: "") }
    var frequency by remember { mutableStateOf(incomeToEdit?.frequency ?: "") }
    val companyNames = viewModel.getAllCompanyNames()
    val frequencies = listOf("Weekly", "Fortnightly", "Monthly")
    var missingFields by remember { mutableStateOf<List<String>>(emptyList()) }
    var incomeDate by remember {
        mutableStateOf(
            SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(
                Date(incomeToEdit?.date ?: System.currentTimeMillis())
            )
        )
    }

    AlertDialog(
        modifier = Modifier
            .fillMaxWidth(),
        onDismissRequest = {},
        confirmButton = {},
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Text("Add Payslip Details",
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
                    company = selectedCompany
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = incomeDate,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^[0-9-]*$"))) {
                            incomeDate = newValue
                        }
                    },
                    label = { Text("Date  (dd-MM-yyyy)", fontSize = 17.sp) },
                    textStyle = TextStyle(fontSize = 22.sp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    trailingIcon = {
                        IconButton(
                            onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select Date",
                                tint = DarkBlue,
                                modifier = Modifier
                                    .size(45.dp)
                                    .padding(end = 5.dp)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(5.dp))

                FrequencyDropdownMenuField(
                    label = "Frequency",
                    selectedItem = frequency,
                    items = frequencies
                ) { frequency = it }

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedTextField(
                    value = incomeAmount,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            incomeAmount = newValue
                        }
                    },
                    label = { Text("Income Amount (€)", fontSize = 17.sp) },
                    textStyle = TextStyle(fontSize = 22.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(
                            orientation = Orientation.Horizontal,
                            state = rememberScrollState(),
                            enabled = true
                        )
                )

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedTextField(
                    value = taxPaid,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            taxPaid = newValue
                        }
                    },
                    label = { Text("Tax Paid (PAYE) (€)", fontSize = 17.sp) },
                    textStyle = TextStyle(fontSize = 22.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(
                            orientation = Orientation.Horizontal,
                            state = rememberScrollState(),
                            enabled = true
                        )
                )

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedTextField(
                    value = prsi,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            prsi = newValue
                        }
                    },
                    label = { Text("PRSI (€)", fontSize = 17.sp) },
                    textStyle = TextStyle(fontSize = 22.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(
                            orientation = Orientation.Horizontal,
                            state = rememberScrollState(),
                            enabled = true
                        )
                )

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedTextField(
                    value = usc,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            usc = newValue
                        }
                    },
                    label = { Text("USC (€)", fontSize = 17.sp) },
                    textStyle = TextStyle(fontSize = 22.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scrollable(
                            orientation = Orientation.Horizontal,
                            state = rememberScrollState(),
                            enabled = true
                        )
                )

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { onCancel() },
                        modifier = Modifier
                            .width(115.dp)
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(Red)
                    ) {
                        Text(
                            "CANCEL",
                            fontSize = 16.sp,
                            fontWeight = Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = {
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

                            val incomeAmountValue = incomeAmount.toDoubleOrNull() ?: 0.0
                            val actualTaxPaid =
                                (taxPaid.toDoubleOrNull() ?: 0.0) + (usc.toDoubleOrNull()
                                    ?: 0.0) + (prsi.toDoubleOrNull() ?: 0.0)

                            val tuitionFeeRelief = expenseViewModel.getTuitionFeeRelief()
                            val rentTaxCredit = expenseViewModel.getRentTaxCredit()

                            val (expectedPAYE, expectedUSC, expectedPRSI) = TaxCalculator.calculateTax(
                                incomeAmountValue,
                                selectedFrequency,
                                tuitionFeeRelief,
                                rentTaxCredit
                            )
                            val totalExpectedTax = expectedPAYE + expectedUSC + expectedPRSI

                            val taxDifference = actualTaxPaid - totalExpectedTax
                            val overpaidTax = if (taxDifference > 0) taxDifference else 0.0
                            val underpaidTax = if (taxDifference < 0) -taxDifference else 0.0

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
                        colors = ButtonDefaults.buttonColors(DarkBlue)
                    ) {
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