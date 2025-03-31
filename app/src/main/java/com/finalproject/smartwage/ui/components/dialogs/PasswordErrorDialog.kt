package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import com.finalproject.smartwage.ui.components.cards.PasswordErrorCard
import com.finalproject.smartwage.utils.PasswordValidationError

/**
 * PasswordErrorDialog is a Composable function that displays a dialog with password validation errors.
 *
 * @param errors A list of password validation errors to be displayed in the dialog.
 * @param onDismiss A callback function to be called when the dialog is dismissed.
 */

@Composable
fun PasswordErrorDialog(
    // Parameters:
    errors: List<PasswordValidationError>,
    onDismiss: () -> Unit
) {
    // If there are any errors, show the dialog.
    if (errors.isNotEmpty()
    ) {
        // Dialog that will be displayed when there are password validation errors.
        Dialog(
            onDismissRequest = onDismiss
        ) {
            // Display the PasswordErrorCard with the list of errors.
            PasswordErrorCard(
                errors = errors,
                onDismiss = onDismiss
            )
        }
    }
}
