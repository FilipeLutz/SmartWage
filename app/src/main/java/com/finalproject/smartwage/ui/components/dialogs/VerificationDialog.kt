package com.finalproject.smartwage.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.ui.theme.DarkBlue

@Composable
fun VerificationDialog(
    email: String,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                "Verify Your Email",
                fontWeight = Bold,
                fontSize = 25.sp,
                color = DarkBlue
            )
        },
        text = {
            Column (
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    "A verification email has been sent to",
                    fontSize = 20.sp,
                    fontWeight = SemiBold,
                    color = Black
                )
                Text(
                    email,
                    fontSize = 20.sp,
                    fontWeight = SemiBold
                )
                Text(
                    "Please check your email and confirm before logging in.",
                    fontSize = 20.sp,
                    fontWeight = SemiBold,
                    color = Black
                )
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(DarkBlue),
                onClick = onConfirm
            ) {
                Text(
                    "OK",
                    fontSize = 18.sp
                )
            }
        }
    )
}