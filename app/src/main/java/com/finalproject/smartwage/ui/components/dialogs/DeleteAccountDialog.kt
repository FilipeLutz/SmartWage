package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.Red

@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Delete Account",
                    fontSize = 30.sp,
                    fontWeight = Bold,
                    color = Red
                )
            }
        },
        text = {
            Text(
                "Are you sure you want to delete your account? \nIt cannot be undone.",
                fontSize = 21.sp,
                fontWeight = SemiBold,
                style = TextStyle(lineHeight = 22.sp)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text("DELETE",
                    color = Red,
                    fontSize = 20.sp,
                    fontWeight = Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL",
                    color = DarkBlue,
                    fontSize = 20.sp,
                    fontWeight = Bold
                )
            }
        }
    )
}