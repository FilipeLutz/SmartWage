package com.finalproject.smartwage.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.viewModel.IncomeViewModel
import com.google.firebase.auth.FirebaseAuth
import java.lang.System
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayslipFormCard(viewModel: IncomeViewModel, incomeToEdit: Income?, onDismiss: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    var company by remember { mutableStateOf(incomeToEdit?.source ?: "") }
    var incomeAmount by remember { mutableStateOf(incomeToEdit?.amount?.toString() ?: "") }
    var taxPaid by remember { mutableStateOf(incomeToEdit?.paye?.toString() ?: "") }
    var usc by remember { mutableStateOf(incomeToEdit?.usc?.toString() ?: "") }
    var prsi by remember { mutableStateOf(incomeToEdit?.prsi?.toString() ?: "") }
    var incomeDate by remember { mutableStateOf(SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(Date(incomeToEdit?.date ?: System.currentTimeMillis()))) }
    var frequency by remember { mutableStateOf(incomeToEdit?.frequency ?: "Monthly") }
    var payPeriod by remember { mutableStateOf(incomeToEdit?.payPeriod?.toString() ?: "1") }

    var showDatePicker by remember { mutableStateOf(false) }
    val frequencies = listOf("Weekly", "Fortnightly", "Monthly")
    val payPeriods = (1..52).map { it.toString() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Enter Payslip Details", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = company,
                onValueChange = { company = it },
                label = { Text("Company Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = incomeAmount,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        incomeAmount = newValue
                    }
                },
                label = { Text("Income Amount (€)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = taxPaid,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        taxPaid = newValue
                    }
                },
                label = { Text("Tax Paid (PAYE) (€)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = usc,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        usc = newValue
                    }
                },
                label = { Text("USC (€)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = prsi,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        prsi = newValue
                    }
                },
                label = { Text("PRSI (€)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Date Field with Calendar Picker
            OutlinedTextField(
                value = incomeDate,
                onValueChange = { incomeDate = it },
                label = { Text("Income Date (dd-MM-yyyy)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                }
            )

            // Show DatePickerDialog when needed
            if (showDatePicker) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        Button(onClick = {
                            val selectedDate = datePickerState.selectedDateMillis
                            if (selectedDate != null) {
                                incomeDate = SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(Date(selectedDate))
                            }
                            showDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            DropdownMenuField("Frequency", frequency, frequencies) { frequency = it }
            DropdownMenuField("Pay Period", payPeriod, payPeriods) { payPeriod = it }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val newIncome = Income(
                        id = incomeToEdit?.id ?: UUID.randomUUID().toString(),
                        source = company,
                        amount = incomeAmount.toDoubleOrNull() ?: 0.0,
                        paye = taxPaid.toDoubleOrNull() ?: 0.0,
                        usc = usc.toDoubleOrNull() ?: 0.0,
                        prsi = prsi.toDoubleOrNull() ?: 0.0,
                        date = SimpleDateFormat("dd-MM-yyyy", Locale.UK).parse(incomeDate)?.time ?: System.currentTimeMillis(),
                        frequency = frequency,
                        payPeriod = payPeriod.toInt(),
                        userId = currentUser?.uid ?: ""
                    )

                    viewModel.addIncome(newIncome)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (incomeToEdit == null) "Save Income" else "Update Income")
            }
        }
    }
}

@Composable
fun IncomeListCard(income: Income, viewModel: IncomeViewModel, onEdit: (Income) -> Unit) {
    val totalTax = income.paye + income.usc + income.prsi
    val netPay = income.amount - totalTax

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Company: ${income.source}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Edit Icon (Top Right)
                IconButton(onClick = { onEdit(income) }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Income", tint = DarkBlue)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text("Gross Pay: €${String.format(Locale.UK, "%.2f", income.amount)}")
            Text("Total Tax: €${String.format(Locale.UK, "%.2f", totalTax)}")
            Text("Net Pay: €${String.format(Locale.UK, "%.2f", netPay)}")
            Text("Pay Period: ${income.payPeriod}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Delete Icon (Bottom Right)
                IconButton(onClick = { viewModel.deleteIncome(income.id, income.userId) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Income", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun DropdownMenuField(label: String, selectedItem: String, items: List<String>, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}