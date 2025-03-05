package com.finalproject.smartwage.ui.components.cards

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.utils.TaxCalculator
import com.finalproject.smartwage.viewModel.IncomeViewModel
import com.google.firebase.auth.FirebaseAuth
import java.lang.System
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayslipFormCard(
    viewModel: IncomeViewModel,
    incomeToEdit: Income?,
    onDismiss: () -> Unit,
    onCancel: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    var showDatePicker by remember { mutableStateOf(false) }
    var company by remember { mutableStateOf(incomeToEdit?.source ?: "") }
    var incomeAmount by remember { mutableStateOf(incomeToEdit?.amount?.toString() ?: "") }
    var taxPaid by remember { mutableStateOf(incomeToEdit?.paye?.toString() ?: "") }
    var usc by remember { mutableStateOf(incomeToEdit?.usc?.toString() ?: "") }
    var prsi by remember { mutableStateOf(incomeToEdit?.prsi?.toString() ?: "") }
    var incomeDate by remember { mutableStateOf(SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(Date(incomeToEdit?.date ?: System.currentTimeMillis()))) }
    var frequency by remember { mutableStateOf(incomeToEdit?.frequency ?: "") }
    val frequencies = listOf("Weekly", "Fortnightly", "Monthly")

    //var payPeriod by remember { mutableStateOf(incomeToEdit?.payPeriod?.toString() ?: "1") }
    /*val payPeriods = remember(frequency) {
        when (frequency) {
            "Weekly" -> (1..52).map { it.toString() }
            "Fortnightly" -> (1..26).map { it.toString() }
            else -> (1..12).map { it.toString() }
        }
    }

    // Reset payPeriod when frequency changes
    LaunchedEffect(frequency) {
        payPeriod = payPeriods.firstOrNull() ?: "1"
    }
    */

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text("Add Payslip Details", fontSize = 28.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(35.dp))

            OutlinedTextField(
                value = company,
                onValueChange = { company = it },
                label = { Text("Company Name", fontSize = 17.sp) },
                textStyle = TextStyle(fontSize = 22.sp),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DropdownMenuField(
                    label = "Frequency",
                    selectedItem = frequency,
                    items = frequencies,
                    modifier = Modifier
                        .weight(0.95f)
                        .clickable { frequency }
                ) { frequency = it }

                OutlinedTextField(
                    value = incomeDate,
                    onValueChange = { incomeDate = it },
                    label = { Text("Income Date", fontSize = 17.sp) },
                    textStyle = TextStyle(fontSize = 20.sp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDatePicker = true },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select Date",
                            tint = DarkBlue,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .size(33.dp)
                                .clickable { showDatePicker = true }
                        )
                    },
                    readOnly = true
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = incomeAmount,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        incomeAmount = newValue
                    }
                },
                label = { Text("Income Amount (€)", fontSize = 17.sp) },
                textStyle = TextStyle(fontSize = 22.sp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = taxPaid,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        taxPaid = newValue
                    }
                },
                label = { Text("Tax Paid (PAYE) (€)", fontSize = 17.sp) },
                textStyle = TextStyle(fontSize = 22.sp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = prsi,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        prsi = newValue
                    }
                },
                label = { Text("PRSI (€)", fontSize = 17.sp) },
                textStyle = TextStyle(fontSize = 22.sp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = usc,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        usc = newValue
                    }
                },
                label = { Text("USC (€)", fontSize = 17.sp) },
                textStyle = TextStyle(fontSize = 22.sp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Show DatePickerDialog when needed
            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = incomeToEdit?.date ?: System.currentTimeMillis()
                )
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(DarkBlue),
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                val selectedDate = datePickerState.selectedDateMillis
                                if (selectedDate != null) {
                                    incomeDate = SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(Date(selectedDate))
                                }
                                showDatePicker = false
                            }) {
                            Text("OK", fontSize = 18.sp)
                        }
                    },
                    dismissButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(DarkBlue),
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = { showDatePicker = false }) {
                            Text("Cancel", fontSize = 18.sp)
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onCancel() },
                    modifier = Modifier
                        .width(120.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(Red)
                ) {
                    Text(
                        "Cancel",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        val selectedFrequency = when (frequency) {
                            "Weekly" -> TaxCalculator.PaymentFrequency.WEEKLY
                            "Fortnightly" -> TaxCalculator.PaymentFrequency.FORTNIGHTLY
                            else -> TaxCalculator.PaymentFrequency.MONTHLY
                        }

                        val incomeAmountValue = incomeAmount.toDoubleOrNull() ?: 0.0
                        val actualTaxPaid = (taxPaid.toDoubleOrNull() ?: 0.0) + (usc.toDoubleOrNull() ?: 0.0) + (prsi.toDoubleOrNull() ?: 0.0)

                        // Calculate tax using correct frequency
                        val (expectedPAYE, expectedUSC, expectedPRSI) = TaxCalculator.calculateTax(incomeAmountValue, selectedFrequency)
                        val totalExpectedTax = expectedPAYE + expectedUSC + expectedPRSI

                        // Compare tax paid vs expected tax
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
                            date = SimpleDateFormat("dd-MM-yyyy", Locale.UK).parse(incomeDate)?.time ?: System.currentTimeMillis(),
                            frequency = frequency,
                            userId = currentUser?.uid ?: "",
                            overpaidTax = overpaidTax,
                            underpaidTax = underpaidTax
                        )

                        viewModel.addIncome(newIncome)
                        onDismiss()
                    },
                    modifier = Modifier
                        .width(120.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(DarkBlue)
                ) {
                    Text(
                        "Save",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun IncomeListCard(income: Income, viewModel: IncomeViewModel, onEdit: (Income) -> Unit) {
    val totalTax = income.paye + income.usc + income.prsi
    val netPay = income.amount - totalTax
    val incomeTextStyle = TextStyle(
        fontSize = 16.sp,
        color = Color.Black
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(12.dp),
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Company: ${income.source}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("Gross Pay: ${String.format("%.2f", income.amount)}", style = incomeTextStyle, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text("Total Tax: ${String.format("%.2f", totalTax)}", style = incomeTextStyle, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text("Net Pay: ${String.format("%.2f", netPay)}", style = incomeTextStyle, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text("Date: ${SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(Date(income.date))}", style = incomeTextStyle, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            // Icons
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Edit Icon
                IconButton(
                    onClick = { onEdit(income) },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        modifier = Modifier
                        .size(35.dp),
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Income",
                        tint = DarkBlue
                    )
                }
                // Delete Icon
                IconButton(
                    onClick = { viewModel.deleteIncome(income.id, income.userId) },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(35.dp),
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Income",
                        tint = Red
                    )
                }
            }
        }
    }
}


@Composable
fun DropdownMenuField(
    label: String,
    selectedItem: String,
    items: List<String>,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            label = { Text(label, fontSize = 17.sp) },
            textStyle = TextStyle(fontSize = 20.sp),
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier
                        .size(35.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown",
                        tint = DarkBlue
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        )

        if (expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(167.dp)
                    .heightIn(max = 200.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 250.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .fillMaxWidth()
                    ) {
                        items.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = {
                                    Text(item, style = TextStyle(fontSize = 22.sp)) },
                                onClick = {
                                    onItemSelected(item)
                                    expanded = false
                                }
                            )
                            if (index < items.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}