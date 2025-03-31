package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
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

/**
 * CalendarDialog is a Composable function that displays a date picker dialog.
 * It allows the user to select a date and returns the selected date in "dd-MM-yyyy" format.
 *
 * @param showDialog Boolean flag to control the visibility of the dialog.
 * @param onDismiss Callback function to be called when the dialog is dismissed.
 * @param onDateSelected Callback function to be called with the selected date.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    // Parameters for the CalendarDialog function
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    // Check if the dialog should be shown
    if (showDialog) {
        // DatePickerState with the current date
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )
        // DatePickerDialog is a Material Design component for selecting a date
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                // Button for confirming the selected date
                Button(
                    colors = buttonColors(DarkBlue),
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        // Get the selected date in milliseconds
                        val selectedDate = datePickerState.selectedDateMillis
                        // Format the selected date to a string
                        if (selectedDate != null) {
                            // Format the date to "dd-MM-yyyy"
                            val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(
                                Date(selectedDate)
                            )
                            onDateSelected(formattedDate)
                        }
                        onDismiss()
                    }) {
                    Text(
                        "OK",
                        fontSize = 18.sp
                    )
                }
            },
            // Dismiss button
            dismissButton = {
                // Button for dismissing the dialog
                Button(
                    colors = buttonColors(DarkBlue),
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = onDismiss
                ) {
                    Text(
                        "Cancel",
                        fontSize = 18.sp
                    )
                }
            }
        ) {
            // DatePicker State
            DatePicker(
                state = datePickerState
            )
        }
    }
}