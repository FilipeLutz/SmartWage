package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.ui.theme.DarkBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(DarkBlue),
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        val selectedDate = datePickerState.selectedDateMillis
                        if (selectedDate != null) {
                            val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(
                                Date(selectedDate)
                            )
                            onDateSelected(formattedDate)
                        }
                        onDismiss()
                    }) {
                    Text("OK", fontSize = 18.sp)
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(DarkBlue),
                    modifier = Modifier.padding(10.dp),
                    onClick = onDismiss
                ) {
                    Text("Cancel", fontSize = 18.sp)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}