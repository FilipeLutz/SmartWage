package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import com.finalproject.smartwage.ui.components.cards.PasswordErrorCard
import com.finalproject.smartwage.utils.PasswordValidationError

@Composable
fun PasswordErrorDialog(
    errors: List<PasswordValidationError>,
    onDismiss: () -> Unit
) {
    if (errors.isNotEmpty()) {
        Dialog(onDismissRequest = onDismiss) {
            PasswordErrorCard(errors = errors, onDismiss = onDismiss)
        }
    }
}
